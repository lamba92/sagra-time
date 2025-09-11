package it.sagratime.app.core.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
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
    screenType: ScreenType,
    isDark: Boolean = LocalIsSystemDarkProvider.current.isDark,
    seedColor: Color = Color.Yellow,
    typography: Typography = InterTypography(),
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
    CompositionLocalProvider(
        LocalMetrics provides SagraTimeMetrics(screenType = screenType),
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            content = content,
        )
    }
}

@Composable
fun WithLocalTypography(
    typography: Typography,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        typography = typography,
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
    val isDark
        @Composable
        get() = LocalIsSystemDarkProvider.current.isDark

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

enum class ScreenType {
    SMALL,
    MEDIUM,
    LARGE,
}

val LocalMetrics =
    staticCompositionLocalOf { SagraTimeMetrics.DEFAULT }

data class SagraTimeMetrics(
    val cards: CardsMetrics = CardsMetrics.DEFAULT,
    val screenType: ScreenType = ScreenType.SMALL,
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
private fun InterTypography(
    interFont: FontFamily =
        FontFamily(
            Font(Res.font.Inter_24pt_Regular, FontWeight.Normal),
            Font(Res.font.Inter_24pt_SemiBold, FontWeight.Bold),
        ),
    displayLarge: TextStyle =
        MaterialTheme.typography.displayLarge.copy(
            fontFamily = interFont,
            fontWeight = FontWeight.Bold,
        ),
    displayMedium: TextStyle =
        MaterialTheme.typography.displayMedium.copy(
            fontFamily = interFont,
            fontWeight = FontWeight.Bold,
        ),
    displaySmall: TextStyle =
        MaterialTheme.typography.displaySmall.copy(
            fontFamily = interFont,
            fontWeight = FontWeight.Bold,
        ),
    headlineLarge: TextStyle =
        MaterialTheme.typography.headlineLarge.copy(
            fontFamily = interFont,
            fontWeight = FontWeight.Bold,
        ),
    headlineMedium: TextStyle =
        MaterialTheme.typography.headlineMedium.copy(
            fontFamily = interFont,
            fontWeight = FontWeight.Bold,
        ),
    headlineSmall: TextStyle =
        MaterialTheme.typography.headlineSmall.copy(
            fontFamily = interFont,
            fontWeight = FontWeight.Bold,
        ),
    titleLarge: TextStyle =
        MaterialTheme.typography.titleLarge.copy(
            fontFamily = interFont,
            fontWeight = FontWeight.Bold,
        ),
    titleMedium: TextStyle =
        MaterialTheme.typography.titleMedium.copy(
            fontFamily = interFont,
            fontWeight = FontWeight.Bold,
        ),
    titleSmall: TextStyle =
        MaterialTheme.typography.titleSmall.copy(
            fontFamily = interFont,
            fontWeight = FontWeight.Bold,
        ),
    labelLarge: TextStyle =
        MaterialTheme.typography.labelLarge.copy(
            fontFamily = interFont,
            fontWeight = FontWeight.Normal,
        ),
    labelMedium: TextStyle =
        MaterialTheme.typography.labelMedium.copy(
            fontFamily = interFont,
            fontWeight = FontWeight.Normal,
        ),
    labelSmall: TextStyle =
        MaterialTheme.typography.labelSmall.copy(
            fontFamily = interFont,
            fontWeight = FontWeight.Normal,
        ),
    bodyLarge: TextStyle =
        MaterialTheme.typography.bodyLarge.copy(
            fontFamily = interFont,
            fontWeight = FontWeight.Normal,
        ),
    bodyMedium: TextStyle =
        MaterialTheme.typography.bodyMedium.copy(
            fontFamily = interFont,
            fontWeight = FontWeight.Normal,
        ),
    bodySmall: TextStyle =
        MaterialTheme.typography.bodySmall.copy(
            fontFamily = interFont,
            fontWeight = FontWeight.Normal,
        ),
): Typography =
    MaterialTheme.typography.copy(
        displayLarge = displayLarge,
        displayMedium = displayMedium,
        displaySmall = displaySmall,
        headlineLarge = headlineLarge,
        headlineMedium = headlineMedium,
        headlineSmall = headlineSmall,
        titleLarge = titleLarge,
        titleMedium = titleMedium,
        titleSmall = titleSmall,
        labelLarge = labelLarge,
        labelMedium = labelMedium,
        labelSmall = labelSmall,
        bodyLarge = bodyLarge,
        bodyMedium = bodyMedium,
        bodySmall = bodySmall,
    )
