package it.sagratime.app.web.server

import io.ktor.http.HttpHeaders
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.compression.deflate
import io.ktor.server.plugins.compression.gzip
import io.ktor.server.request.path
import io.ktor.server.routing.RoutingContext
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import it.sagratime.app.core.feature.router.SagraTimeRoute

@Suppress("FunctionName")
fun Application.AppWebServer() {
    install(Compression) {
        gzip()
        deflate()
    }

    routing {
        get("{...}") {
            handleRequest()
        }
    }
}

suspend fun RoutingContext.handleRequest() {
    val locales =
        parseAcceptLanguage(call.request.headers[HttpHeaders.AcceptLanguage])
    when (SagraTimeRoute.Companion.fromUrl(call.request.path())) {
        is SagraTimeRoute.EventType -> TODO()
        SagraTimeRoute.Home -> getHomeBootstrapDocument(locales)
        is SagraTimeRoute.Region -> TODO()
        is SagraTimeRoute.Sagra -> TODO()
        null -> serveResources()
    }
}
