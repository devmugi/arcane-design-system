package io.github.devmugi.arcane.chat.model

import io.github.devmugi.arcane.chat.models.ChatMessage
import io.github.devmugi.arcane.chat.models.MessageBlock
import io.github.devmugi.arcane.chat.models.TextStyle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Unit tests for chat message models.
 *
 * Tests the data classes and sealed classes in the chat module:
 * - ChatMessage (User, Assistant)
 * - MessageBlock (Text, Image, AgentSuggestions, Custom)
 * - TextStyle enum
 */
class MessageTest {

    // ==================== ChatMessage Tests ====================

    @Test
    fun userMessageCreatedWithRequiredFields() {
        val textBlock = MessageBlock.Text(
            id = "block-1",
            content = "Hello, assistant!"
        )
        val message = ChatMessage.User(
            id = "msg-1",
            blocks = listOf(textBlock)
        )

        assertEquals("msg-1", message.id)
        assertEquals(1, message.blocks.size)
        assertNull(message.timestamp)
    }

    @Test
    fun userMessageCreatedWithTimestamp() {
        val textBlock = MessageBlock.Text(
            id = "block-1",
            content = "Hello!"
        )
        val message = ChatMessage.User(
            id = "msg-1",
            blocks = listOf(textBlock),
            timestamp = "2024-01-15T10:30:00Z"
        )

        assertEquals("2024-01-15T10:30:00Z", message.timestamp)
    }

    @Test
    fun assistantMessageCreatedWithDefaults() {
        val textBlock = MessageBlock.Text(
            id = "block-1",
            content = "Hello, user!"
        )
        val message = ChatMessage.Assistant(
            id = "msg-2",
            blocks = listOf(textBlock)
        )

        assertEquals("msg-2", message.id)
        assertEquals("Assistant", message.title)
        assertFalse(message.isLoading)
        assertNull(message.timestamp)
    }

    @Test
    fun assistantMessageCreatedWithCustomTitle() {
        val textBlock = MessageBlock.Text(
            id = "block-1",
            content = "Analyzing..."
        )
        val message = ChatMessage.Assistant(
            id = "msg-2",
            title = "Claude",
            blocks = listOf(textBlock),
            isLoading = true
        )

        assertEquals("Claude", message.title)
        assertTrue(message.isLoading)
    }

    @Test
    fun chatMessageSealedClassPolymorphism() {
        val userBlock = MessageBlock.Text(id = "b1", content = "User text")
        val assistantBlock = MessageBlock.Text(id = "b2", content = "Assistant text")

        val messages: List<ChatMessage> = listOf(
            ChatMessage.User(id = "1", blocks = listOf(userBlock)),
            ChatMessage.Assistant(id = "2", blocks = listOf(assistantBlock))
        )

        assertIs<ChatMessage.User>(messages[0])
        assertIs<ChatMessage.Assistant>(messages[1])
    }

    // ==================== MessageBlock Tests ====================

    @Test
    fun textBlockCreatedWithDefaultStyle() {
        val block = MessageBlock.Text(
            id = "text-1",
            content = "Hello world"
        )

        assertEquals("text-1", block.id)
        assertEquals("Hello world", block.content)
        assertEquals(TextStyle.Body, block.style)
    }

    @Test
    fun textBlockCreatedWithCustomStyle() {
        val block = MessageBlock.Text(
            id = "text-1",
            content = "# Heading",
            style = TextStyle.Heading
        )

        assertEquals(TextStyle.Heading, block.style)
    }

    @Test
    fun imageBlockCreatedWithRequiredFields() {
        val block = MessageBlock.Image(
            id = "img-1",
            url = "https://example.com/image.png"
        )

        assertEquals("img-1", block.id)
        assertEquals("https://example.com/image.png", block.url)
        assertNull(block.alt)
        assertNull(block.aspectRatio)
    }

    @Test
    fun imageBlockCreatedWithAllFields() {
        val block = MessageBlock.Image(
            id = "img-1",
            url = "https://example.com/image.png",
            alt = "A sample image",
            aspectRatio = 16f / 9f
        )

        assertEquals("A sample image", block.alt)
        assertEquals(16f / 9f, block.aspectRatio)
    }

    @Test
    fun messageBlockSealedClassPolymorphism() {
        val blocks: List<MessageBlock> = listOf(
            MessageBlock.Text(id = "1", content = "Text"),
            MessageBlock.Image(id = "2", url = "https://example.com/img.png")
        )

        assertIs<MessageBlock.Text>(blocks[0])
        assertIs<MessageBlock.Image>(blocks[1])
    }

    @Test
    fun messageBlockIdAccessibleThroughBaseType() {
        val block: MessageBlock = MessageBlock.Text(id = "base-id", content = "Test")
        assertEquals("base-id", block.id)
    }

    // ==================== TextStyle Enum Tests ====================

    @Test
    fun textStyleEnumContainsExpectedValues() {
        val styles = TextStyle.entries

        assertEquals(4, styles.size)
        assertTrue(styles.contains(TextStyle.Body))
        assertTrue(styles.contains(TextStyle.Heading))
        assertTrue(styles.contains(TextStyle.Code))
        assertTrue(styles.contains(TextStyle.Caption))
    }

    @Test
    fun textStyleEnumValuesHaveCorrectOrdinals() {
        assertEquals(0, TextStyle.Body.ordinal)
        assertEquals(1, TextStyle.Heading.ordinal)
        assertEquals(2, TextStyle.Code.ordinal)
        assertEquals(3, TextStyle.Caption.ordinal)
    }

    // ==================== Data Class Equality Tests ====================

    @Test
    fun textBlocksWithSameContentAreEqual() {
        val block1 = MessageBlock.Text(id = "1", content = "Hello", style = TextStyle.Body)
        val block2 = MessageBlock.Text(id = "1", content = "Hello", style = TextStyle.Body)

        assertEquals(block1, block2)
    }

    @Test
    fun userMessagesWithSameContentAreEqual() {
        val block = MessageBlock.Text(id = "1", content = "Test")
        val message1 = ChatMessage.User(id = "msg-1", blocks = listOf(block))
        val message2 = ChatMessage.User(id = "msg-1", blocks = listOf(block))

        assertEquals(message1, message2)
    }

    @Test
    fun assistantMessagesWithSameContentAreEqual() {
        val block = MessageBlock.Text(id = "1", content = "Test")
        val message1 = ChatMessage.Assistant(
            id = "msg-1",
            title = "Bot",
            blocks = listOf(block),
            isLoading = false
        )
        val message2 = ChatMessage.Assistant(
            id = "msg-1",
            title = "Bot",
            blocks = listOf(block),
            isLoading = false
        )

        assertEquals(message1, message2)
    }

    // ==================== Copy Tests ====================

    @Test
    fun userMessageCopyWithModification() {
        val block = MessageBlock.Text(id = "1", content = "Original")
        val original = ChatMessage.User(id = "msg-1", blocks = listOf(block))
        val copied = original.copy(timestamp = "2024-01-15T12:00:00Z")

        assertEquals(original.id, copied.id)
        assertEquals(original.blocks, copied.blocks)
        assertEquals("2024-01-15T12:00:00Z", copied.timestamp)
    }

    @Test
    fun assistantMessageCopyWithModification() {
        val block = MessageBlock.Text(id = "1", content = "Loading...")
        val original = ChatMessage.Assistant(
            id = "msg-1",
            blocks = listOf(block),
            isLoading = true
        )
        val completed = original.copy(isLoading = false)

        assertEquals(original.id, completed.id)
        assertEquals(original.blocks, completed.blocks)
        assertTrue(original.isLoading)
        assertFalse(completed.isLoading)
    }

    @Test
    fun textBlockCopyWithStyleChange() {
        val original = MessageBlock.Text(id = "1", content = "Text", style = TextStyle.Body)
        val modified = original.copy(style = TextStyle.Code)

        assertEquals(original.id, modified.id)
        assertEquals(original.content, modified.content)
        assertEquals(TextStyle.Body, original.style)
        assertEquals(TextStyle.Code, modified.style)
    }

    // ==================== Multiple Blocks Tests ====================

    @Test
    fun messageCanContainMultipleBlocks() {
        val blocks = listOf(
            MessageBlock.Text(id = "1", content = "Here is an image:"),
            MessageBlock.Image(id = "2", url = "https://example.com/img.png"),
            MessageBlock.Text(id = "3", content = "What do you think?")
        )
        val message = ChatMessage.User(id = "msg-1", blocks = blocks)

        assertEquals(3, message.blocks.size)
        assertIs<MessageBlock.Text>(message.blocks[0])
        assertIs<MessageBlock.Image>(message.blocks[1])
        assertIs<MessageBlock.Text>(message.blocks[2])
    }

    @Test
    fun messageCanBeCreatedWithEmptyBlocks() {
        val message = ChatMessage.User(id = "empty-msg", blocks = emptyList())

        assertEquals(0, message.blocks.size)
    }
}
