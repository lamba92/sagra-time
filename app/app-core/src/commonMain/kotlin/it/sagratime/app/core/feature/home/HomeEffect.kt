package it.sagratime.app.core.feature.home

import kotlinx.serialization.Serializable

@Serializable
sealed interface HomeEffect {
    @Serializable
    data object NotifyLocationServicesDisabled : HomeEffect
}
