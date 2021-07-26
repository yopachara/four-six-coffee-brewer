package com.yopachara.fourtosixmethod.data

enum class State(index: Int) {
        First(1), Second(2), Third(3), Forth(4), Fifth(5), Sixth(6)
    }
fun Int.intToState(): State {
    return when (this) {
        1 -> return State.First
        2 -> return State.Second
        3 -> return State.Third
        4 -> return State.Forth
        5 -> return State.Fifth
        6 -> return State.Sixth
        else -> State.First
    }
}