package com.yopachara.fourtosixmethod.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yopachara.fourtosixmethod.data.Balance
import com.yopachara.fourtosixmethod.data.Level
import com.yopachara.fourtosixmethod.data.TimerState
import com.yopachara.fourtosixmethod.ui.BaseViewModel
import com.yopachara.fourtosixmethod.ui.home.usecase.TimerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor() : BaseViewModel<HomeViewState, HomeViewEffect>() {

    private val timerIntent = TimerUseCase(viewModelScope)
    val timerStateFlow: StateFlow<TimerState> = timerIntent.timerStateFlow

    fun setWeight(value: Float) = timerIntent.setCoffeeWeight(value)
    fun setRatio(value: Int) = timerIntent.setCoffeeRatio(value)
    fun setBalance(value: Balance) = timerIntent.setCoffeeBalance(value)
    fun setLevel(value: Level) = timerIntent.setCoffeeLevel(value)

    fun toggleStart() = timerIntent.toggleTime()
}