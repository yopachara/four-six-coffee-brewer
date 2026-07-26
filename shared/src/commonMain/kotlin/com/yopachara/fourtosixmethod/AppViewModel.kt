package com.yopachara.fourtosixmethod

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yopachara.fourtosixmethod.core.data.model.UserSettings
import com.yopachara.fourtosixmethod.core.data.repository.UserSettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class AppViewModel(
    userSettingsRepository: UserSettingsRepository,
) : ViewModel() {

    val userSettings: StateFlow<UserSettings> = userSettingsRepository.userSettings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = UserSettings(),
        )
}
