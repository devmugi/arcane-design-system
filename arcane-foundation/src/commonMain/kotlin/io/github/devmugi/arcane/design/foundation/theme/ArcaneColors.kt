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

        /**
         * Perplexity theme variant inspired by Perplexity AI design.
         * Features a cyan/turquoise accent with neutral gray surfaces,
         * creating a minimal, flat aesthetic.
         */
        fun perplexity(): ArcaneColors = ArcaneColors(
            primary = Color(0xFF4DD4AC),           // Cyan/Turquoise
            primaryVariant = Color(0xFF6DE0BC),    // Lighter cyan
            surface = Color(0xFF1C1C1E),           // Very dark gray (neutral)
            surfaceRaised = Color(0xFF2C2C2E),     // Dark gray
            surfaceInset = Color(0xFF16161A),      // Darker than surface
            surfacePressed = Color(0xFF3A3A3C),    // Medium dark gray
            glow = Color(0xFF4DD4AC).copy(alpha = 0.15f),      // Subtle cyan glow
            glowStrong = Color(0xFF4DD4AC).copy(alpha = 0.3f), // Moderate cyan glow
            glowRaised = Color(0xFF4DD4AC).copy(alpha = 0.2f), // Cyan for raised
            glowPressed = Color(0xFF4DD4AC).copy(alpha = 0.25f), // Cyan for pressed
            text = Color(0xFFFFFFFF),              // White
            textSecondary = Color(0xFF9CA3AF),     // Medium gray
            textDisabled = Color(0xFF6B6B6D),      // Darker gray
            border = Color(0xFF3A3A3C),            // Subtle gray border
            borderFocused = Color(0xFF4DD4AC),     // Cyan when focused
            error = Color(0xFFFF6B6B),             // Keep red
            success = Color(0xFF4DD4AC),           // Cyan for success
            warning = Color(0xFFFFB347),           // Keep orange
        )

        /**
         * Claude theme variant inspired by Claude desktop app design.
         * Features a warm orange/coral accent with near-black backgrounds,
         * creating a professional, minimal aesthetic.
         */
        fun claude(): ArcaneColors = ArcaneColors(
            primary = Color(0xFFE07856),           // Orange/Coral
            primaryVariant = Color(0xFFE89A78),    // Lighter coral
            surface = Color(0xFF1A1A1A),           // Near-black (sidebar)
            surfaceRaised = Color(0xFF202020),     // Dark gray (main area)
            surfaceInset = Color(0xFF141414),      // Darker than surface
            surfacePressed = Color(0xFF2A2A2A),    // Medium dark gray (pressed state)
            glow = Color(0xFFE07856).copy(alpha = 0.15f),      // Subtle orange glow
            glowStrong = Color(0xFFE07856).copy(alpha = 0.3f), // Moderate orange glow
            glowRaised = Color(0xFFE07856).copy(alpha = 0.2f), // Orange for raised
            glowPressed = Color(0xFFE07856).copy(alpha = 0.25f), // Orange for pressed
            text = Color(0xFFFFFFFF),              // White
            textSecondary = Color(0xFF9CA3AF),     // Medium gray
            textDisabled = Color(0xFF6B6B6D),      // Darker gray
            border = Color(0xFF2E2E2E),            // Subtle gray border
            borderFocused = Color(0xFFE07856),     // Orange when focused
            error = Color(0xFFFF6B6B),             // Keep red
            success = Color(0xFFE07856),           // Orange for success
            warning = Color(0xFFFFB347),           // Keep orange/yellow
        )

        /**
         * MTG theme variant inspired by Magic: The Gathering deck builders.
         * Features a warm gold/amber accent with very dark backgrounds,
         * creating a professional deck builder aesthetic reminiscent of legendary card borders.
         */
        fun mtg(): ArcaneColors = ArcaneColors(
            primary = Color(0xFFD4AF37),           // Gold/Amber (legendary border color)
            primaryVariant = Color(0xFFE5C158),    // Lighter gold
            surface = Color(0xFF0E0E0E),           // Very dark gray/black (darkest surface)
            surfaceRaised = Color(0xFF1A1A1A),     // Dark gray
            surfaceInset = Color(0xFF080808),      // Darker than surface (nearly black)
            surfacePressed = Color(0xFF242424),    // Medium dark gray
            glow = Color(0xFFD4AF37).copy(alpha = 0.15f),      // Subtle gold glow
            glowStrong = Color(0xFFD4AF37).copy(alpha = 0.3f), // Moderate gold glow
            glowRaised = Color(0xFFD4AF37).copy(alpha = 0.2f), // Gold for raised
            glowPressed = Color(0xFFD4AF37).copy(alpha = 0.25f), // Gold for pressed
            text = Color(0xFFFFFFFF),              // White
            textSecondary = Color(0xFF9CA3AF),     // Medium gray
            textDisabled = Color(0xFF6B6B6D),      // Darker gray
            border = Color(0xFF2A2A2A),            // Subtle dark gray border
            borderFocused = Color(0xFFD4AF37),     // Gold when focused
            error = Color(0xFFFF6B6B),             // Keep red
            success = Color(0xFFD4AF37),           // Gold for success (matches theme)
            warning = Color(0xFFFFB347),           // Keep orange/yellow
        )
    }
}

val LocalArcaneColors = staticCompositionLocalOf { ArcaneColors() }
