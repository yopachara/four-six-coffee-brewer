package com.yopachara.fourtosixmethod.core.domain

import com.yopachara.fourtosixmethod.core.data.model.Recipe
import com.yopachara.fourtosixmethod.core.data.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow

class GetRecipeHistoryListUseCase(
    private val repository: RecipeRepository,
) {
    operator fun invoke(): Flow<List<Recipe>> = repository.getListRecipe()
}