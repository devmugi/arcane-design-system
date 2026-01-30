package io.github.devmugi.arcane.chat.components.welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

/**
 * Default values for [ArcaneChatWelcomeSection].
 */
object ArcaneChatWelcomeSectionDefaults {
    /** Default content padding */
    val ContentPadding = PaddingValues(horizontal = 24.dp)

    /** Default vertical spacing between elements */
    val VerticalSpacing = 16.dp
}

/**
 * A welcome/empty state section for chat screens.
 *
 * This component provides a flexible container for displaying welcome content
 * when a chat conversation is empty. It uses slots for title, subtitle, and
 * suggestions to allow complete customization.
 *
 * @param modifier Modifier for the container
 * @param contentPadding Padding around the content
 * @param verticalArrangement Vertical arrangement of child elements
 * @param horizontalAlignment Horizontal alignment of child elements
 * @param title Slot for the title content (typically a large heading)
 * @param subtitle Slot for the subtitle content (typically a description)
 * @param suggestions Slot for suggestion chips or other call-to-action content
 *
 * Example:
 * ```
 * ArcaneChatWelcomeSection(
 *     title = {
 *         Text(
 *             "Welcome!",
 *             style = ArcaneTheme.typography.displaySmall
 *         )
 *     },
 *     subtitle = {
 *         Text(
 *             "Ask me anything about...",
 *             style = ArcaneTheme.typography.bodyLarge
 *         )
 *     },
 *     suggestions = {
 *         ArcaneSuggestionChipsGrid(
 *             suggestions = listOf("Tell me about...", "How does..."),
 *             onSuggestionClick = { viewModel.sendMessage(it) }
 *         )
 *     }
 * )
 * ```
 */
@Composable
fun ArcaneChatWelcomeSection(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = ArcaneChatWelcomeSectionDefaults.ContentPadding,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(ArcaneChatWelcomeSectionDefaults.VerticalSpacing),
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    title: @Composable () -> Unit = {},
    subtitle: @Composable () -> Unit = {},
    suggestions: @Composable () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(contentPadding),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        title()
        subtitle()
        suggestions()
    }
}
