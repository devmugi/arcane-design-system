package io.github.devmugi.arcane.design.components.display

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme

/**
 * Defines semantic color variants for text in the Arcane Design System.
 *
 * Using semantic variants ensures consistent color usage across the UI:
 * - Primary text for main content
 * - Secondary text for supporting content
 * - Disabled text for inactive states
 * - Custom for special cases requiring specific colors
 */
@Immutable
sealed class ArcaneTextVariant {
    /**
     * Primary text color - used for main content.
     * Maps to `ArcaneTheme.colors.text`.
     */
    data object Primary : ArcaneTextVariant()

    /**
     * Secondary text color - used for supporting or less prominent content.
     * Maps to `ArcaneTheme.colors.textSecondary`.
     */
    data object Secondary : ArcaneTextVariant()

    /**
     * Disabled text color - used for inactive or disabled states.
     * Maps to `ArcaneTheme.colors.textDisabled`.
     */
    data object Disabled : ArcaneTextVariant()

    /**
     * Custom text color - allows providing a specific color when semantic variants don't apply.
     *
     * @param color The custom color to use for the text.
     */
    data class Custom(val color: Color) : ArcaneTextVariant()
}

/**
 * Arcane Design System text component that enforces theme color usage via semantic variants.
 *
 * This is a minimal wrapper around Material3's [Text] composable that:
 * - Enforces semantic color usage through the [variant] parameter
 * - Provides access to Arcane theme typography via the [style] parameter
 * - Maintains flexibility for custom styling when needed
 *
 * Example usage:
 * ```kotlin
 * // Primary heading
 * ArcaneText(
 *     text = "Welcome",
 *     style = ArcaneTheme.typography.headlineLarge
 * )
 *
 * // Secondary body text
 * ArcaneText(
 *     text = "Supporting information",
 *     variant = ArcaneTextVariant.Secondary,
 *     style = ArcaneTheme.typography.bodyMedium
 * )
 *
 * // Disabled label
 * ArcaneText(
 *     text = "Unavailable",
 *     variant = ArcaneTextVariant.Disabled,
 *     style = ArcaneTheme.typography.labelSmall
 * )
 *
 * // Custom colored text
 * ArcaneText(
 *     text = "Error message",
 *     variant = ArcaneTextVariant.Custom(Color.Red),
 *     style = ArcaneTheme.typography.bodyMedium
 * )
 * ```
 *
 * @param text The text to display
 * @param modifier Modifier to apply to the text
 * @param variant Semantic color variant (Primary, Secondary, Disabled, or Custom)
 * @param style Text style from Arcane typography (displayLarge, headlineMedium, bodySmall, etc.)
 * @param maxLines Maximum number of lines for the text to span
 * @param overflow How visual overflow should be handled
 */
@Composable
fun ArcaneText(
    text: String,
    modifier: Modifier = Modifier,
    variant: ArcaneTextVariant = ArcaneTextVariant.Primary,
    style: TextStyle = ArcaneTheme.typography.bodyMedium,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    val colors = ArcaneTheme.colors

    val color = when (variant) {
        is ArcaneTextVariant.Primary -> colors.text
        is ArcaneTextVariant.Secondary -> colors.textSecondary
        is ArcaneTextVariant.Disabled -> colors.textDisabled
        is ArcaneTextVariant.Custom -> variant.color
    }

    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = style,
        maxLines = maxLines,
        overflow = overflow
    )
}
