package io.github.devmugi.arcane.chat.messages

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import io.github.devmugi.arcane.chat.components.messages.ArcaneAssistantMessageBlock
import io.github.devmugi.arcane.chat.components.messages.ArcaneUserMessageBlock
import io.github.devmugi.arcane.chat.models.MessageBlock
import io.github.devmugi.arcane.design.testing.TestArcaneTheme
import org.junit.Rule
import org.junit.Test

class ChatMessageUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `user message displays content`() {
        val userText = "Hello, this is a user message"
        val blocks = listOf(
            MessageBlock.Text(
                id = "block-1",
                content = userText
            )
        )

        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneUserMessageBlock(blocks = blocks)
            }
        }

        composeTestRule.onNodeWithText(userText).assertExists()
    }

    @Test
    fun `user message displays timestamp when provided`() {
        val timestamp = "10:30 AM"
        val blocks = listOf(
            MessageBlock.Text(
                id = "block-1",
                content = "Message with timestamp"
            )
        )

        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneUserMessageBlock(
                    blocks = blocks,
                    timestamp = timestamp
                )
            }
        }

        composeTestRule.onNodeWithText(timestamp).assertExists()
    }

    @Test
    fun `assistant message displays title and content`() {
        val title = "Claude"
        val contentText = "This is an assistant response"
        val blocks = listOf(
            MessageBlock.Text(
                id = "block-1",
                content = contentText
            )
        )

        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneAssistantMessageBlock(
                    blocks = blocks,
                    title = title
                )
            }
        }

        composeTestRule.onNodeWithText(title).assertExists()
        composeTestRule.onNodeWithText(contentText).assertExists()
    }

    @Test
    fun `assistant message displays multiple text blocks`() {
        val firstBlock = "First paragraph of content"
        val secondBlock = "Second paragraph of content"
        val blocks = listOf(
            MessageBlock.Text(id = "block-1", content = firstBlock),
            MessageBlock.Text(id = "block-2", content = secondBlock)
        )

        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneAssistantMessageBlock(
                    blocks = blocks,
                    enableTruncation = false
                )
            }
        }

        composeTestRule.onNodeWithText(firstBlock).assertExists()
        composeTestRule.onNodeWithText(secondBlock).assertExists()
    }

    @Test
    fun `assistant message without title renders content only`() {
        val contentText = "Content without title"
        val blocks = listOf(
            MessageBlock.Text(
                id = "block-1",
                content = contentText
            )
        )

        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneAssistantMessageBlock(
                    blocks = blocks,
                    title = null
                )
            }
        }

        composeTestRule.onNodeWithText(contentText).assertExists()
    }
}
