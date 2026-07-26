package com.yopachara.fourtosixmethod.feature.timer.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TimerActionReceiver : BroadcastReceiver(), KoinComponent {

    private val controller: TimerController by inject()

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_TOGGLE -> controller.toggle()
            ACTION_STOP -> controller.stop()
        }
    }

    companion object {
        const val ACTION_TOGGLE = "com.yopachara.fourtosixmethod.feature.timer.action.TOGGLE"
        const val ACTION_STOP = "com.yopachara.fourtosixmethod.feature.timer.action.STOP"
    }
}
