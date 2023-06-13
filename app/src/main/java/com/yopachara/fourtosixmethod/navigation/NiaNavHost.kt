package com.yopachara.fourtosixmethod.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.yopachara.fourtosixmethod.feature.history.navigation.historyScreen
import com.yopachara.fourtosixmethod.feature.timer.navigation.navigateToTimer
import com.yopachara.fourtosixmethod.feature.timer.navigation.timerRoute
import com.yopachara.fourtosixmethod.feature.timer.navigation.timerScreen
import com.yopachara.fourtosixmethod.ui.FlowSixAppState

@Composable
fun NavHost(
    appState: FlowSixAppState,
    modifier: Modifier = Modifier,
    startDestination: String = timerRoute,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        // TODO: handle topic clicks from each top level destination
        timerScreen(
            onTopicClick = {
                navController.navigateToTimer()
            },
        )
        historyScreen {

        }
    }
}
