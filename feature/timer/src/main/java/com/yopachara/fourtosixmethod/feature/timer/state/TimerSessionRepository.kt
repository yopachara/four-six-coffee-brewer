package com.yopachara.fourtosixmethod.feature.timer.state

import com.yopachara.fourtosixmethod.core.data.model.toRecipe
import com.yopachara.fourtosixmethod.core.data.repository.UserSettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * App-scoped holder for [TimerDisplayState] so the UI-bound [TimerViewModel]
 * and the process-independent [TimerForegroundService] observe/mutate the
 * same state instead of two divergent copies.
 */
@Singleton
class TimerSessionRepository @Inject constructor(
    userSettingsRepository: UserSettingsRepository,
) {

    private val _state = MutableStateFlow(TimerDisplayState())
    val state: StateFlow<TimerDisplayState> = _state.asStateFlow()

    // App-scoped: seeds the recipe from the last-used settings once at process
    // start, before any screen has a chance to read the default TimerDisplayState.
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    init {
        scope.launch {
            val lastRecipe = userSettingsRepository.userSettings.first().lastRecipe.toRecipe()
            update { it.copy(recipe = lastRecipe) }
        }
    }

    fun update(transform: (TimerDisplayState) -> TimerDisplayState) {
        _state.update(transform)
    }
}
