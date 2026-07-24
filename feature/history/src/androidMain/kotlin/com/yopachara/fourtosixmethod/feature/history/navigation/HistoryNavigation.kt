package com.yopachara.fourtosixmethod.feature.history.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.yopachara.fourtosixmethod.feature.history.screen.HistoryRoute as HistoryScreen
import kotlinx.serialization.Serializable

@Serializable
data object HistoryRoute : NavKey

fun EntryProviderScope<NavKey>.historyScreen(onTopicClick: (String) -> Unit) {
    entry<HistoryRoute> {
        HistoryScreen(onTopicClick)
    }
}