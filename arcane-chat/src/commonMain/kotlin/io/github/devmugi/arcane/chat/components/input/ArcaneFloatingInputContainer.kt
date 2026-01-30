package io.github.devmugi.arcane.chat.components.input

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme

/**
 * Colors for [ArcaneFloatingInputContainer].
 *
 * @param containerColor Background color for the blurred container
 * @param gradientColor Color for the top fade gradient (typically matches screen background)
 */
@Immutable
data class FloatingInputContainerColors(
    val containerColor: Color,
    val gradientColor: Color
)

/**
 * Default values for [ArcaneFloatingInputContainer].
 */
object FloatingInputContainerDefaults {
    /** Default blur radius for the background */
    val BlurRadius = 20.dp

    /** Default horizontal padding */
    val HorizontalPadding = 16.dp

    /** Default content padding */
    val ContentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)

    /** Default gradient height for top fade effect */
    val GradientHeight = 24.dp

    /**
     * Creates default colors for floating input container.
     *
     * @param containerColor Blurred background color, defaults to surface with 85% opacity
     * @param gradientColor Top gradient color, defaults to surface (for fade-in effect)
     */
    @Composable
    fun colors(
        containerColor: Color = ArcaneTheme.colors.surfaceContainerLow.copy(alpha = 0.85f),
        gradientColor: Color = ArcaneTheme.colors.surfaceContainerLow
    ): FloatingInputContainerColors = FloatingInputContainerColors(
        containerColor = containerColor,
        gradientColor = gradientColor
    )
}

/**
 * A floating container for chat input with blurred background effect.
 *
 * This container provides a semi-transparent, blurred background that floats
 * over scrollable content. It includes an optional top gradient for a smooth
 * fade-in effect from the content above.
 *
 * Typically used at the bottom of a chat screen to hold the input field,
 * allowing content to scroll underneath while maintaining input visibility.
 *
 * @param modifier Modifier for the outer container
 * @param blur Blur radius for the background effect
 * @param colors Color configuration
 * @param contentPadding Padding around the content
 * @param showGradient Whether to show the top gradient fade
 * @param gradientHeight Height of the top gradient
 * @param content Content to display inside the container
 *
 * Example:
 * ```
 * ArcaneFloatingInputContainer {
 *     ArcaneAgentChatInput(
 *         value = text,
 *         onValueChange = { text = it },
 *         onSend = { sendMessage() }
 *     )
 * }
 * ```
 */
@Composable
fun ArcaneFloatingInputContainer(
    modifier: Modifier = Modifier,
    blur: Dp = FloatingInputContainerDefaults.BlurRadius,
    colors: FloatingInputContainerColors = FloatingInputContainerDefaults.colors(),
    contentPadding: PaddingValues = FloatingInputContainerDefaults.ContentPadding,
    showGradient: Boolean = true,
    gradientHeight: Dp = FloatingInputContainerDefaults.GradientHeight,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Optional top gradient for fade effect
        if (showGradient) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                colors.gradientColor
                            )
                        )
                    )
                    .padding(top = gradientHeight)
            )
        }

        // Main container with blur
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Background layer with blur
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .blur(radius = blur)
                    .background(colors.containerColor)
            )

            // Content layer (no blur)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = content
            )
        }
    }
}
