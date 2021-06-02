package com.yopachara.fourtosixmethod.data

import java.math.RoundingMode
import java.time.LocalTime
import kotlin.time.Duration
import kotlin.time.DurationUnit

data class TimerState(
    val secondsRemaining: Int? = null,
    val seconds: Int? = null,
    var totalSeconds: Int = 210,
    val textWhenStopped: String = "start",
    var ratio: Int = 12,
    var weight: Float = 15f,
    var balance: Balance = Balance.Basic,
    var level: Level = Level.Basic,
) {
    val displaySeconds: String = (seconds ?: textWhenStopped).toString()

    // Show 100% if seconds remaining is null
    val progressPercentage: Float = (secondsRemaining ?: totalSeconds) / totalSeconds.toFloat()
    val statePercentage: Float = getCurrentStateTime() / getStateTotalTime().toFloat()

    fun getTotalWater(): Float {
        return (weight.times(ratio))
    }

    fun getStateTotalTime(): Int {
        return when (getCurrentStatePosition()) {
            State.First,
            State.Second,
            -> 45
            State.Third,
            State.Forth,
            State.Fifth,
            State.Sixth,
            -> {
                when (level) {

                    Level.Basic ->  if (getCurrentStatePosition()!=State.Fifth) 45 else 30
                    Level.Strong -> 30
                    Level.Week -> 60
                }
            }
            null -> 45
        }
    }

    fun getWaterWeightCurrentState(): Float {
        return getWaterPercentState().times(getTotalWater()).toBigDecimal().setScale(2, RoundingMode.UP).toFloat()
    }

    fun getTotalState(): Int {
        return when (level) {
            Level.Basic -> 5
            Level.Strong -> 6
            Level.Week -> 4
        }
    }

    fun getCurrentStatePosition(): State? {
        return when (secondsRemaining) {
            in 176..210 -> {
                State.First
            }
            in 121..175 -> {
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

    fun getWaterPercentState(): Float {
        return when (getCurrentStatePosition()) {
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

    fun getCurrentStateTime(): Int {
        secondsRemaining?.let { remaining ->
            return when (getCurrentStatePosition()) {
                State.First,
                State.Second,
                -> {
                    remaining.minus(120).rem(45)
                }
                State.Third,
                State.Forth,
                State.Fifth,
                State.Sixth,
                -> {
                    when (level) {
                        Level.Basic -> if (getCurrentStatePosition() != State.Fifth) {
                            remaining.minus(30).rem(45)
                        } else {
                            remaining.rem(30)
                        }
                        Level.Strong -> remaining.rem(30)
                        Level.Week -> remaining.rem(60)
                    }
                }
                null -> TODO()
            }
        } ?: return 45
    }

    fun getTimeRange(isFirst: Boolean): IntRange {
        if (isFirst) {
            return when (balance) {
                Balance.Acid -> TODO()
                Balance.Basic -> TODO()
                Balance.Sweet -> TODO()
            }
        } else {
            return when (level) {
                Level.Basic -> TODO()
                Level.Strong -> TODO()
                Level.Week -> TODO()
            }
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

    override fun toString(): String =
        "Seconds Remaining $secondsRemaining, totalSeconds: $totalSeconds, progress: $progressPercentage"

    enum class State(index: Int) {
        First(1), Second(2), Third(3), Forth(4), Fifth(5), Sixth(6)
    }
}