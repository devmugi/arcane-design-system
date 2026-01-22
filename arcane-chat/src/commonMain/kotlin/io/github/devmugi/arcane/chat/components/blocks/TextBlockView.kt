package io.github.devmugi.arcane.chat.components.blocks

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import io.github.devmugi.arcane.chat.models.MessageBlock
import io.github.devmugi.arcane.chat.models.TextStyle
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme

/**
 * Renders a text block with appropriate styling.
 *
 * @param block The text block to render
 * @param modifier Optional modifier for the text component
 */
@Composable
fun TextBlockView(
    block: MessageBlock.Text,
    modifier: Modifier = Modifier
) {
    val typography = ArcaneTheme.typography
    val colors = ArcaneTheme.colors

    val style = when (block.style) {
        TextStyle.Body -> typography.bodyMedium
        TextStyle.Heading -> typography.headlineMedium
        TextStyle.Code -> typography.bodySmall.copy(fontFamily = FontFamily.Monospace)
        TextStyle.Caption -> typography.labelMedium
    }

    Text(
        text = block.content,
        style = style,
        color = colors.text,
        modifier = modifier
    )
}
