package com.yopachara.fourtosixmethod.feature.timer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yopachara.fourtosixmethod.core.data.model.Balance
import com.yopachara.fourtosixmethod.core.data.model.Level
import com.yopachara.fourtosixmethod.core.data.model.Recipe
import com.yopachara.fourtosixmethod.core.domain.InsertRecipeUseCase
import com.yopachara.fourtosixmethod.feature.timer.state.TimerDisplayState
import com.yopachara.fourtosixmethod.feature.timer.state.TimerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val insertRecipeUseCase: InsertRecipeUseCase,
) : ViewModel() {

    private var job: Job? = null
    private var isStopRequested = false
    private var currentGeneration = 0

    private val _timerDisplayStateFlow = MutableStateFlow(TimerDisplayState())
    val timerDisplayStateFlow: StateFlow<TimerDisplayState> = _timerDisplayStateFlow

    /**
     * Emits [totalSeconds] (or the resume point, if [continueFrom] is set) immediately,
     * then one value per second down to 0. Conflated so a slow collector never delays
     * the next tick.
     */
    private fun tickerFlow(totalSeconds: Int, continueFrom: Int? = null): Flow<Int> = flow {
        if (continueFrom == null) emit(totalSeconds)
        val firstTick = totalSeconds - (continueFrom ?: 0) - 1
        for (remaining in firstTick downTo 0) {
            delay(10)
            emit(remaining)
        }
    }.conflate()

    fun stopTime() {
        isStopRequested = true
        job?.cancel()
        job = null
        _timerDisplayStateFlow.update {
            it.copy(secondsRemaining = null, seconds = null, timerState = TimerState.Stop)
        }
    }

    fun toggleTime() {
        if (job?.isActive == true) {
            job?.cancel()
            job = null
            return
        }

        isStopRequested = false
        val generation = ++currentGeneration
        job = viewModelScope.launch {
            val totalTime = _timerDisplayStateFlow.value.recipe.getTotalTime()
            val continueFrom = _timerDisplayStateFlow.value.seconds

            tickerFlow(totalTime, continueFrom)
                .map { remainingTime ->
                    _timerDisplayStateFlow.value.copy(
                        secondsRemaining = remainingTime,
                        seconds = totalTime - remainingTime,
                        totalSeconds = totalTime,
                        timerState = TimerState.Play,
                    )
                }
                .onStart {
                    _timerDisplayStateFlow.update { it.copy(timerState = TimerState.Play) }
                }
                .onCompletion {
                    if (isStopRequested || generation != currentGeneration) return@onCompletion
                    if (_timerDisplayStateFlow.value.isComplete()) {
                        _timerDisplayStateFlow.update {
                            it.copy(secondsRemaining = null, seconds = null, timerState = TimerState.Stop)
                        }
                        viewModelScope.launch {
                            insertRecipeUseCase(_timerDisplayStateFlow.value.recipe)
                        }
                    } else {
                        _timerDisplayStateFlow.update { it.copy(timerState = TimerState.Pause) }
                    }
                }
                .collect { tickedState ->
                    if (generation != currentGeneration) return@collect
                    _timerDisplayStateFlow.update { tickedState }
                }
        }
    }

    private inline fun updateRecipe(transform: (Recipe) -> Recipe) {
        _timerDisplayStateFlow.update { it.copy(recipe = transform(it.recipe)) }
    }

    fun setCoffeeWeight(value: Float) = updateRecipe { it.copy(coffeeWeight = value) }

    fun setCoffeeRatio(value: Int) = updateRecipe { it.copy(ratio = value) }

    fun setCoffeeBalance(value: Balance) = updateRecipe { it.copy(balance = value) }

    fun setCoffeeLevel(value: Level) = updateRecipe { it.copy(level = value) }

}