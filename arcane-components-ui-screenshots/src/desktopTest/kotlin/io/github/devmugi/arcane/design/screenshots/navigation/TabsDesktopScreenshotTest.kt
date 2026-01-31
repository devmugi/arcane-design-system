package io.github.devmugi.arcane.design.screenshots.navigation

import io.github.devmugi.arcane.design.components.navigation.ArcaneTab
import io.github.devmugi.arcane.design.components.navigation.ArcaneTabStyle
import io.github.devmugi.arcane.design.components.navigation.ArcaneTabs
import io.github.devmugi.arcane.design.screenshots.captureDesktopScreenshot
import kotlin.test.Test

class TabsDesktopScreenshotTest {

    private val basicTabs = listOf(
        ArcaneTab(label = "Home"),
        ArcaneTab(label = "Settings"),
        ArcaneTab(label = "Profile")
    )

    // ========================================================================
    // Filled Style
    // ========================================================================

    @Test
    fun tabs_Filled_Light() {
        captureDesktopScreenshot("navigation", "Tabs_Filled", isDark = false, height = 60) {
            ArcaneTabs(
                tabs = basicTabs,
                selectedIndex = 0,
                onTabSelected = {},
                style = ArcaneTabStyle.Filled
            )
        }
    }

    @Test
    fun tabs_Filled_Dark() {
        captureDesktopScreenshot("navigation", "Tabs_Filled", isDark = true, height = 60) {
            ArcaneTabs(
                tabs = basicTabs,
                selectedIndex = 0,
                onTabSelected = {},
                style = ArcaneTabStyle.Filled
            )
        }
    }

    // ========================================================================
    // Underline Style
    // ========================================================================

    @Test
    fun tabs_Underline_Light() {
        captureDesktopScreenshot("navigation", "Tabs_Underline", isDark = false, height = 60) {
            ArcaneTabs(
                tabs = basicTabs,
                selectedIndex = 0,
                onTabSelected = {},
                style = ArcaneTabStyle.Underline
            )
        }
    }

    @Test
    fun tabs_Underline_Dark() {
        captureDesktopScreenshot("navigation", "Tabs_Underline", isDark = true, height = 60) {
            ArcaneTabs(
                tabs = basicTabs,
                selectedIndex = 0,
                onTabSelected = {},
                style = ArcaneTabStyle.Underline
            )
        }
    }

    // ========================================================================
    // With Disabled Tab
    // ========================================================================

    @Test
    fun tabs_WithDisabled_Light() {
        val tabsWithDisabled = listOf(
            ArcaneTab(label = "Enabled"),
            ArcaneTab(label = "Disabled", enabled = false),
            ArcaneTab(label = "Active")
        )

        captureDesktopScreenshot("navigation", "Tabs_WithDisabled", isDark = false, height = 60) {
            ArcaneTabs(
                tabs = tabsWithDisabled,
                selectedIndex = 0,
                onTabSelected = {},
                style = ArcaneTabStyle.Filled
            )
        }
    }

    @Test
    fun tabs_WithDisabled_Dark() {
        val tabsWithDisabled = listOf(
            ArcaneTab(label = "Enabled"),
            ArcaneTab(label = "Disabled", enabled = false),
            ArcaneTab(label = "Active")
        )

        captureDesktopScreenshot("navigation", "Tabs_WithDisabled", isDark = true, height = 60) {
            ArcaneTabs(
                tabs = tabsWithDisabled,
                selectedIndex = 0,
                onTabSelected = {},
                style = ArcaneTabStyle.Filled
            )
        }
    }
}
