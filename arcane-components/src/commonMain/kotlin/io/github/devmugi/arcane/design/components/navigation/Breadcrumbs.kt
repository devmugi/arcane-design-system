package io.github.devmugi.arcane.design.components.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

/**
 * Represents a single breadcrumb item in the navigation trail.
 *
 * @param label The text to display for this breadcrumb
 * @param onClick The action to perform when clicked. If null, this item is treated
 *                as the current/non-clickable item (typically the last item in the trail)
 */
@Immutable
data class ArcaneBreadcrumb(
    val label: String,
    val onClick: (() -> Unit)? = null
)

/**
 * Default separator displayed between breadcrumb items.
 * Renders a ">" character in secondary text color.
 */
@Composable
fun DefaultBreadcrumbSeparator() {
    Text(
        text = ">",
        style = ArcaneTheme.typography.bodyMedium,
        color = ArcaneTheme.colors.textSecondary,
        modifier = Modifier.padding(horizontal = ArcaneSpacing.XSmall)
    )
}

/**
 * Default collapsed indicator shown when breadcrumbs are collapsed.
 * Renders "..." in primary color.
 */
@Composable
fun DefaultCollapsedIndicator() {
    Text(
        text = "...",
        style = ArcaneTheme.typography.bodyMedium,
        color = ArcaneTheme.colors.primary
    )
}

/**
 * Internal composable for rendering a single breadcrumb item.
 */
@Composable
private fun BreadcrumbItem(
    breadcrumb: ArcaneBreadcrumb,
    modifier: Modifier = Modifier
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography
    val isClickable = breadcrumb.onClick != null

    if (isClickable) {
        val interactionSource = remember { MutableInteractionSource() }
        Text(
            text = breadcrumb.label,
            style = typography.bodyMedium,
            color = colors.primary,
            modifier = modifier.clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button,
                onClick = { breadcrumb.onClick?.invoke() }
            )
        )
    } else {
        // Current item (non-clickable)
        Text(
            text = breadcrumb.label,
            style = typography.bodyMedium,
            color = colors.text,
            fontWeight = FontWeight.Medium,
            modifier = modifier
        )
    }
}

/**
 * A breadcrumb navigation component that displays a hierarchical path.
 *
 * Breadcrumbs help users understand their current location within a navigation hierarchy
 * and provide quick navigation to parent levels.
 *
 * ## Visual Styling
 * - Clickable items (onClick != null): Displayed in primary color
 * - Current item (onClick == null): Displayed in text color with medium font weight
 * - Separator: ">" character in secondary text color
 *
 * ## Collapsing Behavior
 * When the number of items exceeds [maxItems] and maxItems >= 2:
 * - Shows the first item
 * - Shows the collapsed indicator ("...")
 * - Shows the last (maxItems - 1) items
 *
 * Example with maxItems=3 and 5 items: "Home > ... > Categories > Item"
 *
 * @param items List of breadcrumb items to display
 * @param modifier Modifier to be applied to the breadcrumbs container
 * @param separator Composable to display between breadcrumb items
 * @param maxItems Maximum number of items to display before collapsing. Must be >= 2 for collapsing to occur
 * @param collapsedIndicator Composable to display when items are collapsed
 */
@Composable
fun ArcaneBreadcrumbs(
    items: List<ArcaneBreadcrumb>,
    modifier: Modifier = Modifier,
    separator: @Composable () -> Unit = { DefaultBreadcrumbSeparator() },
    maxItems: Int = Int.MAX_VALUE,
    collapsedIndicator: @Composable () -> Unit = { DefaultCollapsedIndicator() }
) {
    if (items.isEmpty()) return

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val shouldCollapse = items.size > maxItems && maxItems >= 2

        if (shouldCollapse) {
            // Show first item
            BreadcrumbItem(breadcrumb = items.first())
            separator()

            // Show collapsed indicator
            collapsedIndicator()
            separator()

            // Show last (maxItems - 1) items
            val lastItems = items.takeLast(maxItems - 1)
            lastItems.forEachIndexed { index, breadcrumb ->
                BreadcrumbItem(breadcrumb = breadcrumb)
                if (index < lastItems.lastIndex) {
                    separator()
                }
            }
        } else {
            // Show all items
            items.forEachIndexed { index, breadcrumb ->
                BreadcrumbItem(breadcrumb = breadcrumb)
                if (index < items.lastIndex) {
                    separator()
                }
            }
        }
    }
}
