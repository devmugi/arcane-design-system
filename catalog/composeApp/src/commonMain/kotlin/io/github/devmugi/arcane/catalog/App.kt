package io.github.devmugi.arcane.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.devmugi.arcane.catalog.screens.ChatScreen
import io.github.devmugi.arcane.catalog.screens.ControlsScreen
import io.github.devmugi.arcane.catalog.screens.DataDisplayScreen
import io.github.devmugi.arcane.catalog.screens.DesignSpecScreen
import io.github.devmugi.arcane.catalog.screens.FeedbackScreen
import io.github.devmugi.arcane.catalog.screens.NavigationScreen
import io.github.devmugi.arcane.design.components.navigation.ArcaneTab
import io.github.devmugi.arcane.design.components.navigation.ArcaneTabStyle
import io.github.devmugi.arcane.design.components.navigation.ArcaneTabs
import io.github.devmugi.arcane.design.foundation.theme.ArcaneColors
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

sealed class Screen {
    data object DesignSpec : Screen()
    data object Controls : Screen()
    data object Navigation : Screen()
    data object DataDisplay : Screen()
    data object Feedback : Screen()
    data object Chat : Screen()
}

enum class ThemeVariant {
    ARCANE,
    PERPLEXITY,
    CLAUDE,
    MTG
}

// Convert Screen to tab index for ArcaneTabs
fun Screen.toTabIndex(): Int = when(this) {
    Screen.DesignSpec -> 0
    Screen.Controls -> 1
    Screen.Navigation -> 2
    Screen.DataDisplay -> 3
    Screen.Feedback -> 4
    Screen.Chat -> 5
}

// Convert tab index back to Screen
fun Int.toScreen(): Screen = when(this) {
    0 -> Screen.DesignSpec
    1 -> Screen.Controls
    2 -> Screen.Navigation
    3 -> Screen.DataDisplay
    4 -> Screen.Feedback
    5 -> Screen.Chat
    else -> Screen.DesignSpec
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
    onThemeChange: (ThemeVariant) -> Unit
) {
    Row(
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
            label = "Claude",
            isSelected = currentTheme == ThemeVariant.CLAUDE,
            onClick = { onThemeChange(ThemeVariant.CLAUDE) }
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
private fun CatalogTopBar(
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit,
    currentTheme: ThemeVariant,
    onThemeChange: (ThemeVariant) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(ArcaneTheme.colors.surfaceRaised)
            .padding(horizontal = ArcaneSpacing.Medium, vertical = ArcaneSpacing.Small),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left side: Navigation tabs
        ArcaneTabs(
            tabs = listOf(
                ArcaneTab("Overview"),
                ArcaneTab("Controls"),
                ArcaneTab("Navigation"),
                ArcaneTab("Data Display"),
                ArcaneTab("Feedback"),
                ArcaneTab("Chat")
            ),
            selectedIndex = currentScreen.toTabIndex(),
            onTabSelected = { index -> onScreenSelected(index.toScreen()) },
            style = ArcaneTabStyle.Filled,
            scrollable = true
        )

        // Right side: Theme selector
        ThemeSelector(
            currentTheme = currentTheme,
            onThemeChange = onThemeChange
        )
    }
}

@Composable
fun App() {
    var currentTheme by remember { mutableStateOf(ThemeVariant.ARCANE) }
    val colors = when (currentTheme) {
        ThemeVariant.ARCANE -> ArcaneColors.default()
        ThemeVariant.PERPLEXITY -> ArcaneColors.perplexity()
        ThemeVariant.CLAUDE -> ArcaneColors.claude()
        ThemeVariant.MTG -> ArcaneColors.mtg()
    }

    ArcaneTheme(colors = colors) {
        var currentScreen by remember { mutableStateOf<Screen>(Screen.DesignSpec) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ArcaneTheme.colors.surface)
        ) {
            // Top navigation bar (persistent)
            CatalogTopBar(
                currentScreen = currentScreen,
                onScreenSelected = { currentScreen = it },
                currentTheme = currentTheme,
                onThemeChange = { currentTheme = it }
            )

            // Screen content area
            Box(modifier = Modifier.weight(1f)) {
                when (currentScreen) {
                    Screen.DesignSpec -> DesignSpecScreen()
                    Screen.Controls -> ControlsScreen()
                    Screen.Navigation -> NavigationScreen()
                    Screen.DataDisplay -> DataDisplayScreen()
                    Screen.Feedback -> FeedbackScreen()
                    Screen.Chat -> ChatScreen()
                }
            }
        }
    }
}
