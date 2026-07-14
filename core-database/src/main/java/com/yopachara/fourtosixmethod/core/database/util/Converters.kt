package com.yopachara.fourtosixmethod.core.database.util

import androidx.room.TypeConverter
import com.yopachara.fourtosixmethod.core.database.model.StepEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateConverter {
    @TypeConverter
    fun localDateToString(date: LocalDate): String {
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE)
    }

    @TypeConverter
    fun stringToLocalDate(value: String): LocalDate {
        return LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE)
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