package com.yopachara.fourtosixmethod.navigation

import androidx.navigation3.runtime.NavKey
import kotlin.test.Test
import kotlin.test.assertNotNull

/**
 * Off Android there is no reflective fallback, so an unregistered route only fails when a saved
 * back stack is restored - late, on one platform, and easy to miss. This fails at build time
 * instead, for every route the bottom navigation can reach.
 */
class NavKeySerializationTest {

    private val serializersModule = NavSavedStateConfiguration.serializersModule

    @Test
    fun every_top_level_route_can_be_saved_and_restored() {
        for (destination in TopLevelDestination.entries) {
            val route = destination.route

            val strategy = serializersModule.getPolymorphic(NavKey::class, route)
            assertNotNull(strategy, "${destination.name}'s route is not registered for saving")

            val restored = serializersModule.getPolymorphic(
                baseClass = NavKey::class,
                serializedClassName = strategy.descriptor.serialName,
            )
            assertNotNull(restored, "${destination.name}'s route cannot be restored")
        }
    }
}
