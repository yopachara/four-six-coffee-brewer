package com.yopachara.fourtosixmethod.ui.home.usecase

import com.yopachara.fourtosixmethod.core.data.model.Balance
import com.yopachara.fourtosixmethod.core.data.model.Level
import com.yopachara.fourtosixmethod.core.data.repository.RecipeRepository
import com.yopachara.fourtosixmethod.core.database.model.Recipe
import com.yopachara.fourtosixmethod.core.database.model.TimerState
import com.yopachara.fourtosixmethod.core.result.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class TimerUseCase @Inject constructor(
    private val timerScope: CoroutineScope,
    private val repository: RecipeRepository,
) {

    private var _timerStateFlow = MutableStateFlow(TimerState())
    private var _historyStateFlow = MutableStateFlow(emptyList<Recipe>())
    val timerStateFlow: StateFlow<TimerState> = _timerStateFlow
    val historyStateFlow: StateFlow<List<Recipe>> = _historyStateFlow

    private var job: Job? = null

    init {
        updateHistory()
    }

    private fun updateHistory() {
        timerScope.launch {
            when (val result = repository.getListRecipe()) {
                is Result.Error -> {
                    _historyStateFlow.value = emptyList()
                }
                is Result.Success -> {
                    _historyStateFlow.value = result.data.also {
                        _timerStateFlow.value.recipe.apply {
                            it.firstOrNull()?.let { latestRecipe ->
                                setCoffeeWeight(latestRecipe.coffeeWeight)
                                setCoffeeBalance(latestRecipe.balance)
                                setCoffeeLevel(latestRecipe.level)
                                setCoffeeRatio(latestRecipe.ratio)

                            }
                        }
                    }

                }
                else -> {
                    _historyStateFlow.value = emptyList()

                }
            }
        }
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
                    repository.saveRecipe(timerStateFlow.value.recipe)
                    updateHistory()
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