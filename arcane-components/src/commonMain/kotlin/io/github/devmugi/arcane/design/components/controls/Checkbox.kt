// arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/Checkbox.kt
package io.github.devmugi.arcane.design.components.controls

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneBorder
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun ArcaneCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography

    val backgroundColor by animateColorAsState(
        targetValue = when {
            !enabled -> colors.surfaceContainerLowest.copy(alpha = 0.5f)
            checked -> colors.primary
            else -> colors.surfaceContainerLowest
        },
        animationSpec = tween(150),
        label = "backgroundColor"
    )

    val borderColor by animateColorAsState(
        targetValue = when {
            !enabled -> colors.textDisabled.copy(alpha = 0.3f)
            checked -> colors.primary
            else -> colors.border
        },
        animationSpec = tween(150),
        label = "borderColor"
    )

    val checkmarkColor = if (checked) colors.surface else Color.Transparent

    Row(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                role = Role.Checkbox,
                onClick = { onCheckedChange(!checked) }
            ),
        horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(ArcaneRadius.Small)
                .background(backgroundColor)
                .border(ArcaneBorder.Thin, borderColor, ArcaneRadius.Small)
                .drawWithContent {
                    drawContent()
                    if (checked) {
                        val strokeWidth = 2.dp.toPx()
                        val padding = 5.dp.toPx()
                        // Draw checkmark
                        drawLine(
                            color = checkmarkColor,
                            start = Offset(padding, size.height / 2),
                            end = Offset(size.width / 2 - 1.dp.toPx(), size.height - padding),
                            strokeWidth = strokeWidth,
                            cap = StrokeCap.Round
                        )
                        drawLine(
                            color = checkmarkColor,
                            start = Offset(size.width / 2 - 1.dp.toPx(), size.height - padding),
                            end = Offset(size.width - padding, padding),
                            strokeWidth = strokeWidth,
                            cap = StrokeCap.Round
                        )
                    }
                },
            contentAlignment = Alignment.Center
        ) {}

        if (label != null) {
            Text(
                text = label,
                style = typography.bodyMedium,
                color = if (enabled) colors.text else colors.textDisabled
            )
        }
    }
}
