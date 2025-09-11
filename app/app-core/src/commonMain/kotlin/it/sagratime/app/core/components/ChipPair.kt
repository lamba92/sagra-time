package it.sagratime.app.core.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp

/**
 * Produce lively chip colors from a base hue without looking washed out.
 *
 * @param vividness how strong the color should feel [0f..1f]. Higher = less pastel.
 *                  0.0 = very pastel, 1.0 = vivid. Try 0.7–0.85.
 */
@Composable
fun chipColorsFrom(
    base: Color,
    vividness: Float = 1f, // washy -> set higher
    dark: Boolean = LocalIsSystemDarkProvider.current.isDark,
): Pair<Color, Color> {
    // Map vividness to how much we blend toward white/black
    // higher vividness => smaller blend (i.e., keep more of base)
    val towardWhite = 0.85f * (1f - vividness) + 0.20f * vividness // ~0.20 at vividness=1
    val textDarken = 0.35f * (1f - vividness) + 0.12f * vividness // ~0.12 at vividness=1

    return if (!dark) {
        val bg = lerp(base, Color.White, towardWhite)
        val fg = lerp(base, Color.Black, textDarken)
        bg to fg
    } else {
        val towardBlack = towardWhite
        val textLighten = 0.70f * (1f - vividness) + 0.25f * vividness // lighter text on dark
        val bg = lerp(base, Color.Black, towardBlack)
        val fg = lerp(base, Color.White, textLighten)
        bg to fg
    }
}

@Composable
fun PastelChip(
    index: Int,
    vividness: Float = 1f,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    label: @Composable () -> Unit,
) {
    PastelChip(
        modifier = modifier,
        color =
            when (index % 3) {
                0 -> SagraTimeTheme.colorScheme.primary
                1 -> SagraTimeTheme.colorScheme.secondary
                else -> SagraTimeTheme.colorScheme.tertiary
            },
        vividness = vividness,
        onClick = onClick,
        label = label,
    )
}

@Composable
fun PastelChip(
    modifier: Modifier = Modifier,
    color: Color,
    vividness: Float = 1f,
    onClick: () -> Unit = {},
    label: @Composable () -> Unit,
) {
    val (bg, fg) = chipColorsFrom(color, vividness)
    AssistChip(
        modifier = modifier,
        onClick = onClick,
        label = label,
        shape = RoundedCornerShape(16.dp),
        border = AssistChipDefaults.assistChipBorder(false),
        colors =
            AssistChipDefaults.assistChipColors(
                containerColor = bg,
                labelColor = fg,
                trailingIconContentColor = fg,
                leadingIconContentColor = fg,
            ),
    )
}
