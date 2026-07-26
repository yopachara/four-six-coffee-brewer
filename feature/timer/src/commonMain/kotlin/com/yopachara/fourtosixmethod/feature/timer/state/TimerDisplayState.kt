package com.yopachara.fourtosixmethod.feature.timer.state

import com.yopachara.fourtosixmethod.core.data.model.Recipe
import com.yopachara.fourtosixmethod.core.data.model.scaleUp

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
        val total = seconds ?: 0
        val minutes = (total / 60).toString().padStart(2, '0')
        val remainder = (total % 60).toString().padStart(2, '0')
        return "$minutes:$remainder"
    }

    // Show 100% if seconds remaining is null
    val progressPercentage: Float = (secondsRemaining ?: totalSeconds) / totalSeconds.toFloat()
    val statePercentage: Float =
        (recipe.getCurrentStateTime(seconds)) / recipe.getStateTotalTime(
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