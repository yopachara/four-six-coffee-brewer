package com.yopachara.fourtosixmethod.feature.timer.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.yopachara.fourtosixmethod.feature.timer.screen.TimerRoute as TimerScreen
import kotlinx.serialization.Serializable

@Serializable
data object TimerRoute : NavKey

fun EntryProviderScope<NavKey>.timerScreen() {
    entry<TimerRoute> {
        TimerScreen()
    }
}