package com.yopachara.fourtosixmethod.core.logging

import android.util.Log

internal actual fun writeLog(
    level: LogLevel,
    tag: String,
    message: String,
    throwable: Throwable?,
) {
    when (level) {
        LogLevel.Verbose -> Log.v(tag, message, throwable)
        LogLevel.Debug -> Log.d(tag, message, throwable)
        LogLevel.Info -> Log.i(tag, message, throwable)
        LogLevel.Warn -> Log.w(tag, message, throwable)
        LogLevel.Error -> Log.e(tag, message, throwable)
    }
}
