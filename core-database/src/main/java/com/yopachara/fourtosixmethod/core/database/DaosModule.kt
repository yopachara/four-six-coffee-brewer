package com.yopachara.fourtosixmethod.core.database

import com.yopachara.fourtosixmethod.core.database.dao.RecipeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {

    @Provides
    fun provideRecipeDao(
        appDatabase: AppDatabase
    ): RecipeDao = appDatabase.recipeDao()

}