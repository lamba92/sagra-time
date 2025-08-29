package it.sagratime.app.core.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.materialkolor.rememberDynamicColorScheme
import it.sagratime.app_core.generated.resources.Inter_24pt_Regular
import it.sagratime.app_core.generated.resources.Inter_24pt_SemiBold
import it.sagratime.app_core.generated.resources.Res
import org.jetbrains.compose.resources.Font

interface IsSystemDarkProvider {
    val isDark: Boolean
        @Composable get

    companion object : IsSystemDarkProvider {
        override val isDark: Boolean
            @Composable get() = isSystemInDarkTheme()
    }
}

val LocalIsSystemDarkProvider =
    staticCompositionLocalOf<IsSystemDarkProvider> { IsSystemDarkProvider }

@Composable
fun SagraTimeTheme(
    isDark: Boolean = LocalIsSystemDarkProvider.current.isDark,
    seedColor: Color = Color.Yellow,
    content: @Composable () -> Unit,
) {
    val colorScheme =
        rememberDynamicColorScheme(
            seedColor = seedColor,
            primary = Color.Yellow,
            secondary = Color.Green,
            tertiary = Color.Red,
            isDark = isDark,
        )
    MaterialTheme(
        colorScheme = colorScheme,
        typography = InterTypography(),
        content = content,
    )
}

@Suppress("UnusedReceiverParameter")
@Composable
fun ButtonDefaults.disabledColors(): ButtonColors =
    ButtonDefaults.buttonColors(
        containerColor = ButtonDefaults.buttonColors().disabledContainerColor,
        contentColor = ButtonDefaults.buttonColors().disabledContentColor,
    )

object SagraTimeTheme {
    val typography
        @Composable @ReadOnlyComposable
        get() = MaterialTheme.typography

    val colorScheme
        @Composable @ReadOnlyComposable
        get() = MaterialTheme.colorScheme

    val shapes
        @Composable @ReadOnlyComposable
        get() = MaterialTheme.shapes

    val textStyles
        @Composable @ReadOnlyComposable
        get() = MaterialTheme.typography

    val metrics
        @Composable @ReadOnlyComposable
        get() = LocalMetrics.current
}

val LocalMetrics =
    staticCompositionLocalOf { SagraTimeMetrics.DEFAULT }

data class SagraTimeMetrics(
    val cards: CardsMetrics = CardsMetrics.DEFAULT,
) {
    companion object {
        val DEFAULT = SagraTimeMetrics()
    }
}

data class CardsMetrics(
    val width: Dp = 400.dp,
    val innerPaddings: Dp = 16.dp,
) {
    companion object {
        val DEFAULT = CardsMetrics()
    }
}

@Composable
private fun InterTypography(): Typography {
    val interFont =
        FontFamily(
            Font(Res.font.Inter_24pt_Regular, FontWeight.Normal),
            Font(Res.font.Inter_24pt_SemiBold, FontWeight.Bold),
        )

    return with(MaterialTheme.typography) {
        copy(
            displayLarge = displayLarge.copy(fontFamily = interFont, fontWeight = FontWeight.Bold),
            displayMedium =
                displayMedium.copy(
                    fontFamily = interFont,
                    fontWeight = FontWeight.Bold,
                ),
            displaySmall = displaySmall.copy(fontFamily = interFont, fontWeight = FontWeight.Bold),
            headlineLarge =
                headlineLarge.copy(
                    fontFamily = interFont,
                    fontWeight = FontWeight.Bold,
                ),
            headlineMedium =
                headlineMedium.copy(
                    fontFamily = interFont,
                    fontWeight = FontWeight.Bold,
                ),
            headlineSmall =
                headlineSmall.copy(
                    fontFamily = interFont,
                    fontWeight = FontWeight.Bold,
                ),
            titleLarge = titleLarge.copy(fontFamily = interFont, fontWeight = FontWeight.Bold),
            titleMedium = titleMedium.copy(fontFamily = interFont, fontWeight = FontWeight.Bold),
            titleSmall = titleSmall.copy(fontFamily = interFont, fontWeight = FontWeight.Bold),
            labelLarge = labelLarge.copy(fontFamily = interFont, fontWeight = FontWeight.Normal),
            labelMedium = labelMedium.copy(fontFamily = interFont, fontWeight = FontWeight.Normal),
            labelSmall = labelSmall.copy(fontFamily = interFont, fontWeight = FontWeight.Normal),
            bodyLarge = bodyLarge.copy(fontFamily = interFont, fontWeight = FontWeight.Normal),
            bodyMedium = bodyMedium.copy(fontFamily = interFont, fontWeight = FontWeight.Normal),
            bodySmall = bodySmall.copy(fontFamily = interFont, fontWeight = FontWeight.Normal),
        )
    }
}
