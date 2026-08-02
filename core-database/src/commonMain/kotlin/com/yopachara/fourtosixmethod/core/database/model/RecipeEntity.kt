package com.yopachara.fourtosixmethod.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yopachara.fourtosixmethod.core.data.model.Balance
import com.yopachara.fourtosixmethod.core.data.model.Level
import com.yopachara.fourtosixmethod.core.data.model.Recipe
import com.yopachara.fourtosixmethod.core.data.model.currentDate
import kotlinx.datetime.LocalDate

@Entity(tableName = "recipes")
class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "steps")
    var steps: List<StepEntity>,
    @ColumnInfo(name = "createAt")
    var createAt: LocalDate = currentDate(),
    @ColumnInfo(name = "ratio")
    var ratio: Int,
    @ColumnInfo(name = "coffee_weight")
    var coffeeWeight: Float,
    @ColumnInfo(name = "balance")
    var balance: Balance,
    @ColumnInfo(name = "level")
    var level: Level,
    @ColumnInfo(name = "is_iced_drip")
    var isIcedDrip: Boolean = false,
    @ColumnInfo(name = "hot_ratio")
    var hotRatio: Int = 10
)

fun RecipeEntity.asExternalModel() = Recipe(
    id = id,
    createAt = createAt,
    ratio = ratio,
    coffeeWeight = coffeeWeight,
    balance = balance,
    level = level,
    isIcedDrip = isIcedDrip,
    hotRatio = hotRatio,
)
