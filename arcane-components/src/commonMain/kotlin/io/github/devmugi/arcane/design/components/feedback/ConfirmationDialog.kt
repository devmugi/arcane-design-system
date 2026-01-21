package io.github.devmugi.arcane.design.components.feedback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.components.controls.ArcaneButton
import io.github.devmugi.arcane.design.components.controls.ArcaneButtonStyle
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Immutable
sealed class ArcaneConfirmationStyle {
    data object Default : ArcaneConfirmationStyle()
    data object Destructive : ArcaneConfirmationStyle()
}

@Composable
fun ArcaneConfirmationDialog(
    visible: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    icon: (@Composable () -> Unit)? = null,
    confirmText: String = "Confirm",
    cancelText: String = "Cancel",
    style: ArcaneConfirmationStyle = ArcaneConfirmationStyle.Default
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography

    val iconColor = when (style) {
        is ArcaneConfirmationStyle.Default -> colors.primary
        is ArcaneConfirmationStyle.Destructive -> colors.error
    }

    ArcaneModal(
        visible = visible,
        onDismissRequest = onDismiss,
        modifier = modifier.width(280.dp),
        dismissOnBackdropClick = true
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icon
            if (icon != null) {
                icon()
            } else {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = iconColor
                )
            }

            Spacer(modifier = Modifier.height(ArcaneSpacing.Medium))

            // Title
            Text(
                text = title,
                style = typography.headlineMedium,
                color = colors.text,
                textAlign = TextAlign.Center
            )

            // Description
            if (description != null) {
                Spacer(modifier = Modifier.height(ArcaneSpacing.XSmall))
                Text(
                    text = description,
                    style = typography.bodyMedium,
                    color = colors.textSecondary,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(ArcaneSpacing.Large))

            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
            ) {
                ArcaneButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    style = ArcaneButtonStyle.Secondary
                ) {
                    Text(cancelText)
                }

                ArcaneButton(
                    onClick = {
                        onConfirm()
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f),
                    style = ArcaneButtonStyle.Primary
                ) {
                    Text(
                        text = confirmText,
                        color = if (style is ArcaneConfirmationStyle.Destructive) {
                            colors.error
                        } else {
                            ArcaneTheme.colors.surface
                        }
                    )
                }
            }
        }
    }
}
