package com.yopachara.fourtosixmethod.feature.timer.state

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * The mm:ss formatting replaced `String.format("%02d:%02d", …)` when the timer state moved to
 * commonMain, so the zero padding is worth pinning down.
 */
class TimerDisplayStateTest {

    @Test
    fun seconds_are_displayed_as_zero_padded_minutes_and_seconds() {
        assertEquals("00:00", TimerDisplayState(seconds = 0).displaySeconds)
        assertEquals("00:09", TimerDisplayState(seconds = 9).displaySeconds)
        assertEquals("01:00", TimerDisplayState(seconds = 60).displaySeconds)
        assertEquals("03:30", TimerDisplayState(seconds = 210).displaySeconds)
    }

    @Test
    fun a_missing_reading_displays_as_zero() {
        assertEquals("00:00", TimerDisplayState(seconds = null).displaySeconds)
    }

    @Test
    fun progress_is_full_until_the_first_tick_arrives() {
        assertEquals(1f, TimerDisplayState(secondsRemaining = null, totalSeconds = 210).progressPercentage)
        assertEquals(0.5f, TimerDisplayState(secondsRemaining = 105, totalSeconds = 210).progressPercentage)
    }
}
