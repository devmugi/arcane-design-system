package io.github.devmugi.arcane.design.foundation.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Elevation tokens following Material 3 elevation system.
 * Use these Dp values with shadow() modifier for consistent elevation.
 */
@Immutable
object ArcaneElevation {
    /** Level 0: No elevation - ContainerLowest, ContainerLow surfaces */
    val Level0: Dp = 0.dp

    /** Level 1: Subtle elevation - subtle depth for cards */
    val Level1: Dp = 1.dp

    /** Level 2: Standard elevation - Container surfaces (default cards) */
    val Level2: Dp = 2.dp

    /** Level 3: Medium elevation - ContainerHigh surfaces (modals) */
    val Level3: Dp = 4.dp

    /** Level 4: High elevation - ContainerHighest surfaces (dialogs) */
    val Level4: Dp = 8.dp

    // Deprecated: Use the new Dp-based values above
    @Deprecated("Use ArcaneOpacity.Subtle instead", ReplaceWith("ArcaneOpacity.Subtle"))
    const val Level1Alpha = 0.2f

    @Deprecated("Use ArcaneOpacity.Medium instead", ReplaceWith("ArcaneOpacity.Medium"))
    const val Level2Alpha = 0.25f

    @Deprecated("Use ArcaneOpacity.Strong instead", ReplaceWith("ArcaneOpacity.Strong"))
    const val Level3Alpha = 0.8f
}

/**
 * Opacity tokens for consistent alpha values across the design system.
 * Use for shadows, overlays, borders, and state layers.
 */
@Immutable
object ArcaneOpacity {
    /** Subtle opacity - light shadows, subtle borders */
    const val Subtle: Float = 0.15f

    /** Medium opacity - standard shadows, borders */
    const val Medium: Float = 0.25f

    /** Strong opacity - emphasis, pressed states */
    const val Strong: Float = 0.5f

    /** Full opacity - solid elements */
    const val Full: Float = 0.8f
}
