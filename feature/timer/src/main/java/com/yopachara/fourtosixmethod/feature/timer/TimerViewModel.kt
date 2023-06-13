package com.yopachara.fourtosixmethod.feature.timer

import androidx.lifecycle.ViewModel
import com.yopachara.fourtosixmethod.core.data.model.Balance
import com.yopachara.fourtosixmethod.core.data.model.Level
import com.yopachara.fourtosixmethod.core.data.model.Recipe
import com.yopachara.fourtosixmethod.core.data.model.TimerState
import com.yopachara.fourtosixmethod.core.domain.GetRecipeHistoryListUseCase
import com.yopachara.fourtosixmethod.core.domain.InsertRecipeUseCase
import com.yopachara.fourtosixmethod.core.result.*
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


    private var _timerStateFlow = MutableStateFlow(TimerState())
    val timerStateFlow: StateFlow<TimerState> = _timerStateFlow


    /**
     * The timer emits the total seconds immediately.
     * Each second after that, it will emit the next value.
     */
    fun <DisplayState> initTimer(
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


    fun toggleTime() {

        job = if (job == null || job?.isCompleted == true) {
            timerScope.launch {
                initTimer(_timerStateFlow.value.recipe.getTotalTime()) { remainingTime ->
                    val second = _timerStateFlow.value.recipe.getTotalTime() - remainingTime
                    _timerStateFlow.value.copy(
                        secondsRemaining = remainingTime,
                        seconds = second,
                    )
                }.onStart {
                    timerScope.launch {
                        insertRecipeUseCase(timerStateFlow.value.recipe)
                    }
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

}