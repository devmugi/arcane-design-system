package io.github.devmugi.arcane.design.screenshots.navigation

import androidx.compose.ui.test.junit4.createComposeRule
import com.github.takahirom.roborazzi.RoborazziRule
import io.github.devmugi.arcane.design.components.navigation.ArcaneTab
import io.github.devmugi.arcane.design.components.navigation.ArcaneTabStyle
import io.github.devmugi.arcane.design.components.navigation.ArcaneTabs
import io.github.devmugi.arcane.design.screenshots.captureArcaneScreenshot
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34])
class TabsScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val roborazziRule = RoborazziRule()

    private val basicTabs = listOf(
        ArcaneTab(label = "Home"),
        ArcaneTab(label = "Settings"),
        ArcaneTab(label = "Profile")
    )

    // ========================================================================
    // Filled Style - First Selected
    // ========================================================================

    @Test
    fun tabs_Filled_FirstSelected_Light() {
        captureArcaneScreenshot(composeTestRule, "navigation", "Tabs_Filled_FirstSelected", isDark = false) {
            ArcaneTabs(
                tabs = basicTabs,
                selectedIndex = 0,
                onTabSelected = {},
                style = ArcaneTabStyle.Filled
            )
        }
    }

    @Test
    fun tabs_Filled_FirstSelected_Dark() {
        captureArcaneScreenshot(composeTestRule, "navigation", "Tabs_Filled_FirstSelected", isDark = true) {
            ArcaneTabs(
                tabs = basicTabs,
                selectedIndex = 0,
                onTabSelected = {},
                style = ArcaneTabStyle.Filled
            )
        }
    }

    // ========================================================================
    // Filled Style - Second Selected
    // ========================================================================

    @Test
    fun tabs_Filled_SecondSelected_Light() {
        captureArcaneScreenshot(composeTestRule, "navigation", "Tabs_Filled_SecondSelected", isDark = false) {
            ArcaneTabs(
                tabs = basicTabs,
                selectedIndex = 1,
                onTabSelected = {},
                style = ArcaneTabStyle.Filled
            )
        }
    }

    @Test
    fun tabs_Filled_SecondSelected_Dark() {
        captureArcaneScreenshot(composeTestRule, "navigation", "Tabs_Filled_SecondSelected", isDark = true) {
            ArcaneTabs(
                tabs = basicTabs,
                selectedIndex = 1,
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
        captureArcaneScreenshot(composeTestRule, "navigation", "Tabs_Underline", isDark = false) {
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
        captureArcaneScreenshot(composeTestRule, "navigation", "Tabs_Underline", isDark = true) {
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

        captureArcaneScreenshot(composeTestRule, "navigation", "Tabs_WithDisabled", isDark = false) {
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

        captureArcaneScreenshot(composeTestRule, "navigation", "Tabs_WithDisabled", isDark = true) {
            ArcaneTabs(
                tabs = tabsWithDisabled,
                selectedIndex = 0,
                onTabSelected = {},
                style = ArcaneTabStyle.Filled
            )
        }
    }

    // ========================================================================
    // Scrollable Tabs
    // ========================================================================

    @Test
    fun tabs_Scrollable_Light() {
        val manyTabs = listOf(
            ArcaneTab(label = "Alpha"),
            ArcaneTab(label = "Beta"),
            ArcaneTab(label = "Gamma"),
            ArcaneTab(label = "Delta"),
            ArcaneTab(label = "Epsilon")
        )

        captureArcaneScreenshot(composeTestRule, "navigation", "Tabs_Scrollable", isDark = false) {
            ArcaneTabs(
                tabs = manyTabs,
                selectedIndex = 0,
                onTabSelected = {},
                style = ArcaneTabStyle.Filled,
                scrollable = true
            )
        }
    }

    @Test
    fun tabs_Scrollable_Dark() {
        val manyTabs = listOf(
            ArcaneTab(label = "Alpha"),
            ArcaneTab(label = "Beta"),
            ArcaneTab(label = "Gamma"),
            ArcaneTab(label = "Delta"),
            ArcaneTab(label = "Epsilon")
        )

        captureArcaneScreenshot(composeTestRule, "navigation", "Tabs_Scrollable", isDark = true) {
            ArcaneTabs(
                tabs = manyTabs,
                selectedIndex = 0,
                onTabSelected = {},
                style = ArcaneTabStyle.Filled,
                scrollable = true
            )
        }
    }

    // ========================================================================
    // Two Tabs Only
    // ========================================================================

    @Test
    fun tabs_TwoTabs_Light() {
        val twoTabs = listOf(
            ArcaneTab(label = "Overview"),
            ArcaneTab(label = "Details")
        )

        captureArcaneScreenshot(composeTestRule, "navigation", "Tabs_TwoTabs", isDark = false) {
            ArcaneTabs(
                tabs = twoTabs,
                selectedIndex = 0,
                onTabSelected = {},
                style = ArcaneTabStyle.Filled
            )
        }
    }

    @Test
    fun tabs_TwoTabs_Dark() {
        val twoTabs = listOf(
            ArcaneTab(label = "Overview"),
            ArcaneTab(label = "Details")
        )

        captureArcaneScreenshot(composeTestRule, "navigation", "Tabs_TwoTabs", isDark = true) {
            ArcaneTabs(
                tabs = twoTabs,
                selectedIndex = 0,
                onTabSelected = {},
                style = ArcaneTabStyle.Filled
            )
        }
    }
}
