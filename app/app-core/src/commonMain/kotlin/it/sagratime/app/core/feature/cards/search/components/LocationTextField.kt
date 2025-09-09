@file:OptIn(ExperimentalMaterial3Api::class)

package it.sagratime.app.core.feature.cards.search.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import it.sagratime.app.core.components.localizedString
import it.sagratime.app.core.feature.cards.search.SearchCardEvent
import it.sagratime.app.core.feature.cards.search.SearchCardState
import it.sagratime.app_core.generated.resources.Res
import it.sagratime.app_core.generated.resources.search_card_location_placeholder
import org.jetbrains.compose.resources.stringResource

@Composable
fun LocationTextField(
    state: SearchCardState,
    onEvent: (SearchCardEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isFocused by remember { mutableStateOf(false) }
    val expanded = isFocused && state.locationQueryTips.isNotEmpty()
    val focusManager = LocalFocusManager.current
    val enabled =
        state.aroundMe == SearchCardState.AroundMe.Unselected ||
            state.aroundMe == SearchCardState.AroundMe.LocationServicesDisabled
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { },
    ) {
        OutlinedTextField(
            enabled = enabled,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .onFocusChanged { isFocused = it.isFocused || it.hasFocus }
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable),
            value = state.locationQuery,
            onValueChange = { onEvent(SearchCardEvent.LocationQueryChanged(it)) },
            leadingIcon = {
                when {
                    state.isLocationQueryTipsLoading && enabled ->
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
                        modifier = Modifier.clickable(enabled) { onEvent(SearchCardEvent.ClearLocationSearchClick) },
                    )
                }
            },
            placeholder = {
                Text(
                    stringResource(Res.string.search_card_location_placeholder),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { },
        ) {
            Column {
                state.locationQueryTips.forEachIndexed { index, location ->
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.LocationCity,
                                contentDescription = null,
                            )
                        },
                        text = {
                            Text("${location.cityName}, ${location.region.localizedString()}")
                        },
                        onClick = {
                            onEvent(SearchCardEvent.LocationTipCLick(location))
                            focusManager.clearFocus()
                        },
                    )
                    if (index != state.locationQueryTips.lastIndex) {
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}
