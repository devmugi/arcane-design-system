package io.github.devmugi.arcane.design.components.display

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme

@Immutable
enum class ArcaneAvatarSize(val dp: Dp) {
    Small(24.dp),
    Medium(32.dp),
    Large(48.dp)
}

@Immutable
data class ArcaneAvatarData(
    val image: Painter? = null,
    val name: String? = null
)

private fun getInitials(name: String?): String {
    if (name.isNullOrBlank()) return "?"
    val parts = name.trim().split(" ").filter { it.isNotEmpty() }
    return when {
        parts.size >= 2 -> "${parts.first().first()}${parts.last().first()}"
        parts.size == 1 -> parts.first().take(2)
        else -> "?"
    }.uppercase()
}

@Composable
fun ArcaneAvatar(
    modifier: Modifier = Modifier,
    image: Painter? = null,
    name: String? = null,
    size: ArcaneAvatarSize = ArcaneAvatarSize.Medium
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography

    val textStyle = when (size) {
        ArcaneAvatarSize.Small -> typography.labelSmall
        ArcaneAvatarSize.Medium -> typography.labelMedium
        ArcaneAvatarSize.Large -> typography.labelLarge
    }

    Box(
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(colors.surfaceInset)
            .border(2.dp, colors.surface, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (image != null) {
            Image(
                painter = image,
                contentDescription = name,
                modifier = Modifier.size(size.dp),
                contentScale = ContentScale.Crop
            )
        } else {
            Text(
                text = getInitials(name),
                style = textStyle,
                color = colors.text
            )
        }
    }
}

@Composable
fun ArcaneAvatarGroup(
    avatars: List<ArcaneAvatarData>,
    modifier: Modifier = Modifier,
    maxVisible: Int = 3,
    size: ArcaneAvatarSize = ArcaneAvatarSize.Medium,
    overlap: Dp = 8.dp
) {
    val colors = ArcaneTheme.colors
    val visibleAvatars = avatars.take(maxVisible)
    val overflowCount = (avatars.size - maxVisible).coerceAtLeast(0)

    Box(modifier = modifier) {
        visibleAvatars.forEachIndexed { index, avatarData ->
            Box(
                modifier = Modifier
                    .offset(x = (size.dp - overlap) * index)
            ) {
                ArcaneAvatar(
                    image = avatarData.image,
                    name = avatarData.name,
                    size = size
                )
            }
        }

        if (overflowCount > 0) {
            Box(
                modifier = Modifier
                    .offset(x = (size.dp - overlap) * visibleAvatars.size)
                    .size(size.dp)
                    .clip(CircleShape)
                    .background(colors.surfaceRaised)
                    .border(2.dp, colors.surface, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+$overflowCount",
                    style = when (size) {
                        ArcaneAvatarSize.Small -> ArcaneTheme.typography.labelSmall
                        ArcaneAvatarSize.Medium -> ArcaneTheme.typography.labelMedium
                        ArcaneAvatarSize.Large -> ArcaneTheme.typography.labelLarge
                    },
                    color = colors.text
                )
            }
        }
    }
}
