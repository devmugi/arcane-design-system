package io.github.devmugi.arcane.design.testing

import androidx.compose.runtime.Composable
import io.github.devmugi.arcane.design.foundation.theme.ArcaneColors
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme

/**
 * Wrapper for tests that provides ArcaneTheme context.
 * Use this in all UI tests to ensure components have proper theme.
 */
@Composable
fun TestArcaneTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    ArcaneTheme(
        isDark = darkTheme,
        colors = if (darkTheme) {
            ArcaneColors.dark()
        } else {
            ArcaneColors.default()
        },
        content = content
    )
}
