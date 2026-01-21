package io.github.devmugi.arcane.catalog.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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

@Composable
fun DesignSpecScreen(
    onNavigateToControls: () -> Unit,
    onNavigateToNavigation: () -> Unit,
    onNavigateToDataDisplay: () -> Unit,
    onNavigateToFeedback: () -> Unit
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(ArcaneSpacing.Medium),
        verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Large)
    ) {
        // ========== FOUNDATION SECTION ==========
        FoundationSection()

        // ========== CONTROLS SECTION ==========
        SectionHeader(title = "CONTROLS", onClick = onNavigateToControls)
        ControlsSection()

        // ========== NAVIGATION SECTION ==========
        SectionHeader(title = "NAVIGATION", onClick = onNavigateToNavigation)
        NavigationSection()

        // ========== DATA DISPLAY SECTION ==========
        SectionHeader(title = "DATA DISPLAY", onClick = onNavigateToDataDisplay)
        DataDisplaySection()

        // ========== FEEDBACK SECTION ==========
        SectionHeader(title = "FEEDBACK", onClick = onNavigateToFeedback)
        FeedbackSection()

        Spacer(modifier = Modifier.height(ArcaneSpacing.XLarge))
    }
}

@Composable
private fun SectionHeader(title: String, onClick: () -> Unit) {
    Text(
        text = title,
        style = ArcaneTheme.typography.displaySmall,
        color = ArcaneTheme.colors.primary,
        modifier = Modifier.clickable { onClick() }
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

@Composable
private fun FoundationSection() {
    val colors = ArcaneTheme.colors

    Column(verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Medium)) {
        // Row 1: Surfaces | Elevation | Iconography
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Medium)
        ) {
            // Surfaces
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)) {
                SubsectionLabel("Surfaces")
                Row(horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)) {
                    SurfaceBox("Base", SurfaceVariant.Base)
                    SurfaceBox("Raised", SurfaceVariant.Raised)
                    SurfaceBox("Inset", SurfaceVariant.Inset)
                    SurfaceBox("Pressed", SurfaceVariant.Pressed)
                }
            }

            // Elevation
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)) {
                SubsectionLabel("Elevation Levels")
                Row(horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)) {
                    ElevationBox("L1", 0.2f)
                    ElevationBox("L2", 0.25f)
                    ElevationBox("L3", 0.8f)
                }
            }

            // Iconography
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)) {
                SubsectionLabel("Iconography Rules")
                Row(
                    horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Home, null, tint = colors.primary, modifier = Modifier.size(15.dp))
                        Text("15px", style = ArcaneTheme.typography.labelSmall, color = colors.textSecondary)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Search, null, tint = colors.primary, modifier = Modifier.size(25.dp))
                        Text("25px", style = ArcaneTheme.typography.labelSmall, color = colors.textSecondary)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Settings, null, tint = colors.primary, modifier = Modifier.size(26.dp))
                        Text("26px", style = ArcaneTheme.typography.labelSmall, color = colors.textSecondary)
                    }
                }
            }
        }

        // Row 2: Border Thickness | Radius Scale
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Medium)
        ) {
            // Border Thickness
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)) {
                SubsectionLabel("Border Thickness Tokens")
                Row(horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)) {
                    BorderBox("Title", ArcaneBorder.Title)
                    BorderBox("Medium", ArcaneBorder.Medium)
                    BorderBox("Thick", ArcaneBorder.Thick)
                }
            }

            // Radius Scale
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)) {
                SubsectionLabel("Radius Scale")
                Row(horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)) {
                    RadiusBox("B0", RoundedCornerShape(0.dp))
                    RadiusBox("B4", RoundedCornerShape(4.dp))
                    RadiusBox("B8", RoundedCornerShape(8.dp))
                    RadiusBox("R12", RoundedCornerShape(12.dp))
                    RadiusBox("R15", RoundedCornerShape(18.dp))
                }
            }
        }
    }
}

@Composable
private fun SurfaceBox(label: String, variant: SurfaceVariant) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        ArcaneSurface(
            variant = variant,
            modifier = Modifier.size(50.dp)
        ) {}
        Text(label, style = ArcaneTheme.typography.labelSmall, color = ArcaneTheme.colors.textSecondary)
    }
}

@Composable
private fun ElevationBox(label: String, glowAlpha: Float) {
    val colors = ArcaneTheme.colors
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(colors.surfaceRaised, ArcaneRadius.Small)
                .border(1.dp, colors.primary.copy(alpha = glowAlpha), ArcaneRadius.Small)
        )
        Text(label, style = ArcaneTheme.typography.labelSmall, color = colors.textSecondary)
    }
}

@Composable
private fun BorderBox(label: String, borderWidth: androidx.compose.ui.unit.Dp) {
    val colors = ArcaneTheme.colors
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .border(borderWidth, colors.primary, ArcaneRadius.Small)
        )
        Text(label, style = ArcaneTheme.typography.labelSmall, color = colors.textSecondary)
    }
}

@Composable
private fun RadiusBox(label: String, shape: RoundedCornerShape) {
    val colors = ArcaneTheme.colors
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(colors.surfaceRaised, shape)
                .border(1.dp, colors.border, shape)
        )
        Text(label, style = ArcaneTheme.typography.labelSmall, color = colors.textSecondary)
    }
}

// ==================== CONTROLS SECTION ====================

@Composable
private fun ControlsSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Medium)
    ) {
        // Buttons Column
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)
        ) {
            SubsectionLabel("Buttons")
            ArcaneTextButton("Primary", onClick = {}, style = ArcaneButtonStyle.Primary)
            ArcaneTextButton("Secondary", onClick = {}, style = ArcaneButtonStyle.Secondary)
            ArcaneTextButton("Outlined", onClick = {}, style = ArcaneButtonStyle.Outlined())
            ArcaneTextButton("Loading", onClick = {}, loading = true)
            ArcaneTextButton("Disabled", onClick = {}, enabled = false)
        }

        // Text Field Column
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)
        ) {
            SubsectionLabel("Text Field")
            var text1 by remember { mutableStateOf("") }
            ArcaneTextField(value = text1, onValueChange = { text1 = it }, placeholder = "Placeholder")

            var text2 by remember { mutableStateOf("") }
            ArcaneTextField(value = text2, onValueChange = { text2 = it }, label = "Helper Text", helperText = "Password must be 8+ characters")

            var text3 by remember { mutableStateOf("password123") }
            ArcaneTextField(value = text3, onValueChange = { text3 = it }, label = "Password", isPassword = true)

            ArcaneTextField(value = "Invalid email", onValueChange = {}, label = "Error", errorText = "Invalid email format")
        }

        // Tactile + Switch + Slider Column
        Column(
            modifier = Modifier.weight(1f),
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

@Composable
private fun NavigationSection() {
    Column(verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Medium)) {
        // Row 1: Tabs | Breadcrumbs | Stepper
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Medium)
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)) {
                SubsectionLabel("Tabs")
                var selectedTab by remember { mutableStateOf(0) }
                ArcaneTabs(
                    tabs = listOf(ArcaneTab("Home"), ArcaneTab("Profile"), ArcaneTab("Settings")),
                    selectedIndex = selectedTab,
                    onTabSelected = { selectedTab = it }
                )
            }

            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)) {
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

            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)) {
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
        }

        // Row 2: Pagination
        Column(verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)) {
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

// ==================== DATA DISPLAY SECTION ====================

@Composable
private fun DataDisplaySection() {
    Column(verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Medium)) {
        // Row 1: Cards | List Items | Badges | Avatars | Tooltip
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Medium)
        ) {
            // Cards
            Column(modifier = Modifier.weight(1.5f), verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)) {
                SubsectionLabel("Cards")
                ArcaneCard {
                    ArcaneCardContent(
                        title = "Project Phoenix",
                        description = "Lorem ipsum dolor sit amet."
                    )
                }
            }

            // List Items
            Column(modifier = Modifier.weight(1.5f), verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)) {
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
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)) {
                SubsectionLabel("Badges")
                Row(horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)) {
                    ArcaneBadge("New", style = ArcaneBadgeStyle.Success)
                    ArcaneBadge("Featured", style = ArcaneBadgeStyle.Default)
                    ArcaneBadge("Sale", style = ArcaneBadgeStyle.Warning)
                }
            }

            // Avatars
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)) {
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
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)) {
                SubsectionLabel("Tooltip")
                ArcaneTooltip(text = "Helpful information") {
                    Text("Hover me", color = ArcaneTheme.colors.text)
                }
            }
        }
    }
}

// ==================== FEEDBACK SECTION ====================

@Composable
private fun FeedbackSection() {
    Column(verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Medium)) {
        // Row 1: Progress | Spinner | Skeletons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Medium)
        ) {
            // Progress
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)) {
                SubsectionLabel("Progress")
                Row(horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)) {
                    ArcaneCircularProgress(progress = 0.25f, size = 40.dp)
                    ArcaneCircularProgress(progress = 0.5f, size = 40.dp)
                    ArcaneCircularProgress(progress = 0.75f, size = 40.dp)
                }
                ArcaneLinearProgress(progress = 0.6f, modifier = Modifier.fillMaxWidth())
            }

            // Spinner
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)) {
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
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)) {
                SubsectionLabel("Skeletons")
                ArcaneSkeletonListItem()
            }
        }

        // Row 2: Alert Banners
        Column(verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)) {
            SubsectionLabel("Alert Banners")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    ArcaneAlertBanner(message = "Info message", style = ArcaneAlertStyle.Info)
                }
                Column(modifier = Modifier.weight(1f)) {
                    ArcaneAlertBanner(message = "Success!", style = ArcaneAlertStyle.Success)
                }
                Column(modifier = Modifier.weight(1f)) {
                    ArcaneAlertBanner(message = "Warning", style = ArcaneAlertStyle.Warning)
                }
                Column(modifier = Modifier.weight(1f)) {
                    ArcaneAlertBanner(message = "Error", style = ArcaneAlertStyle.Error)
                }
            }
        }
    }
}
