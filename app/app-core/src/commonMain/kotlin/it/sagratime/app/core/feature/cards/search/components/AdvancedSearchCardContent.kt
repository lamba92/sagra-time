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
    modifier: Modifier = Modifier,
    onEvent: (SearchCardEvent) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        TypeSelectionGroup(state, onEvent)
        DateSelectionGroup(state, onEvent)
        DistanceSlider(state, onEvent)
    }
}

@Composable
fun TypeSelectionGroup(
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
            CheckboxRow(
                modifier = Modifier.weight(0.5f),
                eventType = EventType.Sagra,
                isChecked = EventType.Sagra in state.selectedTypes,
                onClick = { onEvent(SearchCardEvent.SelectedTypesChanged(EventType.Sagra)) },
            )
            CheckboxRow(
                modifier = Modifier.weight(0.5f),
                eventType = EventType.TownFestival,
                isChecked = EventType.TownFestival in state.selectedTypes,
                onClick = { onEvent(SearchCardEvent.SelectedTypesChanged(EventType.TownFestival)) },
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(0.5f),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CheckboxRow(
                modifier = Modifier.fillMaxWidth(),
                eventType = EventType.Other,
                isChecked = EventType.Other in state.selectedTypes,
                onClick = { onEvent(SearchCardEvent.SelectedTypesChanged(EventType.Other)) },
            )
        }
    }
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
