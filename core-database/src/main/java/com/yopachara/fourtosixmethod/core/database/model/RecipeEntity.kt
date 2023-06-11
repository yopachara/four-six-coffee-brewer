package com.yopachara.fourtosixmethod.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yopachara.fourtosixmethod.core.data.model.Balance
import com.yopachara.fourtosixmethod.core.data.model.Level
import com.yopachara.fourtosixmethod.core.data.model.Recipe
import java.util.Date

@Entity(tableName = "recipes")
class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "steps")
    var steps: List<StepEntity>,
    @ColumnInfo(name = "createAt")
    var createAt: Date = Date(),
    @ColumnInfo(name = "ratio")
    var ratio: Int,
    @ColumnInfo(name = "coffee_weight")
    var coffeeWeight: Float,
    @ColumnInfo(name = "balance")
    var balance: Balance,
    @ColumnInfo(name = "level")
    var level: Level
)

fun RecipeEntity.asExternalModel() = Recipe(
    id = id,
    createAt = createAt,
    _ratio = ratio,
    _coffeeWeight = coffeeWeight,
    _balance = balance,
    _level = level,
)
