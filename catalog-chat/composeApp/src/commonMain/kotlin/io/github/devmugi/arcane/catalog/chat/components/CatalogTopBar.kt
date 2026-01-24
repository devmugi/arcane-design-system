package io.github.devmugi.arcane.catalog.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.window.core.layout.WindowSizeClass
import io.github.devmugi.arcane.design.components.feedback.ArcaneDropdownMenu
import io.github.devmugi.arcane.design.components.feedback.ArcaneDropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.components.controls.ArcaneButton
import io.github.devmugi.arcane.design.components.controls.ArcaneButtonStyle
import io.github.devmugi.arcane.design.components.navigation.ArcaneTab
import io.github.devmugi.arcane.design.components.navigation.ArcaneTabStyle
import io.github.devmugi.arcane.design.components.navigation.ArcaneTabs
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

enum class CatalogTab(val displayName: String, val icon: ImageVector) {
    Chat("Chat", Icons.Default.Email),
    MessageBlocks("Blocks", Icons.Default.List),
    ChatInput("Input", Icons.Default.Send)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CatalogTopBar(
    selectedTab: CatalogTab,
    onTabSelected: (CatalogTab) -> Unit,
    deviceType: DeviceType,
    onDeviceTypeSelected: (DeviceType) -> Unit,
    selectedTheme: io.github.devmugi.arcane.catalog.chat.ThemeVariant,
    onThemeSelected: (io.github.devmugi.arcane.catalog.chat.ThemeVariant) -> Unit,
    windowSizeClass: WindowSizeClass,
    showTabs: Boolean = true,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .background(ArcaneTheme.colors.surfaceContainerLow)
            .padding(ArcaneSpacing.Medium),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
    ) {
        // Left side: Tab navigation (only on wide screens)
        if (showTabs) {
            ArcaneTabs(
                tabs = CatalogTab.entries.map { ArcaneTab(it.displayName) },
                selectedIndex = selectedTab.ordinal,
                onTabSelected = { index -> onTabSelected(CatalogTab.entries[index]) },
                style = ArcaneTabStyle.Filled,
                scrollable = true
            )
        }

        // Right side: Device selector + Theme selector
        Row(horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)) {
            DeviceSelector(
                deviceType = deviceType,
                onDeviceSelected = onDeviceTypeSelected
            )
            ThemeSelector(
                selectedTheme = selectedTheme,
                onThemeSelected = onThemeSelected
            )
        }
    }
}

@Composable
private fun DeviceSelector(
    deviceType: DeviceType,
    onDeviceSelected: (DeviceType) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        ArcaneButton(
            onClick = { expanded = true },
            style = ArcaneButtonStyle.Outlined()
        ) {
            Text(deviceType.displayName)
        }

        ArcaneDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DeviceType.entries.forEach { device ->
                ArcaneDropdownMenuItem(
                    text = device.displayName,
                    onClick = {
                        onDeviceSelected(device)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun ThemeSelector(
    selectedTheme: io.github.devmugi.arcane.catalog.chat.ThemeVariant,
    onThemeSelected: (io.github.devmugi.arcane.catalog.chat.ThemeVariant) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        ArcaneButton(
            onClick = { expanded = true },
            style = ArcaneButtonStyle.Outlined()
        ) {
            Text(selectedTheme.displayName)
        }

        ArcaneDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            io.github.devmugi.arcane.catalog.chat.ThemeVariant.entries.forEach { theme ->
                ArcaneDropdownMenuItem(
                    text = theme.displayName,
                    onClick = {
                        onThemeSelected(theme)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun CatalogNavigationBar(
    selectedTab: CatalogTab,
    onTabSelected: (CatalogTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = ArcaneTheme.colors.surfaceContainerLow
    ) {
        CatalogTab.entries.forEach { tab ->
            NavigationBarItem(
                selected = selectedTab == tab,
                onClick = { onTabSelected(tab) },
                icon = { Icon(tab.icon, contentDescription = tab.displayName) },
                label = { Text(tab.displayName, style = ArcaneTheme.typography.labelSmall) },
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
