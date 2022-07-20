package com.yopachara.fourtosixmethod.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yopachara.fourtosixmethod.core.database.model.Recipe

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRecipe(recipe: Recipe)

    @Query("SELECT * FROM Recipe")
    suspend fun getRecipeList(): List<Recipe>
}