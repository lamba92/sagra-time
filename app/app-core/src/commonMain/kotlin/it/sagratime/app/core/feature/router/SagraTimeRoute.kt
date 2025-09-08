package it.sagratime.app.core.feature.router

import it.sagratime.core.data.EventId
import it.sagratime.core.data.ItalianRegion
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface SagraTimeRoute {
    @Serializable
    @SerialName("home")
    object Home : SagraTimeRoute

    @Serializable
    @SerialName("sagra")
    data class Sagra(
        val id: EventId,
    ) : SagraTimeRoute

    @Serializable
    @SerialName("region")
    data class Region(
        val region: ItalianRegion,
    ) : SagraTimeRoute

    @Serializable
    @SerialName("event_type")
    data class EventType(
        val type: it.sagratime.core.data.EventType,
    ) : SagraTimeRoute
}
