package io.github.devmugi.arcane.catalog.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
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
        Row(horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)) {
            CatalogTab.entries.forEach { tab ->
                ArcaneButton(
                    text = tab.displayName,
                    style = if (selectedTab == tab) {
                        ArcaneButtonStyle.Primary
                    } else {
                        ArcaneButtonStyle.Outlined()
                    },
                    onClick = { onTabSelected(tab) }
                )
            }
        }

        // Right side: Device selector + Theme toggle
        Row(horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)) {
            DeviceSelector(
                deviceType = deviceType,
                onDeviceSelected = onDeviceTypeSelected
            )
            ThemeToggleButton(
                isDark = isDarkTheme,
                onToggle = onThemeToggle
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
            text = deviceType.displayName,
            style = ArcaneButtonStyle.Outlined(),
            onClick = { expanded = true }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DeviceType.entries.forEach { device ->
                DropdownMenuItem(
                    text = { Text(device.displayName) },
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
private fun ThemeToggleButton(
    isDark: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onToggle,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
            contentDescription = if (isDark) "Switch to light mode" else "Switch to dark mode",
            tint = ArcaneTheme.colors.text,
            modifier = Modifier.size(24.dp)
        )
    }
}
