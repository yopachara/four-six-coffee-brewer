package com.yopachara.fourtosixmethod.core.data.model

import com.yopachara.fourtosixmethod.core.database.model.RecipeEntity


fun Recipe.asEntity(): RecipeEntity {
    return RecipeEntity(
        id = id,
        // This recipe's own schedule, not getDefaultSteps() - that stored the stock
        // 5x36g Basic schedule against every saved brew regardless of its settings.
        steps = steps.map { it.asEntity() },
        createAt = createAt,
        ratio = ratio,
        coffeeWeight = coffeeWeight,
        balance = balance,
        level = level,
        isIcedDrip = isIcedDrip,
        hotRatio = hotRatio
    )
}