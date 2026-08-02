package com.yopachara.fourtosixmethod.feature.timer.service

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.await
import com.yopachara.fourtosixmethod.feature.timer.state.TimerDisplayState
import com.yopachara.fourtosixmethod.feature.timer.state.TimerSessionRepository
import com.yopachara.fourtosixmethod.feature.timer.state.TimerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/** [TimerController] backed by WorkManager + a foreground-service worker ([TimerWorker]). */
class AndroidTimerController(
    private val context: Context,
    private val sessionRepository: TimerSessionRepository,
    private val workManager: WorkManager,
) : TimerController {
    // App-scoped: cancelUniqueWork() is async, and its teardown of the foreground
    // notification races a synchronous notify() call made right after it - awaiting
    // the cancel Operation first avoids that race, which needs a coroutine.
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun toggle() {
        if (sessionRepository.state.value.isPlaying()) pause() else play()
    }

    override fun play() {
        val request = OneTimeWorkRequestBuilder<TimerWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()
        workManager.enqueueUniqueWork(TimerWorker.UNIQUE_WORK_NAME, ExistingWorkPolicy.REPLACE, request)
    }

    override fun pause() {
        sessionRepository.update { it.copy(timerState = TimerState.Pause) }
        scope.launch {
            workManager.cancelUniqueWork(TimerWorker.UNIQUE_WORK_NAME).await()
            // No foreground service is running while paused, so post the notification
            // directly instead of via setForeground() - same ID, so it replaces
            // whichever notification the worker's setForeground() left behind.
            postPausedNotification(sessionRepository.state.value)
        }
    }

    override fun stop() {
        sessionRepository.update {
            it.copy(secondsRemaining = null, seconds = null, timerState = TimerState.Stop)
        }
        scope.launch {
            workManager.cancelUniqueWork(TimerWorker.UNIQUE_WORK_NAME).await()
            NotificationManagerCompat.from(context).cancel(TimerNotifications.NOTIFICATION_ID)
        }
    }

    private fun postPausedNotification(state: TimerDisplayState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        TimerNotifications.ensureChannel(context)
        NotificationManagerCompat.from(context)
            .notify(TimerNotifications.NOTIFICATION_ID, TimerNotifications.build(context, state))
    }
}
