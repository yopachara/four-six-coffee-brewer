package com.yopachara.fourtosixmethod.feature.timer.navigation

import TimerRoute
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation

const val timerRoute = "timer_route"

fun NavController.navigateToTimer(navOptions: NavOptions? = null) {
    this.navigate(timerRoute, navOptions)
}

fun NavGraphBuilder.timerScreen(onTopicClick: (String) -> Unit) {
    composable(route = timerRoute) {
        TimerRoute(onTopicClick)
    }
}
