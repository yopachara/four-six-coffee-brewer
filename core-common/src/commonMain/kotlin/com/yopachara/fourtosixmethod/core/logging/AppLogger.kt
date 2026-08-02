package com.yopachara.fourtosixmethod.core.logging

/**
 * Ordered so [AppLogger.minLevel] can filter with a plain comparison.
 */
enum class LogLevel { Verbose, Debug, Info, Warn, Error }

/**
 * The app's logging entry point, callable from `commonMain`.
 *
 * Kotlin/Common has no `println` worth shipping - on Android it lands in stdout rather than
 * logcat, and on iOS it is invisible outside a debugger session - so every call funnels through
 * an `expect` sink that each platform maps to its own facility.
 */
object AppLogger {

    /** Calls below this level are dropped before they reach the platform sink. */
    var minLevel: LogLevel = LogLevel.Debug

    fun v(tag: String, message: String) = log(LogLevel.Verbose, tag, message)
    fun d(tag: String, message: String) = log(LogLevel.Debug, tag, message)
    fun i(tag: String, message: String) = log(LogLevel.Info, tag, message)
    fun w(tag: String, message: String, throwable: Throwable? = null) =
        log(LogLevel.Warn, tag, message, throwable)

    fun e(tag: String, message: String, throwable: Throwable? = null) =
        log(LogLevel.Error, tag, message, throwable)

    /**
     * [message] is a lambda so an expensive string never gets built for a call that is filtered
     * out - the readout logs on every animation frame, which is exactly the case that matters.
     */
    inline fun d(tag: String, message: () -> String) {
        if (LogLevel.Debug >= minLevel) log(LogLevel.Debug, tag, message())
    }

    fun log(
        level: LogLevel,
        tag: String,
        message: String,
        throwable: Throwable? = null,
    ) {
        if (level < minLevel) return
        writeLog(level, tag, message, throwable)
    }
}

internal expect fun writeLog(
    level: LogLevel,
    tag: String,
    message: String,
    throwable: Throwable?,
)
