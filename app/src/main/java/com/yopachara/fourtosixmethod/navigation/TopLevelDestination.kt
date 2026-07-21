package com.yopachara.fourtosixmethod.navigation

import androidx.navigation3.runtime.NavKey
import com.yopachara.flowsixmethod.feature.about.navigation.AboutRoute
import com.yopachara.fourtosixmethod.R
import com.yopachara.fourtosixmethod.core.designsystem.icon.FlowSixIcons
import com.yopachara.fourtosixmethod.core.designsystem.icon.Icon
import com.yopachara.fourtosixmethod.core.designsystem.icon.Icon.DrawableResourceIcon
import com.yopachara.fourtosixmethod.core.designsystem.icon.Icon.ImageVectorIcon
import com.yopachara.fourtosixmethod.feature.history.navigation.HistoryRoute
import com.yopachara.fourtosixmethod.feature.settings.navigation.SettingsRoute
import com.yopachara.fourtosixmethod.feature.timer.navigation.TimerRoute

/**
 * Type for the top level destinations in the application. Each of these destinations
 * can contain one or more screens (based on the window size). Navigation from one screen to the
 * next within a single destination will be handled directly in composables.
 */
enum class TopLevelDestination(
    val route: NavKey,
    val selectedIcon: Icon,
    val unselectedIcon: Icon,
    val iconTextId: Int,
    val titleTextId: Int,
) {
    TIMER(
        route = TimerRoute,
        selectedIcon = DrawableResourceIcon(
            FlowSixIcons.TimerFilled
        ),
        unselectedIcon = DrawableResourceIcon(
            FlowSixIcons.TimerUnfilled
        ),
        iconTextId = R.string.timer,
        titleTextId = R.string.timer,
    ),
    HISTORY(
        route = HistoryRoute,
        selectedIcon = DrawableResourceIcon(
            FlowSixIcons.HistoryFilled
        ),
        unselectedIcon = DrawableResourceIcon(
            FlowSixIcons.HistoryUnfilled
        ),
        iconTextId = R.string.history,
        titleTextId = R.string.history,
    ),
    SETTINGS(
        route = SettingsRoute,
        selectedIcon = ImageVectorIcon(
            FlowSixIcons.Settings
        ),
        unselectedIcon = ImageVectorIcon(
            FlowSixIcons.SettingsOutlined
        ),
        iconTextId = R.string.settings,
        titleTextId = R.string.settings,
    ),
    ABOUT(
        route = AboutRoute,
        selectedIcon = DrawableResourceIcon(
            FlowSixIcons.AboutFilled
        ),
        unselectedIcon = DrawableResourceIcon(
            FlowSixIcons.AboutUnfilled
        ),
        iconTextId = R.string.about,
        titleTextId = R.string.about,
    ),
}
