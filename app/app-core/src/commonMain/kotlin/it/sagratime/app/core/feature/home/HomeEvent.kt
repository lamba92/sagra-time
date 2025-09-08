package it.sagratime.app.core.feature.home

import it.sagratime.app.core.feature.cards.search.SearchCardEffect
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
sealed interface HomeEvent {
    @Serializable
    @JvmInline
    value class SearchEffect(
        val effect: SearchCardEffect,
    ) : HomeEvent

    @Serializable
    data object TopBarTitleClick : HomeEvent
}
