@file:OptIn(ExperimentalMaterial3Api::class)

package it.sagratime.app.core.feature.cards.search.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.unit.dp
import it.sagratime.app.core.components.ComposableContent
import it.sagratime.app.core.feature.cards.search.SearchCardEvent
import it.sagratime.app.core.feature.cards.search.SearchCardState
import it.sagratime.app_core.generated.resources.Res
import it.sagratime.app_core.generated.resources.search_card_placeholder
import org.jetbrains.compose.resources.stringResource

@Composable
fun SearchCardTextField(
    state: SearchCardState,
    onEvent: (SearchCardEvent) -> Unit,
) {
    var isFocused by remember { mutableStateOf(false) }
    val expanded = isFocused && state.queryTips.isNotEmpty()
    val focusManager = LocalFocusManager.current
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { },
    ) {
        OutlinedTextField(
            modifier =
                Modifier
                    .onFocusChanged { isFocused = it.isFocused || it.hasFocus }
                    .fillMaxWidth()
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable),
            value = state.query,
            onValueChange = { onEvent(SearchCardEvent.QueryChanged(it)) },
            leadingIcon = {
                when {
                    state.isQueryTipsLoading ->
                        CircularProgressIndicator(modifier = Modifier.size(16.dp))

                    else ->
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = null,
                        )
                }
            },
            trailingIcon = {
                if (!state.query.isEmpty()) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = null,
                        modifier = Modifier.clickable { onEvent(SearchCardEvent.ClearSearchClick) },
                    )
                }
            },
            placeholder = { Text(stringResource(Res.string.search_card_placeholder)) },
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { },
        ) {
            Column {
                state.queryTips.forEachIndexed { index, tip ->
                    DropdownMenuItem(
                        text = { Text(tip) },
                        onClick = {
                            onEvent(SearchCardEvent.SearchTipClick(tip))
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

@Composable
private fun getClearTextIconContent(
    text: String,
    onValueChange: (String) -> Unit,
): ComposableContent? =
    if (text.isEmpty()) {
        null
    } else {
        {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = null,
                modifier = Modifier.clickable { onValueChange("") },
            )
        }
    }
