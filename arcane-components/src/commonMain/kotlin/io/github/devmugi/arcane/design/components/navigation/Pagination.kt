package io.github.devmugi.arcane.design.components.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing
import kotlin.math.max
import kotlin.math.min

internal sealed class PaginationItem {
    data class Page(val number: Int) : PaginationItem()
    data object Ellipsis : PaginationItem()
}

internal fun calculatePaginationItems(
    currentPage: Int,
    totalPages: Int,
    siblingCount: Int,
    boundaryCount: Int
): List<PaginationItem> {
    if (totalPages <= 0) return emptyList()

    // Calculate left and right boundary pages
    val leftBoundaryEnd = boundaryCount
    val rightBoundaryStart = totalPages - boundaryCount + 1

    // Calculate the center range (current page with siblings)
    // The center range should always have (siblingCount * 2 + 1) pages when possible
    val centerSize = siblingCount * 2 + 1

    // Calculate ideal center range
    var centerStart = currentPage - siblingCount
    var centerEnd = currentPage + siblingCount

    // Adjust if center range extends past boundaries
    if (centerStart < 1) {
        // Shift right to compensate
        centerEnd += (1 - centerStart)
        centerStart = 1
    }
    if (centerEnd > totalPages) {
        // Shift left to compensate
        centerStart -= (centerEnd - totalPages)
        centerEnd = totalPages
    }
    // Clamp again after shifting
    centerStart = max(1, centerStart)
    centerEnd = min(totalPages, centerEnd)

    // Calculate total visible pages when we show all
    // boundary pages on each side + center range + potential ellipsis gaps
    val minPagesWithEllipsis = boundaryCount + 1 + centerSize + 1 + boundaryCount

    // If we can show all pages, just return them all
    if (totalPages <= minPagesWithEllipsis) {
        return (1..totalPages).map { PaginationItem.Page(it) }
    }

    val result = mutableListOf<PaginationItem>()

    // Add left boundary pages
    for (i in 1..leftBoundaryEnd) {
        result.add(PaginationItem.Page(i))
    }

    // Determine if we need left ellipsis
    // We need ellipsis if there's a gap between boundary end and center start
    val showLeftEllipsis = centerStart > leftBoundaryEnd + 2

    // Determine if we need right ellipsis
    val showRightEllipsis = centerEnd < rightBoundaryStart - 2

    if (showLeftEllipsis) {
        result.add(PaginationItem.Ellipsis)
    } else {
        // Add pages between boundary and center start (no gap, so fill it)
        for (i in (leftBoundaryEnd + 1) until centerStart) {
            result.add(PaginationItem.Page(i))
        }
    }

    // Add center pages (current page with siblings)
    for (i in centerStart..centerEnd) {
        if (i > leftBoundaryEnd && i < rightBoundaryStart) {
            result.add(PaginationItem.Page(i))
        }
    }

    if (showRightEllipsis) {
        result.add(PaginationItem.Ellipsis)
    } else {
        // Add pages between center end and right boundary (no gap, so fill it)
        for (i in (centerEnd + 1) until rightBoundaryStart) {
            result.add(PaginationItem.Page(i))
        }
    }

    // Add right boundary pages
    for (i in rightBoundaryStart..totalPages) {
        result.add(PaginationItem.Page(i))
    }

    return result
}

private val PaginationButtonSize = 32.dp

/**
 * Private composable for pagination buttons with animated background.
 * Used for page numbers and navigation arrows.
 */
@Composable
private fun PaginationButton(
    onClick: () -> Unit,
    enabled: Boolean,
    selected: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val colors = ArcaneTheme.colors
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val backgroundColor by animateColorAsState(
        targetValue = when {
            !enabled -> Color.Transparent
            isPressed -> colors.surfaceContainerHigh
            selected -> colors.primary
            else -> Color.Transparent
        },
        animationSpec = tween(150),
        label = "paginationButtonBackground"
    )

    val contentColor = when {
        !enabled -> colors.textDisabled
        selected -> colors.surface
        else -> colors.text
    }

    Box(
        modifier = modifier
            .size(PaginationButtonSize)
            .clip(ArcaneRadius.Medium)
            .background(backgroundColor, ArcaneRadius.Medium)
            .then(
                if (enabled) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        role = Role.Button,
                        onClick = onClick
                    )
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "",
            style = ArcaneTheme.typography.bodySmall,
            color = contentColor
        )
        Box(contentAlignment = Alignment.Center) {
            androidx.compose.runtime.CompositionLocalProvider(
                androidx.compose.material3.LocalContentColor provides contentColor
            ) {
                content()
            }
        }
    }
}

/**
 * Pagination component for navigating through multiple pages of content.
 *
 * Displays page numbers with ellipsis for large page counts, previous/next arrows,
 * and optional "Page X of Y" information.
 *
 * ## Visual Styling
 * - Current page: Primary background with surface text color
 * - Other pages: Transparent background with text color
 * - Disabled arrows: textDisabled color at boundaries
 * - Ellipsis: Non-interactive, textSecondary color
 *
 * ## Page Number Display (with siblingCount=1, boundaryCount=1)
 * - Page 1:  < [1] 2 3 ... 10 >
 * - Page 5:  < 1 ... 4 [5] 6 ... 10 >
 * - Page 10: < 1 ... 8 9 [10] >
 *
 * @param currentPage The currently selected page (1-indexed)
 * @param totalPages The total number of pages
 * @param onPageSelected Callback when a page is selected
 * @param modifier Modifier to be applied to the pagination container
 * @param showPageInfo Whether to show "Page X of Y" text
 * @param siblingCount Number of pages to show on each side of current page
 * @param boundaryCount Number of pages to show at the start and end
 */
@Composable
fun ArcanePagination(
    currentPage: Int,
    totalPages: Int,
    onPageSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    showPageInfo: Boolean = true,
    siblingCount: Int = 1,
    boundaryCount: Int = 1
) {
    if (totalPages <= 0) return

    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography
    val items = calculatePaginationItems(currentPage, totalPages, siblingCount, boundaryCount)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Page info text
        if (showPageInfo) {
            Text(
                text = "Page $currentPage of $totalPages",
                style = typography.bodySmall,
                color = colors.textSecondary
            )
        }

        // Previous button
        PaginationButton(
            onClick = { if (currentPage > 1) onPageSelected(currentPage - 1) },
            enabled = currentPage > 1,
            selected = false
        ) {
            Text(
                text = "<",
                style = typography.bodySmall,
                color = if (currentPage > 1) colors.text else colors.textDisabled
            )
        }

        // Page items
        items.forEach { item ->
            when (item) {
                is PaginationItem.Page -> {
                    PaginationButton(
                        onClick = { onPageSelected(item.number) },
                        enabled = true,
                        selected = item.number == currentPage
                    ) {
                        Text(
                            text = item.number.toString(),
                            style = typography.bodySmall,
                            color = if (item.number == currentPage) colors.surface else colors.text
                        )
                    }
                }
                is PaginationItem.Ellipsis -> {
                    Box(
                        modifier = Modifier.size(PaginationButtonSize),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "...",
                            style = typography.bodySmall,
                            color = colors.textSecondary
                        )
                    }
                }
            }
        }

        // Next button
        PaginationButton(
            onClick = { if (currentPage < totalPages) onPageSelected(currentPage + 1) },
            enabled = currentPage < totalPages,
            selected = false
        ) {
            Text(
                text = ">",
                style = typography.bodySmall,
                color = if (currentPage < totalPages) colors.text else colors.textDisabled
            )
        }
    }
}
