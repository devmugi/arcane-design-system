// catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/screens/NavigationScreen.kt
package io.github.devmugi.arcane.catalog.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.components.navigation.ArcaneBreadcrumb
import io.github.devmugi.arcane.design.components.navigation.ArcaneBreadcrumbs
import io.github.devmugi.arcane.design.components.navigation.ArcanePagination
import io.github.devmugi.arcane.design.components.navigation.ArcaneStep
import io.github.devmugi.arcane.design.components.navigation.ArcaneStepState
import io.github.devmugi.arcane.design.components.navigation.ArcaneStepper
import io.github.devmugi.arcane.design.components.navigation.ArcaneTab
import io.github.devmugi.arcane.design.components.navigation.ArcaneTabs
import io.github.devmugi.arcane.design.foundation.primitives.ArcaneSurface
import io.github.devmugi.arcane.design.foundation.primitives.SurfaceVariant
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun NavigationScreen(onBack: () -> Unit = {}) {
    val typography = ArcaneTheme.typography
    val colors = ArcaneTheme.colors
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(ArcaneSpacing.Medium),
        verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Large)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = colors.primary,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onBack() }
            )
            Text(
                text = "Navigation",
                style = typography.displayMedium,
                color = colors.text
            )
        }

        // Tabs Section
        SectionTitle("Tabs")
        ArcaneSurface(
            variant = SurfaceVariant.Raised,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
            ) {
                var selectedTab by remember { mutableStateOf(0) }
                ArcaneTabs(
                    tabs = listOf(
                        ArcaneTab("Home"),
                        ArcaneTab("Profile"),
                        ArcaneTab("Settings")
                    ),
                    selectedIndex = selectedTab,
                    onTabSelected = { selectedTab = it }
                )
            }
        }

        // Breadcrumbs Section
        SectionTitle("Breadcrumbs")
        ArcaneSurface(
            variant = SurfaceVariant.Raised,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
            ) {
                ArcaneBreadcrumbs(
                    items = listOf(
                        ArcaneBreadcrumb("Home") { },
                        ArcaneBreadcrumb("Products") { },
                        ArcaneBreadcrumb("Categories") { },
                        ArcaneBreadcrumb("Item")  // current - no onClick
                    )
                )
            }
        }

        // Pagination Section
        SectionTitle("Pagination")
        ArcaneSurface(
            variant = SurfaceVariant.Raised,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
            ) {
                var currentPage by remember { mutableStateOf(1) }
                ArcanePagination(
                    currentPage = currentPage,
                    totalPages = 10,
                    onPageSelected = { currentPage = it }
                )
            }
        }

        // Stepper Section
        SectionTitle("Stepper")
        ArcaneSurface(
            variant = SurfaceVariant.Raised,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
            ) {
                ArcaneStepper(
                    steps = listOf(
                        ArcaneStep("Account", state = ArcaneStepState.Completed),
                        ArcaneStep("Profile", state = ArcaneStepState.Completed),
                        ArcaneStep("Preferences", state = ArcaneStepState.Completed),
                        ArcaneStep("Confirmation", "Review details", state = ArcaneStepState.Active),
                        ArcaneStep("Complete", state = ArcaneStepState.Pending)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(ArcaneSpacing.XLarge))
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = ArcaneTheme.typography.headlineLarge,
        color = ArcaneTheme.colors.textSecondary
    )
}
