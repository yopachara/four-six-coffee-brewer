package com.yopachara.fourtosixmethod.feature.timer.service

import com.yopachara.fourtosixmethod.core.domain.InsertRecipeUseCase
import com.yopachara.fourtosixmethod.feature.timer.state.TimerDisplayState
import com.yopachara.fourtosixmethod.feature.timer.state.TimerSessionRepository
import com.yopachara.fourtosixmethod.feature.timer.state.TimerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

/**
 * The countdown itself, platform-neutral: it owns the tick loop, the session-state
 * updates and the save-on-completion, so every platform brews identically and only
 * differs in what it does per tick ([onTick]) - a foreground-service notification
 * refresh on Android, a local-notification refresh on iOS.
 *
 * Cancelling the coroutine running [run] is how pause/stop work: the collect loop
 * throws out and the completion tail below never runs.
 */
class TimerEngine(
    private val sessionRepository: TimerSessionRepository,
    private val insertRecipeUseCase: InsertRecipeUseCase,
) {

    suspend fun run(onTick: suspend (TimerDisplayState) -> Unit) {
        val totalTime = sessionRepository.state.value.recipe.getTotalTime()
        val continueFrom = sessionRepository.state.value.seconds

        tickerFlow(totalTime, continueFrom)
            .map { remainingTime ->
                sessionRepository.state.value.copy(
                    secondsRemaining = remainingTime,
                    seconds = totalTime - remainingTime,
                    totalSeconds = totalTime,
                    timerState = TimerState.Play,
                )
            }
            .onStart {
                sessionRepository.update { it.copy(timerState = TimerState.Play) }
            }
            .collect { tickedState ->
                sessionRepository.update { tickedState }
                onTick(tickedState)
            }

        // Reached only on natural completion - cancellation (pause/stop) throws
        // out of the collect loop above instead of falling through to here.
        sessionRepository.update {
            it.copy(secondsRemaining = null, seconds = null, timerState = TimerState.Stop)
        }
        insertRecipeUseCase(sessionRepository.state.value.recipe)
    }

    private fun tickerFlow(totalSeconds: Int, continueFrom: Int?): Flow<Int> = flow {
        if (continueFrom == null) emit(totalSeconds)
        val firstTick = totalSeconds - (continueFrom ?: 0) - 1
        for (remaining in firstTick downTo 0) {
            delay(1000)
            emit(remaining)
        }
    }.conflate()
}
