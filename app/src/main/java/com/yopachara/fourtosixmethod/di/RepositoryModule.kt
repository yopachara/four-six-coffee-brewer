package com.yopachara.fourtosixmethod.di

import com.yopachara.fourtosixmethod.persistence.RecipeDao
import com.yopachara.fourtosixmethod.repository.RecipeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideRecipeRepository(
        recipeDao: RecipeDao,
        coroutineDispatcher: CoroutineDispatcher
    ): RecipeRepository {
        return RecipeRepository(recipeDao, coroutineDispatcher)
    }

}