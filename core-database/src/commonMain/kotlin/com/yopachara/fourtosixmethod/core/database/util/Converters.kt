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
        return json.encodeToString(recipe)
    }

    @TypeConverter
    fun stringToState(value: String): List<StepEntity> {
        return json.decodeFromString<List<StepEntity>>(value)
    }
}

// This JSON is a persisted format, so decoding has to survive StepEntity gaining a field:
// strict decoding would reject every previously-stored row the moment one is added.
private val json = Json { ignoreUnknownKeys = true }