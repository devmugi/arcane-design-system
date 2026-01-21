package io.github.devmugi.arcane.design.components.display

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing
import kotlinx.coroutines.delay

@Composable
fun ArcaneTooltipBox(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val colors = ArcaneTheme.colors

    Box(
        modifier = modifier
            .shadow(4.dp, ArcaneRadius.Small)
            .background(colors.surfaceRaised, ArcaneRadius.Small)
            .padding(
                horizontal = ArcaneSpacing.XSmall,
                vertical = ArcaneSpacing.XXSmall
            )
            .widthIn(max = 200.dp)
    ) {
        content()
    }
}

@Composable
fun ArcaneTooltip(
    tooltip: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    delayMs: Long = 500L,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    var showTooltip by remember { mutableStateOf(false) }

    LaunchedEffect(isHovered, enabled) {
        if (isHovered && enabled) {
            delay(delayMs)
            showTooltip = true
        } else {
            showTooltip = false
        }
    }

    Box(
        modifier = modifier.hoverable(interactionSource)
    ) {
        content()

        if (showTooltip) {
            Popup(
                alignment = Alignment.TopCenter,
                properties = PopupProperties(focusable = false)
            ) {
                AnimatedVisibility(
                    visible = showTooltip,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    ArcaneTooltipBox {
                        tooltip()
                    }
                }
            }
        }
    }
}

@Composable
fun ArcaneTooltip(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    delayMs: Long = 500L,
    content: @Composable () -> Unit
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography

    ArcaneTooltip(
        tooltip = {
            Text(
                text = text,
                style = typography.bodySmall,
                color = colors.text
            )
        },
        modifier = modifier,
        enabled = enabled,
        delayMs = delayMs,
        content = content
    )
}
