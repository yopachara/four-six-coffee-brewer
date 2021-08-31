package com.yopachara.fourtosixmethod.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yopachara.fourtosixmethod.data.*

@TypeConverters(StateListConverter::class, DateConverter::class)
@Database(entities = [Recipe::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao
}