package com.yopachara.fourtosixmethod.core.data.model

import com.yopachara.fourtosixmethod.core.database.model.StepEntity

fun Step.asEntity(): StepEntity {
    return StepEntity(
        id = id,
        time = time,
        waterWeight = waterWeight,
        stepPercentage = stepPercentage,
        state = state
    )
}