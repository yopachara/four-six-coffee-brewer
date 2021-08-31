package com.yopachara.fourtosixmethod.data

import androidx.room.Entity
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

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