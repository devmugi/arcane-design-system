package io.github.devmugi.arcane.design.foundation.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
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

    val glowAlpha = when (variant) {
        SurfaceVariant.Base -> ArcaneElevation.Level1Alpha
        SurfaceVariant.Raised -> ArcaneElevation.Level2Alpha
        SurfaceVariant.Inset -> 0f
        SurfaceVariant.Pressed -> ArcaneElevation.Level1Alpha
    }

    Box(
        modifier = modifier
            .then(
                if (showGlow && glowAlpha > 0f) {
                    Modifier.drawBehind {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    colors.glow.copy(alpha = glowAlpha),
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
            .then(
                if (showBorder) {
                    Modifier.border(ArcaneBorder.Title, colors.border, shape)
                } else Modifier
            ),
        content = content
    )
}
