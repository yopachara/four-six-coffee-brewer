package com.yopachara.fourtosixmethod.feature.timer.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.yopachara.fourtosixmethod.feature.timer.screen.TimerRoute

const val timerRoute = "timer_route"

fun NavController.navigateToTimer(navOptions: NavOptions? = null) {
    this.navigate(timerRoute, navOptions)
}

fun NavGraphBuilder.timerScreen() {
    composable(route = timerRoute) {
        TimerRoute()
    }
}
