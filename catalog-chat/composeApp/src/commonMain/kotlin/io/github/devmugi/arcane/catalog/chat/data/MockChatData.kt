package io.github.devmugi.arcane.catalog.chat.data

import io.github.devmugi.arcane.chat.models.ChatMessage
import io.github.devmugi.arcane.chat.models.MessageBlock
import io.github.devmugi.arcane.chat.models.TextStyle

object MockChatData {
    val sampleConversation = listOf(
        ChatMessage.User(
            id = "1",
            blocks = listOf(
                MessageBlock.Text(
                    id = "1-text",
                    content = "Hello, Claude!",
                    style = TextStyle.Body
                )
            ),
            timestamp = "2:30 PM"
        ),
        ChatMessage.Assistant(
            id = "2",
            blocks = listOf(
                MessageBlock.Text(
                    id = "2-text",
                    content = "Hello! I'd be happy to help you with that.",
                    style = TextStyle.Body
                )
            ),
            timestamp = "2:30 PM"
        ),
        ChatMessage.User(
            id = "3",
            blocks = listOf(
                MessageBlock.Text(
                    id = "3-text",
                    content = "Can you help me understand how to implement a chat interface in Compose?",
                    style = TextStyle.Body
                )
            ),
            timestamp = "2:34 PM"
        ),
        ChatMessage.Assistant(
            id = "4",
            blocks = listOf(
                MessageBlock.Text(
                    id = "4-text",
                    content = """
                        I'd be happy to help you understand chat interfaces in Compose! Here are the key concepts:

                        1. **LazyColumn for messages**: Use LazyColumn to efficiently render message lists. It only composes visible items.

                        2. **State management**: Keep your messages in a ViewModel or state holder. Use mutableStateListOf for reactive updates.

                        3. **Message alignment**: User messages typically align right, assistant messages align left.

                        4. **Auto-scroll behavior**: When new messages arrive, scroll to bottom if user was already at bottom. Show a "scroll to bottom" button if scrolled up.

                        5. **Input handling**: Use BasicTextField with keyboard actions for the input area.

                        6. **Loading states**: Show typing indicators or progress when waiting for responses.

                        Would you like me to elaborate on any of these points?
                    """.trimIndent(),
                    style = TextStyle.Body
                )
            ),
            timestamp = "2:34 PM"
        )
    )

    val shortConversation = listOf(
        ChatMessage.User(
            id = "1",
            blocks = listOf(
                MessageBlock.Text(id = "1-text", content = "Hi!", style = TextStyle.Body)
            )
        ),
        ChatMessage.Assistant(
            id = "2",
            blocks = listOf(
                MessageBlock.Text(id = "2-text", content = "Hello! How can I help?", style = TextStyle.Body)
            )
        )
    )

    val emptyConversation = emptyList<ChatMessage>()

    val loadingConversation = sampleConversation + ChatMessage.Assistant(
        id = "loading",
        blocks = listOf(
            MessageBlock.Text(id = "loading-text", content = "Thinking...", style = TextStyle.Body)
        ),
        isLoading = true
    )

    // Individual message samples for MessageBlocksScreen
    val sampleUserMessage = ChatMessage.User(
        id = "sample-user",
        blocks = listOf(
            MessageBlock.Text(
                id = "sample-user-text",
                content = "This is a sample user message",
                style = TextStyle.Body
            )
        ),
        timestamp = "2:34 PM"
    )

    val sampleAssistantMessage = ChatMessage.Assistant(
        id = "sample-assistant",
        blocks = listOf(
            MessageBlock.Text(
                id = "sample-assistant-text",
                content = "This is a sample assistant message with some helpful information.",
                style = TextStyle.Body
            )
        ),
        timestamp = "2:34 PM"
    )

    val longAssistantMessage = ChatMessage.Assistant(
        id = "sample-long",
        blocks = listOf(
            MessageBlock.Text(
                id = "sample-long-text",
                content = """
                    This is a much longer assistant message that will demonstrate the truncation behavior.

                    It contains multiple paragraphs and should trigger the "Show more" functionality
                    when the content exceeds the maximum height threshold.

                    You can use this to test how the message block handles overflow content
                    and whether the fade gradient appears correctly.
                """.trimIndent(),
                style = TextStyle.Body
            )
        )
    )
}
