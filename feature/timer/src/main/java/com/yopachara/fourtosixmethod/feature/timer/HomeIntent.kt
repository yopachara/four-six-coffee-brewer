package com.yopachara.fourtosixmethod.feature.timer

import com.yopachara.fourtosixmethod.feature.timer.state.TimerDisplayState


sealed class HomeIntent {
}

sealed class HomeViewEffect {

}

data class HomeViewState(
    val timerDisplayState: TimerDisplayState? = null
)