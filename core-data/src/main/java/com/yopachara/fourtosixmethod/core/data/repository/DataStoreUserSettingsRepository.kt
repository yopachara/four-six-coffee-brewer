package com.yopachara.fourtosixmethod.core.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.yopachara.fourtosixmethod.core.data.model.AccentColor
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
    }

    override val userSettings: Flow<UserSettings> =
        context.userSettingsDataStore.data.map { preferences ->
            UserSettings(
                themeConfig = preferences[Keys.THEME_CONFIG]
                    ?.let { name -> ThemeConfig.entries.find { it.name == name } }
                    ?: ThemeConfig.SYSTEM,
                accentColor = preferences[Keys.ACCENT_COLOR]
                    ?.let { name -> AccentColor.entries.find { it.name == name } }
                    ?: AccentColor.CLAY,
                stepsDefaultExpanded = preferences[Keys.STEPS_DEFAULT_EXPANDED] ?: false,
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
}
