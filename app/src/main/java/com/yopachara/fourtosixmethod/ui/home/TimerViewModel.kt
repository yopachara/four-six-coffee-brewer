package com.yopachara.fourtosixmethod.ui.home

import com.yopachara.fourtosixmethod.core.data.model.Balance
import com.yopachara.fourtosixmethod.core.data.model.Level
import com.yopachara.fourtosixmethod.core.database.model.Recipe
import com.yopachara.fourtosixmethod.core.database.model.TimerState
import com.yopachara.fourtosixmethod.ui.BaseViewModel
import com.yopachara.fourtosixmethod.ui.home.usecase.TimerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val timerIntent: TimerUseCase
) : BaseViewModel<HomeViewState, HomeViewEffect>() {

    val timerStateFlow: StateFlow<TimerState> = timerIntent.timerStateFlow
    val historyStateFlow: StateFlow<List<Recipe>> = timerIntent.historyStateFlow

    fun setWeight(value: Float) = timerIntent.setCoffeeWeight(value)
    fun setRatio(value: Int) = timerIntent.setCoffeeRatio(value)
    fun setBalance(value: Balance) = timerIntent.setCoffeeBalance(value)
    fun setLevel(value: Level) = timerIntent.setCoffeeLevel(value)

    fun toggleStart() = timerIntent.toggleTime()
}