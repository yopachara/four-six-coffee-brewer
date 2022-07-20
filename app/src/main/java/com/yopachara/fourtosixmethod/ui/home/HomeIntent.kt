package com.yopachara.fourtosixmethod.ui.home

import com.yopachara.fourtosixmethod.core.database.model.TimerState

sealed class HomeIntent {
}

sealed class HomeViewEffect {

}

data class HomeViewState(
    val timerState: TimerState? = null
)