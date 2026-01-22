package io.github.devmugi.arcane.chat.components.blocks

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import io.github.devmugi.arcane.chat.models.MessageBlock
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

/**
 * Renders an interactive suggestions block with expandable chips.
 *
 * Behavior:
 * - Collapsed (default): Shows chips in flow layout
 * - Expanded: Selected chip highlighted + details area below
 * - Toggle: Click selected chip again to collapse
 *
 * @param block The agent suggestions block to render
 * @param modifier Optional modifier
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AgentSuggestionsBlockView(
    block: MessageBlock.AgentSuggestions,
    modifier: Modifier = Modifier
) {
    val colors = ArcaneTheme.colors

    var selectedSuggestionId by remember { mutableStateOf<String?>(null) }
    val isExpanded = selectedSuggestionId != null

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(ArcaneRadius.Medium)
            .background(colors.surfaceContainerLowest)
            .padding(ArcaneSpacing.Small)
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
    ) {
        // Chips in FlowRow
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall),
            verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)
        ) {
            block.suggestions.forEach { suggestion ->
                SuggestionChip(
                    suggestion = suggestion,
                    isSelected = selectedSuggestionId == suggestion.id,
                    onClick = {
                        selectedSuggestionId = if (selectedSuggestionId == suggestion.id) {
                            null  // Toggle off if already selected
                        } else {
                            suggestion.id  // Select new chip
                        }
                        block.onSuggestionSelected(suggestion)
                    }
                )
            }
        }

        // Expanded details area
        if (isExpanded) {
            val selectedSuggestion = block.suggestions.find { it.id == selectedSuggestionId }
            selectedSuggestion?.let {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(ArcaneRadius.Small)
                        .background(colors.surfaceContainer)
                        .padding(ArcaneSpacing.Medium)
                ) {
                    it.detailsContent()  // Render injected composable slot
                }
            }
        }
    }
}
