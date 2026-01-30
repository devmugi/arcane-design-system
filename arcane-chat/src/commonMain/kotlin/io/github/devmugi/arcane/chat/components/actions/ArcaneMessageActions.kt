package io.github.devmugi.arcane.chat.components.actions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme

/**
 * State for like/dislike feedback actions.
 */
enum class LikeState {
    /** No feedback given */
    None,
    /** User liked the message */
    Liked,
    /** User disliked the message */
    Disliked
}

/**
 * Colors for [ArcaneMessageActions].
 *
 * @param iconColor Default color for action icons
 * @param activeColor Color for active/selected state (e.g., liked)
 */
@Immutable
data class MessageActionsColors(
    val iconColor: Color,
    val activeColor: Color
)

/**
 * Default values for [ArcaneMessageActions].
 */
object MessageActionsDefaults {
    /** Default icon size */
    val IconSize = 20.dp

    /** Default ripple radius for touch feedback */
    val RippleRadius = 16.dp

    /** Default spacing between actions */
    val Spacing = 16.dp

    /**
     * Creates default colors for message actions.
     *
     * @param iconColor Default icon color, defaults to theme textSecondary
     * @param activeColor Active state color, defaults to theme primary
     */
    @Composable
    fun colors(
        iconColor: Color = ArcaneTheme.colors.textSecondary,
        activeColor: Color = ArcaneTheme.colors.primary
    ): MessageActionsColors = MessageActionsColors(
        iconColor = iconColor,
        activeColor = activeColor
    )
}

/**
 * A row of action icons for chat messages.
 *
 * Each action is optional - pass null to hide that action. This allows
 * flexible configuration based on message type or user permissions.
 *
 * @param onCopy Callback for copy action, null hides the icon
 * @param onShare Callback for share action, null hides the icon
 * @param onLike Callback for like action, null hides the icon
 * @param onDislike Callback for dislike action, null hides the icon
 * @param onRegenerate Callback for regenerate action, null hides the icon
 * @param modifier Modifier for the container row
 * @param likeState Current like/dislike state for visual feedback
 * @param colors Color configuration
 * @param iconSize Size of action icons
 * @param arrangement Horizontal arrangement of icons
 *
 * Example:
 * ```
 * ArcaneMessageActions(
 *     onCopy = { clipboard.setText(message.content) },
 *     onShare = { shareSheet.show(message.content) },
 *     onLike = { viewModel.like(message.id) },
 *     onDislike = { viewModel.dislike(message.id) },
 *     likeState = message.likeState
 * )
 * ```
 */
@Composable
fun ArcaneMessageActions(
    modifier: Modifier = Modifier,
    onCopy: (() -> Unit)? = null,
    onShare: (() -> Unit)? = null,
    onLike: (() -> Unit)? = null,
    onDislike: (() -> Unit)? = null,
    onRegenerate: (() -> Unit)? = null,
    likeState: LikeState = LikeState.None,
    colors: MessageActionsColors = MessageActionsDefaults.colors(),
    iconSize: Dp = MessageActionsDefaults.IconSize,
    arrangement: Arrangement.Horizontal = Arrangement.spacedBy(MessageActionsDefaults.Spacing)
) {
    Row(
        modifier = modifier,
        horizontalArrangement = arrangement
    ) {
        if (onCopy != null) {
            ActionIcon(
                icon = Icons.Default.ContentCopy,
                contentDescription = "Copy",
                onClick = onCopy,
                iconColor = colors.iconColor,
                iconSize = iconSize
            )
        }

        if (onShare != null) {
            ActionIcon(
                icon = Icons.Default.Share,
                contentDescription = "Share",
                onClick = onShare,
                iconColor = colors.iconColor,
                iconSize = iconSize
            )
        }

        if (onLike != null) {
            ActionIcon(
                icon = if (likeState == LikeState.Liked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                contentDescription = "Like",
                onClick = onLike,
                iconColor = if (likeState == LikeState.Liked) colors.activeColor else colors.iconColor,
                iconSize = iconSize
            )
        }

        if (onDislike != null) {
            ActionIcon(
                icon = if (likeState == LikeState.Disliked) Icons.Filled.ThumbDown else Icons.Outlined.ThumbDown,
                contentDescription = "Dislike",
                onClick = onDislike,
                iconColor = if (likeState == LikeState.Disliked) colors.activeColor else colors.iconColor,
                iconSize = iconSize
            )
        }

        if (onRegenerate != null) {
            ActionIcon(
                icon = Icons.Default.Refresh,
                contentDescription = "Regenerate",
                onClick = onRegenerate,
                iconColor = colors.iconColor,
                iconSize = iconSize
            )
        }
    }
}

/**
 * Internal action icon with unbounded ripple.
 */
@Composable
private fun ActionIcon(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    iconColor: Color,
    iconSize: Dp,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    Icon(
        imageVector = icon,
        contentDescription = contentDescription,
        tint = iconColor,
        modifier = modifier
            .size(iconSize)
            .semantics { role = Role.Button }
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = false, radius = MessageActionsDefaults.RippleRadius),
                onClick = onClick
            )
    )
}
