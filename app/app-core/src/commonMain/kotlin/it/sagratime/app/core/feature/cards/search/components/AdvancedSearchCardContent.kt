package it.sagratime.app.core.feature.cards.search.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.sagratime.app.core.components.SagraTimeTheme
import it.sagratime.app.core.components.ScreenType
import it.sagratime.app.core.feature.cards.search.SearchCardEvent
import it.sagratime.app.core.feature.cards.search.SearchCardState
import it.sagratime.app_core.generated.resources.Res
import it.sagratime.app_core.generated.resources.event_type
import it.sagratime.app_core.generated.resources.other
import it.sagratime.app_core.generated.resources.sagre
import it.sagratime.app_core.generated.resources.town_festivals
import it.sagratime.core.data.EventType
import org.jetbrains.compose.resources.stringResource

@Composable
fun AdvancedSearchCardContent(
    state: SearchCardState,
    onEvent: (SearchCardEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (SagraTimeTheme.metrics.screenType) {
        ScreenType.SMALL ->
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                SmallScreenTypeSelectionGroup(state, onEvent)
                DateSelectionGroup(state, onEvent)
            }
        else ->
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                DateSelectionGroup(
                    state = state,
                    onEvent = onEvent,
                    modifier = Modifier.weight(0.7f),
                )
                MediumScreenTypeSelectionGroup(
                    modifier = Modifier.weight(0.3f),
                    state = state,
                    onEvent = onEvent,
                )
            }
    }
}

@Composable
fun LargeScreenTypeSelectionGroup(
    state: SearchCardState,
    onEvent: (SearchCardEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(Res.string.event_type),
            style = SagraTimeTheme.typography.labelLarge,
        )
        SagraCheckboxRow(
            modifier = Modifier.weight(1f),
            isChecked = EventType.Sagra in state.selectedTypes,
            onCLick = onEvent,
        )
        TownFestivalCheckboxRow(
            modifier = Modifier.weight(1f),
            isChecked = EventType.TownFestival in state.selectedTypes,
            onCLick = onEvent,
        )
        OtherEventCheckboxRow(
            modifier = Modifier.weight(1f),
            isChecked = EventType.Other in state.selectedTypes,
            onCLick = onEvent,
        )
    }
}

@Composable
fun MediumScreenTypeSelectionGroup(
    state: SearchCardState,
    onEvent: (SearchCardEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(Res.string.event_type),
            style = SagraTimeTheme.typography.labelLarge,
        )
        SagraCheckboxRow(
            modifier = Modifier.fillMaxWidth(),
            isChecked = EventType.Sagra in state.selectedTypes,
            onCLick = onEvent,
        )
        TownFestivalCheckboxRow(
            modifier = Modifier.fillMaxWidth(),
            isChecked = EventType.TownFestival in state.selectedTypes,
            onCLick = onEvent,
        )
        OtherEventCheckboxRow(
            modifier = Modifier.fillMaxWidth(),
            isChecked = EventType.Other in state.selectedTypes,
            onCLick = onEvent,
        )
    }
}

@Composable
fun SmallScreenTypeSelectionGroup(
    state: SearchCardState,
    onEvent: (SearchCardEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(Res.string.event_type),
            style = SagraTimeTheme.typography.labelLarge,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SagraCheckboxRow(
                modifier = Modifier.weight(0.5f),
                isChecked = EventType.Sagra in state.selectedTypes,
                onCLick = onEvent,
            )
            TownFestivalCheckboxRow(
                modifier = Modifier.weight(0.5f),
                isChecked = EventType.TownFestival in state.selectedTypes,
                onCLick = onEvent,
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(0.5f),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OtherEventCheckboxRow(
                modifier = Modifier.weight(0.5f),
                isChecked = EventType.Other in state.selectedTypes,
                onCLick = onEvent,
            )
        }
    }
}

@Composable
fun SagraCheckboxRow(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    onCLick: (SearchCardEvent.SelectedTypesChanged) -> Unit = {},
) {
    CheckboxRow(
        modifier = modifier,
        eventType = EventType.Sagra,
        isChecked = isChecked,
        onClick = { onCLick(SearchCardEvent.SelectedTypesChanged(EventType.Sagra)) },
    )
}

@Composable
fun TownFestivalCheckboxRow(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    onCLick: (SearchCardEvent.SelectedTypesChanged) -> Unit = {},
) {
    CheckboxRow(
        modifier = modifier,
        eventType = EventType.TownFestival,
        isChecked = isChecked,
        onClick = { onCLick(SearchCardEvent.SelectedTypesChanged(EventType.TownFestival)) },
    )
}

@Composable
fun OtherEventCheckboxRow(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    onCLick: (SearchCardEvent.SelectedTypesChanged) -> Unit = {},
) {
    CheckboxRow(
        modifier = modifier,
        eventType = EventType.Other,
        isChecked = isChecked,
        onClick = { onCLick(SearchCardEvent.SelectedTypesChanged(EventType.Other)) },
    )
}

@Composable
fun CheckboxRow(
    eventType: EventType,
    isChecked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .clickable(onClick = onClick)
                .padding(6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(checked = isChecked, onCheckedChange = null)
        val resourceName =
            when (eventType) {
                EventType.Sagra -> Res.string.sagre
                EventType.TownFestival -> Res.string.town_festivals
                EventType.Other -> Res.string.other
            }
        Text(text = stringResource(resourceName))
    }
}
