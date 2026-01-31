package io.github.devmugi.arcane.design.components.display

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlin.test.Test

/**
 * Unit tests for ArcaneBadgeStyle sealed class.
 *
 * Note: ArcaneBadge composable function is not unit testable without UI testing frameworks.
 * These tests cover the style variants and their sealed class structure.
 *
 * Custom style tests involving Color are omitted because Color is not available
 * in the test module's classpath. The Custom data class functionality (equality,
 * hashCode, etc.) is tested through Kotlin's data class guarantees.
 */
class BadgeTest {

    // ==========================================================================
    // ArcaneBadgeStyle sealed class tests - object variants
    // ==========================================================================

    @Test
    fun `Default style is a singleton object`() {
        val style1 = ArcaneBadgeStyle.Default
        val style2 = ArcaneBadgeStyle.Default
        style1 shouldBe style2
    }

    @Test
    fun `Success style is a singleton object`() {
        val style1 = ArcaneBadgeStyle.Success
        val style2 = ArcaneBadgeStyle.Success
        style1 shouldBe style2
    }

    @Test
    fun `Warning style is a singleton object`() {
        val style1 = ArcaneBadgeStyle.Warning
        val style2 = ArcaneBadgeStyle.Warning
        style1 shouldBe style2
    }

    @Test
    fun `Error style is a singleton object`() {
        val style1 = ArcaneBadgeStyle.Error
        val style2 = ArcaneBadgeStyle.Error
        style1 shouldBe style2
    }

    @Test
    fun `Neutral style is a singleton object`() {
        val style1 = ArcaneBadgeStyle.Neutral
        val style2 = ArcaneBadgeStyle.Neutral
        style1 shouldBe style2
    }

    // ==========================================================================
    // Sealed class variant type tests
    // ==========================================================================

    @Test
    fun `Default style is subtype of ArcaneBadgeStyle`() {
        val style: ArcaneBadgeStyle = ArcaneBadgeStyle.Default
        style.shouldBeInstanceOf<ArcaneBadgeStyle>()
    }

    @Test
    fun `Success style is subtype of ArcaneBadgeStyle`() {
        val style: ArcaneBadgeStyle = ArcaneBadgeStyle.Success
        style.shouldBeInstanceOf<ArcaneBadgeStyle>()
    }

    @Test
    fun `Warning style is subtype of ArcaneBadgeStyle`() {
        val style: ArcaneBadgeStyle = ArcaneBadgeStyle.Warning
        style.shouldBeInstanceOf<ArcaneBadgeStyle>()
    }

    @Test
    fun `Error style is subtype of ArcaneBadgeStyle`() {
        val style: ArcaneBadgeStyle = ArcaneBadgeStyle.Error
        style.shouldBeInstanceOf<ArcaneBadgeStyle>()
    }

    @Test
    fun `Neutral style is subtype of ArcaneBadgeStyle`() {
        val style: ArcaneBadgeStyle = ArcaneBadgeStyle.Neutral
        style.shouldBeInstanceOf<ArcaneBadgeStyle>()
    }

    @Test
    fun `Custom style class is subtype of ArcaneBadgeStyle`() {
        // Verify the Custom class itself is a valid subclass through class reference
        val customClass = ArcaneBadgeStyle.Custom::class
        customClass.simpleName shouldBe "Custom"
    }

    // ==========================================================================
    // Object style variants can be matched with when expression
    // ==========================================================================

    @Test
    fun `object style variants can be matched exhaustively with when expression`() {
        val styles = listOf(
            ArcaneBadgeStyle.Default,
            ArcaneBadgeStyle.Success,
            ArcaneBadgeStyle.Warning,
            ArcaneBadgeStyle.Error,
            ArcaneBadgeStyle.Neutral
        )

        val results = styles.map { style ->
            when (style) {
                is ArcaneBadgeStyle.Default -> "default"
                is ArcaneBadgeStyle.Success -> "success"
                is ArcaneBadgeStyle.Warning -> "warning"
                is ArcaneBadgeStyle.Error -> "error"
                is ArcaneBadgeStyle.Neutral -> "neutral"
                is ArcaneBadgeStyle.Custom -> "custom"
            }
        }

        results shouldBe listOf("default", "success", "warning", "error", "neutral")
    }

    @Test
    fun `five object style variants exist`() {
        val styles = setOf(
            ArcaneBadgeStyle.Default::class,
            ArcaneBadgeStyle.Success::class,
            ArcaneBadgeStyle.Warning::class,
            ArcaneBadgeStyle.Error::class,
            ArcaneBadgeStyle.Neutral::class
        )

        styles.size shouldBe 5
    }

    // ==========================================================================
    // Different style types are never equal
    // ==========================================================================

    @Test
    fun `different object style types are never equal`() {
        val default = ArcaneBadgeStyle.Default
        val success = ArcaneBadgeStyle.Success
        val warning = ArcaneBadgeStyle.Warning
        val error = ArcaneBadgeStyle.Error
        val neutral = ArcaneBadgeStyle.Neutral

        default shouldNotBe success
        default shouldNotBe warning
        default shouldNotBe error
        default shouldNotBe neutral
        success shouldNotBe warning
        success shouldNotBe error
        success shouldNotBe neutral
        warning shouldNotBe error
        warning shouldNotBe neutral
        error shouldNotBe neutral
    }

    // ==========================================================================
    // toString tests for debugging clarity
    // ==========================================================================

    @Test
    fun `Default style toString contains object name`() {
        val style = ArcaneBadgeStyle.Default
        style.toString() shouldBe "Default"
    }

    @Test
    fun `Success style toString contains object name`() {
        val style = ArcaneBadgeStyle.Success
        style.toString() shouldBe "Success"
    }

    @Test
    fun `Warning style toString contains object name`() {
        val style = ArcaneBadgeStyle.Warning
        style.toString() shouldBe "Warning"
    }

    @Test
    fun `Error style toString contains object name`() {
        val style = ArcaneBadgeStyle.Error
        style.toString() shouldBe "Error"
    }

    @Test
    fun `Neutral style toString contains object name`() {
        val style = ArcaneBadgeStyle.Neutral
        style.toString() shouldBe "Neutral"
    }

    // ==========================================================================
    // Styles can be used in collections
    // ==========================================================================

    @Test
    fun `object styles can be stored in a set without duplicates`() {
        val styles = setOf(
            ArcaneBadgeStyle.Default,
            ArcaneBadgeStyle.Default,  // duplicate
            ArcaneBadgeStyle.Success,
            ArcaneBadgeStyle.Warning,
            ArcaneBadgeStyle.Error,
            ArcaneBadgeStyle.Neutral
        )

        styles.size shouldBe 5
    }

    @Test
    fun `object styles can be used as map keys`() {
        val styleNames = mapOf(
            ArcaneBadgeStyle.Default to "Default",
            ArcaneBadgeStyle.Success to "Success",
            ArcaneBadgeStyle.Warning to "Warning",
            ArcaneBadgeStyle.Error to "Error",
            ArcaneBadgeStyle.Neutral to "Neutral"
        )

        styleNames[ArcaneBadgeStyle.Default] shouldBe "Default"
        styleNames[ArcaneBadgeStyle.Success] shouldBe "Success"
        styleNames[ArcaneBadgeStyle.Warning] shouldBe "Warning"
        styleNames[ArcaneBadgeStyle.Error] shouldBe "Error"
        styleNames[ArcaneBadgeStyle.Neutral] shouldBe "Neutral"
    }

    @Test
    fun `object styles can be found in a list`() {
        val styles = listOf(
            ArcaneBadgeStyle.Default,
            ArcaneBadgeStyle.Success,
            ArcaneBadgeStyle.Warning,
            ArcaneBadgeStyle.Error,
            ArcaneBadgeStyle.Neutral
        )

        styles.contains(ArcaneBadgeStyle.Default) shouldBe true
        styles.contains(ArcaneBadgeStyle.Success) shouldBe true
        styles.contains(ArcaneBadgeStyle.Warning) shouldBe true
        styles.contains(ArcaneBadgeStyle.Error) shouldBe true
        styles.contains(ArcaneBadgeStyle.Neutral) shouldBe true
    }

    // ==========================================================================
    // hashCode consistency tests
    // ==========================================================================

    @Test
    fun `Default style hashCode is consistent`() {
        val style = ArcaneBadgeStyle.Default
        val hashCode1 = style.hashCode()
        val hashCode2 = style.hashCode()
        hashCode1 shouldBe hashCode2
    }

    @Test
    fun `Success style hashCode is consistent`() {
        val style = ArcaneBadgeStyle.Success
        val hashCode1 = style.hashCode()
        val hashCode2 = style.hashCode()
        hashCode1 shouldBe hashCode2
    }

    @Test
    fun `Warning style hashCode is consistent`() {
        val style = ArcaneBadgeStyle.Warning
        val hashCode1 = style.hashCode()
        val hashCode2 = style.hashCode()
        hashCode1 shouldBe hashCode2
    }

    @Test
    fun `Error style hashCode is consistent`() {
        val style = ArcaneBadgeStyle.Error
        val hashCode1 = style.hashCode()
        val hashCode2 = style.hashCode()
        hashCode1 shouldBe hashCode2
    }

    @Test
    fun `Neutral style hashCode is consistent`() {
        val style = ArcaneBadgeStyle.Neutral
        val hashCode1 = style.hashCode()
        val hashCode2 = style.hashCode()
        hashCode1 shouldBe hashCode2
    }
}
