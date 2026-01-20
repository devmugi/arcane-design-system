package io.github.devmugi.arcane.design.foundation.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun ArcaneTheme(
    colors: ArcaneColors = ArcaneColors.default(),
    typography: ArcaneTypography = ArcaneTypography(),
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
