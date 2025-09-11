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
import it.sagratime.core.data.Event
import it.sagratime.core.data.toEventSearchQuery
import it.sagratime.server.Principal
import it.sagratime.server.service.EventProvider
import kotlinx.serialization.ExperimentalSerializationApi
import kotlin.time.ExperimentalTime

fun Route.v1Routes(eventProvider: EventProvider) {
    route("v1") {
        route("sagra") {
            get("search") {
                val message = eventProvider.search(call.parameters.toEventSearchQuery())
                call.respond(message)
            }

            authenticate {
                post("add") {
                    if (call.principal<Principal>() != Principal.Admin)
                        return@post call.respond(HttpStatusCode.Unauthorized)
                    val event = call.receive<Event>()
                    eventProvider.addSagra(event)
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }
}
