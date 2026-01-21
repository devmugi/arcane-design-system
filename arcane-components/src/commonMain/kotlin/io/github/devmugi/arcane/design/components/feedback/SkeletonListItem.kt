package io.github.devmugi.arcane.design.components.feedback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun ArcaneSkeletonListItem(
    modifier: Modifier = Modifier,
    showLeadingIcon: Boolean = true,
    showTrailingContent: Boolean = false
) {
    Row(
        modifier = modifier
            .defaultMinSize(minHeight = 56.dp)
            .padding(
                horizontal = ArcaneSpacing.Medium,
                vertical = ArcaneSpacing.Small
            ),
        horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showLeadingIcon) {
            SkeletonBox(
                modifier = Modifier
                    .defaultMinSize(32.dp, 32.dp),
                shape = CircleShape
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XXSmall)
        ) {
            SkeletonBox(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(16.dp),
                shape = RoundedCornerShape(4.dp)
            )
            SkeletonBox(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(12.dp),
                shape = RoundedCornerShape(4.dp)
            )
        }

        if (showTrailingContent) {
            SkeletonBox(
                modifier = Modifier
                    .width(48.dp)
                    .height(20.dp),
                shape = RoundedCornerShape(4.dp)
            )
        }
    }
}
