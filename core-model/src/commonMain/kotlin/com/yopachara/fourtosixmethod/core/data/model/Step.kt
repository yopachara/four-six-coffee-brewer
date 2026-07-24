package com.yopachara.fourtosixmethod.core.data.model

import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.pow

data class Step(
    var id: Int = 0,
    val time: Int,
    val waterWeight: Float,
    val stepPercentage: Float,
    val state: State,
) {
    fun getWaterWithScale(scale: Int): String {
        return waterWeight.formatScaleUp(scale)
    }
}

/**
 * Multiplatform replacement for `BigDecimal.setScale(scale, RoundingMode.UP).toString()`:
 * rounds away from zero to [scale] decimals and always renders exactly [scale] fraction digits.
 */
internal fun Float.formatScaleUp(scale: Int): String {
    val factor = 10.0.pow(scale)
    val magnitude = ceil(abs(this.toDouble()) * factor).toLong()
    val unit = factor.toLong()
    val whole = magnitude / unit
    val frac = magnitude % unit
    val sign = if (this < 0f && magnitude != 0L) "-" else ""
    return if (scale <= 0) {
        "$sign$whole"
    } else {
        "$sign$whole.${frac.toString().padStart(scale, '0')}"
    }
}

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

