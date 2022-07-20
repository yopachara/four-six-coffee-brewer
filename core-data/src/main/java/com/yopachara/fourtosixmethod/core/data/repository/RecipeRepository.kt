package com.yopachara.fourtosixmethod.core.data.repository

import com.yopachara.fourtosixmethod.core.database.dao.RecipeDao
import com.yopachara.fourtosixmethod.core.database.model.Recipe
import com.yopachara.fourtosixmethod.core.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class RecipeRepository internal constructor(
    private val recipeDao: RecipeDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun getListRecipe(): Result<List<Recipe>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(recipeDao.getRecipeList().sortedByDescending {
                it.createAt
            })
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun saveRecipe(recipe: Recipe) = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(recipeDao.saveRecipe(recipe.apply {
                createAt = Date()
            }))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}