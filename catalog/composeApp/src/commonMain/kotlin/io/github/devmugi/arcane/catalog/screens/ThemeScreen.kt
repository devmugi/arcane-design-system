package io.github.devmugi.arcane.catalog.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun ThemeScreen(windowSizeClass: WindowSizeClass? = null) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(ArcaneSpacing.Medium),
        verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Large)
    ) {
        // Color Palette Section
        SectionTitle("COLOR PALETTE")
        ColorPaletteSection()

        // Surface System Section
        SectionTitle("SURFACE SYSTEM")
        SurfaceSystemSection()

        // Text Colors Section
        SectionTitle("TEXT COLORS")
        TextColorsSection()

        // Outline Colors Section
        SectionTitle("OUTLINE & BORDERS")
        OutlineColorsSection()

        // State Layers Section
        SectionTitle("STATE LAYERS")
        StateLayersSection()

        // Typography Section
        SectionTitle("TYPOGRAPHY SCALE")
        TypographySection()

        // Spacing Section
        SectionTitle("SPACING SCALE")
        SpacingSection()

        // Radius Section
        SectionTitle("RADIUS SCALE")
        RadiusSection()

        // Effects Section
        SectionTitle("EFFECTS")
        EffectsSection()

        Spacer(modifier = Modifier.height(ArcaneSpacing.XLarge))
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = ArcaneTheme.typography.titleLarge,
        color = ArcaneTheme.colors.primary
    )
}

@Composable
private fun SubsectionLabel(text: String) {
    Text(
        text = text,
        style = ArcaneTheme.typography.titleSmall,
        color = ArcaneTheme.colors.textSecondary
    )
}

// ==================== COLOR PALETTE ====================

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ColorPaletteSection() {
    val colors = ArcaneTheme.colors

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Large),
        verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Medium)
    ) {
        // Primary Tonal Group
        TonalGroup(
            title = "Primary",
            colors = listOf(
                ColorItem("primary", colors.primary, colors.onPrimary),
                ColorItem("onPrimary", colors.onPrimary, colors.primary),
                ColorItem("primaryContainer", colors.primaryContainer, colors.onPrimaryContainer),
                ColorItem("onPrimaryContainer", colors.onPrimaryContainer, colors.primaryContainer)
            )
        )

        // Secondary Tonal Group
        TonalGroup(
            title = "Secondary",
            colors = listOf(
                ColorItem("secondaryContainer", colors.secondaryContainer, colors.onSecondaryContainer),
                ColorItem("onSecondaryContainer", colors.onSecondaryContainer, colors.secondaryContainer)
            )
        )

        // Tertiary Tonal Group
        TonalGroup(
            title = "Tertiary",
            colors = listOf(
                ColorItem("tertiary", colors.tertiary, colors.onTertiary),
                ColorItem("onTertiary", colors.onTertiary, colors.tertiary),
                ColorItem("tertiaryContainer", colors.tertiaryContainer, colors.onTertiaryContainer),
                ColorItem("onTertiaryContainer", colors.onTertiaryContainer, colors.tertiaryContainer)
            )
        )

        // Semantic Colors
        TonalGroup(
            title = "Semantic",
            colors = listOf(
                ColorItem("error", colors.error, Color.White),
                ColorItem("success", colors.success, Color.White),
                ColorItem("warning", colors.warning, Color.Black)
            )
        )
    }
}

private data class ColorItem(
    val name: String,
    val color: Color,
    val onColor: Color
)

@Composable
private fun TonalGroup(
    title: String,
    colors: List<ColorItem>
) {
    Column(
        modifier = Modifier.widthIn(min = 140.dp),
        verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)
    ) {
        SubsectionLabel(title)
        colors.forEach { item ->
            ColorSwatch(
                name = item.name,
                color = item.color,
                onColor = item.onColor
            )
        }
    }
}

@Composable
private fun ColorSwatch(
    name: String,
    color: Color,
    onColor: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(ArcaneRadius.Small)
                .background(color)
                .border(1.dp, ArcaneTheme.colors.outline.copy(alpha = 0.3f), ArcaneRadius.Small),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Aa",
                style = ArcaneTheme.typography.labelSmall,
                color = onColor
            )
        }
        Text(
            text = name,
            style = ArcaneTheme.typography.labelSmall,
            color = ArcaneTheme.colors.textSecondary
        )
    }
}

// ==================== SURFACE SYSTEM ====================

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SurfaceSystemSection() {
    val colors = ArcaneTheme.colors

    Column(verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)) {
        SubsectionLabel("Surface Containers (M3 Tonal Elevation)")

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small),
            verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
        ) {
            SurfaceLevelBox("Lowest", colors.surfaceContainerLowest, "Inset/recessed")
            SurfaceLevelBox("Low", colors.surfaceContainerLow, "Base level")
            SurfaceLevelBox("Container", colors.surfaceContainer, "Cards (2dp)")
            SurfaceLevelBox("High", colors.surfaceContainerHigh, "Modals (4dp)")
            SurfaceLevelBox("Highest", colors.surfaceContainerHighest, "Dialogs (8dp)")
        }
    }
}

@Composable
private fun SurfaceLevelBox(
    label: String,
    color: Color,
    description: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(ArcaneRadius.Medium)
                .background(color)
                .border(1.dp, ArcaneTheme.colors.outline.copy(alpha = 0.3f), ArcaneRadius.Medium)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = ArcaneTheme.typography.labelSmall,
            color = ArcaneTheme.colors.text
        )
        Text(
            text = description,
            style = ArcaneTheme.typography.labelSmall,
            color = ArcaneTheme.colors.textDisabled
        )
    }
}

// ==================== TEXT COLORS ====================

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TextColorsSection() {
    val colors = ArcaneTheme.colors

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Large),
        verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
    ) {
        TextColorDemo("text", "Primary text for headlines and body", colors.text)
        TextColorDemo("textSecondary", "Supporting text and labels", colors.textSecondary)
        TextColorDemo("textDisabled", "Disabled or hint text", colors.textDisabled)
    }
}

@Composable
private fun TextColorDemo(
    name: String,
    description: String,
    color: Color
) {
    Column(modifier = Modifier.widthIn(max = 200.dp)) {
        Text(
            text = name,
            style = ArcaneTheme.typography.titleMedium,
            color = color
        )
        Text(
            text = description,
            style = ArcaneTheme.typography.bodySmall,
            color = ArcaneTheme.colors.textDisabled
        )
    }
}

// ==================== OUTLINE COLORS ====================

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun OutlineColorsSection() {
    val colors = ArcaneTheme.colors

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Large),
        verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
    ) {
        OutlineDemo("outline", "Borders, input fields", colors.outline)
        OutlineDemo("outlineVariant", "Subtle dividers", colors.outlineVariant)
    }
}

@Composable
private fun OutlineDemo(
    name: String,
    usage: String,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .border(2.dp, color, ArcaneRadius.Medium)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(name, style = ArcaneTheme.typography.labelSmall, color = ArcaneTheme.colors.text)
        Text(usage, style = ArcaneTheme.typography.labelSmall, color = ArcaneTheme.colors.textDisabled)
    }
}

// ==================== STATE LAYERS ====================

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun StateLayersSection() {
    val colors = ArcaneTheme.colors

    Column(verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)) {
        SubsectionLabel("Interactive State Overlays (M3 Standard)")

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Medium),
            verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
        ) {
            StateLayerDemo("Hover", colors.stateLayerHover, "8%")
            StateLayerDemo("Pressed", colors.stateLayerPressed, "12%")
            StateLayerDemo("Focus", colors.stateLayerFocus, "12%")
            StateLayerDemo("Dragged", colors.stateLayerDragged, "16%")
        }
    }
}

@Composable
private fun StateLayerDemo(
    name: String,
    alpha: Float,
    percentage: String
) {
    val colors = ArcaneTheme.colors

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(ArcaneRadius.Medium)
                .background(colors.surfaceContainer)
                .border(1.dp, colors.outline.copy(alpha = 0.3f), ArcaneRadius.Medium),
            contentAlignment = Alignment.Center
        ) {
            // State overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(ArcaneRadius.Medium)
                    .background(colors.primary.copy(alpha = alpha))
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(name, style = ArcaneTheme.typography.labelSmall, color = colors.text)
        Text(percentage, style = ArcaneTheme.typography.labelSmall, color = colors.textDisabled)
    }
}

// ==================== TYPOGRAPHY ====================

@Composable
private fun TypographySection() {
    val typography = ArcaneTheme.typography
    val colors = ArcaneTheme.colors

    Column(verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)) {
        // Display
        SubsectionLabel("Display")
        Text("Display Large (32sp)", style = typography.displayLarge, color = colors.text)
        Text("Display Medium (24sp)", style = typography.displayMedium, color = colors.text)
        Text("Display Small (20sp)", style = typography.displaySmall, color = colors.text)

        Spacer(modifier = Modifier.height(ArcaneSpacing.Small))

        // Headline
        SubsectionLabel("Headline")
        Text("Headline Large (18sp)", style = typography.headlineLarge, color = colors.text)
        Text("Headline Medium (16sp)", style = typography.headlineMedium, color = colors.text)
        Text("Headline Small (24sp)", style = typography.headlineSmall, color = colors.text)

        Spacer(modifier = Modifier.height(ArcaneSpacing.Small))

        // Title
        SubsectionLabel("Title")
        Text("Title Large (22sp)", style = typography.titleLarge, color = colors.text)
        Text("Title Medium (16sp)", style = typography.titleMedium, color = colors.text)
        Text("Title Small (14sp)", style = typography.titleSmall, color = colors.text)

        Spacer(modifier = Modifier.height(ArcaneSpacing.Small))

        // Body
        SubsectionLabel("Body")
        Text("Body Large (16sp)", style = typography.bodyLarge, color = colors.textSecondary)
        Text("Body Medium (14sp)", style = typography.bodyMedium, color = colors.textSecondary)
        Text("Body Small (12sp)", style = typography.bodySmall, color = colors.textSecondary)

        Spacer(modifier = Modifier.height(ArcaneSpacing.Small))

        // Label
        SubsectionLabel("Label")
        Text("Label Large (14sp)", style = typography.labelLarge, color = colors.textSecondary)
        Text("Label Medium (12sp)", style = typography.labelMedium, color = colors.textSecondary)
        Text("Label Small (10sp)", style = typography.labelSmall, color = colors.textSecondary)
    }
}

// ==================== SPACING ====================

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SpacingSection() {
    val colors = ArcaneTheme.colors

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small),
        verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
    ) {
        SpacingDemo("XXS", ArcaneSpacing.XXSmall, "4dp")
        SpacingDemo("XS", ArcaneSpacing.XSmall, "8dp")
        SpacingDemo("S", ArcaneSpacing.Small, "12dp")
        SpacingDemo("M", ArcaneSpacing.Medium, "16dp")
        SpacingDemo("L", ArcaneSpacing.Large, "24dp")
        SpacingDemo("XL", ArcaneSpacing.XLarge, "32dp")
        SpacingDemo("XXL", ArcaneSpacing.XXLarge, "48dp")
    }
}

@Composable
private fun SpacingDemo(
    label: String,
    spacing: androidx.compose.ui.unit.Dp,
    value: String
) {
    val colors = ArcaneTheme.colors

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(spacing.coerceAtLeast(16.dp))
                .background(colors.primary.copy(alpha = 0.3f), ArcaneRadius.Small)
                .border(1.dp, colors.primary, ArcaneRadius.Small)
        )
        Text(label, style = ArcaneTheme.typography.labelSmall, color = colors.text)
        Text(value, style = ArcaneTheme.typography.labelSmall, color = colors.textDisabled)
    }
}

// ==================== RADIUS ====================

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RadiusSection() {
    val colors = ArcaneTheme.colors

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small),
        verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
    ) {
        RadiusDemo("None", ArcaneRadius.None, "0dp")
        RadiusDemo("Small", ArcaneRadius.Small, "4dp")
        RadiusDemo("Medium", ArcaneRadius.Medium, "8dp")
        RadiusDemo("Large", ArcaneRadius.Large, "12dp")
        RadiusDemo("XLarge", ArcaneRadius.ExtraLarge, "16dp")
        RadiusDemo("Full", ArcaneRadius.Full, "50%")
    }
}

@Composable
private fun RadiusDemo(
    label: String,
    shape: androidx.compose.foundation.shape.RoundedCornerShape,
    value: String
) {
    val colors = ArcaneTheme.colors

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(colors.surfaceContainer, shape)
                .border(1.dp, colors.outline, shape)
        )
        Text(label, style = ArcaneTheme.typography.labelSmall, color = colors.text)
        Text(value, style = ArcaneTheme.typography.labelSmall, color = colors.textDisabled)
    }
}

// ==================== EFFECTS ====================

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EffectsSection() {
    val colors = ArcaneTheme.colors

    Column(verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)) {
        SubsectionLabel("Glow Effects (Arcane-specific)")

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Large),
            verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
        ) {
            GlowDemo("glow", colors.glow, "30% alpha")
            GlowDemo("glowStrong", colors.glowStrong, "60% alpha")
        }
    }
}

@Composable
private fun GlowDemo(
    name: String,
    glowColor: Color,
    description: String
) {
    val colors = ArcaneTheme.colors

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            // Glow effect (outer)
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(ArcaneRadius.Medium)
                    .background(glowColor)
            )
            // Inner box
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(ArcaneRadius.Medium)
                    .background(colors.surfaceContainer)
                    .border(1.dp, colors.primary, ArcaneRadius.Medium)
            )
        }
        Text(name, style = ArcaneTheme.typography.labelSmall, color = colors.text)
        Text(description, style = ArcaneTheme.typography.labelSmall, color = colors.textDisabled)
    }
}
