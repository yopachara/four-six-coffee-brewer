package com.yopachara.fourtosixmethod

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yopachara.fourtosixmethod.core.data.model.UserSettings
import com.yopachara.fourtosixmethod.core.data.repository.UserSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    userSettingsRepository: UserSettingsRepository,
) : ViewModel() {

    val userSettings: StateFlow<UserSettings> = userSettingsRepository.userSettings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = UserSettings(),
        )
}
