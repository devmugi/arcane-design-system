package io.github.devmugi.arcane.catalog.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.components.controls.ArcaneButtonStyle
import io.github.devmugi.arcane.design.components.controls.ArcaneCheckbox
import io.github.devmugi.arcane.design.components.controls.ArcaneRadioButton
import io.github.devmugi.arcane.design.components.controls.ArcaneSlider
import io.github.devmugi.arcane.design.components.controls.ArcaneSwitch
import io.github.devmugi.arcane.design.components.controls.ArcaneTextButton
import io.github.devmugi.arcane.design.components.controls.ArcaneTextField
import io.github.devmugi.arcane.design.components.display.ArcaneAvatar
import io.github.devmugi.arcane.design.components.display.ArcaneAvatarData
import io.github.devmugi.arcane.design.components.display.ArcaneAvatarGroup
import io.github.devmugi.arcane.design.components.display.ArcaneAvatarSize
import io.github.devmugi.arcane.design.components.display.ArcaneBadge
import io.github.devmugi.arcane.design.components.display.ArcaneBadgeStyle
import io.github.devmugi.arcane.design.components.display.ArcaneCard
import io.github.devmugi.arcane.design.components.display.ArcaneCardContent
import io.github.devmugi.arcane.design.components.display.ArcaneListItem
import io.github.devmugi.arcane.design.components.display.ArcaneTooltip
import io.github.devmugi.arcane.design.components.feedback.ArcaneAlertBanner
import io.github.devmugi.arcane.design.components.feedback.ArcaneAlertStyle
import io.github.devmugi.arcane.design.components.feedback.ArcaneCircularProgress
import io.github.devmugi.arcane.design.components.feedback.ArcaneLinearProgress
import io.github.devmugi.arcane.design.components.feedback.ArcaneSkeletonCard
import io.github.devmugi.arcane.design.components.feedback.ArcaneSkeletonListItem
import io.github.devmugi.arcane.design.components.feedback.ArcaneSpinner
import io.github.devmugi.arcane.design.components.feedback.ArcaneSpinnerSize
import io.github.devmugi.arcane.design.components.navigation.ArcaneBreadcrumb
import io.github.devmugi.arcane.design.components.navigation.ArcaneBreadcrumbs
import io.github.devmugi.arcane.design.components.navigation.ArcanePagination
import io.github.devmugi.arcane.design.components.navigation.ArcaneStepper
import io.github.devmugi.arcane.design.components.navigation.ArcaneStep
import io.github.devmugi.arcane.design.components.navigation.ArcaneStepState
import io.github.devmugi.arcane.design.components.navigation.ArcaneTab
import io.github.devmugi.arcane.design.components.navigation.ArcaneTabs
import io.github.devmugi.arcane.design.foundation.primitives.ArcaneSurface
import io.github.devmugi.arcane.design.foundation.primitives.SurfaceVariant
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneBorder
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing
import androidx.window.core.layout.WindowSizeClass

@Composable
fun DesignSpecScreen(windowSizeClass: WindowSizeClass? = null) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography
    val scrollState = rememberScrollState()

    // Determine layout columns based on window size
    val isExpanded = windowSizeClass?.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
    ) == true
    val isMedium = windowSizeClass?.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND
    ) == true

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(ArcaneSpacing.Medium),
        verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Large)
    ) {
        // ========== FOUNDATION SECTION ==========
        FoundationSection(isExpanded = isExpanded, isMedium = isMedium)

        // ========== CONTROLS SECTION ==========
        SectionHeader(title = "CONTROLS")
        ControlsSection(isExpanded = isExpanded, isMedium = isMedium)

        // ========== NAVIGATION SECTION ==========
        SectionHeader(title = "NAVIGATION")
        NavigationSection(isExpanded = isExpanded, isMedium = isMedium)

        // ========== DATA DISPLAY SECTION ==========
        SectionHeader(title = "DATA DISPLAY")
        DataDisplaySection(isExpanded = isExpanded, isMedium = isMedium)

        // ========== FEEDBACK SECTION ==========
        SectionHeader(title = "FEEDBACK")
        FeedbackSection(isExpanded = isExpanded, isMedium = isMedium)

        Spacer(modifier = Modifier.height(ArcaneSpacing.XLarge))
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = ArcaneTheme.typography.displaySmall,
        color = ArcaneTheme.colors.primary
    )
}

@Composable
private fun SubsectionLabel(text: String) {
    Text(
        text = text,
        style = ArcaneTheme.typography.labelMedium,
        color = ArcaneTheme.colors.textSecondary
    )
}

// ==================== FOUNDATION SECTION ====================

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FoundationSection(isExpanded: Boolean, isMedium: Boolean) {
    val colors = ArcaneTheme.colors

    Column(verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Large)) {
        // Responsive grid of foundation items using FlowRow
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Large),
            verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Large)
        ) {
            // Surfaces (Material 3 levels)
            FoundationCard(
                title = "Surfaces",
                minWidth = if (isExpanded) 200 else 280
            ) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small),
                    verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
                ) {
                    SurfaceBox("Lowest", SurfaceVariant.ContainerLowest)
                    SurfaceBox("Low", SurfaceVariant.ContainerLow)
                    SurfaceBox("Container", SurfaceVariant.Container)
                    SurfaceBox("High", SurfaceVariant.ContainerHigh)
                    SurfaceBox("Highest", SurfaceVariant.ContainerHighest)
                }
            }

            // Elevation - 3D stacked visualization
            FoundationCard(
                title = "Elevation Levels",
                minWidth = if (isExpanded) 240 else 300
            ) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Medium),
                    verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
                ) {
                    ElevationStack(
                        label = "Low",
                        spec = "(3px, 0, 4px)",
                        layers = 2,
                        shadowAlpha = 0.2f
                    )
                    ElevationStack(
                        label = "Mid",
                        spec = "(4px, 0, 8px)",
                        layers = 3,
                        shadowAlpha = 0.25f
                    )
                    ElevationStack(
                        label = "High",
                        spec = "(8px, 0, 16px)",
                        shadowAlpha = 0.3f,
                        layers = 4
                    )
                }
            }

            // Iconography
            FoundationCard(
                title = "Iconography",
                minWidth = 180
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)) {
                    IconRow(size = 18.dp, label = "18px", colors = colors)
                    IconRow(size = 22.dp, label = "22px", colors = colors)
                    IconRow(size = 26.dp, label = "26px", colors = colors)
                }
            }

            // Border Thickness
            FoundationCard(
                title = "Border Thickness",
                minWidth = 200
            ) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small),
                    verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
                ) {
                    BorderBox("Thin", ArcaneBorder.Thin, "(1px)")
                    BorderBox("Medium", ArcaneBorder.Medium, "(2px)")
                    BorderBox("Thick", ArcaneBorder.Thick, "(3px)")
                }
            }

            // Radius Scale
            FoundationCard(
                title = "Radius Scale",
                minWidth = if (isExpanded) 280 else 320
            ) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small),
                    verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
                ) {
                    RadiusBox("R0", RoundedCornerShape(0.dp))
                    RadiusBox("R4", RoundedCornerShape(4.dp), "(4px)")
                    RadiusBox("R8", RoundedCornerShape(8.dp), "(8px)")
                    RadiusBox("R12", RoundedCornerShape(12.dp), "(12px)")
                    RadiusBox("R16", RoundedCornerShape(16.dp), "(16px)")
                }
            }
        }
    }
}

@Composable
private fun FoundationCard(
    title: String,
    minWidth: Int,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.widthIn(min = minWidth.dp),
        verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
    ) {
        SubsectionLabel(title)
        content()
    }
}

@Composable
private fun SurfaceBox(label: String, variant: SurfaceVariant) {
    val colors = ArcaneTheme.colors
    val surfaceSize = 70.dp
    val innerSize = 50.dp

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier.size(surfaceSize),
            contentAlignment = Alignment.Center
        ) {
            @Suppress("DEPRECATION")
            when (variant) {
                SurfaceVariant.ContainerLowest, SurfaceVariant.Inset -> {
                    // Inner shadow effect - recessed/depressed appearance (darkest)
                    Box(
                        modifier = Modifier
                            .size(innerSize)
                            .clip(ArcaneRadius.Medium)
                            .background(colors.surfaceContainerLowest)
                            .drawBehind {
                                // Draw inner shadow gradient - darker edges fading to center
                                drawRect(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.4f)
                                        ),
                                        center = Offset(size.width / 2, size.height / 2),
                                        radius = size.minDimension / 1.5f
                                    )
                                )
                            }
                            .border(1.dp, colors.border.copy(alpha = 0.3f), ArcaneRadius.Medium)
                    )
                }
                SurfaceVariant.ContainerLow, SurfaceVariant.Base -> {
                    // Base level - flat surface with subtle neutral border only
                    Box(
                        modifier = Modifier
                            .size(innerSize)
                            .background(colors.surfaceContainerLow, ArcaneRadius.Medium)
                            .border(1.dp, colors.border.copy(alpha = 0.3f), ArcaneRadius.Medium)
                    )
                }
                SurfaceVariant.Container, SurfaceVariant.Raised -> {
                    // Standard elevation - neutral shadow (2dp)
                    Box(
                        modifier = Modifier
                            .size(innerSize)
                            .shadow(2.dp, ArcaneRadius.Medium, ambientColor = Color.Black.copy(alpha = 0.15f))
                            .background(colors.surfaceContainer, ArcaneRadius.Medium)
                            .border(1.dp, colors.border.copy(alpha = 0.3f), ArcaneRadius.Medium)
                    )
                }
                SurfaceVariant.ContainerHigh -> {
                    // High elevation - neutral shadow (4dp)
                    Box(
                        modifier = Modifier
                            .size(innerSize)
                            .shadow(4.dp, ArcaneRadius.Medium, ambientColor = Color.Black.copy(alpha = 0.15f))
                            .background(colors.surfaceContainerHigh, ArcaneRadius.Medium)
                            .border(1.dp, colors.border.copy(alpha = 0.3f), ArcaneRadius.Medium)
                    )
                }
                SurfaceVariant.ContainerHighest -> {
                    // Maximum elevation - neutral shadow (8dp)
                    Box(
                        modifier = Modifier
                            .size(innerSize)
                            .shadow(8.dp, ArcaneRadius.Medium, ambientColor = Color.Black.copy(alpha = 0.15f))
                            .background(colors.surfaceContainerHighest, ArcaneRadius.Medium)
                            .border(1.dp, colors.border.copy(alpha = 0.3f), ArcaneRadius.Medium)
                    )
                }
            }
        }
        Text(label, style = ArcaneTheme.typography.labelSmall, color = colors.textSecondary)
    }
}

@Composable
private fun ElevationStack(
    label: String,
    spec: String,
    layers: Int,
    shadowAlpha: Float
) {
    val colors = ArcaneTheme.colors
    val layerSize = 45.dp
    val layerOffset = 4.dp

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(layerSize + (layerOffset * layers))
                .padding(top = layerOffset * (layers - 1)),
            contentAlignment = Alignment.TopStart
        ) {
            // Draw stacked layers from back to front
            for (i in (layers - 1) downTo 0) {
                val offset = layerOffset * i
                Box(
                    modifier = Modifier
                        .offset(x = offset / 2, y = -offset)
                        .size(layerSize)
                        .background(
                            if (i == 0) colors.surfaceContainer else colors.surfaceContainerLow.copy(alpha = 0.8f - (i * 0.15f)),
                            ArcaneRadius.Small
                        )
                        .border(
                            1.dp,
                            colors.border.copy(alpha = if (i == 0) 0.5f else 0.2f),
                            ArcaneRadius.Small
                        )
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, style = ArcaneTheme.typography.labelSmall, color = colors.textSecondary)
        Text(spec, style = ArcaneTheme.typography.labelSmall, color = colors.textDisabled)
    }
}

@Composable
private fun BorderBox(label: String, borderWidth: androidx.compose.ui.unit.Dp, pixelValue: String? = null) {
    val colors = ArcaneTheme.colors
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .border(borderWidth, colors.primary, ArcaneRadius.Small)
        )
        Text(label, style = ArcaneTheme.typography.labelSmall, color = colors.textSecondary)
        if (pixelValue != null) {
            Text(pixelValue, style = ArcaneTheme.typography.labelSmall, color = colors.textDisabled)
        }
    }
}

@Composable
private fun RadiusBox(label: String, shape: RoundedCornerShape, pixelValue: String? = null) {
    val colors = ArcaneTheme.colors
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(colors.surfaceContainer, shape)
                .border(1.dp, colors.border, shape)
        )
        Text(label, style = ArcaneTheme.typography.labelSmall, color = colors.textSecondary)
        if (pixelValue != null) {
            Text(pixelValue, style = ArcaneTheme.typography.labelSmall, color = colors.textDisabled)
        }
    }
}

@Composable
private fun IconRow(size: androidx.compose.ui.unit.Dp, label: String, colors: io.github.devmugi.arcane.design.foundation.theme.ArcaneColors) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = ArcaneTheme.typography.labelSmall, color = colors.textSecondary, modifier = Modifier.width(32.dp))
        Icon(Icons.Default.Home, null, tint = colors.primary, modifier = Modifier.size(size))
        Icon(Icons.Default.Search, null, tint = colors.primary, modifier = Modifier.size(size))
        Icon(Icons.Default.Settings, null, tint = colors.primary, modifier = Modifier.size(size))
        Icon(Icons.Default.Person, null, tint = colors.primary, modifier = Modifier.size(size))
    }
}

// ==================== CONTROLS SECTION ====================

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ControlsSection(isExpanded: Boolean, isMedium: Boolean) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Large),
        verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Large)
    ) {
        // Buttons Column
        Column(
            modifier = Modifier.widthIn(min = 160.dp, max = 220.dp),
            verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)
        ) {
            SubsectionLabel("Buttons")
            ArcaneTextButton("Filled", onClick = {}, style = ArcaneButtonStyle.Filled(), modifier = Modifier.fillMaxWidth())
            ArcaneTextButton("Tonal", onClick = {}, style = ArcaneButtonStyle.Tonal(), modifier = Modifier.fillMaxWidth())
            ArcaneTextButton("Outlined", onClick = {}, style = ArcaneButtonStyle.Outlined(), modifier = Modifier.fillMaxWidth())
            ArcaneTextButton("Loading", onClick = {}, loading = true, modifier = Modifier.fillMaxWidth())
            ArcaneTextButton("Disabled", onClick = {}, enabled = false, modifier = Modifier.fillMaxWidth())
        }

        // Text Field Column
        Column(
            modifier = Modifier.widthIn(min = 200.dp, max = 300.dp),
            verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)
        ) {
            SubsectionLabel("Text Field")
            var text1 by remember { mutableStateOf("") }
            ArcaneTextField(value = text1, onValueChange = { text1 = it }, placeholder = "Placeholder")

            var text2 by remember { mutableStateOf("") }
            ArcaneTextField(value = text2, onValueChange = { text2 = it }, label = "With Helper", helperText = "8+ characters")

            var text3 by remember { mutableStateOf("password123") }
            ArcaneTextField(value = text3, onValueChange = { text3 = it }, label = "Password", isPassword = true)

            ArcaneTextField(value = "Invalid", onValueChange = {}, label = "Error", errorText = "Invalid format")
        }

        // Tactile + Switch + Slider Column
        Column(
            modifier = Modifier.widthIn(min = 180.dp),
            verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
        ) {
            // Tactile
            SubsectionLabel("Tactile")
            var checked by remember { mutableStateOf(true) }
            ArcaneCheckbox(checked = checked, onCheckedChange = { checked = it }, label = "Checkbox")

            var radioSelected by remember { mutableStateOf(true) }
            ArcaneRadioButton(selected = radioSelected, onClick = { radioSelected = !radioSelected }, label = "Radio Button")

            // Switch
            Spacer(modifier = Modifier.height(ArcaneSpacing.XSmall))
            SubsectionLabel("Switch")
            Row(horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Medium)) {
                var switchOn by remember { mutableStateOf(true) }
                ArcaneSwitch(checked = switchOn, onCheckedChange = { switchOn = it }, label = "ON")
                ArcaneSwitch(checked = false, onCheckedChange = {}, label = "OFF")
            }

            // Slider
            Spacer(modifier = Modifier.height(ArcaneSpacing.XSmall))
            SubsectionLabel("Slider")
            var sliderValue by remember { mutableStateOf(0.5f) }
            ArcaneSlider(value = sliderValue, onValueChange = { sliderValue = it })
        }
    }
}

// ==================== NAVIGATION SECTION ====================

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun NavigationSection(isExpanded: Boolean, isMedium: Boolean) {
    Column(verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Medium)) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Large),
            verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Large)
        ) {
            // Tabs
            Column(
                modifier = Modifier.widthIn(min = 200.dp),
                verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)
            ) {
                SubsectionLabel("Tabs")
                var selectedTab by remember { mutableStateOf(0) }
                ArcaneTabs(
                    tabs = listOf(ArcaneTab("Home"), ArcaneTab("Profile"), ArcaneTab("Settings")),
                    selectedIndex = selectedTab,
                    onTabSelected = { selectedTab = it }
                )
            }

            // Breadcrumbs
            Column(
                modifier = Modifier.widthIn(min = 220.dp),
                verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)
            ) {
                SubsectionLabel("Breadcrumbs")
                ArcaneBreadcrumbs(
                    items = listOf(
                        ArcaneBreadcrumb("Home") { },
                        ArcaneBreadcrumb("Products") { },
                        ArcaneBreadcrumb("Categories") { },
                        ArcaneBreadcrumb("Item")
                    )
                )
            }

            // Stepper
            Column(
                modifier = Modifier.widthIn(min = 200.dp),
                verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)
            ) {
                SubsectionLabel("5-Step Process")
                ArcaneStepper(
                    steps = listOf(
                        ArcaneStep("1", state = ArcaneStepState.Completed),
                        ArcaneStep("2", state = ArcaneStepState.Completed),
                        ArcaneStep("3", state = ArcaneStepState.Active),
                        ArcaneStep("4", state = ArcaneStepState.Pending),
                        ArcaneStep("5", state = ArcaneStepState.Pending)
                    )
                )
            }

            // Pagination
            Column(
                modifier = Modifier.widthIn(min = 200.dp),
                verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)
            ) {
                SubsectionLabel("Pagination")
                var currentPage by remember { mutableStateOf(1) }
                ArcanePagination(
                    currentPage = currentPage,
                    totalPages = 10,
                    onPageSelected = { currentPage = it }
                )
            }
        }
    }
}

// ==================== DATA DISPLAY SECTION ====================

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DataDisplaySection(isExpanded: Boolean, isMedium: Boolean) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Large),
        verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Large)
    ) {
        // Cards
        Column(
            modifier = Modifier.widthIn(min = 200.dp, max = 300.dp),
            verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)
        ) {
            SubsectionLabel("Cards")
            ArcaneCard {
                ArcaneCardContent(
                    title = "Project Phoenix",
                    description = "Lorem ipsum dolor sit amet."
                )
            }
        }

        // List Items
        Column(
            modifier = Modifier.widthIn(min = 200.dp, max = 300.dp),
            verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)
        ) {
            SubsectionLabel("List Items")
            ArcaneListItem(
                headlineText = "Meeting Tomorrow",
                supportingText = "10:00 AM - Room A"
            )
            ArcaneListItem(
                headlineText = "Project Review",
                supportingText = "2:00 PM - Virtual"
            )
        }

        // Badges
        Column(
            modifier = Modifier.widthIn(min = 160.dp),
            verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)
        ) {
            SubsectionLabel("Badges")
            FlowRow(horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)) {
                ArcaneBadge("New", style = ArcaneBadgeStyle.Success)
                ArcaneBadge("Featured", style = ArcaneBadgeStyle.Default)
                ArcaneBadge("Sale", style = ArcaneBadgeStyle.Warning)
            }
        }

        // Avatars
        Column(
            modifier = Modifier.widthIn(min = 140.dp),
            verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)
        ) {
            SubsectionLabel("Avatars")
            ArcaneAvatarGroup(
                avatars = listOf(
                    ArcaneAvatarData(name = "Alice"),
                    ArcaneAvatarData(name = "Bob"),
                    ArcaneAvatarData(name = "Charlie"),
                    ArcaneAvatarData(name = "Diana")
                ),
                maxVisible = 3
            )
        }

        // Tooltip
        Column(
            modifier = Modifier.widthIn(min = 120.dp),
            verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)
        ) {
            SubsectionLabel("Tooltip")
            ArcaneTooltip(text = "Helpful information") {
                Text("Hover me", color = ArcaneTheme.colors.text)
            }
        }
    }
}

// ==================== FEEDBACK SECTION ====================

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FeedbackSection(isExpanded: Boolean, isMedium: Boolean) {
    Column(verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Medium)) {
        // Row 1: Progress | Spinner | Skeletons
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Large),
            verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Large)
        ) {
            // Progress
            Column(
                modifier = Modifier.widthIn(min = 180.dp, max = 280.dp),
                verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)
            ) {
                SubsectionLabel("Progress")
                Row(horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)) {
                    ArcaneCircularProgress(progress = 0.25f, size = 40.dp)
                    ArcaneCircularProgress(progress = 0.5f, size = 40.dp)
                    ArcaneCircularProgress(progress = 0.75f, size = 40.dp)
                }
                ArcaneLinearProgress(progress = 0.6f, modifier = Modifier.fillMaxWidth())
            }

            // Spinner
            Column(
                modifier = Modifier.widthIn(min = 140.dp),
                verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)
            ) {
                SubsectionLabel("Spinner")
                Row(
                    horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Medium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ArcaneSpinner(size = ArcaneSpinnerSize.Small)
                    ArcaneSpinner(size = ArcaneSpinnerSize.Medium)
                    ArcaneSpinner(size = ArcaneSpinnerSize.Large)
                }
            }

            // Skeletons
            Column(
                modifier = Modifier.widthIn(min = 200.dp, max = 300.dp),
                verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)
            ) {
                SubsectionLabel("Skeletons")
                ArcaneSkeletonListItem()
            }
        }

        // Alert Banners - use FlowRow for responsive wrapping
        SubsectionLabel("Alert Banners")
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small),
            verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
        ) {
            Box(modifier = Modifier.widthIn(min = 140.dp, max = 200.dp)) {
                ArcaneAlertBanner(message = "Info message", style = ArcaneAlertStyle.Info)
            }
            Box(modifier = Modifier.widthIn(min = 120.dp, max = 180.dp)) {
                ArcaneAlertBanner(message = "Success!", style = ArcaneAlertStyle.Success)
            }
            Box(modifier = Modifier.widthIn(min = 120.dp, max = 180.dp)) {
                ArcaneAlertBanner(message = "Warning", style = ArcaneAlertStyle.Warning)
            }
            Box(modifier = Modifier.widthIn(min = 100.dp, max = 160.dp)) {
                ArcaneAlertBanner(message = "Error", style = ArcaneAlertStyle.Error)
            }
        }
    }
}
