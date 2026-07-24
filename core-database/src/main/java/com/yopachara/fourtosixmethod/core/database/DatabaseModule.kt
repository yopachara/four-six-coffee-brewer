package com.yopachara.fourtosixmethod.core.database

import android.content.Context
import androidx.room.Room
import com.yopachara.fourtosixmethod.core.database.dao.RecipeDao
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
                get<Context>(),
                AppDatabase::class.java,
                "recipe-database"
            ).fallbackToDestructiveMigration(true)
            .build()
    }

    single<RecipeDao> { get<AppDatabase>().recipeDao() }
}
