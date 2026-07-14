package com.yopachara.fourtosixmethod.feature.timer.state

sealed class HomeIntent {
}

sealed class HomeViewEffect {

}

data class HomeViewState(
    val timerDisplayState: TimerDisplayState? = null
)