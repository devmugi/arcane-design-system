package io.github.devmugi.arcane.chat.components.messages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.chat.components.blocks.MessageBlockRenderer
import io.github.devmugi.arcane.chat.models.MessageBlock
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

/**
 * Default shape for outlined user messages.
 * Asymmetric corners: rounded on left and bottom-right, small radius on top-right.
 */
private val OutlinedUserMessageShape = RoundedCornerShape(
    topStart = 12.dp,
    topEnd = 4.dp,
    bottomStart = 12.dp,
    bottomEnd = 12.dp
)

/**
 * Colors for [ArcaneOutlinedUserMessageBlock].
 *
 * @param containerColor Background color (typically transparent for outlined style)
 * @param contentColor Color for text content
 * @param borderColor Color for the outline border
 */
@Immutable
data class OutlinedUserMessageBlockColors(
    val containerColor: Color,
    val contentColor: Color,
    val borderColor: Color
)

/**
 * Default values for [ArcaneOutlinedUserMessageBlock].
 */
object OutlinedUserMessageBlockDefaults {
    /** Default shape with asymmetric rounded corners */
    val Shape: Shape = OutlinedUserMessageShape

    /** Default content padding */
    val ContentPadding = PaddingValues(
        horizontal = ArcaneSpacing.Small,
        vertical = ArcaneSpacing.XSmall
    )

    /** Default maximum width for the message bubble */
    val MaxWidth = 280.dp

    /** Default border width */
    val BorderWidth = 1.dp

    /**
     * Creates default colors for outlined user message block.
     *
     * @param containerColor Background color, defaults to transparent
     * @param contentColor Text color, defaults to theme text color
     * @param borderColor Border color, defaults to primary with 40% opacity
     */
    @Composable
    fun colors(
        containerColor: Color = Color.Transparent,
        contentColor: Color = ArcaneTheme.colors.text,
        borderColor: Color = ArcaneTheme.colors.primary.copy(alpha = 0.4f)
    ): OutlinedUserMessageBlockColors = OutlinedUserMessageBlockColors(
        containerColor = containerColor,
        contentColor = contentColor,
        borderColor = borderColor
    )

    /**
     * Creates a border stroke with default or custom values.
     *
     * @param color Border color, defaults to primary with 40% opacity
     * @param width Border width, defaults to 1.dp
     */
    @Composable
    fun border(
        color: Color = ArcaneTheme.colors.primary.copy(alpha = 0.4f),
        width: Dp = BorderWidth
    ): BorderStroke = BorderStroke(width, color)
}

/**
 * Renders a user message block with outlined border style.
 *
 * This variant uses a border outline instead of a filled background,
 * providing a lighter, more modern visual style. The message is
 * right-aligned to distinguish it from assistant messages.
 *
 * @param blocks List of content blocks to render
 * @param modifier Optional modifier for the container
 * @param timestamp Optional timestamp to display below content
 * @param maxWidth Maximum width of the message bubble
 * @param shape Shape of the message bubble
 * @param border Border stroke for the outline
 * @param colors Color configuration for the component
 * @param contentPadding Padding inside the message bubble
 *
 * @see ArcaneUserMessageBlock for the filled background variant
 */
@Composable
fun ArcaneOutlinedUserMessageBlock(
    blocks: List<MessageBlock>,
    modifier: Modifier = Modifier,
    timestamp: String? = null,
    maxWidth: Dp = OutlinedUserMessageBlockDefaults.MaxWidth,
    shape: Shape = OutlinedUserMessageBlockDefaults.Shape,
    border: BorderStroke = OutlinedUserMessageBlockDefaults.border(),
    colors: OutlinedUserMessageBlockColors = OutlinedUserMessageBlockDefaults.colors(),
    contentPadding: PaddingValues = OutlinedUserMessageBlockDefaults.ContentPadding
) {
    val typography = ArcaneTheme.typography

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = maxWidth)
                .clip(shape)
                .border(border, shape)
                .padding(contentPadding),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)
        ) {
            // Render all blocks
            blocks.forEach { block ->
                MessageBlockRenderer(block)
            }

            // Optional timestamp
            if (timestamp != null) {
                Text(
                    text = timestamp,
                    style = typography.labelSmall,
                    color = colors.contentColor.copy(alpha = 0.6f),
                    modifier = Modifier.padding(top = ArcaneSpacing.XXSmall)
                )
            }
        }
    }
}

/**
 * Renders a user message block with outlined border style using a slot API.
 *
 * This overload accepts a composable content slot instead of MessageBlock list,
 * providing maximum flexibility for custom content rendering.
 *
 * @param modifier Optional modifier for the container
 * @param maxWidth Maximum width of the message bubble
 * @param shape Shape of the message bubble
 * @param border Border stroke for the outline
 * @param colors Color configuration for the component
 * @param contentPadding Padding inside the message bubble
 * @param content Composable content to render inside the message bubble
 */
@Composable
fun ArcaneOutlinedUserMessageBlock(
    modifier: Modifier = Modifier,
    maxWidth: Dp = OutlinedUserMessageBlockDefaults.MaxWidth,
    shape: Shape = OutlinedUserMessageBlockDefaults.Shape,
    border: BorderStroke = OutlinedUserMessageBlockDefaults.border(),
    colors: OutlinedUserMessageBlockColors = OutlinedUserMessageBlockDefaults.colors(),
    contentPadding: PaddingValues = OutlinedUserMessageBlockDefaults.ContentPadding,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = maxWidth)
                .clip(shape)
                .border(border, shape)
                .padding(contentPadding)
        ) {
            content()
        }
    }
}
