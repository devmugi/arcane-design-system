package io.github.devmugi.arcane.design.components.display

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import io.github.devmugi.arcane.design.foundation.primitives.ArcaneSurface
import io.github.devmugi.arcane.design.foundation.primitives.SurfaceVariant
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun ArcaneCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = ArcaneTheme.colors
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    val glowAlpha by animateFloatAsState(
        targetValue = when {
            onClick == null -> 0f
            isPressed -> 0.4f
            isHovered -> 0.25f
            else -> 0f
        },
        animationSpec = tween(150),
        label = "cardGlowAlpha"
    )

    Box(
        modifier = modifier
            .then(
                if (glowAlpha > 0f) {
                    Modifier.drawBehind {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    colors.glow.copy(alpha = glowAlpha),
                                    Color.Transparent
                                ),
                                center = Offset(size.width / 2, size.height / 2),
                                radius = maxOf(size.width, size.height) * 0.6f
                            )
                        )
                    }
                } else Modifier
            )
    ) {
        ArcaneSurface(
            variant = SurfaceVariant.Raised,
            modifier = Modifier
                .clip(ArcaneRadius.Large)
                .then(
                    if (onClick != null) {
                        Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = onClick
                        )
                    } else Modifier
                )
        ) {
            Column(content = content)
        }
    }
}

@Composable
fun ArcaneCardImage(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier
            .fillMaxWidth()
            .clip(ArcaneRadius.Large),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun ArcaneCardContent(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography

    Column(
        modifier = modifier.padding(ArcaneSpacing.Medium),
        verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)
    ) {
        Text(
            text = title,
            style = typography.headlineMedium,
            color = colors.text
        )
        if (description != null) {
            Text(
                text = description,
                style = typography.bodyMedium,
                color = colors.textSecondary
            )
        }
    }
}

@Composable
fun ArcaneCardActions(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier.padding(
            start = ArcaneSpacing.Medium,
            end = ArcaneSpacing.Medium,
            bottom = ArcaneSpacing.Medium
        ),
        horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small),
        content = content
    )
}
