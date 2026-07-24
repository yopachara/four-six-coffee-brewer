package com.yopachara.fourtosixmethod.core.data.repository

import com.yopachara.fourtosixmethod.core.data.model.AccentColor
import com.yopachara.fourtosixmethod.core.data.model.RecipeSnapshot
import com.yopachara.fourtosixmethod.core.data.model.ThemeConfig
import com.yopachara.fourtosixmethod.core.data.model.UserSettings
import kotlinx.coroutines.flow.Flow

interface UserSettingsRepository {

    val userSettings: Flow<UserSettings>

    suspend fun setThemeConfig(themeConfig: ThemeConfig)

    suspend fun setAccentColor(accentColor: AccentColor)

    suspend fun setStepsDefaultExpanded(expanded: Boolean)

    suspend fun setLastRecipe(snapshot: RecipeSnapshot)
}
