package com.yopachara.fourtosixmethod.feature.timer

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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val insertRecipeUseCase: InsertRecipeUseCase,
    private val timerScope: CoroutineScope,
) : ViewModel() {

    private var job: Job? = null

    fun setWeight(value: Float) = setCoffeeWeight(value)
    fun setRatio(value: Int) = setCoffeeRatio(value)
    fun setBalance(value: Balance) = setCoffeeBalance(value)
    fun setLevel(value: Level) = setCoffeeLevel(value)


    private var _timerDisplayStateFlow = MutableStateFlow(TimerDisplayState())
    val timerDisplayStateFlow: StateFlow<TimerDisplayState> = _timerDisplayStateFlow


    /**
     * The timer emits the total seconds immediately.
     * Each second after that, it will emit the next value.
     */
    fun <DisplayState> initTimer(
        totalSeconds: Int,
        continueTime: Int? = null,
        onTick: (Int) -> DisplayState,
    ): Flow<DisplayState> {
        val startSecond = continueTime?.let {
            totalSeconds?.minus(it)
        } ?: totalSeconds
        return (startSecond - 1 downTo 0).asFlow() // Emit total - 1 because the first was emitted onStart
            .onEach { delay(100) } // Each second later emit a number
            .onStart {
                if (continueTime == null) emit(totalSeconds)
            } // Emit total seconds immediately
            .conflate() // In case the operation onTick takes some time, conflate keeps the time ticking separately
            .transform { remainingSeconds: Int ->
                emit(onTick(remainingSeconds))
            }
    }

    fun stopTime() {
        job?.cancel()
        job = null
        timerScope.launch {
            _timerDisplayStateFlow.emit(
                TimerDisplayState(
                    secondsRemaining = null,
                    seconds = null,
                    timerState = TimerState.Stop
                )
            )

        }
    }

    fun toggleTime() {
        job = if (job == null || job?.isCompleted == true) {
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
                    timerDisplayStateFlow.value.copy(
                        timerState = TimerState.Play
                    )
                }.onCompletion {
                    if (timerDisplayStateFlow.value.isComplete()) {
                        _timerDisplayStateFlow.emit(
                            _timerDisplayStateFlow.value.copy(
                                secondsRemaining = null,
                                seconds = null,
                                timerState = TimerState.Stop
                            )

                        )
                        timerScope.launch {
                            insertRecipeUseCase(timerDisplayStateFlow.value.recipe)
                        }
                    } else {
                        _timerDisplayStateFlow.emit(
                            _timerDisplayStateFlow.value.copy(timerState = TimerState.Pause)
                        )
                    }
                }.collect {
                    _timerDisplayStateFlow.emit(it)
                }
            }
        } else {
            job?.cancel()
            null
        }
    }

    fun setCoffeeWeight(value: Float) {
        _timerDisplayStateFlow.value = _timerDisplayStateFlow.value.copy(
            recipe = _timerDisplayStateFlow.value.recipe.apply {
                coffeeWeight = value
            }
        )
    }

    fun setCoffeeRatio(value: Int) {
        _timerDisplayStateFlow.value = _timerDisplayStateFlow.value.copy().apply {
            recipe.ratio = value
        }
    }

    fun setCoffeeBalance(value: Balance) {
        _timerDisplayStateFlow.value = _timerDisplayStateFlow.value.copy().apply {
            recipe.balance = value
        }
    }

    fun setCoffeeLevel(value: Level) {
        _timerDisplayStateFlow.value = _timerDisplayStateFlow.value.copy(
            recipe = _timerDisplayStateFlow.value.recipe.apply {
                level = value
            }
        )
    }

}