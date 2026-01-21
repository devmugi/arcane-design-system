package io.github.devmugi.arcane.design.components.feedback

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Immutable
sealed class ArcaneSkeletonShape {
    data class Text(val lines: Int = 1, val lastLineWidth: Float = 0.7f) : ArcaneSkeletonShape()
    data class Circle(val size: Dp = 40.dp) : ArcaneSkeletonShape()
    data class Rectangle(
        val width: Dp = Dp.Unspecified,
        val height: Dp = 100.dp,
        val radius: Shape = ArcaneRadius.Medium
    ) : ArcaneSkeletonShape()
}

@Composable
fun ArcaneSkeleton(
    modifier: Modifier = Modifier,
    shape: ArcaneSkeletonShape = ArcaneSkeletonShape.Text()
) {
    when (shape) {
        is ArcaneSkeletonShape.Text -> SkeletonText(
            modifier = modifier,
            lines = shape.lines,
            lastLineWidth = shape.lastLineWidth
        )
        is ArcaneSkeletonShape.Circle -> SkeletonCircle(
            modifier = modifier,
            size = shape.size
        )
        is ArcaneSkeletonShape.Rectangle -> SkeletonRectangle(
            modifier = modifier,
            width = shape.width,
            height = shape.height,
            radius = shape.radius
        )
    }
}

@Composable
private fun SkeletonText(
    modifier: Modifier = Modifier,
    lines: Int,
    lastLineWidth: Float
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)
    ) {
        repeat(lines) { index ->
            val isLastLine = index == lines - 1
            SkeletonBox(
                modifier = Modifier
                    .fillMaxWidth(if (isLastLine) lastLineWidth else 1f)
                    .height(16.dp),
                shape = RoundedCornerShape(4.dp)
            )
        }
    }
}

@Composable
private fun SkeletonCircle(
    modifier: Modifier = Modifier,
    size: Dp
) {
    SkeletonBox(
        modifier = modifier.size(size),
        shape = CircleShape
    )
}

@Composable
private fun SkeletonRectangle(
    modifier: Modifier = Modifier,
    width: Dp,
    height: Dp,
    radius: Shape
) {
    SkeletonBox(
        modifier = modifier
            .then(if (width == Dp.Unspecified) Modifier.fillMaxWidth() else Modifier.size(width, height))
            .height(height),
        shape = radius
    )
}

@Composable
internal fun SkeletonBox(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(4.dp)
) {
    val colors = ArcaneTheme.colors
    val infiniteTransition = rememberInfiniteTransition(label = "skeletonShimmer")

    val shimmerProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing)
        ),
        label = "shimmerProgress"
    )

    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            colors.surfaceInset,
            colors.surfaceRaised,
            colors.surfaceInset
        ),
        start = Offset(shimmerProgress * 1000f - 500f, 0f),
        end = Offset(shimmerProgress * 1000f + 500f, 0f)
    )

    Box(
        modifier = modifier
            .clip(shape)
            .background(shimmerBrush)
    )
}
