package it.sagratime.app.core.feature.cards.search

import it.sagratime.core.data.SearchEventQuery
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
sealed interface SearchCardEffect {
    @Serializable
    data object NotifyLocationServicesDisabled : SearchCardEffect

    @Serializable
    @JvmInline
    value class Search(
        val query: SearchEventQuery,
    ) : SearchCardEffect
}
