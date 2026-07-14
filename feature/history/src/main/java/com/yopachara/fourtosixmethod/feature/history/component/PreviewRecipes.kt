package com.yopachara.fourtosixmethod.feature.history.component

import com.yopachara.fourtosixmethod.core.data.model.Balance
import com.yopachara.fourtosixmethod.core.data.model.Level
import com.yopachara.fourtosixmethod.core.data.model.Recipe
import java.time.LocalDate

internal fun previewRecipes(): List<Recipe> = listOf(
    Recipe(
        id = 1,
        _ratio = 16,
        _coffeeWeight = 20f,
        _balance = Balance.Basic,
        _level = Level.Basic,
        createAt = LocalDate.of(2026, 1, 15)
    ),
    Recipe(
        id = 2,
        _ratio = 15,
        _coffeeWeight = 18f,
        _balance = Balance.Sweet,
        _level = Level.Week,
        createAt = LocalDate.of(2026, 1, 16)
    ),
    Recipe(
        id = 3,
        _ratio = 14,
        _coffeeWeight = 22f,
        _balance = Balance.Acid,
        _level = Level.Strong,
        createAt = LocalDate.of(2026, 1, 17)
    ),
    Recipe(
        id = 3,
        _ratio = 14,
        _coffeeWeight = 22f,
        _balance = Balance.Acid,
        _level = Level.Strong,
        createAt = LocalDate.of(2026, 1, 17)
    ),
    Recipe(
        id = 3,
        _ratio = 14,
        _coffeeWeight = 22f,
        _balance = Balance.Acid,
        _level = Level.Strong,
        createAt = LocalDate.of(2026, 1, 18)
    )
)