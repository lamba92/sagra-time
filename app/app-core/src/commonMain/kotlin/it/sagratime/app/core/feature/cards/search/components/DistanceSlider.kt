package it.sagratime.app.core.feature.cards.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.sagratime.app.core.components.SagraTimeTheme
import it.sagratime.app.core.feature.cards.search.SearchCardEvent
import it.sagratime.app.core.feature.cards.search.SearchCardState
import it.sagratime.app_core.generated.resources.Res
import it.sagratime.app_core.generated.resources.distance
import it.sagratime.app_core.generated.resources.km
import it.sagratime.app_core.generated.resources.miles
import it.sagratime.app_core.generated.resources.search_radius
import it.sagratime.core.units.Length
import it.sagratime.core.units.LengthUnit
import it.sagratime.core.units.MeasurementSystem
import org.jetbrains.compose.resources.stringResource

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
                            MeasurementSystem.Metric ->
                                Length.from(
                                    it,
                                    LengthUnit.Kilometers,
                                )

                            MeasurementSystem.Imperial ->
                                Length.from(
                                    it,
                                    LengthUnit.Miles,
                                )
                        },
                    ),
                )
            },
        )
    }
}
