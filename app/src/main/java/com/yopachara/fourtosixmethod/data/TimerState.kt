package com.yopachara.fourtosixmethod.data

import java.math.RoundingMode
import java.util.concurrent.TimeUnit

data class TimerState(
    val secondsRemaining: Int? = null,
    val seconds: Int? = null,
    var totalSeconds: Int = 210,
    val textWhenStopped: String = "start",
    var recipe: Recipe = Recipe(),
) {
    val displaySeconds: String = (getTimeDisplay(seconds))


    private fun getTimeDisplay(seconds: Int?): String {
        return if (seconds != null) {
            String.format(
                "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(seconds.toLong() * 1000),
                TimeUnit.MILLISECONDS.toSeconds(seconds.toLong() * 1000) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(seconds.toLong() * 1000))
            )
        } else {
            textWhenStopped
        }

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


    override fun toString(): String =
        "Seconds Remaining $secondsRemaining, totalSeconds: $totalSeconds, progress: $progressPercentage, state percentage: $statePercentage,  second $seconds"

}