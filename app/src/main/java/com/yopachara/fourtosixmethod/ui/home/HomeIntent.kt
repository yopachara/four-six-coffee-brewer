package com.yopachara.fourtosixmethod.ui.home

import com.yopachara.fourtosixmethod.data.TimerState

sealed class HomeIntent {
}
sealed class HomeViewEffect {

}

data class HomeViewState(
    val timerState: TimerState? = null
)