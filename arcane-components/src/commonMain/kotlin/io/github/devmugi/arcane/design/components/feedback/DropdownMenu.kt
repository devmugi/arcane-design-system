package io.github.devmugi.arcane.design.components.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.primitives.ArcaneSurface
import io.github.devmugi.arcane.design.foundation.primitives.SurfaceVariant
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

/**
 * Arcane Design System dropdown menu component.
 *
 * A themed wrapper around Material3's DropdownMenu that provides:
 * - Arcane surface styling with elevation
 * - Consistent theming across all platforms
 * - Proper border and shadow treatment
 *
 * Use this component for displaying popup menus with selectable options.
 * Leverages Material3's positioning logic while applying Arcane visual styling.
 *
 * Example usage:
 * ```kotlin
 * var expanded by remember { mutableStateOf(false) }
 *
 * Box {
 *     ArcaneButton(onClick = { expanded = true }) {
 *         Text("Open Menu")
 *     }
 *
 *     ArcaneDropdownMenu(
 *         expanded = expanded,
 *         onDismissRequest = { expanded = false }
 *     ) {
 *         ArcaneDropdownMenuItem(
 *             text = "Option 1",
 *             onClick = {
 *                 // Handle selection
 *                 expanded = false
 *             }
 *         )
 *         ArcaneDropdownMenuItem(
 *             text = "Option 2",
 *             onClick = {
 *                 // Handle selection
 *                 expanded = false
 *             }
 *         )
 *     }
 * }
 * ```
 *
 * @param expanded Whether the dropdown menu is currently visible
 * @param onDismissRequest Called when the user requests to dismiss the menu
 * @param modifier Modifier to apply to the dropdown menu
 * @param content The menu items to display (typically [ArcaneDropdownMenuItem])
 */
@Composable
fun ArcaneDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = ArcaneTheme.colors

    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            surface = colors.surfaceContainerHigh,
            onSurface = colors.text
        )
    ) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            modifier = modifier
                .background(colors.surfaceContainerHigh)
                .border(
                    width = 1.dp,
                    color = colors.border,
                    shape = ArcaneRadius.Medium
                )
                .clip(ArcaneRadius.Medium),
            content = content
        )
    }
}

/**
 * Arcane Design System dropdown menu item.
 *
 * A themed menu item for use within [ArcaneDropdownMenu]. Provides:
 * - Hover and pressed state feedback
 * - Optional leading icon
 * - Disabled state support
 * - Arcane typography and spacing
 *
 * Example usage:
 * ```kotlin
 * ArcaneDropdownMenu(
 *     expanded = expanded,
 *     onDismissRequest = { expanded = false }
 * ) {
 *     ArcaneDropdownMenuItem(
 *         text = "Edit",
 *         leadingIcon = {
 *             Icon(Icons.Default.Edit, contentDescription = null)
 *         },
 *         onClick = { /* Handle edit */ }
 *     )
 *
 *     ArcaneDropdownMenuItem(
 *         text = "Delete",
 *         onClick = { /* Handle delete */ },
 *         enabled = false
 *     )
 * }
 * ```
 *
 * @param text The text to display in the menu item
 * @param onClick Called when the menu item is clicked
 * @param modifier Modifier to apply to the menu item
 * @param leadingIcon Optional composable to display before the text (e.g., an icon)
 * @param enabled Whether the menu item is enabled and clickable
 */
@Composable
fun ArcaneDropdownMenuItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    enabled: Boolean = true
) {
    val colors = ArcaneTheme.colors
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    val stateOverlay = when {
        !enabled -> Color.Transparent
        isPressed -> colors.primary.copy(alpha = colors.stateLayerPressed)
        isHovered -> colors.primary.copy(alpha = colors.stateLayerHover)
        else -> Color.Transparent
    }

    val textColor = if (enabled) colors.text else colors.textDisabled

    Row(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            )
            .background(stateOverlay)
            .padding(
                horizontal = ArcaneSpacing.Medium,
                vertical = ArcaneSpacing.Small
            ),
        horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingIcon != null) {
            leadingIcon()
        }

        Text(
            text = text,
            style = ArcaneTheme.typography.bodyMedium,
            color = textColor
        )
    }
}
