package com.yopachara.fourtosixmethod.feature.timer.service

import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.yopachara.fourtosixmethod.feature.timer.state.TimerSessionRepository

/**
 * Android's driver for [TimerEngine]. WorkManager promotes it to a foreground service
 * via [setForeground] for the duration of [doWork] - cancelling the unique work
 * (pause/stop, via [AndroidTimerController]) tears the notification down automatically,
 * no manual stopForeground() needed.
 */
class TimerWorker(
    appContext: Context,
    params: WorkerParameters,
    private val sessionRepository: TimerSessionRepository,
    private val timerEngine: TimerEngine,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        TimerNotifications.ensureChannel(applicationContext)
        setForeground(createForegroundInfo())

        timerEngine.run { setForeground(createForegroundInfo()) }

        return Result.success()
    }

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
