package com.yopachara.fourtosixmethod.data

import androidx.room.*
import com.google.gson.Gson
import java.util.*
import javax.inject.Inject

@Entity
class Recipe(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    _ratio: Int = 12,
    _coffeeWeight: Float = 15f,
    _balance: Balance = Balance.Basic,
    _level: Level = Level.Basic,
    @ColumnInfo(name = "steps")
    var steps: List<Step> = getDefaultSteps(),
    @ColumnInfo(name = "createAt")
    var createAt: Date = Date()
) {

    @ColumnInfo(name = "ratio")
    var ratio: Int = _ratio
        set(value) {
            field = value
            generateSteps()
        }

    @ColumnInfo(name = "coffee_weight")
    var coffeeWeight: Float = _coffeeWeight
        set(value) {
            field = value
            generateSteps()
        }

    @ColumnInfo(name = "balance")
    var balance: Balance = _balance
        set(value) {
            field = value
            generateSteps()
        }

    @ColumnInfo(name = "level")
    var level: Level = _level
        set(value) {
            field = value
            generateSteps()
        }

    fun getTotalWater(): Float {
        return (coffeeWeight.times(ratio))
    }

    fun getTotalState(): Int {
        return when (level) {
            Level.Basic -> 5
            Level.Strong -> 6
            Level.Week -> 4
        }
    }


    fun getStateTotalTime(state: State?): Int {
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

    fun getTotalTime(): Int {
        return when (level) {
            Level.Basic,
            Level.Strong,
            Level.Week,
            -> 210
        }
    }

    fun getCurrentWater(state: State?): Float {
        return steps[state?.ordinal ?: 0].waterWeight
    }

    fun getWaterPercentState(secondsRemaining: Int?): Float {
        return when (getCurrentStatePosition(secondsRemaining)) {
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


    fun getCurrentStatePosition(secondsRemaining: Int?): State? {
        return when (secondsRemaining) {
            in 166..210 -> {
                State.First
            }
            in 121..165 -> {
                State.Second
            }
            in 0..120 -> {
                return when (level) {
                    Level.Basic -> when (secondsRemaining) {
                        in 0..30 -> State.Fifth
                        in 31..75 -> State.Forth
                        in 76..120 -> State.Third
                        else -> null
                    }

                    Level.Strong -> when (secondsRemaining) {
                        in 0..30 -> State.Sixth
                        in 31..60 -> State.Fifth
                        in 61..90 -> State.Forth
                        in 91..120 -> State.Third
                        else -> null
                    }

                    Level.Week -> when (secondsRemaining) {
                        in 0..60 -> State.Forth
                        in 61..120 -> State.Third
                        else -> null
                    }
                }
            }
            else -> null
        }
    }


    private fun generateSteps() {
        steps = arrayListOf<Step>().apply {
            for (i in 1..getTotalState()) {
                add(
                    computeStep(
                        state = i.intToState(),
                        level = level,
                        balance = balance,
                        weight = getTotalWater()
                    )
                )
            }
        }
    }

    fun getCurrentStateTime(second: Int?): Int {
        return second?.minus(getTotalStatePass(second ?: 0)) ?: 0
    }

    fun getTotalStatePass(second: Int): Int {
        return when (second) {
            in 0..45 -> {
                0
            }
            in 46..90 -> {
                45
            }
            in 91..225 -> {
                return when (level) {
                    Level.Basic -> when (second) {
                        in 91..135 -> 90
                        in 136..180 -> 135
                        in 181..225 -> 180
                        else -> 0
                    }

                    Level.Strong -> when (second) {
                        in 91..120 -> 90
                        in 121..150 -> 120
                        in 151..180 -> 150
                        in 181..210 -> 180
                        else -> 0
                    }

                    Level.Week -> when (second) {
                        in 91..150 -> 90
                        in 151..210 -> 150
                        else -> 0
                    }
                }
            }
            else -> 0
        }
    }

    override fun equals(other: Any?): Boolean {
        return false
    }
}

fun getDefaultSteps(): List<Step> {
    return arrayListOf<Step>().apply {
        add(
            Step(
                time = 45,
                waterWeight = 36f,
                stepPercentage = 0.20f,
                state = State.First
            )
        )
        add(
            Step(
                time = 45,
                waterWeight = 36f,
                stepPercentage = 0.20f,
                state = State.Second
            )
        )
        add(
            Step(
                time = 45,
                waterWeight = 36f,
                stepPercentage = 0.20f,
                state = State.Third
            )
        )
        add(
            Step(
                time = 45,
                waterWeight = 36f,
                stepPercentage = 0.20f,
                state = State.Forth
            )
        )
        add(
            Step(
                time = 30,
                waterWeight = 36f,
                stepPercentage = 0.20f,
                state = State.Fifth
            )
        )
    }
}

@ProvidedTypeConverter
class DateConverter @Inject constructor() {
    @TypeConverter
    fun recipeToString(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun stringToRecipe(value: Long): Date {
        return Date(value)
    }
}

@ProvidedTypeConverter
class StateListConverter @Inject constructor() {
    @TypeConverter
    fun stateToString(recipe: List<Step>): String {
        return Gson().toJson(recipe)
    }

    @TypeConverter
    fun stringToState(value: String): List<Step> {
        val result = Gson().fromJson(value, Array<Step>::class.java).toList()
        return result
    }
}