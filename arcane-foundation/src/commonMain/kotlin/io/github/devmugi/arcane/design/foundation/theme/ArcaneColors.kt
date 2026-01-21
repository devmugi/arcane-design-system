package io.github.devmugi.arcane.design.foundation.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class ArcaneColors(
    val primary: Color = Color(0xFF8B5CF6),
    val primaryVariant: Color = Color(0xFFA78BFA),
    val surface: Color = Color(0xFF181B2E),
    val surfaceRaised: Color = Color(0xFF1E2235),
    val surfaceInset: Color = Color(0xFF0F1219),
    val surfacePressed: Color = Color(0xFF252A40),
    val glow: Color = Color(0xFF8B5CF6).copy(alpha = 0.3f),
    val glowStrong: Color = Color(0xFF8B5CF6).copy(alpha = 0.6f),
    val glowRaised: Color = Color(0xFF4ADE80),      // Green/teal for Raised variant
    val glowPressed: Color = Color(0xFFD4A574),     // Golden/warm for Pressed variant
    val text: Color = Color(0xFFFFFFFF),
    val textSecondary: Color = Color(0xFF9CA3AF),
    val textDisabled: Color = Color(0xFF4B5563),
    val border: Color = Color(0xFF8B5CF6).copy(alpha = 0.4f),
    val borderFocused: Color = Color(0xFF8B5CF6),
    val error: Color = Color(0xFFFF6B6B),
    val success: Color = Color(0xFF8B5CF6),
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
