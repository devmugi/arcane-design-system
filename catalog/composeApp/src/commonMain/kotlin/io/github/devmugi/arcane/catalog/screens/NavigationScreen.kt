// catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/screens/NavigationScreen.kt
package io.github.devmugi.arcane.catalog.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
fun NavigationScreen() {
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
        Text(
            text = "Navigation",
            style = typography.displayMedium,
            color = colors.text
        )

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
