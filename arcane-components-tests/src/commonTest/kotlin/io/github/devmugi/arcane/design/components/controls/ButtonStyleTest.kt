package io.github.devmugi.arcane.design.components.controls

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlin.test.Test

class ButtonStyleTest {

    // ==========================================================================
    // ArcaneButtonStyle sealed class tests - default values
    // ==========================================================================

    @Test
    fun `Filled style can be created with defaults`() {
        val style = ArcaneButtonStyle.Filled()
        style.shouldBeInstanceOf<ArcaneButtonStyle.Filled>()
    }

    @Test
    fun `Tonal style can be created with defaults`() {
        val style = ArcaneButtonStyle.Tonal()
        style.shouldBeInstanceOf<ArcaneButtonStyle.Tonal>()
    }

    @Test
    fun `Outlined style can be created with defaults`() {
        val style = ArcaneButtonStyle.Outlined()
        style.shouldBeInstanceOf<ArcaneButtonStyle.Outlined>()
    }

    @Test
    fun `Elevated style can be created with defaults`() {
        val style = ArcaneButtonStyle.Elevated()
        style.shouldBeInstanceOf<ArcaneButtonStyle.Elevated>()
    }

    @Test
    fun `Text style is a singleton object`() {
        val style1 = ArcaneButtonStyle.Text
        val style2 = ArcaneButtonStyle.Text
        style1 shouldBe style2
    }

    // ==========================================================================
    // Sealed class variant type tests
    // ==========================================================================

    @Test
    fun `all style variants are subtypes of ArcaneButtonStyle`() {
        val styles: List<ArcaneButtonStyle> = listOf(
            ArcaneButtonStyle.Filled(),
            ArcaneButtonStyle.Tonal(),
            ArcaneButtonStyle.Outlined(),
            ArcaneButtonStyle.Elevated(),
            ArcaneButtonStyle.Text
        )

        styles.forEach { style ->
            style.shouldBeInstanceOf<ArcaneButtonStyle>()
        }
    }

    @Test
    fun `style variants can be matched exhaustively with when expression`() {
        val styles = listOf(
            ArcaneButtonStyle.Filled(),
            ArcaneButtonStyle.Tonal(),
            ArcaneButtonStyle.Outlined(),
            ArcaneButtonStyle.Elevated(),
            ArcaneButtonStyle.Text
        )

        val results = styles.map { style ->
            when (style) {
                is ArcaneButtonStyle.Filled -> "filled"
                is ArcaneButtonStyle.Tonal -> "tonal"
                is ArcaneButtonStyle.Outlined -> "outlined"
                is ArcaneButtonStyle.Elevated -> "elevated"
                is ArcaneButtonStyle.Text -> "text"
            }
        }

        results shouldBe listOf("filled", "tonal", "outlined", "elevated", "text")
    }

    @Test
    fun `five distinct style variants exist`() {
        val styles = setOf(
            ArcaneButtonStyle.Filled()::class,
            ArcaneButtonStyle.Tonal()::class,
            ArcaneButtonStyle.Outlined()::class,
            ArcaneButtonStyle.Elevated()::class,
            ArcaneButtonStyle.Text::class
        )

        styles.size shouldBe 5
    }

    // ==========================================================================
    // Data class equality tests
    // ==========================================================================

    @Test
    fun `Filled styles with default values are equal`() {
        val style1 = ArcaneButtonStyle.Filled()
        val style2 = ArcaneButtonStyle.Filled()
        style1 shouldBe style2
    }

    @Test
    fun `Tonal styles with default values are equal`() {
        val style1 = ArcaneButtonStyle.Tonal()
        val style2 = ArcaneButtonStyle.Tonal()
        style1 shouldBe style2
    }

    @Test
    fun `Outlined styles with default values are equal`() {
        val style1 = ArcaneButtonStyle.Outlined()
        val style2 = ArcaneButtonStyle.Outlined()
        style1 shouldBe style2
    }

    @Test
    fun `Elevated styles with default values are equal`() {
        val style1 = ArcaneButtonStyle.Elevated()
        val style2 = ArcaneButtonStyle.Elevated()
        style1 shouldBe style2
    }

    @Test
    fun `different style types are never equal`() {
        val filled = ArcaneButtonStyle.Filled()
        val tonal = ArcaneButtonStyle.Tonal()
        val outlined = ArcaneButtonStyle.Outlined()
        val elevated = ArcaneButtonStyle.Elevated()
        val text = ArcaneButtonStyle.Text

        filled shouldNotBe tonal
        filled shouldNotBe outlined
        filled shouldNotBe elevated
        filled shouldNotBe text
        tonal shouldNotBe outlined
        tonal shouldNotBe elevated
        tonal shouldNotBe text
        outlined shouldNotBe elevated
        outlined shouldNotBe text
        elevated shouldNotBe text
    }

    // ==========================================================================
    // ArcaneButtonSize enum tests
    // ==========================================================================

    @Test
    fun `ArcaneButtonSize has three values`() {
        val sizes = ArcaneButtonSize.entries
        sizes.size shouldBe 3
    }

    @Test
    fun `ArcaneButtonSize values are in correct order`() {
        val sizes = ArcaneButtonSize.entries
        sizes[0] shouldBe ArcaneButtonSize.ExtraSmall
        sizes[1] shouldBe ArcaneButtonSize.Small
        sizes[2] shouldBe ArcaneButtonSize.Medium
    }

    @Test
    fun `ArcaneButtonSize ExtraSmall has ordinal 0`() {
        ArcaneButtonSize.ExtraSmall.ordinal shouldBe 0
    }

    @Test
    fun `ArcaneButtonSize Small has ordinal 1`() {
        ArcaneButtonSize.Small.ordinal shouldBe 1
    }

    @Test
    fun `ArcaneButtonSize Medium has ordinal 2`() {
        ArcaneButtonSize.Medium.ordinal shouldBe 2
    }

    @Test
    fun `ArcaneButtonSize name property returns correct string`() {
        ArcaneButtonSize.ExtraSmall.name shouldBe "ExtraSmall"
        ArcaneButtonSize.Small.name shouldBe "Small"
        ArcaneButtonSize.Medium.name shouldBe "Medium"
    }

    @Test
    fun `ArcaneButtonSize can be retrieved by name`() {
        ArcaneButtonSize.valueOf("ExtraSmall") shouldBe ArcaneButtonSize.ExtraSmall
        ArcaneButtonSize.valueOf("Small") shouldBe ArcaneButtonSize.Small
        ArcaneButtonSize.valueOf("Medium") shouldBe ArcaneButtonSize.Medium
    }

    // ==========================================================================
    // ArcaneButtonShape enum tests
    // ==========================================================================

    @Test
    fun `ArcaneButtonShape has three values`() {
        val shapes = ArcaneButtonShape.entries
        shapes.size shouldBe 3
    }

    @Test
    fun `ArcaneButtonShape values are in correct order`() {
        val shapes = ArcaneButtonShape.entries
        shapes[0] shouldBe ArcaneButtonShape.Round
        shapes[1] shouldBe ArcaneButtonShape.Rounded
        shapes[2] shouldBe ArcaneButtonShape.Square
    }

    @Test
    fun `ArcaneButtonShape Round has ordinal 0`() {
        ArcaneButtonShape.Round.ordinal shouldBe 0
    }

    @Test
    fun `ArcaneButtonShape Rounded has ordinal 1`() {
        ArcaneButtonShape.Rounded.ordinal shouldBe 1
    }

    @Test
    fun `ArcaneButtonShape Square has ordinal 2`() {
        ArcaneButtonShape.Square.ordinal shouldBe 2
    }

    @Test
    fun `ArcaneButtonShape name property returns correct string`() {
        ArcaneButtonShape.Round.name shouldBe "Round"
        ArcaneButtonShape.Rounded.name shouldBe "Rounded"
        ArcaneButtonShape.Square.name shouldBe "Square"
    }

    @Test
    fun `ArcaneButtonShape can be retrieved by name`() {
        ArcaneButtonShape.valueOf("Round") shouldBe ArcaneButtonShape.Round
        ArcaneButtonShape.valueOf("Rounded") shouldBe ArcaneButtonShape.Rounded
        ArcaneButtonShape.valueOf("Square") shouldBe ArcaneButtonShape.Square
    }

    // ==========================================================================
    // toString tests for debugging clarity
    // ==========================================================================

    @Test
    fun `Filled style toString contains class name`() {
        val style = ArcaneButtonStyle.Filled()
        style.toString() shouldBe "Filled(containerColor=null)"
    }

    @Test
    fun `Tonal style toString contains class name`() {
        val style = ArcaneButtonStyle.Tonal()
        style.toString() shouldBe "Tonal(containerColor=null)"
    }

    @Test
    fun `Outlined style toString contains class name`() {
        val style = ArcaneButtonStyle.Outlined()
        style.toString() shouldBe "Outlined(borderColor=null)"
    }

    @Test
    fun `Elevated style toString contains class name`() {
        val style = ArcaneButtonStyle.Elevated()
        style.toString() shouldBe "Elevated(containerColor=null)"
    }

    @Test
    fun `Text style toString contains object name`() {
        val style = ArcaneButtonStyle.Text
        style.toString() shouldBe "Text"
    }

    // ==========================================================================
    // hashCode tests for collections usage
    // ==========================================================================

    @Test
    fun `Filled styles with defaults have same hashCode`() {
        val style1 = ArcaneButtonStyle.Filled()
        val style2 = ArcaneButtonStyle.Filled()
        style1.hashCode() shouldBe style2.hashCode()
    }

    @Test
    fun `Tonal styles with defaults have same hashCode`() {
        val style1 = ArcaneButtonStyle.Tonal()
        val style2 = ArcaneButtonStyle.Tonal()
        style1.hashCode() shouldBe style2.hashCode()
    }

    @Test
    fun `Outlined styles with defaults have same hashCode`() {
        val style1 = ArcaneButtonStyle.Outlined()
        val style2 = ArcaneButtonStyle.Outlined()
        style1.hashCode() shouldBe style2.hashCode()
    }

    @Test
    fun `Elevated styles with defaults have same hashCode`() {
        val style1 = ArcaneButtonStyle.Elevated()
        val style2 = ArcaneButtonStyle.Elevated()
        style1.hashCode() shouldBe style2.hashCode()
    }

    // ==========================================================================
    // Styles can be used in collections
    // ==========================================================================

    @Test
    fun `styles can be stored in a set without duplicates`() {
        val styles = setOf(
            ArcaneButtonStyle.Filled(),
            ArcaneButtonStyle.Filled(),  // duplicate
            ArcaneButtonStyle.Tonal(),
            ArcaneButtonStyle.Outlined(),
            ArcaneButtonStyle.Elevated(),
            ArcaneButtonStyle.Text
        )

        styles.size shouldBe 5
    }

    @Test
    fun `styles can be used as map keys`() {
        val styleNames = mapOf(
            ArcaneButtonStyle.Filled() to "Filled",
            ArcaneButtonStyle.Tonal() to "Tonal",
            ArcaneButtonStyle.Outlined() to "Outlined",
            ArcaneButtonStyle.Elevated() to "Elevated",
            ArcaneButtonStyle.Text to "Text"
        )

        styleNames[ArcaneButtonStyle.Filled()] shouldBe "Filled"
        styleNames[ArcaneButtonStyle.Text] shouldBe "Text"
    }
}
