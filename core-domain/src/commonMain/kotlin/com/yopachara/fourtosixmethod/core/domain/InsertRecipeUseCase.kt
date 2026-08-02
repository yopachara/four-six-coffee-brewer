package com.yopachara.fourtosixmethod.core.domain

import com.yopachara.fourtosixmethod.core.data.model.Recipe
import com.yopachara.fourtosixmethod.core.data.repository.RecipeRepository
import com.yopachara.fourtosixmethod.core.result.Result

class InsertRecipeUseCase(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(recipe: Recipe): Result<Unit> =
        repository.saveRecipe(recipe)
}