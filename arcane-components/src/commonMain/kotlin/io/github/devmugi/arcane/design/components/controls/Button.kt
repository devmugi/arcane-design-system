// arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/Button.kt
package io.github.devmugi.arcane.design.components.controls

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneBorder
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

sealed class ArcaneButtonStyle {
    data object Primary : ArcaneButtonStyle()
    data object Secondary : ArcaneButtonStyle()
}

@Composable
fun ArcaneButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    style: ArcaneButtonStyle = ArcaneButtonStyle.Primary,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = ArcaneSpacing.Medium,
        vertical = ArcaneSpacing.Small
    ),
    content: @Composable RowScope.() -> Unit
) {
    val colors = ArcaneTheme.colors
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val backgroundColor = when {
        !enabled -> colors.surfaceInset
        loading -> colors.surfacePressed
        isPressed -> colors.surfacePressed
        style is ArcaneButtonStyle.Primary -> colors.primary
        else -> colors.surface
    }

    val contentColor = when {
        !enabled -> colors.textDisabled
        style is ArcaneButtonStyle.Primary && !isPressed && !loading -> colors.surface
        else -> colors.text
    }

    val borderColor = when {
        !enabled -> colors.textDisabled.copy(alpha = 0.3f)
        else -> colors.border
    }

    val glowAlpha by animateFloatAsState(
        targetValue = if (enabled && !loading && style is ArcaneButtonStyle.Primary) 0.3f else 0f,
        animationSpec = tween(150),
        label = "glowAlpha"
    )

    Box(
        modifier = modifier
            .defaultMinSize(minHeight = 44.dp)
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
                                radius = maxOf(size.width, size.height) * 0.8f
                            )
                        )
                    }
                } else Modifier
            )
            .clip(ArcaneRadius.Full)
            .background(backgroundColor, ArcaneRadius.Full)
            .border(ArcaneBorder.Title, borderColor, ArcaneRadius.Full)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled && !loading,
                role = Role.Button,
                onClick = onClick
            )
            .padding(contentPadding),
        contentAlignment = Alignment.Center
    ) {
        val contentAlpha by animateFloatAsState(
            targetValue = if (loading) 0f else 1f,
            animationSpec = tween(150),
            label = "contentAlpha"
        )

        Row(
            modifier = Modifier.alpha(contentAlpha),
            horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            androidx.compose.runtime.CompositionLocalProvider(
                androidx.compose.material3.LocalContentColor provides contentColor
            ) {
                content()
            }
        }

        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = colors.primary,
                strokeWidth = 2.dp
            )
        }
    }
}

@Composable
fun ArcaneTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    style: ArcaneButtonStyle = ArcaneButtonStyle.Primary,
) {
    ArcaneButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        loading = loading,
        style = style
    ) {
        Text(
            text = text,
            style = ArcaneTheme.typography.labelLarge,
            color = androidx.compose.material3.LocalContentColor.current
        )
    }
}
