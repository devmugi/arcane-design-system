// arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/AgentChatInput.kt
package io.github.devmugi.arcane.design.components.controls

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneBorder
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun ArcaneAgentChatInput(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Reply to Claude...",
    enabled: Boolean = true,
    maxLines: Int = 6,
    onVoiceToTextClick: (() -> Unit)? = null,
    onAudioRecordClick: (() -> Unit)? = null,
    addMenuContent: (@Composable () -> Unit)? = null,
    activeItemsContent: (@Composable RowScope.() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography
    val isFocused by interactionSource.collectIsFocusedAsState()
    val hasText = value.isNotBlank()

    val borderColor by animateColorAsState(
        targetValue = when {
            !enabled -> colors.textDisabled.copy(alpha = 0.3f)
            isFocused -> colors.borderFocused
            else -> colors.border
        },
        animationSpec = tween(150),
        label = "borderColor"
    )

    val contentColor = if (enabled) colors.textSecondary else colors.textDisabled

    // Calculate max height for auto-expand (approximate line height * maxLines)
    val lineHeight = with(LocalDensity.current) { typography.bodyLarge.lineHeight.toDp() }
    val verticalPadding = ArcaneSpacing.Small * 2
    val maxHeight = lineHeight * maxLines + verticalPadding

    Column(
        modifier = modifier
            .clip(ArcaneRadius.Large)
            .border(ArcaneBorder.Thin, borderColor, ArcaneRadius.Large)
            .padding(ArcaneSpacing.XSmall),
        verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)
    ) {
        // Row 1: Text Input
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 40.dp, max = maxHeight)
                .clip(ArcaneRadius.Medium)
                .background(colors.surfaceInset)
                .padding(horizontal = ArcaneSpacing.Medium, vertical = ArcaneSpacing.Small)
                .onPreviewKeyEvent { keyEvent ->
                    if (keyEvent.type == KeyEventType.KeyDown &&
                        keyEvent.key == Key.Enter &&
                        !keyEvent.isShiftPressed &&
                        hasText
                    ) {
                        onSend()
                        true
                    } else {
                        false
                    }
                },
            enabled = enabled,
            textStyle = typography.bodyLarge.copy(color = colors.text),
            cursorBrush = SolidColor(colors.primary),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = { if (hasText) onSend() }),
            interactionSource = interactionSource,
            decorationBox = { innerTextField ->
                Box {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = typography.bodyLarge,
                            color = colors.textSecondary
                        )
                    }
                    innerTextField()
                }
            }
        )

        // Row 2: Actions Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = ArcaneSpacing.XSmall),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side: Add button + active items
            Row(
                horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Add button placeholder
                // Active items slot placeholder
            }

            // Right side: Voice buttons or Send button
            Row(
                horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Action buttons placeholder
            }
        }
    }
}
