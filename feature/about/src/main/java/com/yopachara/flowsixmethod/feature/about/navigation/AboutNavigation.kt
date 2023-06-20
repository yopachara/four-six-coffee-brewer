package com.yopachara.flowsixmethod.feature.about.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.yopachara.flowsixmethod.feature.about.AboutRoute

const val aboutRoute = "about_route"

fun NavController.navigateToAbout(navOptions: NavOptions? = null) {
    this.navigate(aboutRoute, navOptions)
}

fun NavGraphBuilder.aboutScreen(onTopicClick: (String) -> Unit) {
    composable(route = aboutRoute) {
        AboutRoute(onTopicClick)
    }
}
