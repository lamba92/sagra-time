@file:OptIn(InternalResourceApi::class, ExperimentalResourceApi::class)

package it.sagratime.app.web.server

import io.ktor.server.application.Application
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.InternalResourceApi

fun main() {
    embeddedServer(
        factory = CIO,
        port = 8080,
        module = Application::AppWebServer,
    ).start(wait = true)
}
