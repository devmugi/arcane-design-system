package io.github.devmugi.arcane.design.foundation.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class ArcaneColors(
    val primary: Color = Color(0xFF8B5CF6),
    val onPrimary: Color = Color(0xFFFFFFFF),           // Text on primary background

    // Material 3 Primary Container (tonal variant of primary)
    val primaryContainer: Color = Color(0xFF2D2647),    // Muted purple container
    val onPrimaryContainer: Color = Color(0xFFA78BFA),  // Light purple text on container

    // Material 3 Secondary (neutral/desaturated variant for lower emphasis)
    val secondaryContainer: Color = Color(0xFF363347),  // Neutral purple-gray
    val onSecondaryContainer: Color = Color(0xFFB8B0C8), // Muted purple text

    // Material 3 Tertiary (optional accent color)
    val tertiary: Color = Color(0xFF4DD4AC),            // Cyan accent
    val onTertiary: Color = Color(0xFF000000),
    val tertiaryContainer: Color = Color(0xFF1A3D38),
    val onTertiaryContainer: Color = Color(0xFF6DE0BC),

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

    // Material 3 Outline colors
    val outline: Color = Color(0xFF8B5CF6).copy(alpha = 0.4f),        // Standard outline (borders)
    val outlineVariant: Color = Color(0xFF8B5CF6).copy(alpha = 0.2f), // Subtle dividers

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

    @Deprecated("Use primaryContainer for M3 compliance", ReplaceWith("primaryContainer"))
    val primaryVariant: Color get() = onPrimaryContainer

    @Deprecated("Use outline for M3 compliance", ReplaceWith("outline"))
    val border: Color get() = outline

    @Deprecated("Use outline for M3 compliance", ReplaceWith("outline"))
    val borderFocused: Color get() = primary

    companion object {
        fun default(): ArcaneColors = ArcaneColors()

        fun withPrimary(primary: Color): ArcaneColors = ArcaneColors(
            primary = primary,
            onPrimary = Color(0xFFFFFFFF),
            primaryContainer = primary.copy(alpha = 0.2f),
            onPrimaryContainer = primary.copy(alpha = 0.8f),
            secondaryContainer = Color(0xFF363347),           // Neutral gray (distinct from primary)
            onSecondaryContainer = Color(0xFFB8B0C8),         // Muted text
            tertiary = Color(0xFF4DD4AC),
            onTertiary = Color(0xFF000000),
            tertiaryContainer = Color(0xFF1A3D38),
            onTertiaryContainer = Color(0xFF6DE0BC),
            glow = primary.copy(alpha = 0.3f),
            glowStrong = primary.copy(alpha = 0.6f),
            outline = primary.copy(alpha = 0.4f),
            outlineVariant = primary.copy(alpha = 0.2f),
            success = primary,
        )

        /**
         * Perplexity theme variant inspired by Perplexity AI design.
         * Features a cyan/turquoise accent with neutral gray surfaces,
         * creating a minimal, flat aesthetic.
         */
        fun perplexity(): ArcaneColors = ArcaneColors(
            primary = Color(0xFF4DD4AC),                        // Cyan/Turquoise
            onPrimary = Color(0xFF000000),                      // Black text on cyan
            primaryContainer = Color(0xFF1A3D38),               // Muted cyan container
            onPrimaryContainer = Color(0xFF6DE0BC),             // Light cyan text
            secondaryContainer = Color(0xFF2A2E30),             // Neutral gray (distinct)
            onSecondaryContainer = Color(0xFFA0A8AC),           // Muted gray text
            tertiary = Color(0xFF8B5CF6),                       // Purple accent
            onTertiary = Color(0xFFFFFFFF),
            tertiaryContainer = Color(0xFF2D2647),
            onTertiaryContainer = Color(0xFFA78BFA),
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
            outline = Color(0xFF3A3A3C),                        // Subtle gray outline
            outlineVariant = Color(0xFF2C2C2E),                 // Subtle dividers
            error = Color(0xFFFF6B6B),                          // Red
            success = Color(0xFF4DD4AC),                        // Cyan for success
            warning = Color(0xFFFFB347),                        // Orange
        )

        /**
         * P2D theme - Perplexity AI dark mode.
         * Features true teal accent (#20B2AA), pure neutral gray surfaces,
         * and minimal glow effects for a flat, professional aesthetic.
         */
        fun p2d(): ArcaneColors = ArcaneColors(
            primary = Color(0xFF20B2AA),                        // True teal (LightSeaGreen)
            onPrimary = Color(0xFF000000),                      // Black text on teal
            primaryContainer = Color(0xFF1A3333),               // Dark neutral teal container
            onPrimaryContainer = Color(0xFF5DD3CB),             // Light teal text
            secondaryContainer = Color(0xFF2A2D2D),             // Warm neutral (distinct)
            onSecondaryContainer = Color(0xFF9CA3A2),           // Muted warm gray text
            tertiary = Color(0xFF8B5CF6),                       // Purple accent
            onTertiary = Color(0xFFFFFFFF),
            tertiaryContainer = Color(0xFF2D2647),
            onTertiaryContainer = Color(0xFFA78BFA),
            surfaceContainerLowest = Color(0xFF111111),         // Deepest black
            surfaceContainerLow = Color(0xFF191919),            // Main background
            surfaceContainer = Color(0xFF242424),               // Cards
            surfaceContainerHigh = Color(0xFF2E2E2E),           // Modals
            surfaceContainerHighest = Color(0xFF383838),        // Dialogs
            glow = Color(0xFF20B2AA).copy(alpha = 0.08f),       // Very subtle teal glow
            glowStrong = Color(0xFF20B2AA).copy(alpha = 0.15f), // Restrained teal glow
            text = Color(0xFFFFFFFF),                           // White
            textSecondary = Color(0xFF9CA3AF),                  // Medium gray
            textDisabled = Color(0xFF6B7280),                   // Darker gray
            outline = Color(0xFF383838),                        // Neutral border
            outlineVariant = Color(0xFF2E2E2E),                 // Subtle dividers
            error = Color(0xFFEF4444),                          // Tailwind red-500
            success = Color(0xFF22C55E),                        // Tailwind green-500
            warning = Color(0xFFF59E0B),                        // Tailwind amber-500
        )

        /**
         * P2L theme - Perplexity AI light mode.
         * Features true teal accent (#20B2AA), clean white/off-white surfaces,
         * and minimal styling for a professional aesthetic.
         */
        fun p2l(): ArcaneColors = ArcaneColors(
            primary = Color(0xFF20B2AA),                        // True teal (same as dark)
            onPrimary = Color(0xFFFFFFFF),                      // White text on teal
            primaryContainer = Color(0xFFE0F7F5),               // Light teal container
            onPrimaryContainer = Color(0xFF0D6D66),             // Dark teal text
            secondaryContainer = Color(0xFFE8EAEA),             // Neutral light gray (distinct)
            onSecondaryContainer = Color(0xFF4A5252),           // Dark neutral text
            tertiary = Color(0xFF8B5CF6),                       // Purple accent
            onTertiary = Color(0xFFFFFFFF),
            tertiaryContainer = Color(0xFFEDE9FE),              // Light purple
            onTertiaryContainer = Color(0xFF5B21B6),            // Dark purple
            surfaceContainerLowest = Color(0xFFFFFFFF),         // Pure white (cards)
            surfaceContainerLow = Color(0xFFFAFAFA),            // Off-white (main bg)
            surfaceContainer = Color(0xFFF5F5F5),               // Light gray
            surfaceContainerHigh = Color(0xFFEEEEEE),           // Medium gray
            surfaceContainerHighest = Color(0xFFE0E0E0),        // Darker gray
            glow = Color(0xFF20B2AA).copy(alpha = 0.06f),       // Very subtle teal glow
            glowStrong = Color(0xFF20B2AA).copy(alpha = 0.12f), // Restrained teal glow
            text = Color(0xFF111827),                           // Near-black
            textSecondary = Color(0xFF6B7280),                  // Medium gray
            textDisabled = Color(0xFF9CA3AF),                   // Light gray
            outline = Color(0xFFE5E7EB),                        // Light border
            outlineVariant = Color(0xFFF3F4F6),                 // Subtle dividers
            error = Color(0xFFDC2626),                          // Red-600
            success = Color(0xFF16A34A),                        // Green-600
            warning = Color(0xFFD97706),                        // Amber-600
        )

        /**
         * ClaudeD theme - Claude dark mode with warm brownish surfaces.
         * Features orange/coral accent (#E07856), warm brown-tinted grays,
         * and cream-white text matching actual claude.ai branding.
         */
        fun claudeD(): ArcaneColors = ArcaneColors(
            primary = Color(0xFFE07856),                        // Orange/Coral
            onPrimary = Color(0xFF000000),                      // Black text on coral
            primaryContainer = Color(0xFF3D2A22),               // Muted orange container
            onPrimaryContainer = Color(0xFFE89A78),             // Light coral text
            secondaryContainer = Color(0xFF3D3835),             // Warm stone (distinct)
            onSecondaryContainer = Color(0xFFC4B8B0),           // Muted warm text
            tertiary = Color(0xFF4DD4AC),                       // Cyan accent
            onTertiary = Color(0xFF000000),
            tertiaryContainer = Color(0xFF1A3D38),
            onTertiaryContainer = Color(0xFF6DE0BC),
            surfaceContainerLowest = Color(0xFF1C1917),         // Warm dark (stone-900)
            surfaceContainerLow = Color(0xFF292524),            // Main bg (stone-800)
            surfaceContainer = Color(0xFF2F2B29),               // Cards (warm brown)
            surfaceContainerHigh = Color(0xFF3D3835),           // Modals
            surfaceContainerHighest = Color(0xFF4A4542),        // Dialogs
            glow = Color(0xFFE07856).copy(alpha = 0.12f),       // Subtle orange glow
            glowStrong = Color(0xFFE07856).copy(alpha = 0.25f), // Moderate orange glow
            text = Color(0xFFFAF9F6),                           // Cream white
            textSecondary = Color(0xFFA8A29E),                  // Warm gray (stone-400)
            textDisabled = Color(0xFF78716C),                   // Warm disabled (stone-500)
            outline = Color(0xFF44403C),                        // Warm border (stone-700)
            outlineVariant = Color(0xFF3D3835),                 // Warm divider
            error = Color(0xFFFF6B6B),                          // Red
            success = Color(0xFF22C55E),                        // Green (not orange)
            warning = Color(0xFFFFB347),                        // Amber
        )

        /**
         * ClaudeL theme - Claude light mode with warm cream surfaces.
         * Features orange/coral accent, warm beige/cream backgrounds,
         * and dark brown text matching actual claude.ai light branding.
         */
        fun claudeL(): ArcaneColors = ArcaneColors(
            primary = Color(0xFFC45A3B),                        // Darker coral for light bg
            onPrimary = Color(0xFFFFFFFF),                      // White text on coral
            primaryContainer = Color(0xFFFFEBE5),               // Light peach container
            onPrimaryContainer = Color(0xFF8B3A22),             // Dark coral text
            secondaryContainer = Color(0xFFF0EBE8),             // Warm stone (distinct)
            onSecondaryContainer = Color(0xFF5C534E),           // Dark warm text
            tertiary = Color(0xFF0D9488),                       // Teal accent for light
            onTertiary = Color(0xFFFFFFFF),
            tertiaryContainer = Color(0xFFCCFBF1),              // Light teal
            onTertiaryContainer = Color(0xFF115E59),            // Dark teal
            surfaceContainerLowest = Color(0xFFFFFFFF),         // Pure white (cards)
            surfaceContainerLow = Color(0xFFFAF9F6),            // Cream (main bg)
            surfaceContainer = Color(0xFFF5F0E8),               // Light warm gray
            surfaceContainerHigh = Color(0xFFEDE8E0),           // Medium warm gray
            surfaceContainerHighest = Color(0xFFE5E0D8),        // Darker warm gray
            glow = Color(0xFFC45A3B).copy(alpha = 0.08f),       // Very subtle coral glow
            glowStrong = Color(0xFFC45A3B).copy(alpha = 0.15f), // Restrained coral glow
            text = Color(0xFF1C1917),                           // Near-black (stone-900)
            textSecondary = Color(0xFF78716C),                  // Warm gray (stone-500)
            textDisabled = Color(0xFFA8A29E),                   // Light warm gray (stone-400)
            outline = Color(0xFFE7E5E4),                        // Light border (stone-200)
            outlineVariant = Color(0xFFF5F5F4),                 // Subtle divider (stone-100)
            error = Color(0xFFDC2626),                          // Red-600
            success = Color(0xFF16A34A),                        // Green-600
            warning = Color(0xFFD97706),                        // Amber-600
        )

        /**
         * MTG theme variant inspired by Magic: The Gathering deck builders.
         * Features a warm gold/amber accent with very dark backgrounds,
         * creating a professional deck builder aesthetic reminiscent of legendary card borders.
         */
        fun mtg(): ArcaneColors = ArcaneColors(
            primary = Color(0xFFD4AF37),                        // Gold/Amber (legendary border color)
            onPrimary = Color(0xFF000000),                      // Black text on gold
            primaryContainer = Color(0xFF2D2517),               // Muted gold container
            onPrimaryContainer = Color(0xFFE5C158),             // Light gold text
            secondaryContainer = Color(0xFF2D2820),             // Dark bronze (distinct)
            onSecondaryContainer = Color(0xFFB8A890),           // Muted bronze text
            tertiary = Color(0xFF8B5CF6),                       // Purple accent
            onTertiary = Color(0xFFFFFFFF),
            tertiaryContainer = Color(0xFF2D2647),
            onTertiaryContainer = Color(0xFFA78BFA),
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
            outline = Color(0xFF2A2A2A),                        // Subtle dark gray outline
            outlineVariant = Color(0xFF1E1E1E),                 // Subtle dividers
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
            onPrimary = Color(0xFF1A1A2E),                      // Dark text on purple
            primaryContainer = Color(0xFF3D2F5C),               // Muted purple container
            onPrimaryContainer = Color(0xFF8B72FF),             // Medium purple text

            secondaryContainer = Color(0xFF3D3850),             // Muted purple-gray (distinct)
            onSecondaryContainer = Color(0xFFA898C0),           // Desaturated purple text

            tertiary = Color(0xFF6DE0BC),                       // Light cyan accent
            onTertiary = Color(0xFF000000),
            tertiaryContainer = Color(0xFF1A3D38),
            onTertiaryContainer = Color(0xFF4DD4AC),

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

            outline = Color(0xFFB19EFF).copy(alpha = 0.4f),     // Purple outline
            outlineVariant = Color(0xFFB19EFF).copy(alpha = 0.2f), // Subtle dividers

            error = Color(0xFFFF8A80),                          // Light red for dark bg
            success = Color(0xFFB19EFF),                        // Purple for success
            warning = Color(0xFFFFD54F),                        // Light yellow for dark bg
        )
    }
}

val LocalArcaneColors = staticCompositionLocalOf { ArcaneColors() }
