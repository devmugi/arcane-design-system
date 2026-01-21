package io.github.devmugi.arcane.design.components.navigation

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
