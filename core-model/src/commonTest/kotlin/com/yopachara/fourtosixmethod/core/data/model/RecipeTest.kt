package com.yopachara.fourtosixmethod.core.data.model

import kotlin.test.Test
import kotlin.test.assertEquals

class RecipeTest {

    @Test
    fun steps_are_regenerated_when_a_setting_changes() {
        val recipe = Recipe(coffeeWeight = 20f, ratio = 15)

        assertEquals(5, recipe.steps.size)
        assertEquals(300f, recipe.getTotalWater())

        assertEquals(6, recipe.copy(level = Level.Strong).steps.size)
        assertEquals(4, recipe.copy(level = Level.Week).steps.size)
        // The original is untouched - Recipe is immutable, copy() derives a fresh schedule.
        assertEquals(5, recipe.steps.size)
    }

    @Test
    fun the_first_two_pours_follow_the_balance_and_the_rest_follow_the_level() {
        val recipe = Recipe(coffeeWeight = 20f, ratio = 15, balance = Balance.Basic)
        val total = recipe.getTotalWater()

        assertEquals(Balance.Basic.sweetIndex * total, recipe.steps[0].waterWeight)
        assertEquals(Balance.Basic.acidIndex * total, recipe.steps[1].waterWeight)
        assertEquals(Level.Basic.firstIndex * total, recipe.steps[2].waterWeight)
    }

    @Test
    fun iced_drip_pours_only_the_hot_water_share() {
        val recipe = Recipe(coffeeWeight = 20f, ratio = 15, isIcedDrip = true, hotRatio = 10)

        assertEquals(200f, recipe.getHotWaterWeight())
        assertEquals(100f, recipe.getIceWeight())
        // Pours are scaled to the hot water, not the total.
        assertEquals(Balance.Basic.sweetIndex * 200f, recipe.steps[0].waterWeight)
    }

    @Test
    fun the_hot_ratio_stays_below_the_total_ratio() {
        // Asking for more hot water than the recipe holds clamps to ratio - 1.
        assertEquals(9, Recipe(ratio = 10, hotRatio = 20).getEffectiveHotRatio())
        // And it never drops below the 6:1 floor.
        assertEquals(6, Recipe(ratio = 15, hotRatio = 1).getEffectiveHotRatio())
    }

    @Test
    fun the_current_pour_is_derived_from_the_seconds_remaining() {
        val recipe = Recipe(level = Level.Basic)

        assertEquals(State.First, recipe.getCurrentStatePosition(210))
        assertEquals(State.Second, recipe.getCurrentStatePosition(165))
        assertEquals(State.Third, recipe.getCurrentStatePosition(120))
        assertEquals(State.Forth, recipe.getCurrentStatePosition(75))
        assertEquals(State.Fifth, recipe.getCurrentStatePosition(30))
        assertEquals(null, recipe.getCurrentStatePosition(211))
    }
}
