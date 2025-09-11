package it.sagratime.app.core.feature.cards.welcome

import it.sagratime.core.data.EventsStatistics
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
sealed interface WelcomeCardState {
    @Serializable
    data object Loading : WelcomeCardState

    @Serializable
    @JvmInline
    value class Ready(
        val statistics: EventsStatistics,
    ) : WelcomeCardState
}
