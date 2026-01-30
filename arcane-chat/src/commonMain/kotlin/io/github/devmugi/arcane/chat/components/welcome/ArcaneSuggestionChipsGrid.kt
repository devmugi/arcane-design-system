package io.github.devmugi.arcane.chat.components.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

/**
 * Colors for [ArcaneTextSuggestionChip].
 *
 * @param containerColor Background color of the chip
 * @param contentColor Text color
 * @param borderColor Border color
 */
@Immutable
data class TextSuggestionChipColors(
    val containerColor: Color,
    val contentColor: Color,
    val borderColor: Color
)

/**
 * Default values for [ArcaneSuggestionChipsGrid] and [ArcaneTextSuggestionChip].
 */
object ArcaneSuggestionChipsGridDefaults {
    /** Default number of columns */
    const val Columns = 2

    /** Default horizontal spacing between chips */
    val HorizontalSpacing = 8.dp

    /** Default vertical spacing between rows */
    val VerticalSpacing = 8.dp

    /**
     * Creates default colors for text suggestion chips.
     *
     * @param containerColor Background color, defaults to transparent
     * @param contentColor Text color, defaults to theme text color
     * @param borderColor Border color, defaults to primary with 40% opacity
     */
    @Composable
    fun chipColors(
        containerColor: Color = Color.Transparent,
        contentColor: Color = ArcaneTheme.colors.text,
        borderColor: Color = ArcaneTheme.colors.primary.copy(alpha = 0.4f)
    ): TextSuggestionChipColors = TextSuggestionChipColors(
        containerColor = containerColor,
        contentColor = contentColor,
        borderColor = borderColor
    )
}

/**
 * A grid layout for suggestion chips.
 *
 * Arranges suggestions in a grid with configurable columns and spacing.
 * By default, uses [ArcaneTextSuggestionChip] for rendering, but can be
 * customized via the chipContent parameter.
 *
 * @param suggestions List of suggestion strings to display
 * @param onSuggestionClick Callback when a suggestion is clicked
 * @param modifier Modifier for the container
 * @param columns Number of columns in the grid
 * @param horizontalArrangement Horizontal arrangement of chips in each row
 * @param verticalArrangement Vertical arrangement of rows
 * @param chipContent Custom chip rendering, defaults to ArcaneTextSuggestionChip
 *
 * Example:
 * ```
 * ArcaneSuggestionChipsGrid(
 *     suggestions = listOf(
 *         "Tell me about your experience",
 *         "What projects have you worked on?",
 *         "What are your skills?",
 *         "How can I contact you?"
 *     ),
 *     onSuggestionClick = { suggestion ->
 *         viewModel.sendMessage(suggestion)
 *     }
 * )
 * ```
 */
@Composable
fun ArcaneSuggestionChipsGrid(
    suggestions: List<String>,
    onSuggestionClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    columns: Int = ArcaneSuggestionChipsGridDefaults.Columns,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(
        ArcaneSuggestionChipsGridDefaults.HorizontalSpacing,
        Alignment.CenterHorizontally
    ),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(
        ArcaneSuggestionChipsGridDefaults.VerticalSpacing
    ),
    chipContent: @Composable (String, () -> Unit) -> Unit = { text, onClick ->
        ArcaneTextSuggestionChip(
            text = text,
            onClick = onClick
        )
    }
) {
    if (suggestions.isEmpty()) return

    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement
    ) {
        suggestions.chunked(columns).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = horizontalArrangement
            ) {
                rowItems.forEach { suggestion ->
                    chipContent(suggestion) {
                        onSuggestionClick(suggestion)
                    }
                }
            }
        }
    }
}

/**
 * A simple text-based suggestion chip with outlined style.
 *
 * @param text Text to display
 * @param onClick Callback when chip is clicked
 * @param modifier Modifier for the chip
 * @param colors Color configuration
 * @param borderWidth Width of the border
 */
@Composable
fun ArcaneTextSuggestionChip(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: TextSuggestionChipColors = ArcaneSuggestionChipsGridDefaults.chipColors(),
    borderWidth: Dp = 1.dp
) {
    val typography = ArcaneTheme.typography

    Text(
        text = text,
        style = typography.labelMedium,
        color = colors.contentColor,
        modifier = modifier
            .clip(ArcaneRadius.Large)
            .border(borderWidth, colors.borderColor, ArcaneRadius.Large)
            .background(colors.containerColor)
            .clickable(onClick = onClick)
            .padding(
                horizontal = ArcaneSpacing.Medium,
                vertical = ArcaneSpacing.Small
            )
    )
}
