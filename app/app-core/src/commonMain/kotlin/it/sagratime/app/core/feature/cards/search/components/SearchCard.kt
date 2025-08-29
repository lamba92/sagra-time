@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)

package it.sagratime.app.core.feature.cards.search.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import it.sagratime.app.core.components.SagraTimeCard
import it.sagratime.app.core.components.SagraTimeTheme
import it.sagratime.app.core.components.SagraTimeToggleButton
import it.sagratime.app.core.feature.cards.search.SearchCardEffect
import it.sagratime.app.core.feature.cards.search.SearchCardEvent
import it.sagratime.app.core.feature.cards.search.SearchCardState
import it.sagratime.app.core.feature.cards.search.SearchCardViewModel
import it.sagratime.app_core.generated.resources.Res
import it.sagratime.app_core.generated.resources.custom
import it.sagratime.app_core.generated.resources.distance
import it.sagratime.app_core.generated.resources.km
import it.sagratime.app_core.generated.resources.miles
import it.sagratime.app_core.generated.resources.next_week
import it.sagratime.app_core.generated.resources.save
import it.sagratime.app_core.generated.resources.search_radius
import it.sagratime.app_core.generated.resources.this_weekend
import it.sagratime.app_core.generated.resources.today
import it.sagratime.app_core.generated.resources.when_to_search
import it.sagratime.core.units.Length
import it.sagratime.core.units.LengthUnit
import it.sagratime.core.units.MeasurementSystem
import it.sagratime.core.units.toStringWithPrecision
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Composable
fun SearchCard(
    modifier: Modifier = Modifier.Companion,
    viewModel: SearchCardViewModel = koinInject(),
    onDisabledLocationServicesRequest: () -> Unit = { },
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                SearchCardEffect.NotifyLocationServicesDisabled ->
                    onDisabledLocationServicesRequest()
            }
        }
    }
    SearchCard(
        modifier = modifier,
        state = state,
        onEvent = viewModel::onEvent,
    )
}

@Composable
fun SearchCard(
    modifier: Modifier = Modifier.Companion,
    state: SearchCardState,
    onEvent: (SearchCardEvent) -> Unit,
) {
    SagraTimeCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(SagraTimeTheme.metrics.cards.innerPaddings),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SearchCardTitle()
            SearchCardTextField(
                text = state.query,
                onValueChange = {
                    onEvent(
                        SearchCardEvent.QueryChanged(it),
                    )
                },
            )
            AnimatedContent(targetState = state.isAdvancedSearch) { isAdvancedSearch ->
                when {
                    isAdvancedSearch -> AdvancedSearchCardContent(state = state, onEvent = onEvent)
                    else -> SimpleSearchCardContent(state = state, onEvent = onEvent)
                }
            }
        }
    }
}

@Composable
fun AdvancedSearchCardContent(
    state: SearchCardState,
    modifier: Modifier = Modifier,
    onEvent: (SearchCardEvent) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        DateSelectionGroup(onEvent, state)
        DistanceSlider(state, onEvent)
    }
}

@Composable
fun DistanceSlider(
    state: SearchCardState,
    onEvent: (SearchCardEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(Res.string.distance),
            style = SagraTimeTheme.typography.labelLarge,
        )
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = stringResource(Res.string.search_radius),
                modifier = Modifier.align(Alignment.CenterStart),
            )
            val kmString = stringResource(Res.string.km)
            val milesString = stringResource(Res.string.miles)
            val text =
                rememberRadiusString(
                    kmString = kmString,
                    milesString = milesString,
                    measurementSystem = state.measurementSystem,
                    length = state.searchRadius,
                )
            Text(
                text = text,
                modifier = Modifier.align(Alignment.CenterEnd),
                color = SagraTimeTheme.colorScheme.primary,
            )
        }
        Slider(
            value =
                when (state.measurementSystem) {
                    MeasurementSystem.Metric -> state.searchRadius.inKilometers.toFloat()
                    MeasurementSystem.Imperial -> state.searchRadius.inMiles.toFloat()
                },
            valueRange =
                when (state.measurementSystem) {
                    MeasurementSystem.Metric -> 5f..500f
                    MeasurementSystem.Imperial -> 3f..300f
                },
            onValueChange = {
                onEvent(
                    SearchCardEvent.SearchRadiusChanged(
                        when (state.measurementSystem) {
                            MeasurementSystem.Metric -> Length.from(it, LengthUnit.Kilometers)
                            MeasurementSystem.Imperial -> Length.from(it, LengthUnit.Miles)
                        },
                    ),
                )
            },
        )
    }
}

@Composable
private fun rememberRadiusString(
    kmString: String,
    milesString: String,
    measurementSystem: MeasurementSystem,
    length: Length,
): String =
    remember(
        kmString,
        milesString,
        measurementSystem,
        length,
    ) {
        buildString {
            append(
                when (measurementSystem) {
                    MeasurementSystem.Metric ->
                        length.inKilometers.toStringWithPrecision(0)

                    MeasurementSystem.Imperial ->
                        length.inMiles.toStringWithPrecision(0)
                },
            )
            append(" ")
            append(
                when (measurementSystem) {
                    MeasurementSystem.Metric -> kmString
                    MeasurementSystem.Imperial -> milesString
                },
            )
        }
    }

@Composable
fun DateSelectionGroup(
    onEvent: (SearchCardEvent) -> Unit,
    state: SearchCardState,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier.fillMaxWidth(),
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        Column(
            modifier =
                modifier
                    .fillMaxWidth()
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(Res.string.when_to_search),
                style = SagraTimeTheme.typography.labelLarge,
            )
            FirstDateSelectionRow(onEvent, state)
            SecondDateSelectionRow(
                state = state,
                onEvent = onEvent,
                onCustomDateClick = { expanded = true },
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset(8.dp, 8.dp),
        ) {
            DateRangeSelectorPopup(
                onDismissRequest = { event ->
                    expanded = false
                    if (event != null) {
                        onEvent(event)
                    }
                },
            )
        }
    }
}

@Composable
private fun SecondDateSelectionRow(
    state: SearchCardState,
    onEvent: (SearchCardEvent) -> Unit,
    onCustomDateClick: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        SagraTimeToggleButton(
            modifier = Modifier.weight(1f),
            onClick = { onEvent(SearchCardEvent.DateSelectionChanged(SearchCardState.DateSelection.NextWeek)) },
            isActive = state.dateSelection == SearchCardState.DateSelection.NextWeek,
            content = { Text(stringResource(Res.string.next_week)) },
        )

        SagraTimeToggleButton(
            modifier = Modifier.weight(1f),
            onClick = onCustomDateClick,
            isActive = state.dateSelection is SearchCardState.DateSelection.Custom,
            content = { Text(stringResource(Res.string.custom)) },
        )
    }
}

@Composable
private fun DateRangeSelectorPopup(
    modifier: Modifier = Modifier,
    onDismissRequest: (SearchCardEvent.DateSelectionChanged?) -> Unit,
) {
    Column(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        val datePickerState =
            rememberDateRangePickerState(
                selectableDates =
                    object : SelectableDates {
                        override fun isSelectableDate(utcTimeMillis: Long): Boolean =
                            utcTimeMillis >= (Clock.System.now() - 24.hours).toEpochMilliseconds()
                    },
            )

        val selectedRange by derivedStateOf {
            val start = datePickerState.selectedStartDateMillis
            val end = datePickerState.selectedEndDateMillis
            if (start != null && end != null) {
                SearchCardState.DateSelection.Custom(
                    from =
                        Instant
                            .fromEpochMilliseconds(start)
                            .toLocalDateTime(TimeZone.currentSystemDefault())
                            .toLocalDate(),
                    to =
                        Instant
                            .fromEpochMilliseconds(end)
                            .toLocalDateTime(TimeZone.currentSystemDefault())
                            .toLocalDate(),
                )
            } else {
                null
            }
        }
        DateRangePicker(state = datePickerState)
        Button(
            onClick = {
                val actualRange = selectedRange
                if (actualRange != null) {
                    onDismissRequest(SearchCardEvent.DateSelectionChanged(actualRange))
                }
            },
            modifier = Modifier.align(Alignment.End),
            content = { Text(stringResource(Res.string.save)) },
        )
    }
}

@Composable
fun FirstDateSelectionRow(
    onEvent: (SearchCardEvent) -> Unit,
    state: SearchCardState,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        SagraTimeToggleButton(
            modifier = Modifier.weight(1f),
            onClick = { onEvent(SearchCardEvent.DateSelectionChanged(SearchCardState.DateSelection.Today)) },
            isActive = state.dateSelection == SearchCardState.DateSelection.Today,
            content = { Text(stringResource(Res.string.today)) },
        )
        SagraTimeToggleButton(
            modifier = Modifier.weight(1f),
            onClick = { onEvent(SearchCardEvent.DateSelectionChanged(SearchCardState.DateSelection.ThisWeekend)) },
            isActive = state.dateSelection == SearchCardState.DateSelection.ThisWeekend,
            content = { Text(stringResource(Res.string.this_weekend)) },
        )
    }
}

private fun LocalDateTime.toLocalDate() = LocalDate(year, month, day)
