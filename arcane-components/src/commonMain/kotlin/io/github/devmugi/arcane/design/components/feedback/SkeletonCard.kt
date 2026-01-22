package io.github.devmugi.arcane.design.components.feedback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.primitives.ArcaneSurface
import io.github.devmugi.arcane.design.foundation.primitives.SurfaceVariant
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun ArcaneSkeletonCard(
    modifier: Modifier = Modifier,
    showImage: Boolean = true,
    showActions: Boolean = true
) {
    ArcaneSurface(
        variant = SurfaceVariant.Container,
        modifier = modifier,
        shape = ArcaneRadius.Large
    ) {
        Column {
            if (showImage) {
                SkeletonBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                )
            }

            Column(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)
            ) {
                SkeletonBox(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(20.dp),
                    shape = RoundedCornerShape(4.dp)
                )
                SkeletonBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp),
                    shape = RoundedCornerShape(4.dp)
                )
                SkeletonBox(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(14.dp),
                    shape = RoundedCornerShape(4.dp)
                )
            }

            if (showActions) {
                Row(
                    modifier = Modifier.padding(
                        start = ArcaneSpacing.Medium,
                        end = ArcaneSpacing.Medium,
                        bottom = ArcaneSpacing.Medium
                    ),
                    horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
                ) {
                    SkeletonBox(
                        modifier = Modifier
                            .width(100.dp)
                            .height(36.dp),
                        shape = RoundedCornerShape(18.dp)
                    )
                }
            }
        }
    }
}
