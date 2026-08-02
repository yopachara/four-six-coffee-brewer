package com.yopachara.fourtosixmethod.core.logging

import platform.Foundation.NSLog

internal actual fun writeLog(
    level: LogLevel,
    tag: String,
    message: String,
    throwable: Throwable?,
) {
    val suffix = throwable?.let { "\n${it.stackTraceToString()}" }.orEmpty()
    // NSLog rather than os_log: the message is already interpolated, and NSLog is the one that
    // shows up unchanged in both the Xcode console and the simulator's system log.
    NSLog("%s/%s: %s%s", level.tag, tag, message, suffix)
}

private val LogLevel.tag: String
    get() = when (this) {
        LogLevel.Verbose -> "V"
        LogLevel.Debug -> "D"
        LogLevel.Info -> "I"
        LogLevel.Warn -> "W"
        LogLevel.Error -> "E"
    }
