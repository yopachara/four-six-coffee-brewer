package com.yopachara.fourtosixmethod.feature.timer.viewmodel

import androidx.lifecycle.ViewModel
import com.yopachara.fourtosixmethod.core.data.model.Balance
import com.yopachara.fourtosixmethod.core.data.model.Level
import com.yopachara.fourtosixmethod.core.domain.InsertRecipeUseCase
import com.yopachara.fourtosixmethod.feature.timer.state.TimerDisplayState
import com.yopachara.fourtosixmethod.feature.timer.state.TimerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val insertRecipeUseCase: InsertRecipeUseCase,
    private val timerScope: CoroutineScope,
) : ViewModel() {

    private var job: Job? = null
    private var isStopRequested = false

    private var _timerDisplayStateFlow = MutableStateFlow(TimerDisplayState())
    val timerDisplayStateFlow: StateFlow<TimerDisplayState> = _timerDisplayStateFlow

    /**
     * The timer emits the total seconds immediately.
     * Each second after that, it will emit the next value.
     */
    private fun initTimer(
        totalSeconds: Int,
        continueTime: Int? = null,
        onTick: (Int) -> TimerDisplayState,
    ): Flow<TimerDisplayState> {
        val second = totalSeconds.minus(continueTime ?: 0)
        return (second - 1 downTo 0).asFlow() // Emit total - 1 because the first was emitted onStart
            .onEach { delay(1000) } // Each second later emit a number
            .onStart {
                if (continueTime == null) emit(totalSeconds)
            } // Emit total seconds immediately
            .conflate() // In case the operation onTick takes some time, conflate keeps the time ticking separately
            .transform { remainingSeconds: Int ->
                emit(onTick(remainingSeconds))
            }
    }

    fun stopTime() {
        isStopRequested = true
        job?.cancel()
        job = null
        _timerDisplayStateFlow.update { currentState ->
            TimerDisplayState(
                secondsRemaining = null,
                seconds = null,
                timerState = TimerState.Stop,
                recipe = currentState.recipe // Retain current recipe selection on stop
            )
        }
    }

    fun toggleTime() {
        job = if (job == null || job?.isCompleted == true) {
            isStopRequested = false
            timerScope.launch {
                initTimer(
                    _timerDisplayStateFlow.value.recipe.getTotalTime(),
                    _timerDisplayStateFlow.value.seconds
                ) { remainingTime ->
                    val second = _timerDisplayStateFlow.value.recipe.getTotalTime() - remainingTime
                    _timerDisplayStateFlow.value.copy(
                        secondsRemaining = remainingTime,
                        seconds = second,
                        timerState = TimerState.Play
                    )
                }.onStart {
                    _timerDisplayStateFlow.update {
                        it.copy(timerState = TimerState.Play)
                    }
                }.onCompletion {
                    if (isStopRequested) return@onCompletion
                    if (_timerDisplayStateFlow.value.isComplete()) {
                        _timerDisplayStateFlow.update {
                            it.copy(
                                secondsRemaining = null,
                                seconds = null,
                                timerState = TimerState.Stop
                            )
                        }
                        timerScope.launch {
                            insertRecipeUseCase(_timerDisplayStateFlow.value.recipe)
                        }
                    } else {
                        _timerDisplayStateFlow.update {
                            it.copy(timerState = TimerState.Pause)
                        }
                    }
                }.collect { tickedState ->
                    _timerDisplayStateFlow.update { tickedState }
                }
            }
        } else {
            job?.cancel()
            null
        }
    }

    fun setCoffeeWeight(value: Float) {
        _timerDisplayStateFlow.update { state ->
            state.copy(recipe = state.recipe.copy(coffeeWeight = value))
        }
    }

    fun setCoffeeRatio(value: Int) {
        _timerDisplayStateFlow.update { state ->
            state.copy(recipe = state.recipe.copy(ratio = value))
        }
    }

    fun setCoffeeBalance(value: Balance) {
        _timerDisplayStateFlow.update { state ->
            state.copy(recipe = state.recipe.copy(balance = value))
        }
    }

    fun setCoffeeLevel(value: Level) {
        _timerDisplayStateFlow.update { state ->
            state.copy(recipe = state.recipe.copy(level = value))
        }
    }

}