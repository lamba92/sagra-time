package it.sagratime.app.core.feature.cards.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.sagratime.app.core.components.SagraTimeTheme
import it.sagratime.app_core.generated.resources.Res
import it.sagratime.app_core.generated.resources.search_card_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun SearchCardTitle(modifier: Modifier = Modifier.Companion) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Companion.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.Search,
            contentDescription = null,
            tint = SagraTimeTheme.colorScheme.primary,
        )
        Text(
            text = stringResource(Res.string.search_card_title),
            style = SagraTimeTheme.typography.titleLarge,
        )
    }
}
