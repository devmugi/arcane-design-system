package io.github.devmugi.arcane.catalog.chat.data

import io.github.devmugi.arcane.chat.models.ChatMessage

object MockChatData {
    val sampleConversation = listOf(
        ChatMessage.User(
            id = "1",
            text = "Hello, Claude!",
            timestamp = "2:30 PM"
        ),
        ChatMessage.Assistant(
            id = "2",
            content = "Hello! I'd be happy to help you with that.",
            timestamp = "2:30 PM"
        ),
        ChatMessage.User(
            id = "3",
            text = "Can you help me understand how to implement a chat interface in Compose?",
            timestamp = "2:34 PM"
        ),
        ChatMessage.Assistant(
            id = "4",
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
            timestamp = "2:34 PM"
        )
    )

    val shortConversation = listOf(
        ChatMessage.User(id = "1", text = "Hi!"),
        ChatMessage.Assistant(id = "2", content = "Hello! How can I help?")
    )

    val emptyConversation = emptyList<ChatMessage>()

    val loadingConversation = sampleConversation + ChatMessage.Assistant(
        id = "loading",
        content = "Thinking...",
        isLoading = true
    )

    // Individual message samples for MessageBlocksScreen
    val sampleUserMessage = ChatMessage.User(
        id = "sample-user",
        text = "This is a sample user message",
        timestamp = "2:34 PM"
    )

    val sampleAssistantMessage = ChatMessage.Assistant(
        id = "sample-assistant",
        content = "This is a sample assistant message with some helpful information.",
        timestamp = "2:34 PM"
    )

    val longAssistantMessage = ChatMessage.Assistant(
        id = "sample-long",
        content = """
            This is a much longer assistant message that will demonstrate the truncation behavior.

            It contains multiple paragraphs and should trigger the "Show more" functionality
            when the content exceeds the maximum height threshold.

            You can use this to test how the message block handles overflow content
            and whether the fade gradient appears correctly.
        """.trimIndent()
    )
}
