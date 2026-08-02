package com.yopachara.fourtosixmethod.feature.settings.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.yopachara.fourtosixmethod.feature.settings.SettingsRoute as SettingsScreen
import kotlinx.serialization.Serializable

@Serializable
data object SettingsRoute : NavKey

fun EntryProviderScope<NavKey>.settingsScreen() {
    entry<SettingsRoute> {
        SettingsScreen()
    }
}