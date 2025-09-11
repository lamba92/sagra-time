package it.sagratime.app.core.feature.cards.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.sagratime.app.core.components.SagraTimeToggleButton
import it.sagratime.app_core.generated.resources.Res
import it.sagratime.app_core.generated.resources.advanced_filters_button_text
import org.jetbrains.compose.resources.stringResource

@Composable
fun AdvancedFiltersToggleButton(
    isActive: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    SagraTimeToggleButton(
        modifier = modifier,
        isActive = isActive,
        onClick = onClick,
        content = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = Icons.Outlined.Tune,
                    contentDescription = null,
                )
                Text(
                    text = stringResource(Res.string.advanced_filters_button_text),
                )
            }
        },
    )
}
