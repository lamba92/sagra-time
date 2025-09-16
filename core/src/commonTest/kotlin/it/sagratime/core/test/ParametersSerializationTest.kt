@file:OptIn(ExperimentalSerializationApi::class)

package it.sagratime.core.test

import io.ktor.http.formUrlEncode
import it.sagratime.core.UrlParameters
import it.sagratime.core.data.DateRange
import it.sagratime.core.data.EventSearchQuery
import it.sagratime.core.data.EventType
import it.sagratime.core.data.GeoCoordinates
import it.sagratime.core.data.Locale
import it.sagratime.core.data.LocationQuery
import it.sagratime.core.datetime.ZonedDate
import it.sagratime.core.decodeFromParameters
import it.sagratime.core.encodeToParameters
import it.sagratime.core.units.kilometers
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.serialization.ExperimentalSerializationApi
import kotlin.test.Test
import kotlin.test.assertEquals

class ParametersSerializationTest {
    companion object {
        val TEST_OBJECT =
            EventSearchQuery(
                page = 1,
                size = 25,
                queryString = "test",
                location =
                    LocationQuery(
                        from = GeoCoordinates(1.0, 2.0),
                        radius = 3.kilometers,
                    ),
                types = setOf(EventType.Sagra, EventType.TownFestival),
                dateRange =
                    DateRange(
                        from =
                            ZonedDate(
                                year = 2022,
                                month = Month.JANUARY,
                                dayOfMonth = 1,
                                timeZone = TimeZone.currentSystemDefault(),
                            ),
                        to =
                            ZonedDate(
                                year = 2022,
                                month = Month.JANUARY,
                                dayOfMonth = 5,
                                timeZone = TimeZone.currentSystemDefault(),
                            ),
                    ),
                locale = Locale.EN,
            )
    }

    @Test
    fun encodingDecoding() {
        val encoded = UrlParameters.encodeToParameters(TEST_OBJECT)
        println(encoded.formUrlEncode())
        val decoded = UrlParameters.decodeFromParameters<EventSearchQuery>(encoded)
        assertEquals(TEST_OBJECT, decoded)
    }
}
