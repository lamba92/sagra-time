package it.sagratime.app.desktop

import coil3.util.Logger

object CoilDebugLogger : Logger {

    override var minLevel = Logger.Level.Debug

    override fun log(
        tag: String,
        level: Logger.Level,
        message: String?,
        throwable: Throwable?,
    ) {
        when (level) {
            Logger.Level.Debug -> println("[DEBUG] $tag - $message")
            Logger.Level.Error -> println("[ERROR] $tag - $message")
            Logger.Level.Info -> println("[INFO] $tag - $message")
            Logger.Level.Warn -> println("[WARN] $tag - $message")
            Logger.Level.Verbose -> println("[VERBOSE] $tag - $message")
        }
    }
}