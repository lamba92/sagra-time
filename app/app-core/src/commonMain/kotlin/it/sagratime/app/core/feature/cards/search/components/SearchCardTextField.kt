package it.sagratime.app.core.feature.cards.search.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import it.sagratime.app.core.components.ComposableContent
import it.sagratime.app_core.generated.resources.Res
import it.sagratime.app_core.generated.resources.search_card_placeholder
import org.jetbrains.compose.resources.stringResource

@Composable
fun SearchCardTextField(
    text: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = text,
        onValueChange = onValueChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
            )
        },
        trailingIcon = getClearTextIconContent(text, onValueChange),
        placeholder = { Text(stringResource(Res.string.search_card_placeholder)) },
    )
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
