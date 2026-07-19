package com.yopachara.fourtosixmethod.feature.timer.state

import com.yopachara.fourtosixmethod.core.data.model.Recipe
import java.math.RoundingMode
import java.util.concurrent.TimeUnit

data class TimerDisplayState(
    val secondsRemaining: Int? = null,
    val seconds: Int? = null,
    var totalSeconds: Int = 210,
    var recipe: Recipe = Recipe(),
    var timerState: TimerState = TimerState.Stop,
) {
    val displaySeconds: String = (getTimeDisplay(seconds))
    val displayTotalSeconds: String = getTimeDisplay(totalSeconds)

    fun isPlaying() = timerState == TimerState.Play
    fun isComplete() = totalSeconds == seconds

    private fun getTimeDisplay(seconds: Int?): String {
        return String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(seconds?.toLong()?.times(1000) ?: 0),
            TimeUnit.MILLISECONDS.toSeconds(seconds?.toLong()?.times(1000) ?: 0) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((seconds?.toLong()
                        ?: 0) * 1000))
        )

    }

    // Show 100% if seconds remaining is null
    val progressPercentage: Float = (secondsRemaining ?: totalSeconds) / totalSeconds.toFloat()
    val statePercentage: Float =
        (recipe.getCurrentStateTime(seconds)) / recipe.getStateTotalTime(
            recipe.getCurrentStatePosition(secondsRemaining)
        ).toFloat()


    fun getWaterWeightCurrentState(): Float {
        return recipe.getCurrentWater(recipe.getCurrentStatePosition(secondsRemaining))
            .toBigDecimal()
            .setScale(2, RoundingMode.UP)
            .toFloat()
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