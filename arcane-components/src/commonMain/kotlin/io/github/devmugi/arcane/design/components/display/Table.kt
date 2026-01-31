package io.github.devmugi.arcane.design.components.display

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Immutable
data class ArcaneTableColumn<T>(
    val header: String,
    val weight: Float = 1f,
    val sortable: Boolean = false,
    val filterable: Boolean = false,
    val content: @Composable (T) -> Unit
)

@Immutable
data class ArcaneTableSortState(
    val columnIndex: Int,
    val ascending: Boolean
)

@Composable
fun <T> ArcaneTable(
    items: List<T>,
    columns: List<ArcaneTableColumn<T>>,
    modifier: Modifier = Modifier,
    onRowClick: ((T) -> Unit)? = null,
    sortState: ArcaneTableSortState? = null,
    onSortChange: ((ArcaneTableSortState) -> Unit)? = null
) {
    Column(
        modifier = modifier
            .clip(ArcaneRadius.Medium)
    ) {
        ArcaneTableHeader(
            columns = columns,
            sortState = sortState,
            onSortChange = onSortChange
        )

        items.forEachIndexed { index, item ->
            ArcaneTableRow(
                onClick = onRowClick?.let { { it(item) } },
                isAlternate = index % 2 == 1
            ) {
                columns.forEach { column ->
                    Box(
                        modifier = Modifier
                            .weight(column.weight)
                            .padding(horizontal = ArcaneSpacing.Small),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        column.content(item)
                    }
                }
            }
        }
    }
}

@Composable
fun <T> ArcaneTableHeader(
    columns: List<ArcaneTableColumn<T>>,
    sortState: ArcaneTableSortState?,
    onSortChange: ((ArcaneTableSortState) -> Unit)?
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surfaceContainerLowest)
            .defaultMinSize(minHeight = 40.dp)
            .padding(vertical = ArcaneSpacing.XSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        columns.forEachIndexed { index, column ->
            val isCurrentSort = sortState?.columnIndex == index
            val interactionSource = remember { MutableInteractionSource() }

            Row(
                modifier = Modifier
                    .weight(column.weight)
                    .padding(horizontal = ArcaneSpacing.Small)
                    .then(
                        if (column.sortable && onSortChange != null) {
                            Modifier.clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                val newState = if (isCurrentSort) {
                                    ArcaneTableSortState(index, !sortState.ascending)
                                } else {
                                    ArcaneTableSortState(index, true)
                                }
                                onSortChange(newState)
                            }
                        } else Modifier
                    ),
                horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.XXSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = column.header,
                    style = typography.labelMedium,
                    color = colors.textSecondary
                )

                if (column.sortable) {
                    val sortIndicator = when {
                        !isCurrentSort -> "\u2195" // ↕
                        sortState.ascending -> "\u2191" // ↑
                        else -> "\u2193" // ↓
                    }
                    Text(
                        text = sortIndicator,
                        style = typography.labelSmall,
                        color = if (isCurrentSort) colors.primary else colors.textSecondary
                    )
                }

                if (column.filterable) {
                    Text(
                        text = "\u25BC", // ▼ funnel-like
                        style = typography.labelSmall,
                        color = colors.textSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun ArcaneTableRow(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    isAlternate: Boolean = false,
    content: @Composable RowScope.() -> Unit
) {
    val colors = ArcaneTheme.colors
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val backgroundColor = when {
        isHovered && onClick != null -> colors.surfaceContainerHigh
        isAlternate -> colors.surface.copy(alpha = 0.5f)
        else -> Color.Transparent
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
            .background(backgroundColor)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick
                    )
                } else Modifier
            )
            .padding(vertical = ArcaneSpacing.Small),
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}
