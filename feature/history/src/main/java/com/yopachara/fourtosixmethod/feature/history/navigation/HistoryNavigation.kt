package com.yopachara.fourtosixmethod.feature.history.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.yopachara.fourtosixmethod.feature.history.HistoryRoute

const val historyRoute = "history_route"

fun NavController.navigateToHistory(navOptions: NavOptions? = null) {
    this.navigate(historyRoute, navOptions)
}

fun NavGraphBuilder.historyScreen(onTopicClick: (String) -> Unit) {
    composable(route = historyRoute) {
        HistoryRoute(onTopicClick)
    }
}
