// arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/Slider.kt
package io.github.devmugi.arcane.design.components.controls

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneBorder
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing
import kotlin.math.roundToInt

@Composable
fun ArcaneSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    showTooltip: Boolean = true,
    valueLabel: (Float) -> String = { "${(it * 100).roundToInt()}%" },
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography
    val density = LocalDensity.current

    val trackHeight = 6.dp
    val thumbSize = 20.dp

    var trackWidthPx by remember { mutableStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }

    val normalizedValue = ((value - valueRange.start) / (valueRange.endInclusive - valueRange.start))
        .coerceIn(0f, 1f)

    val trackColor = if (enabled) colors.surfaceInset else colors.surfaceInset.copy(alpha = 0.5f)
    val activeTrackColor = if (enabled) colors.primary else colors.textDisabled
    val thumbColor = if (enabled) colors.primary else colors.textDisabled
    val borderColor = if (enabled) colors.border else colors.textDisabled.copy(alpha = 0.3f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(thumbSize + 40.dp), // Extra space for tooltip
        contentAlignment = Alignment.BottomStart
    ) {
        // Tooltip
        AnimatedVisibility(
            visible = showTooltip && isDragging,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .offset {
                    val thumbCenterPx = normalizedValue * trackWidthPx
                    IntOffset(
                        x = (thumbCenterPx - with(density) { 24.dp.toPx() }).roundToInt(),
                        y = 0
                    )
                }
        ) {
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .clip(ArcaneRadius.Small)
                    .background(colors.surfaceRaised)
                    .border(ArcaneBorder.Thin, colors.border, ArcaneRadius.Small)
                    .padding(horizontal = ArcaneSpacing.XSmall, vertical = ArcaneSpacing.XXSmall),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = valueLabel(value),
                    style = typography.labelSmall,
                    color = colors.text
                )
            }
        }

        // Track and thumb container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(thumbSize)
                .align(Alignment.BottomStart)
                .onSizeChanged { size ->
                    trackWidthPx = size.width.toFloat()
                }
                .pointerInput(enabled, valueRange) {
                    if (!enabled) return@pointerInput
                    detectTapGestures { offset ->
                        val newNormalized = (offset.x / trackWidthPx).coerceIn(0f, 1f)
                        val newValue = valueRange.start + newNormalized * (valueRange.endInclusive - valueRange.start)
                        onValueChange(newValue)
                    }
                }
                .pointerInput(enabled, valueRange) {
                    if (!enabled) return@pointerInput
                    detectDragGestures(
                        onDragStart = { isDragging = true },
                        onDragEnd = { isDragging = false },
                        onDragCancel = { isDragging = false },
                        onDrag = { change, _ ->
                            change.consume()
                            val newNormalized = (change.position.x / trackWidthPx).coerceIn(0f, 1f)
                            val newValue = valueRange.start + newNormalized * (valueRange.endInclusive - valueRange.start)
                            onValueChange(newValue)
                        }
                    )
                },
            contentAlignment = Alignment.CenterStart
        ) {
            // Inactive track
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(trackHeight)
                    .clip(ArcaneRadius.Full)
                    .background(trackColor)
                    .border(ArcaneBorder.Thin, borderColor, ArcaneRadius.Full)
            )

            // Active track
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = normalizedValue)
                    .height(trackHeight)
                    .clip(ArcaneRadius.Full)
                    .background(activeTrackColor)
            )

            // Thumb
            Box(
                modifier = Modifier
                    .offset {
                        val maxOffset = trackWidthPx - with(density) { thumbSize.toPx() }
                        IntOffset(
                            x = (normalizedValue * maxOffset).roundToInt(),
                            y = 0
                        )
                    }
                    .size(thumbSize)
                    .clip(CircleShape)
                    .background(thumbColor)
                    .border(ArcaneBorder.Medium, colors.surface, CircleShape)
            )
        }
    }
}
