package io.github.devmugi.arcane.design.screenshots.display

import androidx.compose.material3.Text
import androidx.compose.ui.test.junit4.createComposeRule
import com.github.takahirom.roborazzi.RoborazziRule
import io.github.devmugi.arcane.design.components.display.ArcaneCard
import io.github.devmugi.arcane.design.components.display.ArcaneCardContent
import io.github.devmugi.arcane.design.components.display.ArcaneListItem
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
class DisplayScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val roborazziRule = RoborazziRule()

    // ========================================================================
    // ArcaneCard - Title Only
    // ========================================================================

    @Test
    fun card_TitleOnly_Light() {
        captureArcaneScreenshot(composeTestRule, "Card_TitleOnly", isDark = false) {
            ArcaneCard {
                ArcaneCardContent(title = "Card Title")
            }
        }
    }

    @Test
    fun card_TitleOnly_Dark() {
        captureArcaneScreenshot(composeTestRule, "Card_TitleOnly", isDark = true) {
            ArcaneCard {
                ArcaneCardContent(title = "Card Title")
            }
        }
    }

    // ========================================================================
    // ArcaneCard - With Description
    // ========================================================================

    @Test
    fun card_WithDescription_Light() {
        captureArcaneScreenshot(composeTestRule, "Card_WithDescription", isDark = false) {
            ArcaneCard {
                ArcaneCardContent(
                    title = "Card Title",
                    description = "This is a description that provides more context about the card content."
                )
            }
        }
    }

    @Test
    fun card_WithDescription_Dark() {
        captureArcaneScreenshot(composeTestRule, "Card_WithDescription", isDark = true) {
            ArcaneCard {
                ArcaneCardContent(
                    title = "Card Title",
                    description = "This is a description that provides more context about the card content."
                )
            }
        }
    }

    // ========================================================================
    // ArcaneCard - Clickable
    // ========================================================================

    @Test
    fun card_Clickable_Light() {
        captureArcaneScreenshot(composeTestRule, "Card_Clickable", isDark = false) {
            ArcaneCard(onClick = {}) {
                ArcaneCardContent(
                    title = "Clickable Card",
                    description = "Tap to interact"
                )
            }
        }
    }

    @Test
    fun card_Clickable_Dark() {
        captureArcaneScreenshot(composeTestRule, "Card_Clickable", isDark = true) {
            ArcaneCard(onClick = {}) {
                ArcaneCardContent(
                    title = "Clickable Card",
                    description = "Tap to interact"
                )
            }
        }
    }

    // ========================================================================
    // ArcaneCard - Custom Content
    // ========================================================================

    @Test
    fun card_CustomContent_Light() {
        captureArcaneScreenshot(composeTestRule, "Card_CustomContent", isDark = false) {
            ArcaneCard {
                Text("Custom card content")
            }
        }
    }

    @Test
    fun card_CustomContent_Dark() {
        captureArcaneScreenshot(composeTestRule, "Card_CustomContent", isDark = true) {
            ArcaneCard {
                Text("Custom card content")
            }
        }
    }

    // ========================================================================
    // ArcaneListItem - Headline Only
    // ========================================================================

    @Test
    fun listItem_HeadlineOnly_Light() {
        captureArcaneScreenshot(composeTestRule, "ListItem_HeadlineOnly", isDark = false) {
            ArcaneListItem(headlineText = "List Item Title")
        }
    }

    @Test
    fun listItem_HeadlineOnly_Dark() {
        captureArcaneScreenshot(composeTestRule, "ListItem_HeadlineOnly", isDark = true) {
            ArcaneListItem(headlineText = "List Item Title")
        }
    }

    // ========================================================================
    // ArcaneListItem - With Supporting Text
    // ========================================================================

    @Test
    fun listItem_WithSupportingText_Light() {
        captureArcaneScreenshot(composeTestRule, "ListItem_WithSupportingText", isDark = false) {
            ArcaneListItem(
                headlineText = "Primary Text",
                supportingText = "Secondary supporting text"
            )
        }
    }

    @Test
    fun listItem_WithSupportingText_Dark() {
        captureArcaneScreenshot(composeTestRule, "ListItem_WithSupportingText", isDark = true) {
            ArcaneListItem(
                headlineText = "Primary Text",
                supportingText = "Secondary supporting text"
            )
        }
    }

    // ========================================================================
    // ArcaneListItem - Clickable
    // ========================================================================

    @Test
    fun listItem_Clickable_Light() {
        captureArcaneScreenshot(composeTestRule, "ListItem_Clickable", isDark = false) {
            ArcaneListItem(
                headlineText = "Clickable Item",
                supportingText = "Tap to select",
                onClick = {}
            )
        }
    }

    @Test
    fun listItem_Clickable_Dark() {
        captureArcaneScreenshot(composeTestRule, "ListItem_Clickable", isDark = true) {
            ArcaneListItem(
                headlineText = "Clickable Item",
                supportingText = "Tap to select",
                onClick = {}
            )
        }
    }

    // ========================================================================
    // ArcaneListItem - With Leading Content
    // ========================================================================

    @Test
    fun listItem_WithLeading_Light() {
        captureArcaneScreenshot(composeTestRule, "ListItem_WithLeading", isDark = false) {
            ArcaneListItem(
                headlineText = "Item with Icon",
                supportingText = "Leading content example",
                leadingContent = {
                    Text("L")
                }
            )
        }
    }

    @Test
    fun listItem_WithLeading_Dark() {
        captureArcaneScreenshot(composeTestRule, "ListItem_WithLeading", isDark = true) {
            ArcaneListItem(
                headlineText = "Item with Icon",
                supportingText = "Leading content example",
                leadingContent = {
                    Text("L")
                }
            )
        }
    }

    // ========================================================================
    // ArcaneListItem - With Trailing Content
    // ========================================================================

    @Test
    fun listItem_WithTrailing_Light() {
        captureArcaneScreenshot(composeTestRule, "ListItem_WithTrailing", isDark = false) {
            ArcaneListItem(
                headlineText = "Item with Trailing",
                supportingText = "Trailing content example",
                trailingContent = {
                    Text(">")
                }
            )
        }
    }

    @Test
    fun listItem_WithTrailing_Dark() {
        captureArcaneScreenshot(composeTestRule, "ListItem_WithTrailing", isDark = true) {
            ArcaneListItem(
                headlineText = "Item with Trailing",
                supportingText = "Trailing content example",
                trailingContent = {
                    Text(">")
                }
            )
        }
    }

    // ========================================================================
    // ArcaneListItem - Full Featured
    // ========================================================================

    @Test
    fun listItem_FullFeatured_Light() {
        captureArcaneScreenshot(composeTestRule, "ListItem_FullFeatured", isDark = false) {
            ArcaneListItem(
                headlineText = "Full Featured Item",
                supportingText = "With all options",
                leadingContent = { Text("L") },
                trailingContent = { Text(">") },
                onClick = {}
            )
        }
    }

    @Test
    fun listItem_FullFeatured_Dark() {
        captureArcaneScreenshot(composeTestRule, "ListItem_FullFeatured", isDark = true) {
            ArcaneListItem(
                headlineText = "Full Featured Item",
                supportingText = "With all options",
                leadingContent = { Text("L") },
                trailingContent = { Text(">") },
                onClick = {}
            )
        }
    }
}
