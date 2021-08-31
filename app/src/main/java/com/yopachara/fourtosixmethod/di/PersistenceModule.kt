package com.yopachara.fourtosixmethod.di

import android.app.Application
import androidx.room.Room
import com.yopachara.fourtosixmethod.data.DateConverter
import com.yopachara.fourtosixmethod.data.StateListConverter
import com.yopachara.fourtosixmethod.persistence.AppDatabase
import com.yopachara.fourtosixmethod.persistence.RecipeDao
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