package io.github.devmugi.arcane.design.components.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import io.github.devmugi.arcane.design.testing.TestArcaneTheme
import io.kotest.matchers.shouldBe
import org.junit.Rule
import org.junit.Test

class TabsUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `tabs display all tab labels`() {
        val tabs = listOf(
            ArcaneTab(label = "Home"),
            ArcaneTab(label = "Settings"),
            ArcaneTab(label = "Profile")
        )

        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneTabs(
                    tabs = tabs,
                    selectedIndex = 0,
                    onTabSelected = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Home").assertExists()
        composeTestRule.onNodeWithText("Settings").assertExists()
        composeTestRule.onNodeWithText("Profile").assertExists()
    }

    @Test
    fun `tab selection changes on click`() {
        val tabs = listOf(
            ArcaneTab(label = "First"),
            ArcaneTab(label = "Second"),
            ArcaneTab(label = "Third")
        )
        var selectedIndex by mutableStateOf(0)

        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneTabs(
                    tabs = tabs,
                    selectedIndex = selectedIndex,
                    onTabSelected = { selectedIndex = it }
                )
            }
        }

        selectedIndex shouldBe 0

        composeTestRule.onNodeWithText("Second").performClick()
        selectedIndex shouldBe 1

        composeTestRule.onNodeWithText("Third").performClick()
        selectedIndex shouldBe 2

        composeTestRule.onNodeWithText("First").performClick()
        selectedIndex shouldBe 0
    }

    @Test
    fun `disabled tab does not trigger selection`() {
        val tabs = listOf(
            ArcaneTab(label = "Enabled"),
            ArcaneTab(label = "Disabled", enabled = false)
        )
        var selectedIndex by mutableStateOf(0)

        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneTabs(
                    tabs = tabs,
                    selectedIndex = selectedIndex,
                    onTabSelected = { selectedIndex = it }
                )
            }
        }

        composeTestRule.onNodeWithText("Disabled").performClick()
        selectedIndex shouldBe 0
    }

    @Test
    fun `tabs with underline style display correctly`() {
        val tabs = listOf(
            ArcaneTab(label = "Tab A"),
            ArcaneTab(label = "Tab B")
        )

        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneTabs(
                    tabs = tabs,
                    selectedIndex = 0,
                    onTabSelected = {},
                    style = ArcaneTabStyle.Underline
                )
            }
        }

        composeTestRule.onNodeWithText("Tab A").assertExists()
        composeTestRule.onNodeWithText("Tab B").assertExists()
    }

    @Test
    fun `scrollable tabs display all labels`() {
        val tabs = listOf(
            ArcaneTab(label = "Alpha"),
            ArcaneTab(label = "Beta"),
            ArcaneTab(label = "Gamma"),
            ArcaneTab(label = "Delta")
        )

        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneTabs(
                    tabs = tabs,
                    selectedIndex = 0,
                    onTabSelected = {},
                    scrollable = true
                )
            }
        }

        composeTestRule.onNodeWithText("Alpha").assertExists()
        composeTestRule.onNodeWithText("Beta").assertExists()
        composeTestRule.onNodeWithText("Gamma").assertExists()
        composeTestRule.onNodeWithText("Delta").assertExists()
    }

    @Test
    fun `ArcaneTabLayout displays tabs and content`() {
        val tabs = listOf(
            ArcaneTab(label = "Overview"),
            ArcaneTab(label = "Details")
        )
        var selectedIndex by mutableStateOf(0)

        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneTabLayout(
                    tabs = tabs,
                    selectedIndex = selectedIndex,
                    onTabSelected = { selectedIndex = it }
                ) { index ->
                    when (index) {
                        0 -> androidx.compose.material3.Text("Overview Content")
                        1 -> androidx.compose.material3.Text("Details Content")
                    }
                }
            }
        }

        composeTestRule.onNodeWithText("Overview").assertExists()
        composeTestRule.onNodeWithText("Details").assertExists()
        composeTestRule.onNodeWithText("Overview Content").assertExists()

        composeTestRule.onNodeWithText("Details").performClick()
        selectedIndex shouldBe 1
        composeTestRule.onNodeWithText("Details Content").assertExists()
    }
}
