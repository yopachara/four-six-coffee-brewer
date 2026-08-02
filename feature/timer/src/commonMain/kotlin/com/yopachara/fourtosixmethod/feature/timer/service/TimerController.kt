package com.yopachara.fourtosixmethod.feature.timer.service

/**
 * Single place that turns a play/pause/stop command into platform work - shared by
 * [com.yopachara.fourtosixmethod.feature.timer.viewmodel.TimerViewModel] (UI) and by
 * platform notification actions, so both drive the timer identically.
 *
 * Android runs the countdown in a WorkManager foreground worker; iOS runs it in-process
 * and schedules local notifications for the pour boundaries, since it has no
 * foreground-service equivalent.
 */
interface TimerController {
    fun toggle()
    fun play()
    fun pause()
    fun stop()
}
