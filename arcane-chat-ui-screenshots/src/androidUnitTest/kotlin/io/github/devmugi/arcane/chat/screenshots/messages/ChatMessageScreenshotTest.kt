package io.github.devmugi.arcane.chat.screenshots.messages

import androidx.compose.ui.test.junit4.createComposeRule
import com.github.takahirom.roborazzi.RoborazziRule
import io.github.devmugi.arcane.chat.components.messages.ArcaneAssistantMessageBlock
import io.github.devmugi.arcane.chat.components.messages.ArcaneUserMessageBlock
import io.github.devmugi.arcane.chat.models.MessageBlock
import io.github.devmugi.arcane.chat.screenshots.captureChatScreenshot
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34])
class ChatMessageScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val roborazziRule = RoborazziRule()

    // ========================================================================
    // User Message - Basic
    // ========================================================================

    @Test
    fun userMessage_Basic_Light() {
        val blocks = listOf(
            MessageBlock.Text(
                id = "block-1",
                content = "Hello, this is a user message"
            )
        )

        captureChatScreenshot(composeTestRule, "messages", "UserMessage_Basic", isDark = false) {
            ArcaneUserMessageBlock(blocks = blocks)
        }
    }

    @Test
    fun userMessage_Basic_Dark() {
        val blocks = listOf(
            MessageBlock.Text(
                id = "block-1",
                content = "Hello, this is a user message"
            )
        )

        captureChatScreenshot(composeTestRule, "messages", "UserMessage_Basic", isDark = true) {
            ArcaneUserMessageBlock(blocks = blocks)
        }
    }

    // ========================================================================
    // User Message - With Timestamp
    // ========================================================================

    @Test
    fun userMessage_WithTimestamp_Light() {
        val blocks = listOf(
            MessageBlock.Text(
                id = "block-1",
                content = "Message with timestamp"
            )
        )

        captureChatScreenshot(composeTestRule, "messages", "UserMessage_WithTimestamp", isDark = false) {
            ArcaneUserMessageBlock(
                blocks = blocks,
                timestamp = "10:30 AM"
            )
        }
    }

    @Test
    fun userMessage_WithTimestamp_Dark() {
        val blocks = listOf(
            MessageBlock.Text(
                id = "block-1",
                content = "Message with timestamp"
            )
        )

        captureChatScreenshot(composeTestRule, "messages", "UserMessage_WithTimestamp", isDark = true) {
            ArcaneUserMessageBlock(
                blocks = blocks,
                timestamp = "10:30 AM"
            )
        }
    }

    // ========================================================================
    // User Message - Long Text
    // ========================================================================

    @Test
    fun userMessage_LongText_Light() {
        val blocks = listOf(
            MessageBlock.Text(
                id = "block-1",
                content = "This is a longer user message that demonstrates how the component handles text wrapping. It should wrap nicely within the bubble."
            )
        )

        captureChatScreenshot(composeTestRule, "messages", "UserMessage_LongText", isDark = false) {
            ArcaneUserMessageBlock(blocks = blocks)
        }
    }

    @Test
    fun userMessage_LongText_Dark() {
        val blocks = listOf(
            MessageBlock.Text(
                id = "block-1",
                content = "This is a longer user message that demonstrates how the component handles text wrapping. It should wrap nicely within the bubble."
            )
        )

        captureChatScreenshot(composeTestRule, "messages", "UserMessage_LongText", isDark = true) {
            ArcaneUserMessageBlock(blocks = blocks)
        }
    }

    // ========================================================================
    // Assistant Message - Basic
    // ========================================================================

    @Test
    fun assistantMessage_Basic_Light() {
        val blocks = listOf(
            MessageBlock.Text(
                id = "block-1",
                content = "This is an assistant response"
            )
        )

        captureChatScreenshot(composeTestRule, "messages", "AssistantMessage_Basic", isDark = false) {
            ArcaneAssistantMessageBlock(
                blocks = blocks,
                enableTruncation = false
            )
        }
    }

    @Test
    fun assistantMessage_Basic_Dark() {
        val blocks = listOf(
            MessageBlock.Text(
                id = "block-1",
                content = "This is an assistant response"
            )
        )

        captureChatScreenshot(composeTestRule, "messages", "AssistantMessage_Basic", isDark = true) {
            ArcaneAssistantMessageBlock(
                blocks = blocks,
                enableTruncation = false
            )
        }
    }

    // ========================================================================
    // Assistant Message - With Title
    // ========================================================================

    @Test
    fun assistantMessage_WithTitle_Light() {
        val blocks = listOf(
            MessageBlock.Text(
                id = "block-1",
                content = "This is an assistant response with a title"
            )
        )

        captureChatScreenshot(composeTestRule, "messages", "AssistantMessage_WithTitle", isDark = false) {
            ArcaneAssistantMessageBlock(
                blocks = blocks,
                title = "Claude",
                enableTruncation = false
            )
        }
    }

    @Test
    fun assistantMessage_WithTitle_Dark() {
        val blocks = listOf(
            MessageBlock.Text(
                id = "block-1",
                content = "This is an assistant response with a title"
            )
        )

        captureChatScreenshot(composeTestRule, "messages", "AssistantMessage_WithTitle", isDark = true) {
            ArcaneAssistantMessageBlock(
                blocks = blocks,
                title = "Claude",
                enableTruncation = false
            )
        }
    }

    // ========================================================================
    // Assistant Message - Loading State
    // ========================================================================

    @Test
    fun assistantMessage_Loading_Light() {
        val blocks = listOf(
            MessageBlock.Text(
                id = "block-1",
                content = "Thinking..."
            )
        )

        captureChatScreenshot(composeTestRule, "messages", "AssistantMessage_Loading", isDark = false) {
            ArcaneAssistantMessageBlock(
                blocks = blocks,
                title = "Claude",
                isLoading = true,
                enableTruncation = false
            )
        }
    }

    @Test
    fun assistantMessage_Loading_Dark() {
        val blocks = listOf(
            MessageBlock.Text(
                id = "block-1",
                content = "Thinking..."
            )
        )

        captureChatScreenshot(composeTestRule, "messages", "AssistantMessage_Loading", isDark = true) {
            ArcaneAssistantMessageBlock(
                blocks = blocks,
                title = "Claude",
                isLoading = true,
                enableTruncation = false
            )
        }
    }

    // ========================================================================
    // Assistant Message - Multiple Blocks
    // ========================================================================

    @Test
    fun assistantMessage_MultipleBlocks_Light() {
        val blocks = listOf(
            MessageBlock.Text(id = "block-1", content = "First paragraph of content"),
            MessageBlock.Text(id = "block-2", content = "Second paragraph of content")
        )

        captureChatScreenshot(composeTestRule, "messages", "AssistantMessage_MultipleBlocks", isDark = false) {
            ArcaneAssistantMessageBlock(
                blocks = blocks,
                title = "Claude",
                enableTruncation = false
            )
        }
    }

    @Test
    fun assistantMessage_MultipleBlocks_Dark() {
        val blocks = listOf(
            MessageBlock.Text(id = "block-1", content = "First paragraph of content"),
            MessageBlock.Text(id = "block-2", content = "Second paragraph of content")
        )

        captureChatScreenshot(composeTestRule, "messages", "AssistantMessage_MultipleBlocks", isDark = true) {
            ArcaneAssistantMessageBlock(
                blocks = blocks,
                title = "Claude",
                enableTruncation = false
            )
        }
    }
}
