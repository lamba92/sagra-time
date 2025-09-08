package it.sagratime.app.core.feature.home

import it.sagratime.core.data.Event
import kotlinx.serialization.Serializable

@Serializable
sealed interface HomeState {
    @Serializable
    data object Default : HomeState

    @Serializable
    sealed interface Searching : HomeState {
        @Serializable
        data object Loading : Searching

        @Serializable
        data class Success(
            val results: List<Event>,
        ) : Searching
    }
}
