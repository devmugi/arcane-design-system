package io.github.devmugi.arcane.design.components.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneBorder

/**
 * Represents the state of a step in a stepper component.
 */
enum class ArcaneStepState {
    /** Step has not been started yet */
    Pending,
    /** Step is currently active/in progress */
    Active,
    /** Step has been completed */
    Completed
}

/**
 * Data class representing a single step in a stepper.
 *
 * @param label The main text label for the step
 * @param description Optional descriptive text shown for active steps
 * @param state The current state of the step
 */
@Immutable
data class ArcaneStep(
    val label: String,
    val description: String? = null,
    val state: ArcaneStepState = ArcaneStepState.Pending
)

/**
 * Sealed class representing the orientation of a stepper component.
 */
@Immutable
sealed class ArcaneStepperOrientation {
    /** Horizontal layout with steps arranged left to right */
    data object Horizontal : ArcaneStepperOrientation()
    /** Vertical layout with steps arranged top to bottom */
    data object Vertical : ArcaneStepperOrientation()
}

private val StepIndicatorSize = 24.dp

/**
 * Internal composable for rendering a circular step indicator.
 *
 * Visual states:
 * - Completed: Primary background with animated checkmark
 * - Active: Surface background with primary border and glow effect
 * - Pending: Surface background with disabled border
 *
 * @param stepNumber The number to display in the indicator (1-indexed)
 * @param state The current state of the step
 * @param modifier Modifier to be applied to the indicator
 */
@Composable
internal fun StepIndicator(
    stepNumber: Int,
    state: ArcaneStepState,
    modifier: Modifier = Modifier
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography

    val backgroundColor by animateColorAsState(
        targetValue = when (state) {
            ArcaneStepState.Completed -> colors.primary
            ArcaneStepState.Active -> colors.surface
            ArcaneStepState.Pending -> colors.surface
        },
        animationSpec = tween(200),
        label = "stepBackgroundColor"
    )

    val borderColor by animateColorAsState(
        targetValue = when (state) {
            ArcaneStepState.Completed -> colors.primary
            ArcaneStepState.Active -> colors.primary
            ArcaneStepState.Pending -> colors.textDisabled
        },
        animationSpec = tween(200),
        label = "stepBorderColor"
    )

    val textColor by animateColorAsState(
        targetValue = when (state) {
            ArcaneStepState.Completed -> colors.surface
            ArcaneStepState.Active -> colors.primary
            ArcaneStepState.Pending -> colors.textDisabled
        },
        animationSpec = tween(200),
        label = "stepTextColor"
    )

    val checkmarkScale by animateFloatAsState(
        targetValue = if (state == ArcaneStepState.Completed) 1f else 0f,
        animationSpec = tween(200),
        label = "checkmarkScale"
    )

    Box(
        modifier = modifier
            .size(StepIndicatorSize)
            .then(
                if (state == ArcaneStepState.Active) {
                    Modifier.shadow(
                        elevation = 4.dp,
                        shape = CircleShape,
                        ambientColor = colors.glow,
                        spotColor = colors.glow
                    )
                } else {
                    Modifier
                }
            )
            .clip(CircleShape)
            .background(backgroundColor, CircleShape)
            .border(ArcaneBorder.Medium, borderColor, CircleShape)
            .drawWithContent {
                drawContent()
                if (state == ArcaneStepState.Completed && checkmarkScale > 0f) {
                    val strokeWidth = 2.dp.toPx() * checkmarkScale
                    val padding = 6.dp.toPx()
                    val centerY = size.height / 2
                    // Draw checkmark
                    drawLine(
                        color = colors.surface,
                        start = Offset(padding, centerY),
                        end = Offset(size.width / 2 - 1.dp.toPx(), size.height - padding),
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        color = colors.surface,
                        start = Offset(size.width / 2 - 1.dp.toPx(), size.height - padding),
                        end = Offset(size.width - padding, padding),
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round
                    )
                }
            },
        contentAlignment = Alignment.Center
    ) {
        if (state != ArcaneStepState.Completed) {
            Text(
                text = stepNumber.toString(),
                style = typography.labelSmall,
                color = textColor
            )
        }
    }
}
