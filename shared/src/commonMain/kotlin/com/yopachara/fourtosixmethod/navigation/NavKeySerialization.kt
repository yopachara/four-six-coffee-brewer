package com.yopachara.fourtosixmethod.navigation

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import com.yopachara.flowsixmethod.feature.about.navigation.AboutRoute
import com.yopachara.fourtosixmethod.feature.history.navigation.HistoryRoute
import com.yopachara.fourtosixmethod.feature.settings.navigation.SettingsRoute
import com.yopachara.fourtosixmethod.feature.timer.navigation.TimerRoute
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

/**
 * Serialization setup for saving and restoring the back stacks.
 *
 * [NavKey] is an open interface, so restoring a saved back stack means resolving a stored key back
 * to a concrete route type. navigation3 also offers a reflection-based serializer that does this
 * without any registration, but it uses `Class.forName` and is therefore Android-only. Registering
 * the subtypes explicitly keeps navigation working on every target - and it means a new route only
 * restores correctly once it is added here.
 */
internal val NavSavedStateConfiguration = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(TimerRoute::class, TimerRoute.serializer())
            subclass(HistoryRoute::class, HistoryRoute.serializer())
            subclass(SettingsRoute::class, SettingsRoute.serializer())
            subclass(AboutRoute::class, AboutRoute.serializer())
        }
    }
}
