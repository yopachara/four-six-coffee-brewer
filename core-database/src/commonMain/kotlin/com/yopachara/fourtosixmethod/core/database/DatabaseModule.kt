package com.yopachara.fourtosixmethod.core.database

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.yopachara.fourtosixmethod.core.database.dao.RecipeDao
import com.yopachara.fourtosixmethod.core.network.ioDispatcher
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Common Room wiring: finalizes the platform-provided [RoomDatabase.Builder] with the bundled
 * SQLite driver and exposes the DAO. The builder itself comes from [platformDatabaseModule]
 * because its construction (file path / Android [android.content.Context]) is platform-specific.
 */
val databaseModule = module {
    single<AppDatabase> {
        get<RoomDatabase.Builder<AppDatabase>>()
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(ioDispatcher)
            .fallbackToDestructiveMigration(true)
            .build()
    }

    single<RecipeDao> { get<AppDatabase>().recipeDao() }
}

expect val platformDatabaseModule: Module
