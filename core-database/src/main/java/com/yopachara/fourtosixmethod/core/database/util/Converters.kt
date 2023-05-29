package com.yopachara.fourtosixmethod.core.database.util

import androidx.room.TypeConverter
import com.yopachara.fourtosixmethod.core.database.model.StepEntity
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.Date

class DateConverter {
    @TypeConverter
    fun recipeToString(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun stringToRecipe(value: Long): Date {
        return Date(value)
    }
}

class StateListConverter {
    @TypeConverter
    fun stateToString(recipe: List<StepEntity>): String {
        return Json.encodeToString(recipe)
    }

    @TypeConverter
    fun stringToState(value: String): List<StepEntity> {
        val result = Json.decodeFromString<List<StepEntity>>(value)
        return result
    }
}