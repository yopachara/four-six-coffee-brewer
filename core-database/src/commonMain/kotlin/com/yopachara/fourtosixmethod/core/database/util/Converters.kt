package com.yopachara.fourtosixmethod.core.database.util

import androidx.room.TypeConverter
import com.yopachara.fourtosixmethod.core.database.model.StepEntity
import kotlinx.datetime.LocalDate
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LocalDateConverter {
    // kotlinx.datetime.LocalDate.toString()/parse() use ISO-8601 (yyyy-MM-dd),
    // matching the previous DateTimeFormatter.ISO_LOCAL_DATE format, so stored data stays readable.
    @TypeConverter
    fun localDateToString(date: LocalDate): String {
        return date.toString()
    }

    @TypeConverter
    fun stringToLocalDate(value: String): LocalDate {
        return LocalDate.parse(value)
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