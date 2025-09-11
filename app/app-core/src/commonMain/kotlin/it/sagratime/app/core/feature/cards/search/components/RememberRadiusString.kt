package it.sagratime.app.core.feature.cards.search.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import it.sagratime.core.units.Length
import it.sagratime.core.units.MeasurementSystem
import it.sagratime.core.units.toStringWithPrecision

@Composable
fun rememberRadiusString(
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
