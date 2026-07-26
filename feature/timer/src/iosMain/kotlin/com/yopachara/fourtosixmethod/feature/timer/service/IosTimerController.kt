package com.yopachara.fourtosixmethod.feature.timer.service

import com.yopachara.fourtosixmethod.feature.timer.state.TimerSessionRepository
import com.yopachara.fourtosixmethod.feature.timer.state.TimerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNNotificationSound
import platform.UserNotifications.UNTimeIntervalNotificationTrigger
import platform.UserNotifications.UNUserNotificationCenter

/**
 * [TimerController] for iOS, which has no WorkManager or foreground-service equivalent:
 * the countdown runs in-process and stops ticking once the system suspends the app.
 *
 * To keep a backgrounded brew usable, every remaining pour boundary is scheduled up
 * front as a local notification when the timer starts, and those are withdrawn on
 * pause/stop. Fidelity is therefore lower than Android's: the on-screen readout freezes
 * while suspended (it re-syncs on resume from the ticks that did run), but the pour
 * alerts still fire on time.
 */
class IosTimerController(
    private val sessionRepository: TimerSessionRepository,
    private val timerEngine: TimerEngine,
) : TimerController {

    // App-scoped, like the Android controller's: the brew outlives any one screen.
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var tickJob: Job? = null

    override fun toggle() {
        if (sessionRepository.state.value.isPlaying()) pause() else play()
    }

    override fun play() {
        tickJob?.cancel()
        scheduleStepNotifications()
        tickJob = scope.launch {
            timerEngine.run { /* nothing per tick: the UI observes the session state directly */ }
            // Natural completion - the boundary notifications have all fired by now,
            // but clear any the system still holds pending after a resume.
            cancelStepNotifications()
        }
    }

    override fun pause() {
        // Cancel first so an in-flight tick cannot overwrite the Pause state below.
        tickJob?.cancel()
        tickJob = null
        sessionRepository.update { it.copy(timerState = TimerState.Pause) }
        cancelStepNotifications()
    }

    override fun stop() {
        tickJob?.cancel()
        tickJob = null
        sessionRepository.update {
            it.copy(secondsRemaining = null, seconds = null, timerState = TimerState.Stop)
        }
        cancelStepNotifications()
    }

    /**
     * Queues one notification per remaining pour boundary. Resuming re-schedules from
     * the already-elapsed offset, so boundaries that are already behind are skipped.
     */
    private fun scheduleStepNotifications() {
        val state = sessionRepository.state.value
        val steps = state.recipe.steps
        val elapsed = state.seconds ?: 0
        val center = UNUserNotificationCenter.currentNotificationCenter()

        var boundary = 0
        steps.forEachIndexed { index, step ->
            boundary += step.time
            val secondsFromNow = boundary - elapsed
            if (secondsFromNow <= 0) return@forEachIndexed

            val nextStep = steps.getOrNull(index + 1)
            val content = UNMutableNotificationContent().apply {
                setTitle(NOTIFICATION_TITLE)
                setBody(
                    if (nextStep == null) {
                        "Brew complete"
                    } else {
                        "Pour ${index + 2}: +${nextStep.getWaterWithScale(1)}g"
                    }
                )
                setSound(UNNotificationSound.defaultSound)
            }
            val request = UNNotificationRequest.requestWithIdentifier(
                identifier = notificationId(index),
                content = content,
                trigger = UNTimeIntervalNotificationTrigger.triggerWithTimeInterval(
                    timeInterval = secondsFromNow.toDouble(),
                    repeats = false,
                ),
            )
            center.addNotificationRequest(request, null)
        }
    }

    private fun cancelStepNotifications() {
        val identifiers = sessionRepository.state.value.recipe.steps.indices.map(::notificationId)
        UNUserNotificationCenter.currentNotificationCenter()
            .removePendingNotificationRequestsWithIdentifiers(identifiers)
    }

    private fun notificationId(index: Int) = "$NOTIFICATION_ID_PREFIX$index"

    private companion object {
        const val NOTIFICATION_TITLE = "4:6 Coffee Timer"
        const val NOTIFICATION_ID_PREFIX = "timer_step_"
    }
}
