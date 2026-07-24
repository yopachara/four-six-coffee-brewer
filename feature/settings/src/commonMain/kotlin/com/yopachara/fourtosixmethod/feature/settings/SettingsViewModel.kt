
package com.yopachara.fourtosixmethod.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yopachara.fourtosixmethod.core.data.model.AccentColor
import com.yopachara.fourtosixmethod.core.data.model.ThemeConfig
import com.yopachara.fourtosixmethod.core.data.model.UserSettings
import com.yopachara.fourtosixmethod.core.data.repository.UserSettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val userSettingsRepository: UserSettingsRepository,
) : ViewModel() {

    val settingsUiState: StateFlow<UserSettings> = userSettingsRepository.userSettings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UserSettings(),
        )

    fun setThemeConfig(themeConfig: ThemeConfig) = viewModelScope.launch {
        userSettingsRepository.setThemeConfig(themeConfig)
    }

    fun setAccentColor(accentColor: AccentColor) = viewModelScope.launch {
        userSettingsRepository.setAccentColor(accentColor)
    }

    fun setStepsDefaultExpanded(expanded: Boolean) = viewModelScope.launch {
        userSettingsRepository.setStepsDefaultExpanded(expanded)
    }
}
