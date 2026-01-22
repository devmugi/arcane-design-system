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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneBorder
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneElevation
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

    // Neutral shadow elevation (Material 3 approach)
    @Suppress("DEPRECATION")
    val shadowElevation = when (variant) {
        SurfaceVariant.ContainerLowest, SurfaceVariant.Inset -> 0.dp
        SurfaceVariant.ContainerLow, SurfaceVariant.Base -> 0.dp
        SurfaceVariant.Container, SurfaceVariant.Raised -> 2.dp
        SurfaceVariant.ContainerHigh -> 4.dp
        SurfaceVariant.ContainerHighest -> 8.dp
    }

    // Consistent border alpha
    val borderAlpha = 0.3f

    Box(
        modifier = modifier
            // Apply neutral shadow before clipping (M3 approach)
            .then(
                if (shadowElevation > 0.dp) {
                    Modifier.shadow(
                        elevation = shadowElevation,
                        shape = shape,
                        ambientColor = Color.Black.copy(alpha = 0.15f),
                        spotColor = Color.Black.copy(alpha = 0.25f)
                    )
                } else Modifier
            )
            // Optional glow effect (only when explicitly requested via showGlow)
            .then(
                if (showGlow) {
                    Modifier.drawBehind {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    colors.glow.copy(alpha = 0.3f),
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
