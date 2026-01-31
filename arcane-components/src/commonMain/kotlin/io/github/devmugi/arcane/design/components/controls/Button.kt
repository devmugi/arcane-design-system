// arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/Button.kt
package io.github.devmugi.arcane.design.components.controls

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.devmugi.arcane.design.foundation.modifiers.arcaneGlowIf
import io.github.devmugi.arcane.design.foundation.theme.ArcaneColors
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneMotion
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing
import kotlinx.coroutines.delay

// ============================================================================
// Public API
// ============================================================================

sealed class ArcaneButtonStyle {
    data class Filled(val containerColor: Color? = null) : ArcaneButtonStyle()
    data class Tonal(val containerColor: Color? = null) : ArcaneButtonStyle()
    data class Outlined(val borderColor: Color? = null) : ArcaneButtonStyle()
    data class Elevated(val containerColor: Color? = null) : ArcaneButtonStyle()
    data object Text : ArcaneButtonStyle()
}

enum class ArcaneButtonSize {
    ExtraSmall,  // 24dp height
    Small,       // 32dp height
    Medium       // 40dp height (default)
}

enum class ArcaneButtonShape {
    Round,    // Full pill (9999.dp radius)
    Rounded,  // Medium corners (20.dp radius)
    Square    // Sharp corners (0.dp radius)
}

// ============================================================================
// Internal Specifications
// ============================================================================

internal data class ButtonSizeSpec(
    val minHeight: Dp,
    val horizontalPadding: Dp,
    val verticalPadding: Dp,
    val iconSize: Dp,
    val fontSize: TextUnit
)

private val buttonSizeSpecs = mapOf(
    ArcaneButtonSize.ExtraSmall to ButtonSizeSpec(
        minHeight = 24.dp,
        horizontalPadding = 12.dp,
        verticalPadding = 4.dp,
        iconSize = 14.dp,
        fontSize = 11.sp
    ),
    ArcaneButtonSize.Small to ButtonSizeSpec(
        minHeight = 32.dp,
        horizontalPadding = 16.dp,
        verticalPadding = 6.dp,
        iconSize = 16.dp,
        fontSize = 13.sp
    ),
    ArcaneButtonSize.Medium to ButtonSizeSpec(
        minHeight = 40.dp,
        horizontalPadding = 24.dp,
        verticalPadding = 8.dp,
        iconSize = 18.dp,
        fontSize = 14.sp
    )
)

internal data class ButtonColors(
    val backgroundColor: Color,
    val contentColor: Color,
    val borderColor: Color,
    val borderWidth: Dp,
    val spinnerColor: Color,
    val hasElevation: Boolean,
    val hasGlow: Boolean
)

// ============================================================================
// Helper Functions
// ============================================================================

private fun ArcaneButtonShape.toCornerRadius(): Dp = when (this) {
    ArcaneButtonShape.Round -> 9999.dp
    ArcaneButtonShape.Rounded -> 20.dp
    ArcaneButtonShape.Square -> 0.dp
}

private fun resolveButtonColors(
    style: ArcaneButtonStyle,
    colors: ArcaneColors,
    enabled: Boolean
): ButtonColors {
    if (!enabled) {
        return ButtonColors(
            backgroundColor = colors.surfaceContainerLowest,
            contentColor = colors.textDisabled,
            borderColor = colors.textDisabled.copy(alpha = 0.3f),
            borderWidth = 1.dp,
            spinnerColor = colors.textDisabled,
            hasElevation = false,
            hasGlow = false
        )
    }

    return when (style) {
        is ArcaneButtonStyle.Filled -> {
            val bgColor = style.containerColor ?: colors.primary
            ButtonColors(
                backgroundColor = bgColor,
                contentColor = colors.onPrimary,
                borderColor = Color.Transparent,
                borderWidth = 0.dp,
                spinnerColor = colors.onPrimary,
                hasElevation = false,
                hasGlow = true
            )
        }

        is ArcaneButtonStyle.Tonal -> {
            val bgColor = style.containerColor ?: colors.secondaryContainer
            ButtonColors(
                backgroundColor = bgColor,
                contentColor = colors.onSecondaryContainer,
                borderColor = Color.Transparent,
                borderWidth = 0.dp,
                spinnerColor = colors.primary,
                hasElevation = false,
                hasGlow = false
            )
        }

        is ArcaneButtonStyle.Outlined -> {
            val borderColor = style.borderColor ?: colors.primary
            ButtonColors(
                backgroundColor = Color.Transparent,
                contentColor = borderColor,
                borderColor = borderColor,
                borderWidth = 1.dp,
                spinnerColor = borderColor,
                hasElevation = false,
                hasGlow = false
            )
        }

        is ArcaneButtonStyle.Elevated -> {
            val bgColor = style.containerColor ?: colors.surfaceContainerLow
            ButtonColors(
                backgroundColor = bgColor,
                contentColor = colors.primary,
                borderColor = Color.Transparent,
                borderWidth = 0.dp,
                spinnerColor = colors.primary,
                hasElevation = true,
                hasGlow = true
            )
        }

        is ArcaneButtonStyle.Text -> {
            ButtonColors(
                backgroundColor = Color.Transparent,
                contentColor = colors.primary,
                borderColor = Color.Transparent,
                borderWidth = 0.dp,
                spinnerColor = colors.primary,
                hasElevation = false,
                hasGlow = false
            )
        }
    }
}

// ============================================================================
// Main Composable
// ============================================================================

@Composable
fun ArcaneButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    style: ArcaneButtonStyle = ArcaneButtonStyle.Filled(),
    size: ArcaneButtonSize = ArcaneButtonSize.Medium,
    shape: ArcaneButtonShape = ArcaneButtonShape.Round,
    content: @Composable RowScope.() -> Unit
) {
    val colors = ArcaneTheme.colors
    val sizeSpec = buttonSizeSpecs[size] ?: buttonSizeSpecs[ArcaneButtonSize.Medium]!!
    val shapeRadius = shape.toCornerRadius()
    val buttonColors = resolveButtonColors(style, colors, enabled)
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // ============================================================================
    // Animations
    // ============================================================================

    // Scale animation (expressive)
    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled && !loading) 0.96f else 1f,
        animationSpec = spring(dampingRatio = ArcaneMotion.SpringDampingBouncy),
        label = "buttonScale"
    )

    // Glow pulse (triggered by press, only for Filled/Elevated)
    var glowPulse by remember { mutableStateOf(false) }
    LaunchedEffect(isPressed) {
        if (isPressed && enabled && !loading && buttonColors.hasGlow) {
            glowPulse = true
            delay(ArcaneMotion.Medium.toLong())
            glowPulse = false
        }
    }
    val glowAlpha by animateFloatAsState(
        targetValue = if (glowPulse) 0.3f else 0f,
        animationSpec = tween(ArcaneMotion.Medium, easing = ArcaneMotion.EaseOut),
        label = "glowPulse"
    )

    // State layer overlay (M3 standard)
    val stateOverlay = when {
        !enabled || loading -> Color.Transparent
        isPressed -> buttonColors.contentColor.copy(alpha = colors.stateLayerPressed)
        else -> Color.Transparent
    }

    // Content/spinner crossfade
    val contentAlpha by animateFloatAsState(
        targetValue = if (loading) 0f else 1f,
        animationSpec = tween(ArcaneMotion.Fast),
        label = "contentAlpha"
    )

    // ============================================================================
    // UI
    // ============================================================================

    Box(
        modifier = modifier
            .defaultMinSize(minHeight = sizeSpec.minHeight)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            // Glow effect using arcaneGlow modifier
            .arcaneGlowIf(
                enabled = buttonColors.hasGlow,
                color = colors.glow,
                alpha = glowAlpha,
                radiusFactor = 0.8f
            )
            .then(
                if (buttonColors.hasElevation) {
                    Modifier.shadow(2.dp, RoundedCornerShape(shapeRadius))
                } else Modifier
            )
            .clip(RoundedCornerShape(shapeRadius))
            .background(buttonColors.backgroundColor)
            .then(
                if (stateOverlay != Color.Transparent) {
                    Modifier.background(stateOverlay)
                } else Modifier
            )
            .then(
                if (buttonColors.borderWidth > 0.dp) {
                    Modifier.border(
                        buttonColors.borderWidth,
                        buttonColors.borderColor,
                        RoundedCornerShape(shapeRadius)
                    )
                } else Modifier
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled && !loading,
                role = Role.Button,
                onClick = onClick
            )
            .padding(
                horizontal = sizeSpec.horizontalPadding,
                vertical = sizeSpec.verticalPadding
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.alpha(contentAlpha),
            horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            androidx.compose.runtime.CompositionLocalProvider(
                androidx.compose.material3.LocalContentColor provides buttonColors.contentColor
            ) {
                content()
            }
        }

        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(sizeSpec.iconSize),
                color = buttonColors.spinnerColor,
                strokeWidth = 2.dp
            )
        }
    }
}

// ============================================================================
// Convenience Composable
// ============================================================================

@Composable
fun ArcaneTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    style: ArcaneButtonStyle = ArcaneButtonStyle.Filled(),
    size: ArcaneButtonSize = ArcaneButtonSize.Medium,
    shape: ArcaneButtonShape = ArcaneButtonShape.Round,
) {
    val sizeSpec = buttonSizeSpecs[size] ?: buttonSizeSpecs[ArcaneButtonSize.Medium]!!

    ArcaneButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        loading = loading,
        style = style,
        size = size,
        shape = shape
    ) {
        Text(
            text = text,
            fontSize = sizeSpec.fontSize,
            color = androidx.compose.material3.LocalContentColor.current
        )
    }
}
