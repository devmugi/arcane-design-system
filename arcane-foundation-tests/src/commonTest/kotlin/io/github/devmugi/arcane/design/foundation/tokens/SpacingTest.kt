package io.github.devmugi.arcane.design.foundation.tokens

import androidx.compose.ui.unit.Dp
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class SpacingTest {

    @Test
    fun `all spacing values follow 4dp grid`() {
        val spacingValues: List<Dp> = listOf(
            ArcaneSpacing.XXSmall,
            ArcaneSpacing.XSmall,
            ArcaneSpacing.Small,
            ArcaneSpacing.Medium,
            ArcaneSpacing.Large,
            ArcaneSpacing.XLarge,
            ArcaneSpacing.XXLarge
        )

        spacingValues.forEach { spacing ->
            (spacing.value.toInt() % 4) shouldBe 0
        }
    }

    @Test
    fun `spacing values are in ascending order`() {
        ArcaneSpacing.XSmall shouldBeGreaterThan ArcaneSpacing.XXSmall
        ArcaneSpacing.Small shouldBeGreaterThan ArcaneSpacing.XSmall
        ArcaneSpacing.Medium shouldBeGreaterThan ArcaneSpacing.Small
        ArcaneSpacing.Large shouldBeGreaterThan ArcaneSpacing.Medium
        ArcaneSpacing.XLarge shouldBeGreaterThan ArcaneSpacing.Large
        ArcaneSpacing.XXLarge shouldBeGreaterThan ArcaneSpacing.XLarge
    }

    @Test
    fun `XXSmall spacing is 4dp`() {
        ArcaneSpacing.XXSmall.value shouldBe 4f
    }

    @Test
    fun `XSmall spacing is 8dp`() {
        ArcaneSpacing.XSmall.value shouldBe 8f
    }

    @Test
    fun `Small spacing is 12dp`() {
        ArcaneSpacing.Small.value shouldBe 12f
    }

    @Test
    fun `Medium spacing is 16dp`() {
        ArcaneSpacing.Medium.value shouldBe 16f
    }

    @Test
    fun `Large spacing is 24dp`() {
        ArcaneSpacing.Large.value shouldBe 24f
    }

    @Test
    fun `XLarge spacing is 32dp`() {
        ArcaneSpacing.XLarge.value shouldBe 32f
    }

    @Test
    fun `XXLarge spacing is 48dp`() {
        ArcaneSpacing.XXLarge.value shouldBe 48f
    }
}
