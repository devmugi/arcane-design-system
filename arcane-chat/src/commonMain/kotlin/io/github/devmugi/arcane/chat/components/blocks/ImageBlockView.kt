package io.github.devmugi.arcane.chat.components.blocks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import io.github.devmugi.arcane.chat.models.MessageBlock
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

/**
 * Renders an image block.
 * Currently shows a placeholder with alt text until AsyncImage is integrated.
 *
 * @param block The image block to render
 * @param modifier Optional modifier for the image container
 */
@Composable
fun ImageBlockView(
    block: MessageBlock.Image,
    modifier: Modifier = Modifier
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography

    Box(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (block.aspectRatio != null) {
                    Modifier.aspectRatio(block.aspectRatio)
                } else {
                    Modifier
                }
            )
            .clip(ArcaneRadius.Medium)
            .background(colors.surfaceContainerLow),
        contentAlignment = Alignment.Center
    ) {
        // Placeholder for v1 - will be replaced with AsyncImage later
        Text(
            text = block.alt ?: "Image",
            style = typography.labelSmall,
            color = colors.textDisabled,
            modifier = Modifier.padding(ArcaneSpacing.Medium)
        )
    }
}
