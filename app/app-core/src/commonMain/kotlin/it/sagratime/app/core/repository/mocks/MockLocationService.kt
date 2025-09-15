package it.sagratime.app.core.repository.mocks

import it.sagratime.app.core.repository.LocationService
import it.sagratime.app.core.repository.LocationServiceStatus
import it.sagratime.core.data.GeoCoordinates
import it.sagratime.core.data.ItalianRegion
import it.sagratime.core.data.Locale
import it.sagratime.core.data.Location
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

object MockLocationService : LocationService {
    private val _status =
        MutableStateFlow<LocationServiceStatus>(LocationServiceStatus.NeverRequested)
    override val status: StateFlow<LocationServiceStatus> = _status.asStateFlow()

    override suspend fun requestLocation() {
        val currentStatus = _status.value
        if (currentStatus is LocationServiceStatus.Active) return
        _status.value = LocationServiceStatus.Requesting
        delay(Random.Default.nextInt(4, 10).seconds)
        _status.value =
            LocationServiceStatus.Active(GeoCoordinates(0.0, 0.0))
    }

    override suspend fun stopLocation() {
    }

    override suspend fun citiesCompletionQuery(
        query: String,
        locale: Locale,
    ): List<Location> {
        delay(Random.Default.nextInt(1, 2).seconds)
        return List(5) { index ->
            Location(
                geoCoordinates = GeoCoordinates(0.0, 0.0),
                cityName = "City $index",
                region = ItalianRegion.entries[index],
            )
        }
    }

    override suspend fun getIpGeoCoordinates() = GeoCoordinates(0.0, 0.0)
}
