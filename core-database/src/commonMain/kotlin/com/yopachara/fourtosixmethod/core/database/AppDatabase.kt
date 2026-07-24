package com.yopachara.fourtosixmethod.core.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.yopachara.fourtosixmethod.core.database.dao.RecipeDao
import com.yopachara.fourtosixmethod.core.database.model.RecipeEntity
import com.yopachara.fourtosixmethod.core.database.util.LocalDateConverter
import com.yopachara.fourtosixmethod.core.database.util.StateListConverter

@Database(entities = [RecipeEntity::class], version = 3)
@TypeConverters(StateListConverter::class, LocalDateConverter::class)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao
}

// Room's KSP compiler generates the `actual` for each target.
@Suppress("NO_ACTUAL_FOR_EXPECT", "KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}