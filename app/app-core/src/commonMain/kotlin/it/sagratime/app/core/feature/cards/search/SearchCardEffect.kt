package it.sagratime.app.core.feature.cards.search

import kotlinx.serialization.Serializable

@Serializable
sealed interface SearchCardEffect {
    @Serializable
    data object NotifyLocationServicesDisabled : SearchCardEffect
}
