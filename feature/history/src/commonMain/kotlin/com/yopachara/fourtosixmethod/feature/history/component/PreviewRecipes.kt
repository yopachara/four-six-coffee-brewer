package com.yopachara.fourtosixmethod.feature.history.component

import com.yopachara.fourtosixmethod.core.data.model.Balance
import com.yopachara.fourtosixmethod.core.data.model.Level
import com.yopachara.fourtosixmethod.core.data.model.Recipe
import kotlinx.datetime.LocalDate

internal fun previewRecipes(): List<Recipe> = listOf(
    Recipe(
        id = 1,
        ratio = 16,
        coffeeWeight = 20f,
        balance = Balance.Basic,
        level = Level.Basic,
        createAt = LocalDate(2026, 1, 15)
    ),
    Recipe(
        id = 2,
        ratio = 15,
        coffeeWeight = 18f,
        balance = Balance.Sweet,
        level = Level.Week,
        createAt = LocalDate(2026, 1, 16)
    ),
    Recipe(
        id = 3,
        ratio = 14,
        coffeeWeight = 22f,
        balance = Balance.Acid,
        level = Level.Strong,
        createAt = LocalDate(2026, 1, 17)
    ),
    Recipe(
        id = 3,
        ratio = 14,
        coffeeWeight = 22f,
        balance = Balance.Acid,
        level = Level.Strong,
        createAt = LocalDate(2026, 1, 17)
    ),
    Recipe(
        id = 3,
        ratio = 14,
        coffeeWeight = 22f,
        balance = Balance.Acid,
        level = Level.Strong,
        createAt = LocalDate(2026, 1, 18)
    ),
    Recipe(
        id = 4,
        ratio = 15,
        coffeeWeight = 20f,
        balance = Balance.Basic,
        level = Level.Basic,
        isIcedDrip = true,
        hotRatio = 10,
        createAt = LocalDate(2026, 1, 19)
    )
)