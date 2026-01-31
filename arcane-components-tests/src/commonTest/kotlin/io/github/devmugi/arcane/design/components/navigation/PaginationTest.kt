package io.github.devmugi.arcane.design.components.navigation

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class PaginationTest {

    // Test: Page 1 of 10 with siblingCount=1, boundaryCount=1
    // Expected: [1] 2 3 ... 10
    @Test
    fun `calculatePaginationItems returns correct items when on first page`() {
        val result = calculatePaginationItems(
            currentPage = 1,
            totalPages = 10,
            siblingCount = 1,
            boundaryCount = 1
        )

        val expected = listOf(
            PaginationItem.Page(1),
            PaginationItem.Page(2),
            PaginationItem.Page(3),
            PaginationItem.Ellipsis,
            PaginationItem.Page(10)
        )

        result shouldBe expected
    }

    // Test: Page 5 of 10 with siblingCount=1, boundaryCount=1
    // Expected: 1 ... 4 [5] 6 ... 10
    @Test
    fun `calculatePaginationItems returns correct items when in middle`() {
        val result = calculatePaginationItems(
            currentPage = 5,
            totalPages = 10,
            siblingCount = 1,
            boundaryCount = 1
        )

        val expected = listOf(
            PaginationItem.Page(1),
            PaginationItem.Ellipsis,
            PaginationItem.Page(4),
            PaginationItem.Page(5),
            PaginationItem.Page(6),
            PaginationItem.Ellipsis,
            PaginationItem.Page(10)
        )

        result shouldBe expected
    }

    // Test: Page 10 of 10 with siblingCount=1, boundaryCount=1
    // Expected: 1 ... 8 9 [10]
    @Test
    fun `calculatePaginationItems returns correct items when on last page`() {
        val result = calculatePaginationItems(
            currentPage = 10,
            totalPages = 10,
            siblingCount = 1,
            boundaryCount = 1
        )

        val expected = listOf(
            PaginationItem.Page(1),
            PaginationItem.Ellipsis,
            PaginationItem.Page(8),
            PaginationItem.Page(9),
            PaginationItem.Page(10)
        )

        result shouldBe expected
    }

    // Test: Small number of pages where no ellipsis is needed
    @Test
    fun `calculatePaginationItems returns all pages when total is small`() {
        val result = calculatePaginationItems(
            currentPage = 2,
            totalPages = 5,
            siblingCount = 1,
            boundaryCount = 1
        )

        val expected = listOf(
            PaginationItem.Page(1),
            PaginationItem.Page(2),
            PaginationItem.Page(3),
            PaginationItem.Page(4),
            PaginationItem.Page(5)
        )

        result shouldBe expected
    }

    // Test: Only one page
    @Test
    fun `calculatePaginationItems returns single page when totalPages is 1`() {
        val result = calculatePaginationItems(
            currentPage = 1,
            totalPages = 1,
            siblingCount = 1,
            boundaryCount = 1
        )

        val expected = listOf(PaginationItem.Page(1))

        result shouldBe expected
    }

    // Test: Page 2 of 10 - should only show end ellipsis
    @Test
    fun `calculatePaginationItems shows only end ellipsis when near start`() {
        val result = calculatePaginationItems(
            currentPage = 2,
            totalPages = 10,
            siblingCount = 1,
            boundaryCount = 1
        )

        val expected = listOf(
            PaginationItem.Page(1),
            PaginationItem.Page(2),
            PaginationItem.Page(3),
            PaginationItem.Ellipsis,
            PaginationItem.Page(10)
        )

        result shouldBe expected
    }

    // Test: Page 9 of 10 - should only show start ellipsis
    @Test
    fun `calculatePaginationItems shows only start ellipsis when near end`() {
        val result = calculatePaginationItems(
            currentPage = 9,
            totalPages = 10,
            siblingCount = 1,
            boundaryCount = 1
        )

        val expected = listOf(
            PaginationItem.Page(1),
            PaginationItem.Ellipsis,
            PaginationItem.Page(8),
            PaginationItem.Page(9),
            PaginationItem.Page(10)
        )

        result shouldBe expected
    }

    // Test: Higher siblingCount value
    @Test
    fun `calculatePaginationItems respects siblingCount of 2`() {
        val result = calculatePaginationItems(
            currentPage = 6,
            totalPages = 12,
            siblingCount = 2,
            boundaryCount = 1
        )

        val expected = listOf(
            PaginationItem.Page(1),
            PaginationItem.Ellipsis,
            PaginationItem.Page(4),
            PaginationItem.Page(5),
            PaginationItem.Page(6),
            PaginationItem.Page(7),
            PaginationItem.Page(8),
            PaginationItem.Ellipsis,
            PaginationItem.Page(12)
        )

        result shouldBe expected
    }

    // Test: Higher boundaryCount value
    @Test
    fun `calculatePaginationItems respects boundaryCount of 2`() {
        val result = calculatePaginationItems(
            currentPage = 6,
            totalPages = 12,
            siblingCount = 1,
            boundaryCount = 2
        )

        val expected = listOf(
            PaginationItem.Page(1),
            PaginationItem.Page(2),
            PaginationItem.Ellipsis,
            PaginationItem.Page(5),
            PaginationItem.Page(6),
            PaginationItem.Page(7),
            PaginationItem.Ellipsis,
            PaginationItem.Page(11),
            PaginationItem.Page(12)
        )

        result shouldBe expected
    }

    // Test: Edge case - zero pages should return empty list
    @Test
    fun `calculatePaginationItems returns empty list when totalPages is 0`() {
        val result = calculatePaginationItems(
            currentPage = 1,
            totalPages = 0,
            siblingCount = 1,
            boundaryCount = 1
        )

        result shouldBe emptyList()
    }
}
