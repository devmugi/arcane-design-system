package io.github.devmugi.arcane.design.foundation.modifiers

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Adds a radial glow effect behind the content.
 *
 * Use this modifier to create the signature Arcane glow effect for interactive elements.
 * The glow appears centered on the element and fades to transparent at the edges.
 *
 * @param color The glow color (typically from ArcaneTheme.colors.glow)
 * @param alpha The opacity of the glow (0.0 to 1.0)
 * @param radiusFactor How far the glow extends relative to element size (0.6 to 1.0 typical)
 *
 * Example usage:
 * ```kotlin
 * Box(
 *     modifier = Modifier
 *         .arcaneGlow(
 *             color = ArcaneTheme.colors.glow,
 *             alpha = 0.3f
 *         )
 *         .background(...)
 * ) { ... }
 * ```
 */
fun Modifier.arcaneGlow(
    color: Color,
    alpha: Float,
    radiusFactor: Float = 0.8f
): Modifier = if (alpha > 0f) {
    this.drawBehind {
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    color.copy(alpha = alpha),
                    Color.Transparent
                ),
                center = Offset(size.width / 2, size.height / 2),
                radius = maxOf(size.width, size.height) * radiusFactor
            )
        )
    }
} else {
    this
}

/**
 * Conditional glow modifier that only applies when enabled.
 *
 * @param enabled Whether to show the glow
 * @param color The glow color
 * @param alpha The opacity of the glow when enabled
 * @param radiusFactor How far the glow extends
 */
fun Modifier.arcaneGlowIf(
    enabled: Boolean,
    color: Color,
    alpha: Float,
    radiusFactor: Float = 0.8f
): Modifier = if (enabled && alpha > 0f) {
    arcaneGlow(color, alpha, radiusFactor)
} else {
    this
}
