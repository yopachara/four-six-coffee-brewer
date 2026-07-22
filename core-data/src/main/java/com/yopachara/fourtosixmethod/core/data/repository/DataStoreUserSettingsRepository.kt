package com.yopachara.fourtosixmethod.core.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.yopachara.fourtosixmethod.core.data.model.AccentColor
import com.yopachara.fourtosixmethod.core.data.model.Balance
import com.yopachara.fourtosixmethod.core.data.model.Level
import com.yopachara.fourtosixmethod.core.data.model.RecipeSnapshot
import com.yopachara.fourtosixmethod.core.data.model.ThemeConfig
import com.yopachara.fourtosixmethod.core.data.model.UserSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.userSettingsDataStore by preferencesDataStore(name = "user_settings")

@Singleton
class DataStoreUserSettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) : UserSettingsRepository {

    private object Keys {
        val THEME_CONFIG = stringPreferencesKey("theme_config")
        val ACCENT_COLOR = stringPreferencesKey("accent_color")
        val STEPS_DEFAULT_EXPANDED = booleanPreferencesKey("steps_default_expanded")
        val LAST_RECIPE_RATIO = intPreferencesKey("last_recipe_ratio")
        val LAST_RECIPE_COFFEE_WEIGHT = floatPreferencesKey("last_recipe_coffee_weight")
        val LAST_RECIPE_BALANCE = stringPreferencesKey("last_recipe_balance")
        val LAST_RECIPE_LEVEL = stringPreferencesKey("last_recipe_level")
        val LAST_RECIPE_IS_ICED_DRIP = booleanPreferencesKey("last_recipe_is_iced_drip")
        val LAST_RECIPE_HOT_RATIO = intPreferencesKey("last_recipe_hot_ratio")
    }

    override val userSettings: Flow<UserSettings> =
        context.userSettingsDataStore.data.map { preferences ->
            val defaultRecipe = RecipeSnapshot()
            UserSettings(
                themeConfig = preferences[Keys.THEME_CONFIG]
                    ?.let { name -> ThemeConfig.entries.find { it.name == name } }
                    ?: ThemeConfig.SYSTEM,
                accentColor = preferences[Keys.ACCENT_COLOR]
                    ?.let { name -> AccentColor.entries.find { it.name == name } }
                    ?: AccentColor.CLAY,
                stepsDefaultExpanded = preferences[Keys.STEPS_DEFAULT_EXPANDED] ?: false,
                lastRecipe = RecipeSnapshot(
                    ratio = preferences[Keys.LAST_RECIPE_RATIO] ?: defaultRecipe.ratio,
                    coffeeWeight = preferences[Keys.LAST_RECIPE_COFFEE_WEIGHT] ?: defaultRecipe.coffeeWeight,
                    balance = preferences[Keys.LAST_RECIPE_BALANCE]
                        ?.let { name -> Balance.entries.find { it.name == name } }
                        ?: defaultRecipe.balance,
                    level = preferences[Keys.LAST_RECIPE_LEVEL]
                        ?.let { name -> Level.entries.find { it.name == name } }
                        ?: defaultRecipe.level,
                    isIcedDrip = preferences[Keys.LAST_RECIPE_IS_ICED_DRIP] ?: defaultRecipe.isIcedDrip,
                    hotRatio = preferences[Keys.LAST_RECIPE_HOT_RATIO] ?: defaultRecipe.hotRatio,
                ),
            )
        }

    override suspend fun setThemeConfig(themeConfig: ThemeConfig) {
        context.userSettingsDataStore.edit { it[Keys.THEME_CONFIG] = themeConfig.name }
    }

    override suspend fun setAccentColor(accentColor: AccentColor) {
        context.userSettingsDataStore.edit { it[Keys.ACCENT_COLOR] = accentColor.name }
    }

    override suspend fun setStepsDefaultExpanded(expanded: Boolean) {
        context.userSettingsDataStore.edit { it[Keys.STEPS_DEFAULT_EXPANDED] = expanded }
    }

    override suspend fun setLastRecipe(snapshot: RecipeSnapshot) {
        context.userSettingsDataStore.edit { preferences ->
            preferences[Keys.LAST_RECIPE_RATIO] = snapshot.ratio
            preferences[Keys.LAST_RECIPE_COFFEE_WEIGHT] = snapshot.coffeeWeight
            preferences[Keys.LAST_RECIPE_BALANCE] = snapshot.balance.name
            preferences[Keys.LAST_RECIPE_LEVEL] = snapshot.level.name
            preferences[Keys.LAST_RECIPE_IS_ICED_DRIP] = snapshot.isIcedDrip
            preferences[Keys.LAST_RECIPE_HOT_RATIO] = snapshot.hotRatio
        }
    }
}
