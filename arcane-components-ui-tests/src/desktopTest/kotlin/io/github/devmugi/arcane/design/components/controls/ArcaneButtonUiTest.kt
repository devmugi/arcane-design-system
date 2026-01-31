package io.github.devmugi.arcane.design.components.controls

import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import io.github.devmugi.arcane.design.testing.TestArcaneTheme
import io.kotest.matchers.shouldBe
import org.junit.Rule
import org.junit.Test

class ArcaneButtonUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `button displays text content`() {
        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneButton(onClick = {}) {
                    Text("Click Me")
                }
            }
        }

        composeTestRule.onNodeWithText("Click Me").assertExists()
    }

    @Test
    fun `button triggers onClick when clicked`() {
        var clicked = false

        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneButton(onClick = { clicked = true }) {
                    Text("Submit")
                }
            }
        }

        composeTestRule.onNodeWithText("Submit").performClick()

        clicked shouldBe true
    }

    @Test
    fun `disabled button does not trigger onClick`() {
        var clicked = false

        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneButton(
                    onClick = { clicked = true },
                    enabled = false
                ) {
                    Text("Disabled")
                }
            }
        }

        composeTestRule.onNodeWithText("Disabled").performClick()

        clicked shouldBe false
    }

    @Test
    fun `button is clickable by default`() {
        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneButton(onClick = {}) {
                    Text("Default Button")
                }
            }
        }

        composeTestRule.onNodeWithText("Default Button").assertIsEnabled()
    }

    @Test
    fun `disabled button is not enabled`() {
        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneButton(
                    onClick = {},
                    enabled = false
                ) {
                    Text("Disabled Button")
                }
            }
        }

        composeTestRule.onNodeWithText("Disabled Button").assertIsNotEnabled()
    }

    @Test
    fun `ArcaneTextButton displays text`() {
        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneTextButton(
                    text = "Text Button",
                    onClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Text Button").assertExists()
    }

    @Test
    fun `ArcaneTextButton triggers onClick when clicked`() {
        var clicked = false

        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneTextButton(
                    text = "Clickable",
                    onClick = { clicked = true }
                )
            }
        }

        composeTestRule.onNodeWithText("Clickable").performClick()

        clicked shouldBe true
    }
}
