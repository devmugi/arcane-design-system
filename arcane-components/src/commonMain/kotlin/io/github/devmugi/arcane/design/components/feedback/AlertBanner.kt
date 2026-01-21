package io.github.devmugi.arcane.design.components.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Immutable
sealed class ArcaneAlertStyle {
    data object Info : ArcaneAlertStyle()
    data object Success : ArcaneAlertStyle()
    data object Warning : ArcaneAlertStyle()
    data object Error : ArcaneAlertStyle()
}

@Immutable
data class ArcaneAlertAction(
    val label: String,
    val onClick: () -> Unit
)

@Composable
fun ArcaneAlertBanner(
    message: String,
    modifier: Modifier = Modifier,
    style: ArcaneAlertStyle = ArcaneAlertStyle.Info,
    icon: (@Composable () -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
    action: ArcaneAlertAction? = null
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography

    val (accentColor, defaultIcon) = when (style) {
        is ArcaneAlertStyle.Info -> colors.primary to Icons.Default.Info
        is ArcaneAlertStyle.Success -> colors.success to Icons.Default.Check
        is ArcaneAlertStyle.Warning -> colors.warning to Icons.Default.Warning
        is ArcaneAlertStyle.Error -> colors.error to Icons.Default.Warning
    }

    val shape = RoundedCornerShape(8.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(accentColor.copy(alpha = 0.1f))
            .border(
                width = 1.dp,
                color = accentColor.copy(alpha = 0.3f),
                shape = shape
            )
    ) {
        // Left accent border
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .background(accentColor)
                .size(width = 3.dp, height = 56.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = ArcaneSpacing.Medium, end = ArcaneSpacing.Small)
                .padding(vertical = ArcaneSpacing.Small),
            horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            if (icon != null) {
                icon()
            } else {
                Icon(
                    imageVector = defaultIcon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = accentColor
                )
            }

            // Message
            Text(
                text = message,
                style = typography.bodyMedium,
                color = colors.text,
                modifier = Modifier.weight(1f)
            )

            // Action button
            if (action != null) {
                Text(
                    text = action.label,
                    style = typography.labelMedium,
                    color = accentColor,
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = action.onClick
                        )
                        .padding(horizontal = ArcaneSpacing.Small)
                )
            }

            // Dismiss button
            if (onDismiss != null) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Dismiss",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onDismiss
                        ),
                    tint = colors.textSecondary
                )
            }
        }
    }
}
