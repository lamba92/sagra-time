package it.sagratime.app.core.feature.cards.event.components

import androidx.compose.runtime.Composable
import it.sagratime.app.core.components.SagraTimeTheme
import it.sagratime.app_core.generated.resources.Res
import it.sagratime.app_core.generated.resources.april
import it.sagratime.app_core.generated.resources.april_short
import it.sagratime.app_core.generated.resources.august
import it.sagratime.app_core.generated.resources.august_short
import it.sagratime.app_core.generated.resources.december
import it.sagratime.app_core.generated.resources.december_short
import it.sagratime.app_core.generated.resources.february
import it.sagratime.app_core.generated.resources.february_short
import it.sagratime.app_core.generated.resources.january
import it.sagratime.app_core.generated.resources.january_short
import it.sagratime.app_core.generated.resources.july
import it.sagratime.app_core.generated.resources.july_short
import it.sagratime.app_core.generated.resources.june
import it.sagratime.app_core.generated.resources.june_short
import it.sagratime.app_core.generated.resources.march
import it.sagratime.app_core.generated.resources.march_short
import it.sagratime.app_core.generated.resources.may
import it.sagratime.app_core.generated.resources.may_short
import it.sagratime.app_core.generated.resources.november
import it.sagratime.app_core.generated.resources.november_short
import it.sagratime.app_core.generated.resources.october
import it.sagratime.app_core.generated.resources.october_short
import it.sagratime.app_core.generated.resources.one_day
import it.sagratime.app_core.generated.resources.other_placeholder_dark
import it.sagratime.app_core.generated.resources.other_placeholder_light
import it.sagratime.app_core.generated.resources.sagra_placeholder_dark
import it.sagratime.app_core.generated.resources.sagra_placeholder_light
import it.sagratime.app_core.generated.resources.september
import it.sagratime.app_core.generated.resources.september_short
import it.sagratime.app_core.generated.resources.town_festival_placeholder_dark
import it.sagratime.app_core.generated.resources.town_festival_placeholder_light
import it.sagratime.app_core.generated.resources.x_days
import it.sagratime.core.data.Event
import it.sagratime.core.data.EventType
import kotlinx.datetime.Month
import org.jetbrains.compose.resources.stringResource

@Composable
fun Event.localizedDays(): String {
    val days = until.datetime.day - from.datetime.day + 1
    return when (days) {
        1 -> stringResource(Res.string.one_day)
        else -> stringResource(Res.string.x_days, days)
    }
}

@Composable
fun Event.localizedDateString(): String =
    when (from.datetime.year) {
        until.datetime.year ->
            when (from.datetime.month) {
                until.datetime.month ->
                    when (from.datetime.day) {
                        until.datetime.day -> sameDayLocalizedString()
                        else -> sameMonthLocalizedString()
                    }
                else -> sameYearLocalizedString()
            }
        else -> differentYearLocalizedString()
    }

@Composable
fun Event.sameDayLocalizedString() = "${from.datetime.day} ${from.datetime.month.localized()} ${from.datetime.year}"

@Composable
fun Event.sameMonthLocalizedString() =
    "${from.datetime.day} - ${until.datetime.day} ${from.datetime.month.localized()} ${until.datetime.year}"

@Composable
fun Event.sameYearLocalizedString() =
    "${from.datetime.day} ${from.datetime.month.localizedShort()} - ${until.datetime.day} ${until.datetime.month.localizedShort()} ${until.datetime.year}"

@Composable
fun Event.differentYearLocalizedString() =
    "${from.datetime.day} ${from.datetime.month.localizedShort()} ${from.datetime.year} - ${until.datetime.day} ${until.datetime.month.localizedShort()} ${until.datetime.year}"

@Composable
fun Month.localized(caps: Boolean = true): String {
    val resource =
        when (this) {
            Month.JANUARY -> Res.string.january
            Month.FEBRUARY -> Res.string.february
            Month.MARCH -> Res.string.march
            Month.APRIL -> Res.string.april
            Month.MAY -> Res.string.may
            Month.JUNE -> Res.string.june
            Month.JULY -> Res.string.july
            Month.AUGUST -> Res.string.august
            Month.SEPTEMBER -> Res.string.september
            Month.OCTOBER -> Res.string.october
            Month.NOVEMBER -> Res.string.november
            Month.DECEMBER -> Res.string.december
        }
    val string = stringResource(resource)
    return when {
        caps -> string.uppercase()
        else -> string
    }
}

@Composable
fun Month.localizedShort(capitalized: Boolean = true): String {
    val resource =
        when (this) {
            Month.JANUARY -> Res.string.january_short
            Month.FEBRUARY -> Res.string.february_short
            Month.MARCH -> Res.string.march_short
            Month.APRIL -> Res.string.april_short
            Month.MAY -> Res.string.may_short
            Month.JUNE -> Res.string.june_short
            Month.JULY -> Res.string.july_short
            Month.AUGUST -> Res.string.august_short
            Month.SEPTEMBER -> Res.string.september_short
            Month.OCTOBER -> Res.string.october_short
            Month.NOVEMBER -> Res.string.november_short
            Month.DECEMBER -> Res.string.december_short
        }
    val string = stringResource(resource)
    return when {
        capitalized -> string.uppercase()
        else -> string
    }
}

@Composable
fun EventType.placeholderImage() =
    when (this) {
        EventType.Sagra -> sagraPlaceholderImage()
        EventType.TownFestival -> townFestivalPlaceholderImage()
        EventType.Other -> otherPlaceholderImage()
    }

@Composable
fun sagraPlaceholderImage() =
    when {
        SagraTimeTheme.isDark -> Res.drawable.sagra_placeholder_dark
        else -> Res.drawable.sagra_placeholder_light
    }

@Composable
fun townFestivalPlaceholderImage() =
    when {
        SagraTimeTheme.isDark -> Res.drawable.town_festival_placeholder_dark
        else -> Res.drawable.town_festival_placeholder_light
    }

@Composable
fun otherPlaceholderImage() =
    when {
        SagraTimeTheme.isDark -> Res.drawable.other_placeholder_dark
        else -> Res.drawable.other_placeholder_light
    }
