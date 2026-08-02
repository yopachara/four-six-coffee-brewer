package com.yopachara.fourtosixmethod.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation3.runtime.NavKey
import com.yopachara.fourtosixmethod.core.designsystem.icon.Icon
import com.yopachara.fourtosixmethod.core.designsystem.layout.LocalWindowSizeClass
import com.yopachara.fourtosixmethod.core.designsystem.layout.ProvideWindowSizeClass
import com.yopachara.fourtosixmethod.navigation.NavHost
import com.yopachara.fourtosixmethod.navigation.TopLevelDestination
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun FlowSixApp(appState: FlowSixAppState = rememberFlowSixAppState()) {
    ProvideWindowSizeClass(modifier = Modifier.fillMaxSize()) {
        val windowSizeClass = LocalWindowSizeClass.current

        Scaffold(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            bottomBar = {
                // Wide windows move navigation into the rail below, so the bar would be a
                // second copy of the same destinations.
                if (!windowSizeClass.isWide) {
                    FlowSixBottomBar(
                        destinations = appState.topLevelDestinations,
                        onNavigateToDestination = appState::navigateToTopLevelDestination,
                        currentTopLevelRoute = appState.navigationState.topLevelRoute,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            },
        ) { padding ->

            Row(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal,
                        ),
                    ),
            ) {
                if (windowSizeClass.isWide) {
                    FlowSixNavigationRail(
                        destinations = appState.topLevelDestinations,
                        onNavigateToDestination = appState::navigateToTopLevelDestination,
                        currentTopLevelRoute = appState.navigationState.topLevelRoute,
                        showLabels = windowSizeClass.showNavigationRailLabels,
                        modifier = Modifier.fillMaxHeight(),
                    )
                }

                NavHost(appState = appState)
            }
        }
    }
}

@Composable
private fun FlowSixBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentTopLevelRoute: NavKey,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier,
    ) {
        destinations.forEach { destination ->
            val selected = destination.route == currentTopLevelRoute
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = { DestinationIcon(destination = destination, selected = selected) },
                label = { Text(stringResource(destination.iconTextId)) },
            )
        }
    }
}

@Composable
private fun FlowSixNavigationRail(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentTopLevelRoute: NavKey,
    showLabels: Boolean,
    modifier: Modifier = Modifier,
) {
    NavigationRail(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        destinations.forEach { destination ->
            val selected = destination.route == currentTopLevelRoute
            NavigationRailItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = { DestinationIcon(destination = destination, selected = selected) },
                // A phone in landscape is short enough that stacked icon+label rows push the
                // last destination off the rail, so labels only appear on large windows.
                label = if (showLabels) {
                    { Text(stringResource(destination.iconTextId)) }
                } else {
                    null
                },
            )
        }
    }
}

@Composable
private fun DestinationIcon(
    destination: TopLevelDestination,
    selected: Boolean,
) {
    val icon = if (selected) destination.selectedIcon else destination.unselectedIcon
    when (icon) {
        is Icon.ImageVectorIcon -> Icon(
            imageVector = icon.imageVector,
            contentDescription = null,
        )

        is Icon.DrawableResourceIcon -> Icon(
            painter = painterResource(icon.id),
            contentDescription = null,
        )
    }
}
