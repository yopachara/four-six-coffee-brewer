package com.yopachara.fourtosixmethod.core.data.di

import com.yopachara.fourtosixmethod.core.data.repository.OfflineRecipeRepository
import com.yopachara.fourtosixmethod.core.data.repository.RecipeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun provideRecipeRepository(
        recipeRepository: OfflineRecipeRepository,
    ): RecipeRepository

}