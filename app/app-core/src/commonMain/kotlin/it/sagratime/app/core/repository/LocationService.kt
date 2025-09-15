package it.sagratime.app.core.repository

import it.sagratime.core.data.GeoCoordinates
import it.sagratime.core.data.Locale
import it.sagratime.core.data.Location
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

interface LocationService {
    val status: StateFlow<LocationServiceStatus>

    suspend fun requestLocation()

    suspend fun stopLocation()

    // this should go in its own service!
    suspend fun citiesCompletionQuery(
        query: String,
        locale: Locale,
    ): List<Location>

    suspend fun getIpGeoCoordinates(): GeoCoordinates
}

@Serializable
sealed interface LocationServiceStatus {
    @Serializable
    data object NeverRequested : LocationServiceStatus

    @Serializable
    data object Requesting : LocationServiceStatus

    @Serializable
    data object Disabled : LocationServiceStatus

    @Serializable
    data object Inactive : LocationServiceStatus

    @Serializable
    @JvmInline
    value class Active(
        val lastKnownLocation: GeoCoordinates,
    ) : LocationServiceStatus
}
