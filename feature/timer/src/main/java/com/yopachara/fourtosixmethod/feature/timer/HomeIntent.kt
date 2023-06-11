package com.yopachara.fourtosixmethod.feature.timer

import com.yopachara.fourtosixmethod.core.data.model.TimerState

sealed class HomeIntent {
}

sealed class HomeViewEffect {

}

data class HomeViewState(
    val timerState: TimerState? = null
)