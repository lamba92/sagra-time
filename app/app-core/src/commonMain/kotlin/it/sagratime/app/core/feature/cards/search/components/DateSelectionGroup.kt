@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)

package it.sagratime.app.core.feature.cards.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import it.sagratime.app.core.components.SagraTimeTheme
import it.sagratime.app.core.components.WithLocalTypography
import it.sagratime.app.core.feature.cards.search.SearchCardEvent
import it.sagratime.app.core.feature.cards.search.SearchCardState
import it.sagratime.app_core.generated.resources.Res
import it.sagratime.app_core.generated.resources.when_to_search
import it.sagratime.core.datetime.toZonedDate
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

fun LocalDate.toEpochMillis() = toEpochDays() * 24 * 60 * 60 * 1000

object TodayOnwards : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean = utcTimeMillis >= (Clock.System.now() - 24.hours).toEpochMilliseconds()
}

@Composable
fun DateSelectionGroup(
    state: SearchCardState,
    onEvent: (SearchCardEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(Res.string.when_to_search),
            style = SagraTimeTheme.typography.labelLarge,
        )

        val datePickerState =
            rememberDateRangePickerState(
                initialSelectedStartDateMillis =
                    state.selectedDateRange.start.date
                        .toEpochMillis(),
                initialSelectedEndDateMillis =
                    state.selectedDateRange.end.date
                        .toEpochMillis(),
                selectableDates = TodayOnwards,
            )

        LaunchedEffect(Unit) {
            snapshotFlow { datePickerState.selectedStartDateMillis to datePickerState.selectedEndDateMillis }
                .collect { (start, end) ->
                    if (start != null && end != null) {
                        onEvent(
                            SearchCardEvent.DateRangeSelectionChanged(
                                SearchCardState.DateRangeSelection(
                                    start =
                                        Instant
                                            .fromEpochMilliseconds(start)
                                            .toZonedDate(TimeZone.currentSystemDefault()),
                                    end =
                                        Instant
                                            .fromEpochMilliseconds(end)
                                            .toZonedDate(TimeZone.currentSystemDefault()),
                                ),
                            ),
                        )
                    }
                }
        }

        WithLocalTypography(
            SagraTimeTheme.typography.copy(
                titleLarge = SagraTimeTheme.typography.titleMedium,
            ),
        ) {
            DateRangePicker(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(440.dp)
                        .clip(RoundedCornerShape(SagraTimeTheme.shapes.medium.topStart)),
                state = datePickerState,
            )
        }
    }
}
