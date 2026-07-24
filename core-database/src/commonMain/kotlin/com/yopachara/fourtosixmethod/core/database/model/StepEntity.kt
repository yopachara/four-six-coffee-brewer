package com.yopachara.fourtosixmethod.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yopachara.fourtosixmethod.core.data.model.State
import com.yopachara.fourtosixmethod.core.data.model.Step
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class StepEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val time: Int,
    @ColumnInfo(name = "water_weight")
    val waterWeight: Float,
    @ColumnInfo(name = "step_percentage")
    val stepPercentage: Float,
    val state: State,
)

fun StepEntity.asExternalModel() = Step(
    id = id,
    time = time,
    waterWeight = waterWeight,
    stepPercentage = stepPercentage,
    state = state
)