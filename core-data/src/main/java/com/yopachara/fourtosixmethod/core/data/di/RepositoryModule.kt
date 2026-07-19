package com.yopachara.fourtosixmethod.core.data.di

import com.yopachara.fourtosixmethod.core.data.repository.DataStoreUserSettingsRepository
import com.yopachara.fourtosixmethod.core.data.repository.OfflineRecipeRepository
import com.yopachara.fourtosixmethod.core.data.repository.RecipeRepository
import com.yopachara.fourtosixmethod.core.data.repository.UserSettingsRepository
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

    @Binds
    fun provideUserSettingsRepository(
        userSettingsRepository: DataStoreUserSettingsRepository,
    ): UserSettingsRepository

}