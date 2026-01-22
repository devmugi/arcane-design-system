package io.github.devmugi.arcane.design.foundation.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class ArcaneColors(
    val primary: Color = Color(0xFF8B5CF6),
    val primaryVariant: Color = Color(0xFFA78BFA),
    val onPrimary: Color = Color(0xFFFFFFFF),           // Text on primary background

    // Material 3 Secondary (tonal variants)
    val secondaryContainer: Color = Color(0xFF2D2647),  // Muted purple for tonal buttons
    val onSecondaryContainer: Color = Color(0xFFA78BFA), // Text on secondary container

    // Material 3 Surface Containers (tonal elevation system)
    val surfaceContainerLowest: Color = Color(0xFF0F1219),  // Darkest (inset/depressed)
    val surfaceContainerLow: Color = Color(0xFF181B2E),     // Base level
    val surfaceContainer: Color = Color(0xFF1E2235),        // Standard cards
    val surfaceContainerHigh: Color = Color(0xFF252A40),    // Elevated modals
    val surfaceContainerHighest: Color = Color(0xFF2D3348), // Maximum emphasis

    // Glow effects (primary color based)
    val glow: Color = Color(0xFF8B5CF6).copy(alpha = 0.3f),
    val glowStrong: Color = Color(0xFF8B5CF6).copy(alpha = 0.6f),

    // State layer alphas (M3 standard values for interaction overlays)
    val stateLayerHover: Float = 0.08f,
    val stateLayerPressed: Float = 0.12f,
    val stateLayerFocus: Float = 0.12f,
    val stateLayerDragged: Float = 0.16f,

    // Text colors
    val text: Color = Color(0xFFFFFFFF),
    val textSecondary: Color = Color(0xFF9CA3AF),
    val textDisabled: Color = Color(0xFF4B5563),

    // Border colors
    val border: Color = Color(0xFF8B5CF6).copy(alpha = 0.4f),
    val borderFocused: Color = Color(0xFF8B5CF6),

    // Semantic colors
    val error: Color = Color(0xFFFF6B6B),
    val success: Color = Color(0xFF8B5CF6),
    val warning: Color = Color(0xFFFFB347),
) {
    // Deprecated properties for backward compatibility
    @Deprecated("Use surfaceContainerLow", ReplaceWith("surfaceContainerLow"))
    val surface: Color get() = surfaceContainerLow

    @Deprecated("Use surfaceContainer", ReplaceWith("surfaceContainer"))
    val surfaceRaised: Color get() = surfaceContainer

    @Deprecated("Use surfaceContainerLowest", ReplaceWith("surfaceContainerLowest"))
    val surfaceInset: Color get() = surfaceContainerLowest

    @Deprecated("Removed. Use state layer overlays instead", ReplaceWith("surfaceContainerHigh"))
    val surfacePressed: Color get() = surfaceContainerHigh

    @Deprecated("Removed. Use neutral shadows instead")
    val glowRaised: Color get() = glow

    @Deprecated("Removed. Use neutral shadows instead")
    val glowPressed: Color get() = glow
    companion object {
        fun default(): ArcaneColors = ArcaneColors()

        fun withPrimary(primary: Color): ArcaneColors = ArcaneColors(
            primary = primary,
            primaryVariant = primary.copy(alpha = 0.8f),
            onPrimary = Color(0xFFFFFFFF),
            secondaryContainer = primary.copy(alpha = 0.2f),
            onSecondaryContainer = primary,
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
            primary = Color(0xFF4DD4AC),                        // Cyan/Turquoise
            primaryVariant = Color(0xFF6DE0BC),                 // Lighter cyan
            onPrimary = Color(0xFF000000),                      // Black text on cyan
            secondaryContainer = Color(0xFF1A3D38),             // Muted cyan container
            onSecondaryContainer = Color(0xFF6DE0BC),           // Light cyan text
            surfaceContainerLowest = Color(0xFF16161A),         // Darkest
            surfaceContainerLow = Color(0xFF1C1C1E),            // Base level
            surfaceContainer = Color(0xFF2C2C2E),               // Standard
            surfaceContainerHigh = Color(0xFF3A3A3C),           // Elevated
            surfaceContainerHighest = Color(0xFF484848),        // Maximum emphasis
            glow = Color(0xFF4DD4AC).copy(alpha = 0.15f),       // Subtle cyan glow
            glowStrong = Color(0xFF4DD4AC).copy(alpha = 0.3f),  // Moderate cyan glow
            text = Color(0xFFFFFFFF),                           // White
            textSecondary = Color(0xFF9CA3AF),                  // Medium gray
            textDisabled = Color(0xFF6B6B6D),                   // Darker gray
            border = Color(0xFF3A3A3C),                         // Subtle gray border
            borderFocused = Color(0xFF4DD4AC),                  // Cyan when focused
            error = Color(0xFFFF6B6B),                          // Red
            success = Color(0xFF4DD4AC),                        // Cyan for success
            warning = Color(0xFFFFB347),                        // Orange
        )

        /**
         * Claude theme variant inspired by Claude desktop app design.
         * Features a warm orange/coral accent with near-black backgrounds,
         * creating a professional, minimal aesthetic.
         */
        fun claude(): ArcaneColors = ArcaneColors(
            primary = Color(0xFFE07856),                        // Orange/Coral
            primaryVariant = Color(0xFFE89A78),                 // Lighter coral
            onPrimary = Color(0xFF000000),                      // Black text on coral
            secondaryContainer = Color(0xFF3D2A22),             // Muted orange container
            onSecondaryContainer = Color(0xFFE89A78),           // Light coral text
            surfaceContainerLowest = Color(0xFF141414),         // Darkest
            surfaceContainerLow = Color(0xFF1A1A1A),            // Base level
            surfaceContainer = Color(0xFF202020),               // Standard
            surfaceContainerHigh = Color(0xFF2A2A2A),           // Elevated
            surfaceContainerHighest = Color(0xFF343434),        // Maximum emphasis
            glow = Color(0xFFE07856).copy(alpha = 0.15f),       // Subtle orange glow
            glowStrong = Color(0xFFE07856).copy(alpha = 0.3f),  // Moderate orange glow
            text = Color(0xFFFFFFFF),                           // White
            textSecondary = Color(0xFF9CA3AF),                  // Medium gray
            textDisabled = Color(0xFF6B6B6D),                   // Darker gray
            border = Color(0xFF2E2E2E),                         // Subtle gray border
            borderFocused = Color(0xFFE07856),                  // Orange when focused
            error = Color(0xFFFF6B6B),                          // Red
            success = Color(0xFFE07856),                        // Orange for success
            warning = Color(0xFFFFB347),                        // Orange/yellow
        )

        /**
         * MTG theme variant inspired by Magic: The Gathering deck builders.
         * Features a warm gold/amber accent with very dark backgrounds,
         * creating a professional deck builder aesthetic reminiscent of legendary card borders.
         */
        fun mtg(): ArcaneColors = ArcaneColors(
            primary = Color(0xFFD4AF37),                        // Gold/Amber (legendary border color)
            primaryVariant = Color(0xFFE5C158),                 // Lighter gold
            onPrimary = Color(0xFF000000),                      // Black text on gold
            secondaryContainer = Color(0xFF2D2517),             // Muted gold container
            onSecondaryContainer = Color(0xFFE5C158),           // Light gold text
            surfaceContainerLowest = Color(0xFF080808),         // Darkest (nearly black)
            surfaceContainerLow = Color(0xFF0E0E0E),            // Base level
            surfaceContainer = Color(0xFF1A1A1A),               // Standard
            surfaceContainerHigh = Color(0xFF242424),           // Elevated
            surfaceContainerHighest = Color(0xFF2E2E2E),        // Maximum emphasis
            glow = Color(0xFFD4AF37).copy(alpha = 0.15f),       // Subtle gold glow
            glowStrong = Color(0xFFD4AF37).copy(alpha = 0.3f),  // Moderate gold glow
            text = Color(0xFFFFFFFF),                           // White
            textSecondary = Color(0xFF9CA3AF),                  // Medium gray
            textDisabled = Color(0xFF6B6B6D),                   // Darker gray
            border = Color(0xFF2A2A2A),                         // Subtle dark gray border
            borderFocused = Color(0xFFD4AF37),                  // Gold when focused
            error = Color(0xFFFF6B6B),                          // Red
            success = Color(0xFFD4AF37),                        // Gold for success
            warning = Color(0xFFFFB347),                        // Orange/yellow
        )

        /**
         * Dark theme variant with inverted brightness and adjusted purple accent.
         * Maintains the sci-fi aesthetic with deeper backgrounds and brighter text.
         */
        fun dark(): ArcaneColors = ArcaneColors(
            primary = Color(0xFFB19EFF),                        // Lighter purple for dark bg
            primaryVariant = Color(0xFF8B72FF),                 // Medium purple
            onPrimary = Color(0xFF1A1A2E),                      // Dark text on purple

            secondaryContainer = Color(0xFF3D2F5C),             // Muted purple container
            onSecondaryContainer = Color(0xFFB19EFF),           // Light purple text

            // Material 3 Surface Containers (dark mode tonal elevation)
            surfaceContainerLowest = Color(0xFF0F0F1A),         // Darkest
            surfaceContainerLow = Color(0xFF16162A),            // Base level
            surfaceContainer = Color(0xFF1D1D38),               // Standard cards
            surfaceContainerHigh = Color(0xFF242446),           // Elevated modals
            surfaceContainerHighest = Color(0xFF2B2B54),        // Maximum emphasis

            glow = Color(0xFFB19EFF).copy(alpha = 0.3f),        // Purple glow
            glowStrong = Color(0xFFB19EFF).copy(alpha = 0.6f),  // Strong purple glow

            text = Color(0xFFE6E6FF),                           // Very light purple-white
            textSecondary = Color(0xFFB3B3CC),                  // Medium purple-gray
            textDisabled = Color(0xFF666680),                   // Darker purple-gray

            border = Color(0xFFB19EFF).copy(alpha = 0.4f),      // Purple border
            borderFocused = Color(0xFFB19EFF),                  // Bright purple when focused

            error = Color(0xFFFF8A80),                          // Light red for dark bg
            success = Color(0xFFB19EFF),                        // Purple for success
            warning = Color(0xFFFFD54F),                        // Light yellow for dark bg
        )
    }
}

val LocalArcaneColors = staticCompositionLocalOf { ArcaneColors() }
