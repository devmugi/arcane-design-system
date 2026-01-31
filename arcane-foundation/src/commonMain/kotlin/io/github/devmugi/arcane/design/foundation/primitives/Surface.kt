package io.github.devmugi.arcane.design.foundation.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import io.github.devmugi.arcane.design.foundation.modifiers.arcaneGlowIf
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneBorder
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneElevation
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneOpacity
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius

enum class SurfaceVariant {
    // Material 3 surface container levels
    ContainerLowest,   // Inset/depressed (darkest)
    ContainerLow,      // Base level
    Container,         // Standard cards (default)
    ContainerHigh,     // Elevated modals
    ContainerHighest,  // Maximum emphasis (lightest)

    // Deprecated variants (kept for backward compatibility)
    @Deprecated("Use ContainerLow", ReplaceWith("SurfaceVariant.ContainerLow"))
    Base,
    @Deprecated("Use Container", ReplaceWith("SurfaceVariant.Container"))
    Raised,
    @Deprecated("Use ContainerLowest", ReplaceWith("SurfaceVariant.ContainerLowest"))
    Inset,
    // Pressed removed - use state layer overlays instead
}

@Composable
fun ArcaneSurface(
    modifier: Modifier = Modifier,
    variant: SurfaceVariant = SurfaceVariant.ContainerLow,
    shape: Shape = ArcaneRadius.Medium,
    showBorder: Boolean = true,
    showGlow: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    val colors = ArcaneTheme.colors

    // Map both new and deprecated variants to surface colors
    @Suppress("DEPRECATION")
    val backgroundColor = when (variant) {
        SurfaceVariant.ContainerLowest, SurfaceVariant.Inset -> colors.surfaceContainerLowest
        SurfaceVariant.ContainerLow, SurfaceVariant.Base -> colors.surfaceContainerLow
        SurfaceVariant.Container, SurfaceVariant.Raised -> colors.surfaceContainer
        SurfaceVariant.ContainerHigh -> colors.surfaceContainerHigh
        SurfaceVariant.ContainerHighest -> colors.surfaceContainerHighest
    }

    // Neutral shadow elevation (Material 3 approach) using tokens
    @Suppress("DEPRECATION")
    val shadowElevation = when (variant) {
        SurfaceVariant.ContainerLowest, SurfaceVariant.Inset -> ArcaneElevation.Level0
        SurfaceVariant.ContainerLow, SurfaceVariant.Base -> ArcaneElevation.Level0
        SurfaceVariant.Container, SurfaceVariant.Raised -> ArcaneElevation.Level2
        SurfaceVariant.ContainerHigh -> ArcaneElevation.Level3
        SurfaceVariant.ContainerHighest -> ArcaneElevation.Level4
    }

    // Consistent border alpha using opacity token
    val borderAlpha = ArcaneOpacity.Medium

    Box(
        modifier = modifier
            // Apply neutral shadow before clipping (M3 approach)
            .then(
                if (shadowElevation > ArcaneElevation.Level0) {
                    Modifier.shadow(
                        elevation = shadowElevation,
                        shape = shape,
                        ambientColor = Color.Black.copy(alpha = ArcaneOpacity.Subtle),
                        spotColor = Color.Black.copy(alpha = ArcaneOpacity.Medium)
                    )
                } else Modifier
            )
            // Optional glow effect using arcaneGlow modifier
            .arcaneGlowIf(
                enabled = showGlow,
                color = colors.glow,
                alpha = ArcaneOpacity.Medium,
                radiusFactor = 0.8f
            )
            .clip(shape)
            .background(backgroundColor, shape)
            // Inner shadow for ContainerLowest (maintains recessed appearance)
            .then(
                @Suppress("DEPRECATION")
                if (variant == SurfaceVariant.ContainerLowest || variant == SurfaceVariant.Inset) {
                    Modifier.drawWithContent {
                        drawContent()
                        // Draw inner shadow - darker edges fading to center
                        drawRect(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = ArcaneOpacity.Strong)
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
