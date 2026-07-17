package com.yopachara.fourtosixmethod.core.data.repository

import com.yopachara.fourtosixmethod.core.data.model.Recipe
import com.yopachara.fourtosixmethod.core.result.Result
import kotlinx.coroutines.flow.Flow

interface RecipeRepository  {
    fun getListRecipe(): Flow<List<Recipe>>

    suspend fun saveRecipe(recipe: Recipe): Result<Unit>
}