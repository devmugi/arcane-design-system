package io.github.devmugi.arcane.catalog

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.window.core.layout.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import io.github.devmugi.arcane.catalog.components.PrChangesEmptyState
import io.github.devmugi.arcane.catalog.prchanges.PrChangesConfig
import io.github.devmugi.arcane.catalog.prchanges.loadPrChangesManifest
import io.github.devmugi.arcane.catalog.screens.ControlsScreen
import io.github.devmugi.arcane.catalog.screens.DataDisplayScreen
import io.github.devmugi.arcane.catalog.screens.DesignSpecScreen
import io.github.devmugi.arcane.catalog.screens.FeedbackScreen
import io.github.devmugi.arcane.catalog.screens.NavigationScreen
import io.github.devmugi.arcane.catalog.screens.ThemeScreen
import io.github.devmugi.arcane.design.foundation.theme.ArcaneColors
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

sealed class Screen(
    val displayName: String,
    val catalogName: String,
    val icon: ImageVector
) {
    data object Theme : Screen("Theme", "Theme", Icons.Default.Edit)
    data object DesignSpec : Screen("Overview", "Design Spec", Icons.Default.Info)
    data object Controls : Screen("Controls", "Controls", Icons.Default.Build)
    data object Navigation : Screen("Navigation", "Navigation", Icons.Default.Place)
    data object DataDisplay : Screen("Data Display", "Data Display", Icons.Default.List)
    data object Feedback : Screen("Feedback", "Feedback", Icons.Default.Notifications)

    companion object {
        fun all(): List<Screen> = listOf(
            Theme,
            DesignSpec,
            Controls,
            Navigation,
            DataDisplay,
            Feedback
        )

        fun fromCatalogName(name: String): Screen? = when (name) {
            "Theme" -> Theme
            "Design Spec" -> DesignSpec
            "Controls" -> Controls
            "Navigation" -> Navigation
            "Data Display" -> DataDisplay
            "Feedback" -> Feedback
            else -> null
        }
    }
}

enum class ThemeVariant {
    ARCANE,
    PERPLEXITY,
    P2D,
    P2L,
    CLAUDE_D,
    CLAUDE_L,
    MTG
}

@Composable
private fun ThemeOption(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Text(
        text = label,
        style = ArcaneTheme.typography.labelMedium,
        color = if (isSelected) ArcaneTheme.colors.primary else ArcaneTheme.colors.textSecondary,
        modifier = Modifier.clickable { onClick() }
    )
}

@Composable
private fun ThemeSelector(
    currentTheme: ThemeVariant,
    onThemeChange: (ThemeVariant) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ThemeOption(
            label = "Arcane",
            isSelected = currentTheme == ThemeVariant.ARCANE,
            onClick = { onThemeChange(ThemeVariant.ARCANE) }
        )
        Text("|", style = ArcaneTheme.typography.labelMedium, color = ArcaneTheme.colors.textDisabled)

        ThemeOption(
            label = "Perplexity",
            isSelected = currentTheme == ThemeVariant.PERPLEXITY,
            onClick = { onThemeChange(ThemeVariant.PERPLEXITY) }
        )
        Text("|", style = ArcaneTheme.typography.labelMedium, color = ArcaneTheme.colors.textDisabled)

        ThemeOption(
            label = "P2D",
            isSelected = currentTheme == ThemeVariant.P2D,
            onClick = { onThemeChange(ThemeVariant.P2D) }
        )
        Text("|", style = ArcaneTheme.typography.labelMedium, color = ArcaneTheme.colors.textDisabled)

        ThemeOption(
            label = "P2L",
            isSelected = currentTheme == ThemeVariant.P2L,
            onClick = { onThemeChange(ThemeVariant.P2L) }
        )
        Text("|", style = ArcaneTheme.typography.labelMedium, color = ArcaneTheme.colors.textDisabled)

        ThemeOption(
            label = "ClaudeD",
            isSelected = currentTheme == ThemeVariant.CLAUDE_D,
            onClick = { onThemeChange(ThemeVariant.CLAUDE_D) }
        )
        Text("|", style = ArcaneTheme.typography.labelMedium, color = ArcaneTheme.colors.textDisabled)

        ThemeOption(
            label = "ClaudeL",
            isSelected = currentTheme == ThemeVariant.CLAUDE_L,
            onClick = { onThemeChange(ThemeVariant.CLAUDE_L) }
        )
        Text("|", style = ArcaneTheme.typography.labelMedium, color = ArcaneTheme.colors.textDisabled)

        ThemeOption(
            label = "MTG",
            isSelected = currentTheme == ThemeVariant.MTG,
            onClick = { onThemeChange(ThemeVariant.MTG) }
        )
    }
}

@Composable
private fun CatalogNavigationRail(
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit,
    availableScreens: List<Screen>,
    modifier: Modifier = Modifier
) {
    NavigationRail(
        modifier = modifier,
        containerColor = ArcaneTheme.colors.surfaceContainerLow
    ) {
        Spacer(Modifier.height(ArcaneSpacing.Medium))
        availableScreens.forEach { screen ->
            NavigationRailItem(
                selected = currentScreen == screen,
                onClick = { onScreenSelected(screen) },
                icon = { Icon(screen.icon, contentDescription = screen.displayName) },
                label = { Text(screen.displayName, style = ArcaneTheme.typography.labelSmall) },
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = ArcaneTheme.colors.primary,
                    selectedTextColor = ArcaneTheme.colors.primary,
                    indicatorColor = ArcaneTheme.colors.secondaryContainer,
                    unselectedIconColor = ArcaneTheme.colors.textSecondary,
                    unselectedTextColor = ArcaneTheme.colors.textSecondary
                )
            )
        }
    }
}

@Composable
private fun CatalogNavigationBar(
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit,
    availableScreens: List<Screen>,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = ArcaneTheme.colors.surfaceContainerLow
    ) {
        availableScreens.forEach { screen ->
            NavigationBarItem(
                selected = currentScreen == screen,
                onClick = { onScreenSelected(screen) },
                icon = { Icon(screen.icon, contentDescription = screen.displayName) },
                label = { Text(screen.displayName, style = ArcaneTheme.typography.labelSmall) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = ArcaneTheme.colors.primary,
                    selectedTextColor = ArcaneTheme.colors.primary,
                    indicatorColor = ArcaneTheme.colors.secondaryContainer,
                    unselectedIconColor = ArcaneTheme.colors.textSecondary,
                    unselectedTextColor = ArcaneTheme.colors.textSecondary
                )
            )
        }
    }
}

@Composable
private fun CatalogTopBar(
    currentTheme: ThemeVariant,
    onThemeChange: (ThemeVariant) -> Unit,
    title: String? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(ArcaneTheme.colors.surfaceContainer)
            .padding(horizontal = ArcaneSpacing.Medium, vertical = ArcaneSpacing.Small),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left side: Title (optional)
        if (title != null) {
            Text(
                text = title,
                style = ArcaneTheme.typography.titleLarge,
                color = ArcaneTheme.colors.text
            )
        } else {
            Spacer(Modifier)
        }

        // Right side: Theme selector
        ThemeSelector(
            currentTheme = currentTheme,
            onThemeChange = onThemeChange
        )
    }
}

@Composable
private fun ScreenContent(
    currentScreen: Screen,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        when (currentScreen) {
            Screen.Theme -> ThemeScreen(windowSizeClass)
            Screen.DesignSpec -> DesignSpecScreen(windowSizeClass)
            Screen.Controls -> ControlsScreen(windowSizeClass)
            Screen.Navigation -> NavigationScreen(windowSizeClass)
            Screen.DataDisplay -> DataDisplayScreen(windowSizeClass)
            Screen.Feedback -> FeedbackScreen(windowSizeClass)
        }
    }
}

@Composable
fun App(isFilteredMode: Boolean = false) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    var currentTheme by remember { mutableStateOf(ThemeVariant.ARCANE) }
    val colors = when (currentTheme) {
        ThemeVariant.ARCANE -> ArcaneColors.default()
        ThemeVariant.PERPLEXITY -> ArcaneColors.perplexity()
        ThemeVariant.P2D -> ArcaneColors.p2d()
        ThemeVariant.P2L -> ArcaneColors.p2l()
        ThemeVariant.CLAUDE_D -> ArcaneColors.claudeD()
        ThemeVariant.CLAUDE_L -> ArcaneColors.claudeL()
        ThemeVariant.MTG -> ArcaneColors.mtg()
    }

    // Load PR changes manifest on startup
    LaunchedEffect(Unit) {
        if (isFilteredMode) {
            val manifest = loadPrChangesManifest()
            manifest?.let { PrChangesConfig.loadManifest(it) }
        }
    }

    // Determine available screens based on mode
    val availableScreens = if (isFilteredMode && PrChangesConfig.isFilteredMode) {
        PrChangesConfig.affectedScreens.mapNotNull { Screen.fromCatalogName(it) }
    } else {
        Screen.all()
    }

    // Determine if we should use rail navigation (medium width and above)
    val useNavigationRail = windowSizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND
    )

    ArcaneTheme(colors = colors) {
        var currentScreen by remember(availableScreens) {
            mutableStateOf(availableScreens.firstOrNull() ?: Screen.DesignSpec)
        }

        // Show empty state if filtered mode but no affected screens
        if (isFilteredMode && !PrChangesConfig.isFilteredMode) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ArcaneTheme.colors.surfaceContainerLow)
            ) {
                PrChangesEmptyState(
                    onViewFullCatalog = {
                        navigateToFullCatalog()
                    }
                )
            }
        } else if (useNavigationRail) {
            // Expanded layout: NavigationRail on left
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ArcaneTheme.colors.surfaceContainerLow)
            ) {
                CatalogNavigationRail(
                    currentScreen = currentScreen,
                    onScreenSelected = { currentScreen = it },
                    availableScreens = availableScreens
                )
                Column(modifier = Modifier.weight(1f)) {
                    CatalogTopBar(
                        currentTheme = currentTheme,
                        onThemeChange = { currentTheme = it },
                        title = currentScreen.displayName
                    )
                    ScreenContent(
                        currentScreen = currentScreen,
                        windowSizeClass = windowSizeClass,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        } else {
            // Compact layout: NavigationBar at bottom
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ArcaneTheme.colors.surfaceContainerLow)
            ) {
                CatalogTopBar(
                    currentTheme = currentTheme,
                    onThemeChange = { currentTheme = it },
                    title = currentScreen.displayName
                )
                ScreenContent(
                    currentScreen = currentScreen,
                    windowSizeClass = windowSizeClass,
                    modifier = Modifier.weight(1f)
                )
                CatalogNavigationBar(
                    currentScreen = currentScreen,
                    onScreenSelected = { currentScreen = it },
                    availableScreens = availableScreens
                )
            }
        }
    }
}

expect fun navigateToFullCatalog()
