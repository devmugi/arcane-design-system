package io.github.devmugi.arcane.design.foundation.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

/**
 * The Arcane Design System theme wrapper.
 *
 * Uses bundled Roboto fonts for consistent rendering across all platforms.
 *
 * @param isDark Whether to use dark theme colors
 * @param colors Custom color palette (defaults based on isDark)
 * @param typography Custom typography (defaults to bundled Roboto fonts)
 * @param content The composable content to wrap
 */
@Composable
fun ArcaneTheme(
    isDark: Boolean = false,
    colors: ArcaneColors = if (isDark) ArcaneColors.dark() else ArcaneColors.default(),
    typography: ArcaneTypography = arcaneTypography(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalArcaneColors provides colors,
        LocalArcaneTypography provides typography,
        content = content
    )
}

object ArcaneTheme {
    val colors: ArcaneColors
        @Composable
        @ReadOnlyComposable
        get() = LocalArcaneColors.current

    val typography: ArcaneTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalArcaneTypography.current
}
