package com.yopachara.fourtosixmethod.core.data.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

private const val MIN_HOT_RATIO = 6

/** Every brew runs 3:30 regardless of [Level] - the pour count and per-pour times vary, the total does not. */
const val TOTAL_BREW_SECONDS = 210

/** Multiplatform replacement for `java.time.LocalDate.now()`. */
@OptIn(ExperimentalTime::class)
fun currentDate(): LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())

/**
 * The brew parameters, plus the pour schedule derived from them.
 *
 * [steps] is computed lazily from the other properties rather than recomputed by a setter on
 * each of them, which is what lets this be an immutable `data class`: instances end up inside a
 * `StateFlow` and are shared across every already-published timer state, so an in-place write to
 * one would silently edit all of them.
 */
data class Recipe(
    val id: Int = 0,
    val ratio: Int = 12,
    val coffeeWeight: Float = 15f,
    val balance: Balance = Balance.Basic,
    val level: Level = Level.Basic,
    val isIcedDrip: Boolean = false,
    val hotRatio: Int = 10,
    val createAt: LocalDate = currentDate(),
) {

    val steps: List<Step> by lazy {
        (1..getTotalState()).map { i ->
            computeStep(
                state = i.intToState(),
                level = level,
                balance = balance,
                weight = getPourWaterWeight(),
            )
        }
    }

    fun getTotalWater(): Float {
        return (coffeeWeight.times(ratio))
    }

    fun getEffectiveHotRatio(): Int {
        return hotRatio.coerceIn(MIN_HOT_RATIO, (ratio - 1).coerceAtLeast(MIN_HOT_RATIO))
    }

    fun getHotWaterWeight(): Float {
        return coffeeWeight * getEffectiveHotRatio()
    }

    fun getIceWeight(): Float {
        return getTotalWater() - getHotWaterWeight()
    }

    private fun getPourWaterWeight(): Float {
        return if (isIcedDrip) getHotWaterWeight() else getTotalWater()
    }

    fun getTotalState(): Int {
        return when (level) {
            Level.Basic -> 5
            Level.Strong -> 6
            Level.Week -> 4
        }
    }

    fun getStateTotalTime(state: State?): Int = getStateTotalTime(state, level)

    fun getTotalTime(): Int = TOTAL_BREW_SECONDS

    fun getCurrentWater(state: State?): Float {
        return steps[state?.ordinal ?: 0].waterWeight
    }

    fun getWaterPercentState(secondsRemaining: Int?): Float =
        getWaterPercentState(
            // No reading yet means the brew has not started, so report the opening pour.
            state = getCurrentStatePosition(secondsRemaining) ?: State.First,
            balance = balance,
            level = level,
        )

    fun getCurrentStatePosition(secondsRemaining: Int?): State? {
        return when (secondsRemaining) {
            in 166..210 -> {
                State.First
            }

            in 121..165 -> {
                State.Second
            }

            in 0..120 -> {
                when (level) {
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

    fun getCurrentStateTime(second: Int?): Int {
        return second?.minus(getTotalStatePass(second)) ?: 0
    }

    /**
     * Seconds of brew time that elapsed before the pour [second] falls in, so
     * [getCurrentStateTime] can subtract it and get the elapsed time *within* the current pour.
     *
     * The upper bound of each range is exclusive of the next pour's first second: these
     * boundaries have to line up exactly with [getCurrentStatePosition], which switches state at
     * second 45/90/135/180. They used to be one second late (`0..45`, `46..90`, ...), so on the
     * first tick of every pour after the first, the elapsed time of the pour that just *ended*
     * was divided by the new pour's length - a full progress bar that immediately snapped back
     * to empty.
     */
    fun getTotalStatePass(second: Int): Int {
        return when (second) {
            in 0..44 -> {
                0
            }

            in 45..89 -> {
                45
            }

            in 90..TOTAL_BREW_SECONDS -> {
                when (level) {
                    Level.Basic -> when (second) {
                        in 90..134 -> 90
                        in 135..179 -> 135
                        in 180..210 -> 180
                        else -> 0
                    }

                    Level.Strong -> when (second) {
                        in 90..119 -> 90
                        in 120..149 -> 120
                        in 150..179 -> 150
                        in 180..210 -> 180
                        else -> 0
                    }

                    Level.Week -> when (second) {
                        in 90..149 -> 90
                        in 150..210 -> 150
                        else -> 0
                    }
                }
            }

            else -> 0
        }
    }
}
