package com.yopachara.fourtosixmethod.core.database.di

import android.app.Application
import androidx.room.Room
import com.yopachara.fourtosixmethod.core.database.AppDatabase
import com.yopachara.fourtosixmethod.core.database.dao.RecipeDao
import com.yopachara.fourtosixmethod.core.database.model.DateConverter
import com.yopachara.fourtosixmethod.core.database.model.StateListConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        application: Application,
        stateListConverter: StateListConverter,
        dateConverter: DateConverter,
    ): AppDatabase {
        return Room
            .databaseBuilder(application, AppDatabase::class.java, "Recipe.db")
            .fallbackToDestructiveMigration()
            .addTypeConverter(stateListConverter)
            .addTypeConverter(dateConverter)
            .build()
    }

    @Provides
    @Singleton
    fun provideRecipeDao(appDatabase: AppDatabase): RecipeDao {
        return appDatabase.recipeDao()
    }
}