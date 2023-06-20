package com.yopachara.fourtosixmethod.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.yopachara.flowsixmethod.feature.about.navigation.aboutScreen
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
        timerScreen(
            onTopicClick = {
                navController.navigateToTimer()
            },
        )
        historyScreen {

        }
        aboutScreen{

        }
    }
}
