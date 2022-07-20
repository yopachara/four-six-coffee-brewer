package com.yopachara.fourtosixmethod.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yopachara.fourtosixmethod.core.database.dao.RecipeDao
import com.yopachara.fourtosixmethod.core.database.model.DateConverter
import com.yopachara.fourtosixmethod.core.database.model.Recipe
import com.yopachara.fourtosixmethod.core.database.model.StateListConverter

@TypeConverters(StateListConverter::class, DateConverter::class)
@Database(entities = [Recipe::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao
}