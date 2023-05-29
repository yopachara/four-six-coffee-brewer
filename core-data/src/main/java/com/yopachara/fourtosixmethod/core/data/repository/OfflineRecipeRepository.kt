package com.yopachara.fourtosixmethod.core.data.repository

import com.yopachara.fourtosixmethod.core.database.dao.RecipeDao
import com.yopachara.fourtosixmethod.core.database.model.Recipe
import com.yopachara.fourtosixmethod.core.database.model.asEntity
import com.yopachara.fourtosixmethod.core.database.model.asExternalModel
import com.yopachara.fourtosixmethod.core.network.Dispatcher
import com.yopachara.fourtosixmethod.core.network.FsmDispatchers
import com.yopachara.fourtosixmethod.core.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class OfflineRecipeRepository @Inject constructor(
    private val recipeDao: RecipeDao,
    @Dispatcher(FsmDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : RecipeRepository {
    override suspend fun getListRecipe(): Result<List<Recipe>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(recipeDao.getRecipeList().map {
                it.asExternalModel()
            }.sortedByDescending {
                it.createAt
            })
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun saveRecipe(recipe: Recipe) = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(recipeDao.insertRecipe(recipe.apply {
                createAt = Date()
            }.asEntity()))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}