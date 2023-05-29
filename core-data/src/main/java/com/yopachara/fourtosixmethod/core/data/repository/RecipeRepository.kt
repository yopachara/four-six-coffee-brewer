package com.yopachara.fourtosixmethod.core.data.repository

import com.yopachara.fourtosixmethod.core.database.dao.RecipeDao
import com.yopachara.fourtosixmethod.core.database.model.Recipe
import com.yopachara.fourtosixmethod.core.network.Dispatcher
import com.yopachara.fourtosixmethod.core.network.FsmDispatchers
import com.yopachara.fourtosixmethod.core.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

interface RecipeRepository  {
    suspend fun getListRecipe(): Result<List<Recipe>>

    suspend fun saveRecipe(recipe: Recipe): Result<Unit>
}