package com.yopachara.fourtosixmethod.core.data.repository

import com.yopachara.fourtosixmethod.core.data.model.Recipe
import com.yopachara.fourtosixmethod.core.result.Result

interface RecipeRepository  {
    suspend fun getListRecipe(): Result<List<Recipe>>

    suspend fun saveRecipe(recipe: Recipe): Result<Unit>
}