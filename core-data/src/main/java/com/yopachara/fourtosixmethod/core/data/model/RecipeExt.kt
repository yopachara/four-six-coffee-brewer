package com.yopachara.fourtosixmethod.core.data.model

import com.yopachara.fourtosixmethod.core.database.model.RecipeEntity


fun Recipe.asEntity(): RecipeEntity {
    return RecipeEntity(
        id = id,
        steps = getDefaultSteps().map { it.asEntity() },
        createAt = createAt,
        ratio = ratio,
        coffeeWeight = coffeeWeight,
        balance = balance,
        level = level
    )
}