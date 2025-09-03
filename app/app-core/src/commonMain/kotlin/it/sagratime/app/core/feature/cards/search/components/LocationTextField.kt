@file:OptIn(ExperimentalMaterial3Api::class)

package it.sagratime.app.core.feature.cards.search.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.sagratime.app.core.feature.cards.search.SearchCardEvent
import it.sagratime.app.core.feature.cards.search.SearchCardState
import it.sagratime.app_core.generated.resources.Res
import it.sagratime.app_core.generated.resources.search_card_location_placeholder
import org.jetbrains.compose.resources.stringResource

@Composable
fun LocationTextField(
    state: SearchCardState,
    onEvent: (SearchCardEvent) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded =
                when {
                    state.locationQueryTips.isNotEmpty() -> !expanded
                    else -> false
                }
        },
    ) {
        OutlinedTextField(
            enabled =
                state.aroundMe == SearchCardState.AroundMe.Unselected ||
                    state.aroundMe == SearchCardState.AroundMe.LocationServicesDisabled,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable),
            value = state.locationQuery,
            onValueChange = { onEvent(SearchCardEvent.LocationQueryChanged(it)) },
            leadingIcon = {
                when {
                    state.isQueryTipsLoading ->
                        CircularProgressIndicator(modifier = Modifier.size(16.dp))

                    else ->
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = null,
                        )
                }
            },
            trailingIcon = {
                if (!state.locationQuery.isEmpty()) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = null,
                        modifier = Modifier.clickable { onEvent(SearchCardEvent.ClearLocationSearchClick) },
                    )
                }
            },
            placeholder = { Text(stringResource(Res.string.search_card_location_placeholder)) },
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            Column {
                state.locationQueryTips.forEachIndexed { index, location ->
                    DropdownMenuItem(
                        text = { Text(location.cityName) },
                        onClick = {
                            expanded = false
                            onEvent(SearchCardEvent.LocationTipCLick(location))
                        },
                    )
                }
            }
        }
    }
}
