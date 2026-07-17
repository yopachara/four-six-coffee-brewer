package com.yopachara.fourtosixmethod.feature.timer.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TimerActionReceiver : BroadcastReceiver() {

    @Inject
    lateinit var controller: TimerController

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
