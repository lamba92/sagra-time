package it.sagratime.server

import io.ktor.serialization.kotlinx.json.json
import io.ktor.serialization.kotlinx.xml.xml
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.basic
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.routing.routing
import it.sagratime.server.routes.apiRoutes
import it.sagratime.server.service.V1ServerEventRepository

@Suppress("FunctionName")
fun Application.SagraTimeApi(eventProvider: V1ServerEventRepository) {
    install(ContentNegotiation) {
        json()
        xml()
    }
    install(CORS) {
        anyHost()
    }
    install(Authentication) {
        basic {
            validate {
                when (it.name) {
                    ADMIN_USERNAME if it.password == ADMIN_PASSWORD -> Principal.Admin
                    else -> null
                }
            }
        }
    }

    routing {
        apiRoutes(eventProvider)
    }
}
