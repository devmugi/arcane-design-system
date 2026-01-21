package io.github.devmugi.arcane.design.components.feedback

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme

@Immutable
sealed class ArcaneSpinnerSize(val dp: Dp) {
    data object Small : ArcaneSpinnerSize(16.dp)
    data object Medium : ArcaneSpinnerSize(24.dp)
    data object Large : ArcaneSpinnerSize(48.dp)
}

@Composable
fun ArcaneSpinner(
    modifier: Modifier = Modifier,
    size: ArcaneSpinnerSize = ArcaneSpinnerSize.Medium,
    color: Color = ArcaneTheme.colors.primary
) {
    val infiniteTransition = rememberInfiniteTransition(label = "spinnerTransition")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing)
        ),
        label = "spinnerRotation"
    )

    val strokeWidth = when (size) {
        ArcaneSpinnerSize.Small -> 2.dp
        ArcaneSpinnerSize.Medium -> 3.dp
        ArcaneSpinnerSize.Large -> 4.dp
    }

    Canvas(
        modifier = modifier.size(size.dp)
    ) {
        val sweepAngle = 270f
        val startAngle = rotation

        drawArc(
            color = color.copy(alpha = 0.2f),
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
        )

        drawArc(
            color = color,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
        )
    }
}
