package io.github.devmugi.arcane.catalog.chat

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import io.github.devmugi.arcane.chat.models.ChatMessage
import io.github.devmugi.arcane.chat.models.MessageBlock
import io.github.devmugi.arcane.chat.models.TextStyle

/**
 * Shared state controller for the chat screen.
 * Enables JavaScript bridge to interact with chat state in WASM builds.
 */
object ChatController {
    var messages by mutableStateOf<List<ChatMessage>>(emptyList())

    var inputText by mutableStateOf("")

    var onSendMessage: ((String) -> Unit)? = null

    private var messageIdCounter = 1000 // Start high to avoid conflicts with mock data

    fun sendMessage(text: String) {
        if (text.isNotBlank()) {
            // Try callback first (for UI-initiated sends with full logic)
            val callback = onSendMessage
            if (callback != null) {
                callback.invoke(text)
            } else {
                // Fallback: directly add message (for JS bridge calls)
                addUserMessage(text)
            }
        }
    }

    private fun addUserMessage(text: String) {
        val userMessage = ChatMessage.User(
            id = "ctrl_${messageIdCounter++}",
            blocks = listOf(
                MessageBlock.Text(
                    id = "ctrl_${messageIdCounter++}",
                    content = text,
                    style = TextStyle.Body
                )
            ),
            timestamp = "Now"
        )
        messages = messages + userMessage

        // Also add a simple response
        val responseMessage = ChatMessage.Assistant(
            id = "ctrl_${messageIdCounter++}",
            title = "Assistant",
            blocks = listOf(
                MessageBlock.Text(
                    id = "ctrl_${messageIdCounter++}",
                    content = "I received your message: \"$text\"",
                    style = TextStyle.Body
                )
            ),
            isLoading = false,
            timestamp = "Now"
        )
        messages = messages + responseMessage
    }

    fun setInput(text: String) {
        inputText = text
    }

    fun updateMessages(newMessages: List<ChatMessage>) {
        messages = newMessages
    }

    fun clearMessages() {
        messages = emptyList()
    }
}
