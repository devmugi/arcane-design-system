package io.github.devmugi.arcane.design.foundation.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class ArcaneColors(
    val primary: Color = Color(0xFF00D4AA),
    val primaryVariant: Color = Color(0xFF00F5C4),
    val surface: Color = Color(0xFF0A1A1F),
    val surfaceRaised: Color = Color(0xFF122A32),
    val surfaceInset: Color = Color(0xFF051015),
    val surfacePressed: Color = Color(0xFF0D2228),
    val glow: Color = Color(0xFF00D4AA).copy(alpha = 0.3f),
    val glowStrong: Color = Color(0xFF00D4AA).copy(alpha = 0.6f),
    val text: Color = Color(0xFFE0F4F0),
    val textSecondary: Color = Color(0xFF8ABAB0),
    val textDisabled: Color = Color(0xFF4A7A70),
    val border: Color = Color(0xFF00D4AA).copy(alpha = 0.4f),
    val borderFocused: Color = Color(0xFF00D4AA),
    val error: Color = Color(0xFFFF6B6B),
    val success: Color = Color(0xFF00D4AA),
    val warning: Color = Color(0xFFFFB347),
) {
    companion object {
        fun default(): ArcaneColors = ArcaneColors()

        fun withPrimary(primary: Color): ArcaneColors = ArcaneColors(
            primary = primary,
            primaryVariant = primary.copy(alpha = 0.8f),
            glow = primary.copy(alpha = 0.3f),
            glowStrong = primary.copy(alpha = 0.6f),
            border = primary.copy(alpha = 0.4f),
            borderFocused = primary,
            success = primary,
        )
    }
}

val LocalArcaneColors = staticCompositionLocalOf { ArcaneColors() }
