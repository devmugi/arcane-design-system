package io.github.devmugi.arcane.catalog.chat.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import io.github.devmugi.arcane.catalog.chat.ChatController
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import io.github.devmugi.arcane.catalog.chat.components.ComponentPreview
import io.github.devmugi.arcane.catalog.chat.components.DeviceType
import io.github.devmugi.arcane.catalog.chat.data.MockChatData
import io.github.devmugi.arcane.chat.components.input.ArcaneAgentChatInput
import io.github.devmugi.arcane.chat.components.messages.ArcaneAssistantMessageBlock
import io.github.devmugi.arcane.chat.components.messages.ArcaneChatMessageList
import io.github.devmugi.arcane.chat.components.messages.ArcaneUserMessageBlock
import io.github.devmugi.arcane.chat.components.scaffold.ArcaneChatScreenScaffold
import io.github.devmugi.arcane.chat.models.ChatMessage
import io.github.devmugi.arcane.chat.models.MessageBlock
import io.github.devmugi.arcane.chat.models.Suggestion
import io.github.devmugi.arcane.chat.models.TextStyle
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun ChatScreen(deviceType: DeviceType) {
    val scope = rememberCoroutineScope()

    // Initialize ChatController with mock data if empty
    LaunchedEffect(Unit) {
        if (ChatController.messages.isEmpty()) {
            ChatController.updateMessages(MockChatData.conversationWithSuggestions)
        }
    }

    // Observe messages from ChatController (for both UI and JS bridge changes)
    val messages = ChatController.messages
    // Observe input text from ChatController
    val inputText = ChatController.inputText

    // Function to send message (used by UI)
    fun doSendMessage(text: String) {
        if (text.isBlank()) return

        // Add user message
        val userMessage = ChatMessage.User(
            id = generateId(),
            blocks = listOf(
                MessageBlock.Text(
                    id = generateId(),
                    content = text,
                    style = TextStyle.Body
                )
            ),
            timestamp = getCurrentTimestamp()
        )
        ChatController.updateMessages(ChatController.messages + userMessage)
        ChatController.setInput("")

        // Add loading message
        val loadingId = generateId()
        val loadingMessage = ChatMessage.Assistant(
            id = loadingId,
            title = "Assistant",
            blocks = listOf(
                MessageBlock.Text(
                    id = generateId(),
                    content = "",
                    style = TextStyle.Body
                )
            ),
            isLoading = true,
            timestamp = null
        )
        ChatController.updateMessages(ChatController.messages + loadingMessage)

        // Simulate response delay
        scope.launch {
            delay(800 + (0..500).random().toLong())
            ChatController.updateMessages(ChatController.messages.filterNot { it.id == loadingId })
            val response = ChatMessage.Assistant(
                id = generateId(),
                title = "Assistant",
                blocks = listOf(
                    MessageBlock.Text(
                        id = generateId(),
                        content = generateFakeResponse(text),
                        style = TextStyle.Body
                    )
                ),
                isLoading = false,
                timestamp = getCurrentTimestamp()
            )
            ChatController.updateMessages(ChatController.messages + response)
        }
    }

    // Set up JS bridge callback for full UI-initiated sends
    DisposableEffect(Unit) {
        ChatController.onSendMessage = { text ->
            doSendMessage(text)
        }
        onDispose {
            ChatController.onSendMessage = null
        }
    }

    fun handleSuggestionClick(suggestionText: String) {
        // Add loading message
        val loadingId = generateId()
        val loadingMessage = ChatMessage.Assistant(
            id = loadingId,
            title = "Assistant",
            blocks = listOf(
                MessageBlock.Text(
                    id = generateId(),
                    content = "",
                    style = TextStyle.Body
                )
            ),
            isLoading = true,
            timestamp = null
        )
        ChatController.updateMessages(ChatController.messages + loadingMessage)

        // Simulate response delay
        scope.launch {
            delay(700)
            ChatController.updateMessages(ChatController.messages.filterNot { it.id == loadingId })
            val response = ChatMessage.Assistant(
                id = generateId(),
                title = "Assistant",
                blocks = listOf(
                    MessageBlock.Text(
                        id = generateId(),
                        content = "Great choice! Let me tell you more about $suggestionText...\n\nThis is a simulated response showing how suggestion interactions work in the chat interface.",
                        style = TextStyle.Body
                    )
                ),
                isLoading = false,
                timestamp = getCurrentTimestamp()
            )
            ChatController.updateMessages(ChatController.messages + response)
        }
    }

    fun handleSendMessage() {
        doSendMessage(inputText)
    }

    ComponentPreview(deviceType = deviceType) {
        ArcaneChatScreenScaffold(
            isEmpty = messages.isEmpty(),
            emptyState = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
                ) {
                    Text(
                        text = "No messages yet",
                        style = ArcaneTheme.typography.bodyLarge,
                        color = ArcaneTheme.colors.textSecondary
                    )
                    Text(
                        text = "Start a conversation",
                        style = ArcaneTheme.typography.labelMedium,
                        color = ArcaneTheme.colors.textDisabled
                    )
                }
            },
            bottomBar = {
                ArcaneAgentChatInput(
                    value = inputText,
                    onValueChange = { ChatController.setInput(it) },
                    onSend = { handleSendMessage() },
                    placeholder = "Type a message...",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        ) {
            ArcaneChatMessageList(
                messages = messages,
                messageKey = { it.id }
            ) { message ->
                // Map blocks to inject suggestion callback
                val blocksWithCallback = message.blocks.map { block ->
                    if (block is MessageBlock.AgentSuggestions) {
                        MessageBlock.AgentSuggestions(
                            id = block.id,
                            suggestions = block.suggestions,
                            onSuggestionSelected = { suggestion ->
                                handleSuggestionClick(suggestion.text)
                            }
                        )
                    } else {
                        block
                    }
                }

                when (message) {
                    is ChatMessage.User -> ArcaneUserMessageBlock(
                        blocks = blocksWithCallback,
                        timestamp = message.timestamp
                    )
                    is ChatMessage.Assistant -> ArcaneAssistantMessageBlock(
                        blocks = blocksWithCallback,
                        title = message.title,
                        isLoading = message.isLoading
                    )
                }
            }
        }
    }
}

private var messageIdCounter = 0
private fun generateId(): String = "msg_${messageIdCounter++}"

// Use expect/actual for timestamp to avoid kotlinx-datetime WASM issues
internal expect fun getCurrentTimestamp(): String

private fun generateFakeResponse(userInput: String): String {
    val lowerInput = userInput.lowercase()

    return when {
        lowerInput.contains("hello") || lowerInput.contains("hi") || lowerInput.contains("hey") ->
            "Hello! How can I help you today? Feel free to ask me anything about Compose, Kotlin, or UI development."

        lowerInput.contains("how are you") ->
            "I'm doing great, thanks for asking! I'm here to help you with any questions you might have."

        lowerInput.contains("compose") ->
            "Jetpack Compose is Android's modern toolkit for building native UI. It simplifies and accelerates UI development with less code, powerful tools, and intuitive Kotlin APIs.\n\nKey benefits include:\n- Declarative UI\n- Less boilerplate code\n- Powerful state management\n- Great tooling support"

        lowerInput.contains("kotlin") ->
            "Kotlin is a modern programming language that makes developers happier. It's concise, safe, and fully interoperable with Java.\n\nKotlin Multiplatform lets you share code across platforms while keeping native performance."

        lowerInput.contains("help") ->
            "I'd be happy to help! Here are some things I can assist with:\n\n1. UI development questions\n2. Compose best practices\n3. State management patterns\n4. Architecture guidance\n\nWhat would you like to know more about?"

        lowerInput.contains("thank") ->
            "You're welcome! Let me know if there's anything else I can help you with."

        lowerInput.contains("?") ->
            "That's a great question! Based on my understanding, I'd suggest exploring the documentation for more detailed information. Would you like me to elaborate on any specific aspect?"

        else -> {
            val responses = listOf(
                "I understand you're interested in \"$userInput\". Let me provide some insights on that topic.",
                "Thanks for sharing that! Here's what I think about \"$userInput\"...\n\nThis is a fascinating area with lots of potential for exploration.",
                "Interesting point about \"$userInput\"! There are several approaches you could consider here.",
                "I see you mentioned \"$userInput\". That's definitely worth exploring further. What specific aspect would you like to dive into?",
                "Great topic! \"$userInput\" is something many developers are curious about. Let me share some thoughts..."
            )
            responses.random()
        }
    }
}
