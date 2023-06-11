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
            FlowSixIcons.MenuBook
        ),
        unselectedIcon = DrawableResourceIcon(
            FlowSixIcons.MenuBookBorder
        ),
        iconTextId = R.string.app_name,
        titleTextId = R.string.app_name,
    ),
}
