package com.yopachara.fourtosixmethod.feature.timer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yopachara.fourtosixmethod.core.data.model.Balance
import com.yopachara.fourtosixmethod.core.data.model.Level
import com.yopachara.fourtosixmethod.core.data.model.Recipe
import com.yopachara.fourtosixmethod.core.data.model.RecipeSnapshot
import com.yopachara.fourtosixmethod.core.data.model.toSnapshot
import com.yopachara.fourtosixmethod.core.data.repository.UserSettingsRepository
import com.yopachara.fourtosixmethod.feature.timer.service.TimerController
import com.yopachara.fourtosixmethod.feature.timer.state.TimerSessionRepository
import com.yopachara.fourtosixmethod.feature.timer.state.TimerDisplayState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val SETTINGS_WRITE_DEBOUNCE_MS = 300L

class TimerViewModel(
    private val sessionRepository: TimerSessionRepository,
    private val controller: TimerController,
    private val userSettingsRepository: UserSettingsRepository,
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

    // Sliders emit continuously while dragged, and every emission used to be its own
    // DataStore write. Debouncing collapses a drag into a single write once it settles;
    // the in-memory session state still updates on every emission, so the UI is unaffected.
    private val pendingSnapshot = MutableStateFlow<RecipeSnapshot?>(null)

    init {
        viewModelScope.launch { writePendingSnapshots() }
    }

    @OptIn(FlowPreview::class) // debounce is still preview API as of coroutines 1.10.2
    private suspend fun writePendingSnapshots() {
        pendingSnapshot.filterNotNull()
            .debounce(SETTINGS_WRITE_DEBOUNCE_MS)
            .collect { userSettingsRepository.setLastRecipe(it) }
    }

    private inline fun updateRecipe(crossinline transform: (Recipe) -> Recipe) {
        sessionRepository.update { it.copy(recipe = transform(it.recipe)) }
        pendingSnapshot.value = sessionRepository.state.value.recipe.toSnapshot()
    }

    fun setCoffeeWeight(value: Float) = updateRecipe { it.copy(coffeeWeight = value) }

    fun setCoffeeRatio(value: Int) = updateRecipe { it.copy(ratio = value) }

    fun setCoffeeBalance(value: Balance) = updateRecipe { it.copy(balance = value) }

    fun setCoffeeLevel(value: Level) = updateRecipe { it.copy(level = value) }

    fun setIcedDrip(value: Boolean) = updateRecipe { it.copy(isIcedDrip = value) }

    fun setHotRatio(value: Int) = updateRecipe { it.copy(hotRatio = value) }
}