package io.github.devmugi.arcane.design.foundation.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneBorder
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneElevation
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius

enum class SurfaceVariant {
    Base,
    Raised,
    Inset,
    Pressed
}

@Composable
fun ArcaneSurface(
    modifier: Modifier = Modifier,
    variant: SurfaceVariant = SurfaceVariant.Base,
    shape: Shape = ArcaneRadius.Medium,
    showBorder: Boolean = true,
    showGlow: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    val colors = ArcaneTheme.colors

    val backgroundColor = when (variant) {
        SurfaceVariant.Base -> colors.surface
        SurfaceVariant.Raised -> colors.surfaceRaised
        SurfaceVariant.Inset -> colors.surfaceInset
        SurfaceVariant.Pressed -> colors.surfacePressed
    }

    // Variant-specific glow colors
    val glowColor = when (variant) {
        SurfaceVariant.Base -> colors.glow
        SurfaceVariant.Raised -> colors.glowRaised    // Green/teal
        SurfaceVariant.Inset -> Color.Transparent
        SurfaceVariant.Pressed -> colors.glowPressed  // Golden/warm
    }

    val glowAlpha = when (variant) {
        SurfaceVariant.Base -> 0f                     // No glow for Base
        SurfaceVariant.Raised -> ArcaneElevation.Level2Alpha
        SurfaceVariant.Inset -> 0f
        SurfaceVariant.Pressed -> ArcaneElevation.Level2Alpha
    }

    // Border alpha - more subtle for Base
    val borderAlpha = when (variant) {
        SurfaceVariant.Base -> 0.2f
        else -> 0.4f
    }

    Box(
        modifier = modifier
            // Glow effect for Raised/Pressed
            .then(
                if (showGlow && glowAlpha > 0f) {
                    Modifier.drawBehind {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    glowColor.copy(alpha = glowAlpha),
                                    Color.Transparent
                                ),
                                center = Offset(size.width / 2, size.height / 2),
                                radius = maxOf(size.width, size.height) * 0.8f
                            )
                        )
                    }
                } else Modifier
            )
            .clip(shape)
            .background(backgroundColor, shape)
            // Inner shadow for Inset variant (always applied, not dependent on showGlow)
            .then(
                if (variant == SurfaceVariant.Inset) {
                    Modifier.drawWithContent {
                        drawContent()
                        // Draw inner shadow - darker edges fading to center
                        drawRect(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.5f)
                                ),
                                center = Offset(size.width / 2, size.height / 2),
                                radius = size.minDimension / 1.2f
                            )
                        )
                    }
                } else Modifier
            )
            .then(
                if (showBorder) {
                    Modifier.border(
                        ArcaneBorder.Thin,
                        colors.border.copy(alpha = borderAlpha),
                        shape
                    )
                } else Modifier
            ),
        content = content
    )
}
