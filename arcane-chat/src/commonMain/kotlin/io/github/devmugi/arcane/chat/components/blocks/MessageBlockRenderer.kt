package io.github.devmugi.arcane.chat.components.blocks

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.devmugi.arcane.chat.models.MessageBlock

/**
 * Renders a message block based on its type.
 * Delegates to specific block view components.
 *
 * @param block The block to render
 * @param modifier Optional modifier applied to the block view
 */
@Composable
fun MessageBlockRenderer(
    block: MessageBlock,
    modifier: Modifier = Modifier
) {
    when (block) {
        is MessageBlock.Text -> TextBlockView(block, modifier)
        is MessageBlock.Image -> ImageBlockView(block, modifier)
        is MessageBlock.AgentSuggestions -> AgentSuggestionsBlockView(block, modifier)
        is MessageBlock.Custom -> block.content()
    }
}
