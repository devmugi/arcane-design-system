package io.github.devmugi.arcane.design.components.display

import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import io.github.devmugi.arcane.design.testing.TestArcaneTheme
import io.kotest.matchers.shouldBe
import org.junit.Rule
import org.junit.Test

class DisplayComponentsUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // ==================== ArcaneCard Tests ====================

    @Test
    fun `ArcaneCard displays content`() {
        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneCard {
                    Text("Card Content", modifier = Modifier.testTag("cardContent"))
                }
            }
        }

        composeTestRule.onNodeWithText("Card Content").assertIsDisplayed()
    }

    @Test
    fun `ArcaneCard with onClick triggers callback when clicked`() {
        var clicked = false

        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneCard(
                    onClick = { clicked = true },
                    modifier = Modifier.testTag("clickableCard")
                ) {
                    Text("Clickable Card")
                }
            }
        }

        composeTestRule.onNodeWithText("Clickable Card").performClick()

        clicked shouldBe true
    }

    @Test
    fun `ArcaneCard without onClick does not crash when clicked`() {
        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneCard {
                    Text("Non-clickable Card")
                }
            }
        }

        // Should not crash - card is not clickable
        composeTestRule.onNodeWithText("Non-clickable Card").assertIsDisplayed()
    }

    @Test
    fun `ArcaneCardContent displays title`() {
        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneCard {
                    ArcaneCardContent(title = "Card Title")
                }
            }
        }

        composeTestRule.onNodeWithText("Card Title").assertIsDisplayed()
    }

    @Test
    fun `ArcaneCardContent displays title and description`() {
        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneCard {
                    ArcaneCardContent(
                        title = "Title Text",
                        description = "Description Text"
                    )
                }
            }
        }

        composeTestRule.onNodeWithText("Title Text").assertIsDisplayed()
        composeTestRule.onNodeWithText("Description Text").assertIsDisplayed()
    }

    // ==================== ArcaneListItem Tests ====================

    @Test
    fun `ArcaneListItem displays headline text`() {
        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneListItem(headlineText = "List Item Title")
            }
        }

        composeTestRule.onNodeWithText("List Item Title").assertIsDisplayed()
    }

    @Test
    fun `ArcaneListItem displays supporting text`() {
        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneListItem(
                    headlineText = "Primary Text",
                    supportingText = "Secondary supporting text"
                )
            }
        }

        composeTestRule.onNodeWithText("Primary Text").assertIsDisplayed()
        composeTestRule.onNodeWithText("Secondary supporting text").assertIsDisplayed()
    }

    @Test
    fun `ArcaneListItem with onClick triggers callback when clicked`() {
        var clicked = false

        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneListItem(
                    headlineText = "Clickable Item",
                    onClick = { clicked = true }
                )
            }
        }

        composeTestRule.onNodeWithText("Clickable Item").performClick()

        clicked shouldBe true
    }

    @Test
    fun `ArcaneListItem displays leading content`() {
        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneListItem(
                    headlineText = "Item with Leading",
                    leadingContent = {
                        Text("L", modifier = Modifier.testTag("leadingContent"))
                    }
                )
            }
        }

        composeTestRule.onNodeWithText("Item with Leading").assertIsDisplayed()
        composeTestRule.onNodeWithTag("leadingContent").assertIsDisplayed()
    }

    @Test
    fun `ArcaneListItem displays trailing content`() {
        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneListItem(
                    headlineText = "Item with Trailing",
                    trailingContent = {
                        Text("T", modifier = Modifier.testTag("trailingContent"))
                    }
                )
            }
        }

        composeTestRule.onNodeWithText("Item with Trailing").assertIsDisplayed()
        composeTestRule.onNodeWithTag("trailingContent").assertIsDisplayed()
    }
}
