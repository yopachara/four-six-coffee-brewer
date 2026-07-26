package com.yopachara.fourtosixmethod.feature.timer.state

sealed interface TimerState {
    object Play : TimerState
    object Pause : TimerState
    object Stop : TimerState
}