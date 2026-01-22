package io.github.devmugi.arcane.catalog.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
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

enum class CatalogTab {
    Chat,
    MessageBlocks,
    ChatInput;

    val displayName: String
        get() = when (this) {
            Chat -> "Chat"
            MessageBlocks -> "Message Blocks"
            ChatInput -> "Chat Input"
        }
}

@Composable
fun CatalogTopBar(
    selectedTab: CatalogTab,
    onTabSelected: (CatalogTab) -> Unit,
    deviceType: DeviceType,
    onDeviceTypeSelected: (DeviceType) -> Unit,
    selectedTheme: io.github.devmugi.arcane.catalog.chat.ThemeVariant,
    onThemeSelected: (io.github.devmugi.arcane.catalog.chat.ThemeVariant) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(ArcaneTheme.colors.surfaceContainerLow)
            .padding(ArcaneSpacing.Medium),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left side: Tab navigation
        ArcaneTabs(
            tabs = CatalogTab.entries.map { ArcaneTab(it.displayName) },
            selectedIndex = selectedTab.ordinal,
            onTabSelected = { index -> onTabSelected(CatalogTab.entries[index]) },
            style = ArcaneTabStyle.Filled,
            scrollable = true
        )

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
