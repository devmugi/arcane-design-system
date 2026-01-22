package io.github.devmugi.arcane.chat.components.blocks

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.chat.models.Suggestion
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

/**
 * Renders a suggestion chip with icon and text.
 * Visual state changes based on selection (background brightness, border).
 *
 * @param suggestion The suggestion data to display
 * @param isSelected Whether this chip is currently selected
 * @param onClick Callback when chip is clicked
 * @param modifier Optional modifier
 */
@Composable
fun SuggestionChip(
    suggestion: Suggestion,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography

    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) {
            suggestion.color.copy(alpha = 0.3f)  // Brighter when selected
        } else {
            suggestion.color.copy(alpha = 0.15f)  // Subtle when not selected
        }
    )

    val borderColor = if (isSelected) {
        suggestion.color.copy(alpha = 0.6f)
    } else {
        Color.Transparent
    }

    Row(
        modifier = modifier
            .clip(ArcaneRadius.Large)
            .border(
                width = if (isSelected) 1.dp else 0.dp,
                color = borderColor,
                shape = ArcaneRadius.Large
            )
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(
                horizontal = ArcaneSpacing.Small,
                vertical = ArcaneSpacing.XSmall
            ),
        horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.XXSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon with color tint
        Box(modifier = Modifier.size(20.dp)) {
            CompositionLocalProvider(LocalContentColor provides suggestion.color) {
                suggestion.icon()
            }
        }

        Text(
            text = suggestion.text,
            style = typography.labelMedium,
            color = colors.text
        )
    }
}
