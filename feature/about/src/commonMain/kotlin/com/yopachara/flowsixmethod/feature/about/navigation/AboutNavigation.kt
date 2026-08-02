package com.yopachara.flowsixmethod.feature.about.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.yopachara.flowsixmethod.feature.about.AboutRoute as AboutScreen
import kotlinx.serialization.Serializable

@Serializable
data object AboutRoute : NavKey

fun EntryProviderScope<NavKey>.aboutScreen(onTopicClick: (String) -> Unit) {
    entry<AboutRoute> {
        AboutScreen(onTopicClick)
    }
}