package io.github.devmugi.arcane.chat.models

import androidx.compose.runtime.Composable

/**
 * Represents a content block within a chat message.
 * Supports text, images, interactive suggestions, and custom composables.
 */
sealed class MessageBlock {
    abstract val id: String

    /**
     * Text content block with optional styling.
     */
    data class Text(
        override val id: String,
        val content: String,
        val style: TextStyle = TextStyle.Body
    ) : MessageBlock()

    /**
     * Image block with URL and optional metadata.
     */
    data class Image(
        override val id: String,
        val url: String,
        val alt: String? = null,
        val aspectRatio: Float? = null
    ) : MessageBlock()

    /**
     * Interactive suggestion chips with expandable details.
     */
    data class AgentSuggestions(
        override val id: String,
        val suggestions: List<Suggestion>,
        val onSuggestionSelected: (Suggestion) -> Unit
    ) : MessageBlock()

    /**
     * Custom composable block for extensibility.
     * Allows external code to inject any composable without modifying the library.
     */
    data class Custom(
        override val id: String,
        val content: @Composable () -> Unit
    ) : MessageBlock()
}

/**
 * Text styling options for Text blocks.
 */
enum class TextStyle {
    Body,
    Heading,
    Code,
    Caption
}
