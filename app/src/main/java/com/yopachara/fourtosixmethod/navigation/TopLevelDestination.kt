package com.yopachara.fourtosixmethod.navigation

import com.yopachara.fourtosixmethod.R
import com.yopachara.fourtosixmethod.core.designsystem.icon.FlowSixIcons
import com.yopachara.fourtosixmethod.core.designsystem.icon.Icon
import com.yopachara.fourtosixmethod.core.designsystem.icon.Icon.DrawableResourceIcon

/**
 * Type for the top level destinations in the application. Each of these destinations
 * can contain one or more screens (based on the window size). Navigation from one screen to the
 * next within a single destination will be handled directly in composables.
 */
enum class TopLevelDestination(
    val selectedIcon: DrawableResourceIcon,
    val unselectedIcon: Icon,
    val iconTextId: Int,
    val titleTextId: Int,
) {
    TIMER(
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
        selectedIcon = DrawableResourceIcon(
            FlowSixIcons.HistoryFilled
        ),
        unselectedIcon = DrawableResourceIcon(
            FlowSixIcons.HistoryUnfilled
        ),
        iconTextId = R.string.history,
        titleTextId = R.string.history,
    ),
    ABOUT(
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
