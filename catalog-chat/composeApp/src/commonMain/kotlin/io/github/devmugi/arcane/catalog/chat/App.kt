package io.github.devmugi.arcane.catalog.chat

import androidx.compose.foundation.background
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
import io.github.devmugi.arcane.design.foundation.theme.ArcaneColors
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme

// Theme variants
enum class ThemeVariant {
    ARCANE,
    PERPLEXITY,
    CLAUDE,
    MTG;

    val displayName: String
        get() = when (this) {
            ARCANE -> "Arcane"
            PERPLEXITY -> "Perplexity"
            CLAUDE -> "Claude"
            MTG -> "MTG"
        }
}

// Custom savers for enums
private val DeviceTypeSaver = Saver<DeviceType, String>(
    save = { it.name },
    restore = { DeviceType.valueOf(it) }
)

private val CatalogTabSaver = Saver<CatalogTab, String>(
    save = { it.name },
    restore = { CatalogTab.valueOf(it) }
)

private val ThemeVariantSaver = Saver<ThemeVariant, String>(
    save = { it.name },
    restore = { ThemeVariant.valueOf(it) }
)

@Composable
fun App() {
    var selectedTheme by rememberSaveable(stateSaver = ThemeVariantSaver) {
        mutableStateOf(ThemeVariant.ARCANE)
    }
    var deviceType by rememberSaveable(stateSaver = DeviceTypeSaver) {
        mutableStateOf(DeviceType.None)
    }
    var selectedTab by rememberSaveable(stateSaver = CatalogTabSaver) {
        mutableStateOf(CatalogTab.Chat)
    }

    val colors = when (selectedTheme) {
        ThemeVariant.ARCANE -> ArcaneColors.default()
        ThemeVariant.PERPLEXITY -> ArcaneColors.perplexity()
        ThemeVariant.CLAUDE -> ArcaneColors.claude()
        ThemeVariant.MTG -> ArcaneColors.mtg()
    }

    ArcaneTheme(colors = colors) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ArcaneTheme.colors.surfaceContainerLow)
        ) {
            CatalogTopBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                deviceType = deviceType,
                onDeviceTypeSelected = { deviceType = it },
                selectedTheme = selectedTheme,
                onThemeSelected = { selectedTheme = it }
            )

            when (selectedTab) {
                CatalogTab.Chat -> ChatScreen(deviceType)
                CatalogTab.MessageBlocks -> MessageBlocksScreen(deviceType)
                CatalogTab.ChatInput -> ChatInputScreen(deviceType)
            }
        }
    }
}
