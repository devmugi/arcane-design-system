package io.github.devmugi.arcane.catalog.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.window.core.layout.WindowSizeClass
import io.github.devmugi.arcane.catalog.chat.components.CatalogNavigationBar
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
    P2D,
    P2L,
    CLAUDE_D,
    CLAUDE_L,
    CV_AGENT_D,
    CV_AGENT_L,
    AGENT2_D,
    AGENT2_L;

    val displayName: String
        get() = when (this) {
            ARCANE -> "Arcane"
            PERPLEXITY -> "Perplexity"
            P2D -> "P2D"
            P2L -> "P2L"
            CLAUDE_D -> "ClaudeD"
            CLAUDE_L -> "ClaudeL"
            CV_AGENT_D -> "cvAgentD"
            CV_AGENT_L -> "cvAgentL"
            AGENT2_D -> "agent2D"
            AGENT2_L -> "agent2L"
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
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    // Auto-select device based on window width
    val isWideScreen = windowSizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND  // 600dp
    )

    var selectedTheme by rememberSaveable(stateSaver = ThemeVariantSaver) {
        mutableStateOf(ThemeVariant.ARCANE)
    }
    var deviceType by rememberSaveable(stateSaver = DeviceTypeSaver) {
        mutableStateOf(if (isWideScreen) DeviceType.Pixel8 else DeviceType.None)
    }
    var selectedTab by rememberSaveable(stateSaver = CatalogTabSaver) {
        mutableStateOf(CatalogTab.Chat)
    }

    // Auto-update device type when crossing width threshold
    LaunchedEffect(isWideScreen) {
        deviceType = if (isWideScreen) DeviceType.Pixel8 else DeviceType.None
    }

    val colors = when (selectedTheme) {
        ThemeVariant.ARCANE -> ArcaneColors.default()
        ThemeVariant.PERPLEXITY -> ArcaneColors.perplexity()
        ThemeVariant.P2D -> ArcaneColors.p2d()
        ThemeVariant.P2L -> ArcaneColors.p2l()
        ThemeVariant.CLAUDE_D -> ArcaneColors.claudeD()
        ThemeVariant.CLAUDE_L -> ArcaneColors.claudeL()
        ThemeVariant.CV_AGENT_D -> ArcaneColors.cvAgentDark()
        ThemeVariant.CV_AGENT_L -> ArcaneColors.cvAgentLight()
        ThemeVariant.AGENT2_D -> ArcaneColors.agent2Dark()
        ThemeVariant.AGENT2_L -> ArcaneColors.agent2Light()
    }

    ArcaneTheme(colors = colors) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ArcaneTheme.colors.surfaceContainerLow)
        ) {
            // Top bar: show tabs only on wide screens
            CatalogTopBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                deviceType = deviceType,
                onDeviceTypeSelected = { deviceType = it },
                selectedTheme = selectedTheme,
                onThemeSelected = { selectedTheme = it },
                windowSizeClass = windowSizeClass,
                showTabs = isWideScreen
            )

            // Screen content
            Column(modifier = Modifier.weight(1f)) {
                when (selectedTab) {
                    CatalogTab.Chat -> ChatScreen(deviceType)
                    CatalogTab.MessageBlocks -> MessageBlocksScreen(deviceType)
                    CatalogTab.ChatInput -> ChatInputScreen(deviceType)
                }
            }

            // Bottom navigation bar: only on narrow screens
            if (!isWideScreen) {
                CatalogNavigationBar(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )
            }
        }
    }
}
