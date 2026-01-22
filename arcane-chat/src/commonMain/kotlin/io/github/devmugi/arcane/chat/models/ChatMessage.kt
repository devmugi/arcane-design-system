package io.github.devmugi.arcane.chat.models

/**
 * Represents a message in a chat conversation.
 * Messages contain blocks of content rather than simple text.
 *
 * Breaking changes from v0.2.x:
 * - Removed ChatMessage.User.text property
 * - Removed ChatMessage.Assistant.content property
 * - Added blocks: List<MessageBlock> to both types
 */
sealed class ChatMessage {
    abstract val id: String
    abstract val timestamp: String?
    abstract val blocks: List<MessageBlock>

    /**
     * Message sent by the user.
     */
    data class User(
        override val id: String,
        override val blocks: List<MessageBlock>,
        override val timestamp: String? = null
    ) : ChatMessage()

    /**
     * Message sent by the assistant/AI.
     */
    data class Assistant(
        override val id: String,
        val title: String = "Assistant",
        override val blocks: List<MessageBlock>,
        val isLoading: Boolean = false,
        override val timestamp: String? = null
    ) : ChatMessage()
}
