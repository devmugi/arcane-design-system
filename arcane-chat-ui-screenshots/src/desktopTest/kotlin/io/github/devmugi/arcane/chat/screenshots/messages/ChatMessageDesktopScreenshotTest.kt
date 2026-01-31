package io.github.devmugi.arcane.chat.screenshots.messages

import io.github.devmugi.arcane.chat.components.messages.ArcaneAssistantMessageBlock
import io.github.devmugi.arcane.chat.components.messages.ArcaneUserMessageBlock
import io.github.devmugi.arcane.chat.models.MessageBlock
import io.github.devmugi.arcane.chat.screenshots.captureChatDesktopScreenshot
import kotlin.test.Test

class ChatMessageDesktopScreenshotTest {

    // ========================================================================
    // User Message
    // ========================================================================

    @Test
    fun userMessage_Basic_Light() {
        val blocks = listOf(
            MessageBlock.Text(
                id = "block-1",
                content = "Hello, this is a user message"
            )
        )

        captureChatDesktopScreenshot("messages", "UserMessage_Basic", isDark = false, height = 100) {
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

        captureChatDesktopScreenshot("messages", "UserMessage_Basic", isDark = true, height = 100) {
            ArcaneUserMessageBlock(blocks = blocks)
        }
    }

    @Test
    fun userMessage_WithTimestamp_Light() {
        val blocks = listOf(
            MessageBlock.Text(
                id = "block-1",
                content = "Message with timestamp"
            )
        )

        captureChatDesktopScreenshot("messages", "UserMessage_WithTimestamp", isDark = false, height = 100) {
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

        captureChatDesktopScreenshot("messages", "UserMessage_WithTimestamp", isDark = true, height = 100) {
            ArcaneUserMessageBlock(
                blocks = blocks,
                timestamp = "10:30 AM"
            )
        }
    }

    // ========================================================================
    // Assistant Message
    // ========================================================================

    @Test
    fun assistantMessage_Basic_Light() {
        val blocks = listOf(
            MessageBlock.Text(
                id = "block-1",
                content = "This is an assistant response"
            )
        )

        captureChatDesktopScreenshot("messages", "AssistantMessage_Basic", isDark = false, height = 100) {
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

        captureChatDesktopScreenshot("messages", "AssistantMessage_Basic", isDark = true, height = 100) {
            ArcaneAssistantMessageBlock(
                blocks = blocks,
                enableTruncation = false
            )
        }
    }

    @Test
    fun assistantMessage_WithTitle_Light() {
        val blocks = listOf(
            MessageBlock.Text(
                id = "block-1",
                content = "This is an assistant response with a title"
            )
        )

        captureChatDesktopScreenshot("messages", "AssistantMessage_WithTitle", isDark = false, height = 120) {
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

        captureChatDesktopScreenshot("messages", "AssistantMessage_WithTitle", isDark = true, height = 120) {
            ArcaneAssistantMessageBlock(
                blocks = blocks,
                title = "Claude",
                enableTruncation = false
            )
        }
    }
}
