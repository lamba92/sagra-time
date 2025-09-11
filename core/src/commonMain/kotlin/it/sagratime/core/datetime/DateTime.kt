@file:OptIn(ExperimentalTime::class)

package it.sagratime.core.datetime

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
data class ZonedDateTime(
    val datetime: LocalDateTime,
    val timeZone: TimeZone,
) : Comparable<ZonedDateTime> {
    override fun compareTo(other: ZonedDateTime): Int {
        val thisInstant = datetime.toInstant(timeZone)
        val otherInstant = other.datetime.toInstant(other.timeZone)
        return thisInstant.compareTo(otherInstant)
    }
}

fun LocalDateTime.withTimeZone(timeZone: TimeZone) = ZonedDateTime(this, timeZone)

fun Instant.toZonedDateTime(timeZone: TimeZone) = ZonedDateTime(toLocalDateTime(timeZone), timeZone)

@Serializable
data class ZonedDate(
    val date: LocalDate,
    val timeZone: TimeZone,
)

fun LocalDate.withTimeZone(timeZone: TimeZone) = ZonedDate(this, timeZone)

fun Instant.toZonedDate(timeZone: TimeZone) = ZonedDate(toLocalDateTime(timeZone).date, timeZone)
