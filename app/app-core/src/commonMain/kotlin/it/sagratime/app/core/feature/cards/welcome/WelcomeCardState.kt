package it.sagratime.app.core.feature.cards.welcome

import it.sagratime.app.core.repository.SagraStatistics
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
sealed interface WelcomeCardState {
    @Serializable
    data object Loading : WelcomeCardState

    @Serializable
    @JvmInline
    value class Ready(
        val statistics: SagraStatistics,
    ) : WelcomeCardState
}
