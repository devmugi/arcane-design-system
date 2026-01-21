// arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/TextField.kt
package io.github.devmugi.arcane.design.components.controls

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneBorder
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun ArcaneTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    helperText: String? = null,
    errorText: String? = null,
    isError: Boolean = errorText != null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isPassword: Boolean = false,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography
    val isFocused by interactionSource.collectIsFocusedAsState()

    val borderColor by animateColorAsState(
        targetValue = when {
            isError -> colors.error
            isFocused -> colors.borderFocused
            !enabled -> colors.textDisabled.copy(alpha = 0.3f)
            else -> colors.border
        },
        animationSpec = tween(150),
        label = "borderColor"
    )

    val backgroundColor = when {
        !enabled -> colors.surfaceInset.copy(alpha = 0.5f)
        else -> colors.surfaceInset
    }

    Column(modifier = modifier) {
        if (label != null) {
            Text(
                text = label,
                style = typography.labelMedium,
                color = if (isError) colors.error else colors.textSecondary,
                modifier = Modifier.padding(bottom = ArcaneSpacing.XXSmall)
            )
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .clip(ArcaneRadius.Medium)
                .background(backgroundColor)
                .border(ArcaneBorder.Thin, borderColor, ArcaneRadius.Medium)
                .padding(ArcaneSpacing.Small),
            enabled = enabled,
            readOnly = readOnly,
            textStyle = typography.bodyMedium.copy(color = colors.text),
            cursorBrush = SolidColor(colors.primary),
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            interactionSource = interactionSource,
            decorationBox = { innerTextField ->
                Box {
                    if (value.isEmpty() && placeholder != null) {
                        Text(
                            text = placeholder,
                            style = typography.bodyMedium,
                            color = colors.textDisabled
                        )
                    }
                    innerTextField()
                }
            }
        )

        val supportingText = errorText ?: helperText
        if (supportingText != null) {
            Text(
                text = supportingText,
                style = typography.labelSmall,
                color = if (isError) colors.error else colors.textSecondary,
                modifier = Modifier.padding(top = ArcaneSpacing.XXSmall)
            )
        }
    }
}
