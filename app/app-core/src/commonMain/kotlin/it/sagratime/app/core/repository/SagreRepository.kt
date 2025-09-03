package it.sagratime.app.core.repository

import it.sagratime.core.data.GeoCoordinates
import it.sagratime.core.data.ItalianRegion
import it.sagratime.core.data.Locale
import it.sagratime.core.data.Location
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.Serializable
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

interface SagreRepository {
    suspend fun getSagraStatistics(): SagraStatistics

    suspend fun getPopularSearches(locale: Locale): List<String>

    suspend fun searchCompletionQuery(
        query: String,
        locale: Locale,
    ): List<String>
}

@Serializable
data class SagraStatistics(
    val activeEvents: Int,
    val eventsThisYear: Int,
)

object MockSagreRepository : SagreRepository {
    override suspend fun getSagraStatistics(): SagraStatistics {
        delay(Random.nextInt(4, 10).seconds)
        return SagraStatistics(
            activeEvents = 10,
            eventsThisYear = 100,
        )
    }

    override suspend fun getPopularSearches(locale: Locale): List<String> {
        delay(Random.nextInt(4, 10).seconds)
        return listOf(
            "Pizza fritta",
            "Arrosticini",
            "Vino",
            "Castagne",
        )
    }

    override suspend fun searchCompletionQuery(
        query: String,
        locale: Locale,
    ): List<String> {
        delay(Random.nextInt(1, 2).seconds)
        return listOf(
            "Pizza fritta",
            "Arrosticini",
            "Vino",
            "Castagne",
        )
    }
}

object MockLocationService : LocationService {
    private val _status =
        MutableStateFlow<LocationServiceStatus>(LocationServiceStatus.NeverRequested)
    override val status: StateFlow<LocationServiceStatus> = _status.asStateFlow()

    override suspend fun requestLocation() {
        val currentStatus = _status.value
        if (currentStatus is LocationServiceStatus.Active) return
        _status.value = LocationServiceStatus.Requesting
        delay(Random.nextInt(4, 10).seconds)
        _status.value =
            LocationServiceStatus.Active(GeoCoordinates(0.0, 0.0))
    }

    override suspend fun citiesCompletionQuery(
        query: String,
        locale: Locale,
    ): List<Location> {
        delay(Random.nextInt(1, 2).seconds)
        return List(5) { index ->
            Location(
                locale = locale,
                geoCoordinates = GeoCoordinates(0.0, 0.0),
                cityName = "City $index",
                region = ItalianRegion.entries[index],
            )
        }
    }
}
