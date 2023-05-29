package com.yopachara.fourtosixmethod.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yopachara.fourtosixmethod.core.database.dao.RecipeDao
import com.yopachara.fourtosixmethod.core.database.model.RecipeEntity
import com.yopachara.fourtosixmethod.core.database.util.DateConverter
import com.yopachara.fourtosixmethod.core.database.util.StateListConverter

@Database(entities = [RecipeEntity::class], version = 1)
@TypeConverters(StateListConverter::class, DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao
}