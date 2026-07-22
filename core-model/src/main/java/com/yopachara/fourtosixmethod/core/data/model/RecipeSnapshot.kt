package com.yopachara.fourtosixmethod.core.data.model

/**
 * Lightweight, persistable copy of a [Recipe]'s user-editable fields - no id/steps/createAt.
 * Shared shape for "remember last recipe" and any future named-preset feature.
 */
data class RecipeSnapshot(
    val ratio: Int = 12,
    val coffeeWeight: Float = 15f,
    val balance: Balance = Balance.Basic,
    val level: Level = Level.Basic,
    val isIcedDrip: Boolean = false,
    val hotRatio: Int = 10,
)

fun Recipe.toSnapshot(): RecipeSnapshot = RecipeSnapshot(
    ratio = ratio,
    coffeeWeight = coffeeWeight,
    balance = balance,
    level = level,
    isIcedDrip = isIcedDrip,
    hotRatio = hotRatio,
)

fun RecipeSnapshot.toRecipe(): Recipe = Recipe(
    _ratio = ratio,
    _coffeeWeight = coffeeWeight,
    _balance = balance,
    _level = level,
    _isIcedDrip = isIcedDrip,
    _hotRatio = hotRatio,
)
