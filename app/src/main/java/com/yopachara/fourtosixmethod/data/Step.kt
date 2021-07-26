package com.yopachara.fourtosixmethod.data

data class Step(
    val time: Int,
    val waterWeight: Float,
    val stepPercentage: Float,
    val state: State,
)

fun computeStep(state: State, level: Level, balance: Balance, weight: Float): Step {
    val time = getStateTotalTime(state, level)
    val waterPercentage = getWaterPercentState(state = state, balance = balance, level = level)
    val waterWeight = getStepWaterWeight(weight, waterPercentage)
    return Step(
        time = time,
        waterWeight = waterWeight,
        stepPercentage = waterPercentage,
        state = state
    )
}

fun getStepWaterWeight(weight: Float, waterPercentage: Float): Float {
    return weight * waterPercentage
}

fun getWaterPercentState(state: State, balance: Balance, level: Level): Float {
    return when (state) {
        State.First -> balance.sweetIndex
        State.Second -> balance.acidIndex
        State.Third,
        State.Forth,
        State.Fifth,
        State.Sixth,
        -> level.firstIndex
        null -> balance.sweetIndex
    }
}

fun getStateTotalTime(state: State?, level: Level): Int {
    return when (state) {
        State.First,
        State.Second,
        -> 45
        State.Third,
        State.Forth,
        State.Fifth,
        State.Sixth,
        -> {
            when (level) {
                Level.Basic -> if (state != State.Fifth) 45 else 30
                Level.Strong -> 30
                Level.Week -> 60
            }
        }
        null -> 45
    }
}