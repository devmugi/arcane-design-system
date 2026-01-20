// arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/Switch.kt
package io.github.devmugi.arcane.design.components.controls

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneBorder
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun ArcaneSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography

    val trackWidth = 48.dp
    val trackHeight = 26.dp
    val thumbSize = 20.dp
    val thumbPadding = 3.dp

    val thumbOffset by animateDpAsState(
        targetValue = if (checked) trackWidth - thumbSize - thumbPadding * 2 else 0.dp,
        animationSpec = tween(200),
        label = "thumbOffset"
    )

    val trackColor by animateColorAsState(
        targetValue = when {
            !enabled -> colors.surfaceInset.copy(alpha = 0.5f)
            checked -> colors.primary
            else -> colors.surfaceInset
        },
        animationSpec = tween(200),
        label = "trackColor"
    )

    val borderColor by animateColorAsState(
        targetValue = when {
            !enabled -> colors.textDisabled.copy(alpha = 0.3f)
            checked -> colors.primary
            else -> colors.border
        },
        animationSpec = tween(200),
        label = "borderColor"
    )

    val thumbColor by animateColorAsState(
        targetValue = when {
            !enabled -> colors.textDisabled
            checked -> colors.surface
            else -> colors.textSecondary
        },
        animationSpec = tween(200),
        label = "thumbColor"
    )

    Row(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                role = Role.Switch,
                onClick = { onCheckedChange(!checked) }
            ),
        horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(trackWidth)
                .height(trackHeight)
                .clip(ArcaneRadius.Full)
                .background(trackColor)
                .border(ArcaneBorder.Title, borderColor, ArcaneRadius.Full)
                .padding(thumbPadding),
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .offset(x = thumbOffset)
                    .size(thumbSize)
                    .clip(CircleShape)
                    .background(thumbColor)
            )
        }

        if (label != null) {
            Text(
                text = label,
                style = typography.bodyMedium,
                color = if (enabled) colors.text else colors.textDisabled
            )
        }
    }
}
