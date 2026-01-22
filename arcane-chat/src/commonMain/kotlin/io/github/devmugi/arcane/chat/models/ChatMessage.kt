package io.github.devmugi.arcane.chat.models

sealed class ChatMessage {
    abstract val id: String
    abstract val timestamp: String?

    data class User(
        override val id: String,
        val text: String,
        override val timestamp: String? = null
    ) : ChatMessage()

    data class Assistant(
        override val id: String,
        val title: String = "Claude",
        val content: String,
        val isLoading: Boolean = false,
        override val timestamp: String? = null
    ) : ChatMessage()
}
