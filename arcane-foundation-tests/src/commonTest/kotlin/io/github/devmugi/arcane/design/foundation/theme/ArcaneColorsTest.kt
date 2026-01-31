package io.github.devmugi.arcane.design.foundation.theme

import androidx.compose.ui.graphics.Color
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlin.test.Test

class ArcaneColorsTest {

    @Test
    fun `default factory returns ArcaneColors with expected primary`() {
        val colors = ArcaneColors.default()

        colors.primary shouldBe Color(0xFF8B5CF6)
    }

    @Test
    fun `default constructor produces same result as default factory`() {
        val fromConstructor = ArcaneColors()
        val fromFactory = ArcaneColors.default()

        fromConstructor shouldBe fromFactory
    }

    @Test
    fun `light variants have lighter surface colors than dark variants`() {
        val p2Light = ArcaneColors.p2l()
        val p2Dark = ArcaneColors.p2d()

        // Light themes have lighter (higher luminance) surfaces
        // In light theme, surfaceContainerLow is near-white (0xFFFAFAFA)
        // In dark theme, it's dark (0xFF191919)
        p2Light.surfaceContainerLow shouldNotBe p2Dark.surfaceContainerLow

        // Light theme surface should have higher red component (whiter)
        val lightRed = p2Light.surfaceContainerLow.red
        val darkRed = p2Dark.surfaceContainerLow.red

        // Light theme should have higher RGB values (closer to white)
        (lightRed > darkRed) shouldBe true
    }

    @Test
    fun `dark factory returns distinct purple-themed dark colors`() {
        val dark = ArcaneColors.dark()
        val default = ArcaneColors.default()

        // dark() should have a lighter purple primary for dark backgrounds
        dark.primary shouldBe Color(0xFFB19EFF)
        dark.primary shouldNotBe default.primary
    }

    @Test
    fun `withPrimary creates colors with custom primary`() {
        val customPrimary = Color(0xFFFF0000) // Red
        val colors = ArcaneColors.withPrimary(customPrimary)

        colors.primary shouldBe customPrimary
        colors.glow shouldBe customPrimary.copy(alpha = 0.3f)
        colors.glowStrong shouldBe customPrimary.copy(alpha = 0.6f)
    }

    @Test
    fun `state layer alphas follow M3 standard values`() {
        val colors = ArcaneColors.default()

        colors.stateLayerHover shouldBe 0.08f
        colors.stateLayerPressed shouldBe 0.12f
        colors.stateLayerFocus shouldBe 0.12f
        colors.stateLayerDragged shouldBe 0.16f
    }

    @Test
    fun `surface container levels form proper hierarchy`() {
        val colors = ArcaneColors.default()

        // All surface containers should be different
        val surfaces = listOf(
            colors.surfaceContainerLowest,
            colors.surfaceContainerLow,
            colors.surfaceContainer,
            colors.surfaceContainerHigh,
            colors.surfaceContainerHighest
        )

        // All should be distinct
        surfaces.distinct().size shouldBe 5
    }

    @Test
    fun `claude theme variants have warm brown tones`() {
        val claudeDark = ArcaneColors.claudeD()
        val claudeLight = ArcaneColors.claudeL()

        // Claude themes use orange/coral primary
        claudeDark.primary shouldBe Color(0xFFE07856)
        claudeLight.primary shouldBe Color(0xFFC45A3B)

        // Light and dark should have different surfaces
        claudeDark.surfaceContainerLow shouldNotBe claudeLight.surfaceContainerLow
    }

    @Test
    fun `cv agent theme variants use gold accent`() {
        val cvDark = ArcaneColors.cvAgentDark()
        val cvLight = ArcaneColors.cvAgentLight()

        // CV Agent uses gold/amber primary
        cvDark.primary shouldBe Color(0xFFD4AF37)
        cvLight.primary shouldBe Color(0xFFB8942E)
    }

    @Test
    fun `perplexity theme uses cyan accent`() {
        val perplexity = ArcaneColors.perplexity()

        perplexity.primary shouldBe Color(0xFF4DD4AC)
    }

    @Test
    fun `agent2 theme variants use vibrant purple`() {
        val agent2Dark = ArcaneColors.agent2Dark()
        val agent2Light = ArcaneColors.agent2Light()

        // Both use same vibrant purple
        agent2Dark.primary shouldBe Color(0xFF7C3AED)
        agent2Light.primary shouldBe Color(0xFF7C3AED)
    }

    @Test
    fun `deprecated properties map to correct M3 equivalents`() {
        val colors = ArcaneColors.default()

        @Suppress("DEPRECATION")
        run {
            colors.surface shouldBe colors.surfaceContainerLow
            colors.surfaceRaised shouldBe colors.surfaceContainer
            colors.surfaceInset shouldBe colors.surfaceContainerLowest
            colors.border shouldBe colors.outline
        }
    }
}
