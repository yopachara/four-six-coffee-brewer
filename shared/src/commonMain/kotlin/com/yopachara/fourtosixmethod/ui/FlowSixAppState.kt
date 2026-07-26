package com.yopachara.fourtosixmethod.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import com.yopachara.fourtosixmethod.navigation.NavigationState
import com.yopachara.fourtosixmethod.navigation.Navigator
import com.yopachara.fourtosixmethod.navigation.TopLevelDestination
import com.yopachara.fourtosixmethod.navigation.rememberNavigationState


@Composable
fun rememberFlowSixAppState(
    navigationState: NavigationState = rememberNavigationState(
        startRoute = TopLevelDestination.TIMER.route,
        topLevelRoutes = TopLevelDestination.entries.map { it.route }.toSet(),
    ),
): FlowSixAppState {
    val navigator = remember(navigationState) { Navigator(navigationState) }
    return remember(
        navigationState,
        navigator,
    ) {
        FlowSixAppState(navigationState, navigator)
    }
}

@Stable
class FlowSixAppState(
    val navigationState: NavigationState,
    private val navigator: Navigator,
) {

    /**
     * Map of top level destinations to be used in the TopBar, BottomBar and NavRail. The key is the
     * route.
     */
    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries




    /**
     * UI logic for navigating to a top level destination in the app. Top level destinations have
     * only one copy of the destination of the back stack, and save and restore state whenever you
     * navigate to and from it.
     *
     * @param topLevelDestination: The destination the app needs to navigate to.
     */
    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        navigator.navigate(topLevelDestination.route)
    }

    fun goBack() {
        navigator.goBack()
    }

}