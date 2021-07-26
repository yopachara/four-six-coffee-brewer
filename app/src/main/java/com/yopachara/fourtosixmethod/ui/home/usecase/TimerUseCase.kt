package com.yopachara.fourtosixmethod.ui.home.usecase

import com.yopachara.fourtosixmethod.data.Balance
import com.yopachara.fourtosixmethod.data.Level
import com.yopachara.fourtosixmethod.data.Recipe
import com.yopachara.fourtosixmethod.data.TimerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TimerUseCase(private val timerScope: CoroutineScope) {

    private var _timerStateFlow = MutableStateFlow(TimerState())
    val timerStateFlow: StateFlow<TimerState> = _timerStateFlow

    private var job: Job? = null

    fun toggleTime() {
        job = if (job == null || job?.isCompleted == true) {
            timerScope.launch {
                initTimer(_timerStateFlow.value.recipe.getTotalTime()) { remainingTime ->
                    val second = _timerStateFlow.value.recipe.getTotalTime() - remainingTime
                    _timerStateFlow.value.copy(
                        secondsRemaining = remainingTime,
                        seconds = second,
                    )
                }.onCompletion {
                    _timerStateFlow.emit(
                        _timerStateFlow.value.copy(secondsRemaining = null, seconds = null)
                    )
                }.collect { _timerStateFlow.emit(it) }
            }
        } else {
            job?.cancel()
            null
        }
    }

    fun setCoffeeWeight(value: Float) {
        _timerStateFlow.value = _timerStateFlow.value.copy(
            recipe = _timerStateFlow.value.recipe.apply {
                coffeeWeight = value
            }
        )
    }

    fun setCoffeeRatio(value: Int) {
        _timerStateFlow.value = _timerStateFlow.value.copy().apply {
            recipe.ratio = value
        }
    }

    fun setCoffeeBalance(value: Balance) {
        _timerStateFlow.value = _timerStateFlow.value.copy().apply {
            recipe.balance = value
        }
    }

    fun setCoffeeLevel(value: Level) {
        _timerStateFlow.value = _timerStateFlow.value.copy(
            recipe = _timerStateFlow.value.recipe.apply {
                level = value
            }
        )
    }

    /**
     * The timer emits the total seconds immediately.
     * Each second after that, it will emit the next value.
     */
    private fun <DisplayState> initTimer(
        totalSeconds: Int,
        onTick: (Int) -> DisplayState,
    ): Flow<DisplayState> =
//        generateSequence(totalSeconds - 1 ) { it - 1 }.asFlow()
        (totalSeconds - 1 downTo 0).asFlow() // Emit total - 1 because the first was emitted onStart
            .onEach { delay(100) } // Each second later emit a number
            .onStart { emit(totalSeconds) } // Emit total seconds immediately
            .conflate() // In case the operation onTick takes some time, conflate keeps the time ticking separately
            .transform { remainingSeconds: Int ->
                emit(onTick(remainingSeconds))
            }
}