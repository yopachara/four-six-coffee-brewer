package com.yopachara.fourtosixmethod.feature.timer.state

import com.yopachara.fourtosixmethod.core.data.model.Recipe
import com.yopachara.fourtosixmethod.core.data.model.TOTAL_BREW_SECONDS
import com.yopachara.fourtosixmethod.core.data.model.scaleUp

/**
 * All properties are `val` and everything derived is a `get()`: the derived values used to be
 * computed once in the initializer while the inputs stayed `var`, so writing to one left the
 * readouts - and `equals`/`hashCode` - describing the previous state.
 */
data class TimerDisplayState(
    val secondsRemaining: Int? = null,
    val seconds: Int? = null,
    val totalSeconds: Int = TOTAL_BREW_SECONDS,
    val recipe: Recipe = Recipe(),
    val timerState: TimerState = TimerState.Stop,
) {
    val displaySeconds: String get() = getTimeDisplay(seconds)
    val displayTotalSeconds: String get() = getTimeDisplay(totalSeconds)

    fun isPlaying() = timerState == TimerState.Play
    fun isComplete() = totalSeconds == seconds

    private fun getTimeDisplay(seconds: Int?): String {
        val total = seconds ?: 0
        val minutes = (total / 60).toString().padStart(2, '0')
        val remainder = (total % 60).toString().padStart(2, '0')
        return "$minutes:$remainder"
    }

    // Show 100% if seconds remaining is null
    val progressPercentage: Float
        get() = (secondsRemaining ?: totalSeconds) / totalSeconds.toFloat()

    val statePercentage: Float
        get() = (recipe.getCurrentStateTime(seconds)) / recipe.getStateTotalTime(
            recipe.getCurrentStatePosition(secondsRemaining)
        ).toFloat()


    fun getWaterWeightCurrentState(): Float {
        return recipe.getCurrentWater(recipe.getCurrentStatePosition(secondsRemaining))
            .scaleUp(2)
    }

    fun getCurrentStateMaxTime(): Int {
        return recipe.steps.find {
            it.state == recipe.getCurrentStatePosition(secondsRemaining)
        }?.time ?: 45
    }

    fun getCurrentStateIndex(): Int {
        return recipe.getCurrentStatePosition(secondsRemaining)?.ordinal ?: 0
    }

    fun isRunning() = secondsRemaining != null

    fun stateMessage(): String {
        if (!isRunning()) return "Ready to brew"
        if (!isPlaying()) return "Paused"
        return when (getCurrentStateIndex()) {
            0 -> "Pour to bloom"
            1 -> "Balancing sweetness & acidity"
            else -> "Building body"
        }
    }

    fun stepTimeRemainingLabel(): String {
        val maxTime = getCurrentStateMaxTime()
        return if (isRunning()) {
            val elapsed = recipe.getCurrentStateTime(seconds)
            "${(maxTime - elapsed).coerceAtLeast(0)}s left"
        } else {
            "${maxTime}s pour"
        }
    }

    override fun toString(): String =
        "Seconds Remaining $secondsRemaining, totalSeconds: $totalSeconds, progress: $progressPercentage, state percentage: $statePercentage,  second $seconds"

}