package com.yopachara.fourtosixmethod.feature.timer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yopachara.fourtosixmethod.core.data.model.Balance
import com.yopachara.fourtosixmethod.core.data.model.Level
import com.yopachara.fourtosixmethod.core.data.model.Recipe
import com.yopachara.fourtosixmethod.core.data.repository.UserSettingsRepository
import com.yopachara.fourtosixmethod.feature.timer.service.TimerController
import com.yopachara.fourtosixmethod.feature.timer.state.TimerSessionRepository
import com.yopachara.fourtosixmethod.feature.timer.state.TimerDisplayState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val sessionRepository: TimerSessionRepository,
    private val controller: TimerController,
    userSettingsRepository: UserSettingsRepository,
) : ViewModel() {

    val timerDisplayStateFlow: StateFlow<TimerDisplayState> = sessionRepository.state

    val stepsDefaultExpanded: StateFlow<Boolean> = userSettingsRepository.userSettings
        .map { it.stepsDefaultExpanded }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    fun toggleTime() = controller.toggle()

    fun stopTime() = controller.stop()

    private inline fun updateRecipe(crossinline transform: (Recipe) -> Recipe) {
        sessionRepository.update { it.copy(recipe = transform(it.recipe)) }
    }

    fun setCoffeeWeight(value: Float) = updateRecipe { it.copy(coffeeWeight = value) }

    fun setCoffeeRatio(value: Int) = updateRecipe { it.copy(ratio = value) }

    fun setCoffeeBalance(value: Balance) = updateRecipe { it.copy(balance = value) }

    fun setCoffeeLevel(value: Level) = updateRecipe { it.copy(level = value) }

    fun setIcedDrip(value: Boolean) = updateRecipe { it.copy(isIcedDrip = value) }

    fun setHotRatio(value: Int) = updateRecipe { it.copy(hotRatio = value) }
}