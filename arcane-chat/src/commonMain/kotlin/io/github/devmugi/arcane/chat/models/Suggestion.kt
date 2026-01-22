package io.github.devmugi.arcane.chat.models

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Represents a suggestion chip with expandable details.
 * Used within AgentSuggestionsBlock.
 *
 * @param id Unique identifier for the suggestion
 * @param text Display text shown on the chip
 * @param icon Composable icon displayed on the chip
 * @param color Tint color for the chip background and icon
 * @param detailsContent Composable slot for expanded detail content
 */
data class Suggestion(
    val id: String,
    val text: String,
    val icon: @Composable () -> Unit,
    val color: Color,
    val detailsContent: @Composable () -> Unit
)
