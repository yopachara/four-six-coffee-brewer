package com.yopachara.fourtosixmethod.core.data.repository

import com.yopachara.fourtosixmethod.core.data.model.Recipe
import com.yopachara.fourtosixmethod.core.data.model.asEntity
import com.yopachara.fourtosixmethod.core.data.model.currentDate
import com.yopachara.fourtosixmethod.core.database.dao.RecipeDao
import com.yopachara.fourtosixmethod.core.database.model.RecipeEntity
import com.yopachara.fourtosixmethod.core.database.model.asExternalModel
import com.yopachara.fourtosixmethod.core.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class OfflineRecipeRepository(
    private val recipeDao: RecipeDao,
    private val ioDispatcher: CoroutineDispatcher,
) : RecipeRepository {
    override fun getListRecipe(): Flow<List<Recipe>> = recipeDao.getRecipeList()
        .map { entities ->
            entities.map<RecipeEntity, Recipe> { it.asExternalModel() }
                .sortedByDescending { it.createAt }
        }
        .flowOn(ioDispatcher)

    override suspend fun saveRecipe(recipe: Recipe) = withContext(ioDispatcher) {
        return@withContext try {
            // copy(), not apply {}: the caller's Recipe is the mutable instance held in
            // the timer's session StateFlow, and every already-published state shares
            // that reference - stamping createAt in place would edit them all silently.
            Result.Success(recipeDao.insertRecipe(recipe.copy(createAt = currentDate()).asEntity()))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}