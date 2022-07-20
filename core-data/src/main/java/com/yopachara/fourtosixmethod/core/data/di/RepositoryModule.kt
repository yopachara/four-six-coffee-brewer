package com.yopachara.fourtosixmethod.core.data.di

import com.yopachara.fourtosixmethod.core.data.repository.RecipeRepository
import com.yopachara.fourtosixmethod.core.database.dao.RecipeDao
import com.yopachara.fourtosixmethod.core.network.Dispatcher
import com.yopachara.fourtosixmethod.core.network.FsmDispatchers
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
        @Dispatcher(FsmDispatchers.IO) dispatcher: CoroutineDispatcher
    ): RecipeRepository {
        return RecipeRepository(recipeDao, dispatcher)
    }

}