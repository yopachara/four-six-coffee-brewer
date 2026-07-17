package com.yopachara.fourtosixmethod.feature.timer.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.yopachara.foursixmethod.feature.timer.R
import com.yopachara.fourtosixmethod.core.designsystem.R as DesignSystemR
import com.yopachara.fourtosixmethod.feature.timer.state.TimerDisplayState
import com.yopachara.fourtosixmethod.feature.timer.state.TimerState

/**
 * Shared notification builder - used by [TimerWorker] while ticking (via
 * setForeground) and by [TimerController] to post the plain "Paused" state
 * (no foreground service is running while paused, so it can't use setForeground).
 * Same notification ID either way, so one replaces the other in place.
 */
object TimerNotifications {

    private const val CHANNEL_ID = "timer_channel"
    const val NOTIFICATION_ID = 1001

    fun ensureChannel(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val channel = NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.timer_notification_channel_name),
            NotificationManager.IMPORTANCE_LOW,
        )
        context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    fun build(context: Context, state: TimerDisplayState) =
        NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(DesignSystemR.drawable.ic_timer_24)
            .setContentTitle(context.getString(R.string.timer_notification_title))
            .setContentText(contentText(context, state))
            .setOngoing(state.isPlaying())
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(openAppPendingIntent(context))
            .addAction(toggleAction(context, state))
            .addAction(stopAction(context))
            .build()

    private fun contentText(context: Context, state: TimerDisplayState): String {
        val remaining = state.secondsRemaining ?: 0
        val time = "%02d:%02d".format(remaining / 60, remaining % 60)
        return if (state.timerState == TimerState.Pause) {
            context.getString(R.string.timer_notification_text_paused, time)
        } else {
            context.getString(R.string.timer_notification_text_playing, time)
        }
    }

    private fun toggleAction(context: Context, state: TimerDisplayState): NotificationCompat.Action {
        val isPlaying = state.isPlaying()
        val label = context.getString(
            if (isPlaying) R.string.timer_notification_action_pause else R.string.timer_notification_action_play
        )
        val icon = if (isPlaying) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play
        val pendingIntent = actionPendingIntent(context, TimerActionReceiver.ACTION_TOGGLE, requestCode = 0)
        return NotificationCompat.Action(icon, label, pendingIntent)
    }

    private fun stopAction(context: Context): NotificationCompat.Action {
        val pendingIntent = actionPendingIntent(context, TimerActionReceiver.ACTION_STOP, requestCode = 1)
        return NotificationCompat.Action(
            android.R.drawable.ic_menu_close_clear_cancel,
            context.getString(R.string.timer_notification_action_stop),
            pendingIntent,
        )
    }

    private fun actionPendingIntent(context: Context, action: String, requestCode: Int): PendingIntent {
        val intent = Intent(context, TimerActionReceiver::class.java).setAction(action)
        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
    }

    private fun openAppPendingIntent(context: Context): PendingIntent {
        val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)
            ?.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        return PendingIntent.getActivity(
            context,
            0,
            launchIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
    }
}
