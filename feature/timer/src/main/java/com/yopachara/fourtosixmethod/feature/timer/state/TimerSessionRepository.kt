package com.yopachara.fourtosixmethod.feature.timer.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

/**
 * App-scoped holder for [TimerDisplayState] so the UI-bound [TimerViewModel]
 * and the process-independent [TimerForegroundService] observe/mutate the
 * same state instead of two divergent copies.
 */
@Singleton
class TimerSessionRepository @Inject constructor() {

    private val _state = MutableStateFlow(TimerDisplayState())
    val state: StateFlow<TimerDisplayState> = _state.asStateFlow()

    fun update(transform: (TimerDisplayState) -> TimerDisplayState) {
        _state.update(transform)
    }
}
