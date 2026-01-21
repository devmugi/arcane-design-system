// arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/AssistantMessageBlock.kt
package io.github.devmugi.arcane.design.components.controls

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

private val AssistantMessageShape = RoundedCornerShape(
    topStart = 4.dp,
    topEnd = 12.dp,
    bottomStart = 12.dp,
    bottomEnd = 12.dp
)

@Composable
fun ArcaneAssistantMessageBlock(
    text: String,
    modifier: Modifier = Modifier,
    title: String? = null,
    isLoading: Boolean = false,
    maxContentHeight: Dp = 160.dp,
    showBottomActions: Boolean = false,
    autoShowWhenTruncated: Boolean = true,
    onCopyClick: (() -> Unit)? = null,
    onShowMoreClick: (() -> Unit)? = null,
    titleActions: @Composable (RowScope.() -> Unit)? = null,
    bottomActions: @Composable (RowScope.() -> Unit)? = null
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography
    val density = LocalDensity.current

    var isExpanded by remember { mutableStateOf(false) }
    var isTruncated by remember { mutableStateOf(false) }

    val showTitleRow = title != null || isLoading
    val shouldShowBottomActions = showBottomActions || (autoShowWhenTruncated && isTruncated && !isExpanded)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(AssistantMessageShape)
            .background(colors.surfaceRaised)
            .padding(ArcaneSpacing.Small)
            .animateContentSize(animationSpec = tween(150)),
        verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)
    ) {
        // Title Row
        if (showTitleRow) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left side: Loading + Title
                Row(
                    horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = colors.primary,
                            strokeWidth = 2.dp
                        )
                    }
                    if (title != null) {
                        Text(
                            text = title,
                            style = typography.labelLarge,
                            color = colors.textSecondary
                        )
                    }
                }

                // Right side: Copy + custom actions
                Row(
                    horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.XXSmall),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (onCopyClick != null) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(ArcaneRadius.Full)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = onCopyClick
                                )
                                .semantics { contentDescription = "Copy message" },
                            contentAlignment = Alignment.Center
                        ) {
                            CopyIcon(
                                tint = colors.textSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    titleActions?.invoke(this)
                }
            }
        }

        // Message Content
        val maxHeightPx = with(density) { maxContentHeight.toPx() }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (!isExpanded) {
                        Modifier.heightIn(max = maxContentHeight)
                    } else {
                        Modifier
                    }
                )
                .onSizeChanged { size ->
                    if (!isExpanded) {
                        isTruncated = size.height >= maxHeightPx.toInt()
                    }
                }
                .drawWithContent {
                    drawContent()
                    if (isTruncated && !isExpanded) {
                        drawRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, colors.surfaceRaised),
                                startY = size.height * 0.6f,
                                endY = size.height
                            )
                        )
                    }
                }
        ) {
            Text(
                text = text,
                style = typography.bodyMedium,
                color = colors.text,
                overflow = if (!isExpanded) TextOverflow.Clip else TextOverflow.Visible
            )
        }

        // Bottom Actions Row
        if (shouldShowBottomActions) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Show more/less button
                if (isTruncated || isExpanded) {
                    Text(
                        text = if (isExpanded) "Show less" else "Show more",
                        style = typography.labelMedium,
                        color = colors.primary,
                        modifier = Modifier
                            .clip(ArcaneRadius.Small)
                            .clickable {
                                if (onShowMoreClick != null) {
                                    onShowMoreClick()
                                } else {
                                    isExpanded = !isExpanded
                                }
                            }
                            .padding(
                                horizontal = ArcaneSpacing.XSmall,
                                vertical = ArcaneSpacing.XXSmall
                            )
                    )
                }
                bottomActions?.invoke(this)
            }
        }
    }
}

/**
 * Custom copy icon placeholder.
 * Uses simple shapes to represent a copy/clipboard icon since Icons.Outlined.ContentCopy
 * is not available in material-icons-core.
 */
@Composable
private fun CopyIcon(
    tint: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Back document (slightly offset)
        Box(
            modifier = Modifier
                .width(10.dp)
                .height(12.dp)
                .offset(x = 2.dp, y = (-2).dp)
                .border(
                    width = 1.5.dp,
                    color = tint,
                    shape = RoundedCornerShape(2.dp)
                )
        )
        // Front document
        Box(
            modifier = Modifier
                .width(10.dp)
                .height(12.dp)
                .offset(x = (-2).dp, y = 2.dp)
                .border(
                    width = 1.5.dp,
                    color = tint,
                    shape = RoundedCornerShape(2.dp)
                )
        )
    }
}
