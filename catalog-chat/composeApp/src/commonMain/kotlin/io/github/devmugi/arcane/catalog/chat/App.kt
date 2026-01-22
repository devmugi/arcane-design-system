package io.github.devmugi.arcane.catalog.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import io.github.devmugi.arcane.catalog.chat.components.CatalogTab
import io.github.devmugi.arcane.catalog.chat.components.CatalogTopBar
import io.github.devmugi.arcane.catalog.chat.components.DeviceType
import io.github.devmugi.arcane.catalog.chat.screens.ChatInputScreen
import io.github.devmugi.arcane.catalog.chat.screens.ChatScreen
import io.github.devmugi.arcane.catalog.chat.screens.MessageBlocksScreen
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme

// Custom savers for enums
private val DeviceTypeSaver = Saver<DeviceType, String>(
    save = { it.name },
    restore = { DeviceType.valueOf(it) }
)

private val CatalogTabSaver = Saver<CatalogTab, String>(
    save = { it.name },
    restore = { CatalogTab.valueOf(it) }
)

@Composable
fun App() {
    var isDarkTheme by rememberSaveable { mutableStateOf(false) }
    var deviceType by rememberSaveable(stateSaver = DeviceTypeSaver) {
        mutableStateOf(DeviceType.None)
    }
    var selectedTab by rememberSaveable(stateSaver = CatalogTabSaver) {
        mutableStateOf(CatalogTab.Chat)
    }

    ArcaneTheme(isDark = isDarkTheme) {
        Column(Modifier.fillMaxSize()) {
            CatalogTopBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                deviceType = deviceType,
                onDeviceTypeSelected = { deviceType = it },
                isDarkTheme = isDarkTheme,
                onThemeToggle = { isDarkTheme = !isDarkTheme }
            )

            when (selectedTab) {
                CatalogTab.Chat -> ChatScreen(deviceType)
                CatalogTab.MessageBlocks -> MessageBlocksScreen(deviceType)
                CatalogTab.ChatInput -> ChatInputScreen(deviceType)
            }
        }
    }
}
