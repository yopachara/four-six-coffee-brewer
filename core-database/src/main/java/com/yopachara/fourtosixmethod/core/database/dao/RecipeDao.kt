package com.yopachara.fourtosixmethod.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yopachara.fourtosixmethod.core.database.model.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity)

    @Query(value = "SELECT * FROM recipes")
    fun getRecipeList(): Flow<List<RecipeEntity>>
}