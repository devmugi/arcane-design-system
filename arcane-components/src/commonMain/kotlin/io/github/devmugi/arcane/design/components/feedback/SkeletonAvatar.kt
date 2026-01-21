package io.github.devmugi.arcane.design.components.feedback

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.devmugi.arcane.design.components.display.ArcaneAvatarSize

@Composable
fun ArcaneSkeletonAvatar(
    modifier: Modifier = Modifier,
    size: ArcaneAvatarSize = ArcaneAvatarSize.Medium
) {
    SkeletonBox(
        modifier = modifier.size(size.dp),
        shape = CircleShape
    )
}
