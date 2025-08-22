package it.sagrabot.server

import com.github.lamba92.kotlin.document.store.core.DataStore
import com.github.lamba92.kotlin.document.store.core.KotlinDocumentStore
import com.github.lamba92.kotlin.document.store.core.getObjectCollection
import io.ktor.serialization.kotlinx.json.json
import io.ktor.serialization.kotlinx.xml.xml
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.basic
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import it.sagrabot.core.data.Sagra
import it.sagrabot.server.routes.apiRoutes
import kotlinx.coroutines.async

fun Application.SagraBot(dBStore: DataStore) {
    val sagreCollection =
        async {KotlinDocumentStore(dBStore).getObjectCollection<Sagra>("sagre") }

    install(ContentNegotiation) {
        json()
        xml()
    }

    install(Authentication.Companion) {
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
        apiRoutes(sagreCollection)
    }
}
