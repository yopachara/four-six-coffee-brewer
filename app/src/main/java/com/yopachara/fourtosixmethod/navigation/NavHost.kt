package com.yopachara.fourtosixmethod.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.yopachara.flowsixmethod.feature.about.navigation.aboutScreen
import com.yopachara.fourtosixmethod.feature.history.navigation.historyScreen
import com.yopachara.fourtosixmethod.feature.settings.navigation.settingsScreen
import com.yopachara.fourtosixmethod.feature.timer.navigation.timerScreen
import com.yopachara.fourtosixmethod.ui.FlowSixAppState

private val entryProvider = entryProvider {
    timerScreen()
    historyScreen(onTopicClick = {})
    settingsScreen()
    aboutScreen(onTopicClick = {})
}

@Composable
fun NavHost(
    appState: FlowSixAppState,
    modifier: Modifier = Modifier,
) {
    NavDisplay(
        entries = appState.navigationState.toEntries(entryProvider),
        onBack = { appState.goBack() },
        transitionSpec = {
            // Slide in from right when navigating forward
            slideInHorizontally(initialOffsetX = { it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { -it })
        },
        popTransitionSpec = {
            // Slide in from left when navigating back
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
        },
        predictivePopTransitionSpec = {
            // Slide in from left when navigating back
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
        },
        modifier = modifier,
    )
}