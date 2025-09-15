@file:OptIn(ExperimentalTime::class, ExperimentalSerializationApi::class)

package it.sagratime.server.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import it.sagratime.core.UrlParameters
import it.sagratime.core.data.Event
import it.sagratime.core.data.EventSearchQuery
import it.sagratime.core.data.Locale
import it.sagratime.core.data.SearchCompletionQuery
import it.sagratime.core.decodeFromParameters
import it.sagratime.server.Principal
import it.sagratime.server.service.V1ServerEventRepository
import kotlinx.serialization.ExperimentalSerializationApi
import kotlin.time.ExperimentalTime

fun Route.v1Routes(eventProvider: V1ServerEventRepository) {
    route("v1") {
        route("events") {
            get("search") {
                val query =
                    runCatching { UrlParameters.decodeFromParameters<EventSearchQuery>(call.parameters) }
                        .getOrNull()
                if (query == null) {
                    return@get call.respond(HttpStatusCode.BadRequest)
                }
                call.respond(eventProvider.search(query))
            }

            get("statistics") {
                call.respond(eventProvider.getEventStatistics())
            }

            get("popular") {
                val locale =
                    runCatching { UrlParameters.decodeFromParameters<Locale>(call.parameters) }
                        .getOrNull()
                if (locale == null) {
                    return@get call.respond(HttpStatusCode.BadRequest)
                }
                call.respond(eventProvider.getPopularSearches(locale))
            }

            get("autocomplete") {
                val query =
                    runCatching { UrlParameters.decodeFromParameters<SearchCompletionQuery>(call.parameters) }
                        .getOrNull()
                if (query == null) {
                    return@get call.respond(HttpStatusCode.BadRequest)
                }
                call.respond(eventProvider.searchCompletion(query))
            }

            authenticate {
                post("add") {
                    if (call.principal<Principal>() != Principal.Admin) {
                        return@post call.respond(HttpStatusCode.Unauthorized)
                    }
                    val event = call.receive<Event>()
                    eventProvider.addSagra(event)
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }
}
