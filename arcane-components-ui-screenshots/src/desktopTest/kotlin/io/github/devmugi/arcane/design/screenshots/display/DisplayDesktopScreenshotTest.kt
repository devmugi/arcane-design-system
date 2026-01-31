package io.github.devmugi.arcane.design.screenshots.display

import androidx.compose.material3.Text
import io.github.devmugi.arcane.design.components.display.ArcaneCard
import io.github.devmugi.arcane.design.components.display.ArcaneCardContent
import io.github.devmugi.arcane.design.components.display.ArcaneListItem
import io.github.devmugi.arcane.design.screenshots.captureDesktopScreenshot
import kotlin.test.Test

class DisplayDesktopScreenshotTest {

    // ========================================================================
    // ArcaneCard - Title Only
    // ========================================================================

    @Test
    fun card_TitleOnly_Light() {
        captureDesktopScreenshot("Card_TitleOnly", isDark = false, height = 100) {
            ArcaneCard {
                ArcaneCardContent(title = "Card Title")
            }
        }
    }

    @Test
    fun card_TitleOnly_Dark() {
        captureDesktopScreenshot("Card_TitleOnly", isDark = true, height = 100) {
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
        captureDesktopScreenshot("Card_WithDescription", isDark = false, height = 120) {
            ArcaneCard {
                ArcaneCardContent(
                    title = "Card Title",
                    description = "Description text for the card"
                )
            }
        }
    }

    @Test
    fun card_WithDescription_Dark() {
        captureDesktopScreenshot("Card_WithDescription", isDark = true, height = 120) {
            ArcaneCard {
                ArcaneCardContent(
                    title = "Card Title",
                    description = "Description text for the card"
                )
            }
        }
    }

    // ========================================================================
    // ArcaneListItem - Headline Only
    // ========================================================================

    @Test
    fun listItem_HeadlineOnly_Light() {
        captureDesktopScreenshot("ListItem_HeadlineOnly", isDark = false, height = 60) {
            ArcaneListItem(headlineText = "List Item Title")
        }
    }

    @Test
    fun listItem_HeadlineOnly_Dark() {
        captureDesktopScreenshot("ListItem_HeadlineOnly", isDark = true, height = 60) {
            ArcaneListItem(headlineText = "List Item Title")
        }
    }

    // ========================================================================
    // ArcaneListItem - With Supporting Text
    // ========================================================================

    @Test
    fun listItem_WithSupportingText_Light() {
        captureDesktopScreenshot("ListItem_WithSupportingText", isDark = false, height = 70) {
            ArcaneListItem(
                headlineText = "Primary Text",
                supportingText = "Secondary supporting text"
            )
        }
    }

    @Test
    fun listItem_WithSupportingText_Dark() {
        captureDesktopScreenshot("ListItem_WithSupportingText", isDark = true, height = 70) {
            ArcaneListItem(
                headlineText = "Primary Text",
                supportingText = "Secondary supporting text"
            )
        }
    }
}
