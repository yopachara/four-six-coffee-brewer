package com.yopachara.fourtosixmethod.feature.timer.service

import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.yopachara.fourtosixmethod.core.domain.InsertRecipeUseCase
import com.yopachara.fourtosixmethod.feature.timer.state.TimerSessionRepository
import com.yopachara.fourtosixmethod.feature.timer.state.TimerState
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

/**
 * Long-running worker owning the countdown. WorkManager promotes it to a
 * foreground service via [setForeground] for the duration of [doWork] -
 * cancelling the unique work (pause/stop, via [TimerController]) tears the
 * notification down automatically, no manual stopForeground() needed.
 */
@HiltWorker
class TimerWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val sessionRepository: TimerSessionRepository,
    private val insertRecipeUseCase: InsertRecipeUseCase,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        TimerNotifications.ensureChannel(applicationContext)
        setForeground(createForegroundInfo())

        val totalTime = sessionRepository.state.value.recipe.getTotalTime()
        val continueFrom = sessionRepository.state.value.seconds

        tickerFlow(totalTime, continueFrom)
            .map { remainingTime ->
                sessionRepository.state.value.copy(
                    secondsRemaining = remainingTime,
                    seconds = totalTime - remainingTime,
                    totalSeconds = totalTime,
                    timerState = TimerState.Play,
                )
            }
            .onStart {
                sessionRepository.update { it.copy(timerState = TimerState.Play) }
            }
            .collect { tickedState ->
                sessionRepository.update { tickedState }
                setForeground(createForegroundInfo())
            }

        // Reached only on natural completion - cancellation (pause/stop) throws
        // out of the collect loop above instead of falling through to here.
        sessionRepository.update {
            it.copy(secondsRemaining = null, seconds = null, timerState = TimerState.Stop)
        }
        insertRecipeUseCase(sessionRepository.state.value.recipe)
        return Result.success()
    }

    private fun tickerFlow(totalSeconds: Int, continueFrom: Int?): Flow<Int> = flow {
        if (continueFrom == null) emit(totalSeconds)
        val firstTick = totalSeconds - (continueFrom ?: 0) - 1
        for (remaining in firstTick downTo 0) {
            delay(1000)
            emit(remaining)
        }
    }.conflate()

    private fun createForegroundInfo(): ForegroundInfo {
        val notification = TimerNotifications.build(applicationContext, sessionRepository.state.value)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ForegroundInfo(
                TimerNotifications.NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE,
            )
        } else {
            ForegroundInfo(TimerNotifications.NOTIFICATION_ID, notification)
        }
    }

    companion object {
        const val UNIQUE_WORK_NAME = "timer_work"
    }
}
