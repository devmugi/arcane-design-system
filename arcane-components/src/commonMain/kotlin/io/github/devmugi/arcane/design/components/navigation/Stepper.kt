package io.github.devmugi.arcane.design.components.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

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

private val ConnectorHeight = 2.dp
private val VerticalConnectorHeight = 32.dp

/**
 * Horizontal connector line between step indicators.
 *
 * @param isCompleted Whether the previous step is completed (affects color)
 */
@Composable
private fun RowScope.HorizontalConnector(isCompleted: Boolean) {
    val colors = ArcaneTheme.colors
    val connectorColor by animateColorAsState(
        targetValue = if (isCompleted) colors.primary else colors.textDisabled,
        animationSpec = tween(200),
        label = "connectorColor"
    )

    Box(
        modifier = Modifier
            .weight(1f)
            .height(ConnectorHeight)
            .background(connectorColor)
    )
}

/**
 * Vertical connector line between step indicators.
 *
 * @param isCompleted Whether the previous step is completed (affects color)
 */
@Composable
private fun VerticalConnector(isCompleted: Boolean) {
    val colors = ArcaneTheme.colors
    val connectorColor by animateColorAsState(
        targetValue = if (isCompleted) colors.primary else colors.textDisabled,
        animationSpec = tween(200),
        label = "verticalConnectorColor"
    )

    Box(
        modifier = Modifier
            .width(ConnectorHeight)
            .height(VerticalConnectorHeight)
            .background(connectorColor)
    )
}

/**
 * Horizontal stepper layout with steps arranged left to right.
 *
 * @param steps List of steps to display
 * @param modifier Modifier to be applied to the stepper
 */
@Composable
private fun HorizontalStepper(
    steps: List<ArcaneStep>,
    modifier: Modifier = Modifier
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        steps.forEachIndexed { index, step ->
            // Each step column
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Row with optional connectors and indicator
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left connector (except for first step)
                    if (index > 0) {
                        val previousCompleted = steps[index - 1].state == ArcaneStepState.Completed
                        HorizontalConnector(isCompleted = previousCompleted)
                    } else {
                        Box(modifier = Modifier.weight(1f))
                    }

                    // Step indicator
                    StepIndicator(
                        stepNumber = index + 1,
                        state = step.state
                    )

                    // Right connector (except for last step)
                    if (index < steps.lastIndex) {
                        val currentCompleted = step.state == ArcaneStepState.Completed
                        HorizontalConnector(isCompleted = currentCompleted)
                    } else {
                        Box(modifier = Modifier.weight(1f))
                    }
                }

                // Label below indicator
                Text(
                    text = step.label,
                    style = typography.labelSmall,
                    color = when (step.state) {
                        ArcaneStepState.Active -> colors.primary
                        ArcaneStepState.Completed -> colors.text
                        ArcaneStepState.Pending -> colors.textDisabled
                    },
                    modifier = Modifier.padding(top = ArcaneSpacing.XSmall)
                )

                // Description only for active step
                if (step.state == ArcaneStepState.Active && step.description != null) {
                    Text(
                        text = step.description,
                        style = typography.bodySmall,
                        color = colors.textSecondary,
                        modifier = Modifier.padding(top = ArcaneSpacing.XXSmall)
                    )
                }
            }
        }
    }
}

/**
 * Vertical stepper layout with steps arranged top to bottom.
 *
 * @param steps List of steps to display
 * @param modifier Modifier to be applied to the stepper
 */
@Composable
private fun VerticalStepper(
    steps: List<ArcaneStep>,
    modifier: Modifier = Modifier
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography

    Column(modifier = modifier) {
        steps.forEachIndexed { index, step ->
            // Row with indicator and text
            Row(
                verticalAlignment = Alignment.Top
            ) {
                // Column with indicator and optional connector
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    StepIndicator(
                        stepNumber = index + 1,
                        state = step.state
                    )

                    // Vertical connector (except for last step)
                    if (index < steps.lastIndex) {
                        val currentCompleted = step.state == ArcaneStepState.Completed
                        VerticalConnector(isCompleted = currentCompleted)
                    }
                }

                // Label and description to the right
                Column(
                    modifier = Modifier.padding(start = ArcaneSpacing.Small)
                ) {
                    Text(
                        text = step.label,
                        style = typography.labelMedium,
                        color = when (step.state) {
                            ArcaneStepState.Active -> colors.primary
                            ArcaneStepState.Completed -> colors.text
                            ArcaneStepState.Pending -> colors.textDisabled
                        }
                    )

                    // Description for active step or always if provided
                    if (step.description != null) {
                        Text(
                            text = step.description,
                            style = typography.bodySmall,
                            color = if (step.state == ArcaneStepState.Active) {
                                colors.textSecondary
                            } else {
                                colors.textDisabled
                            },
                            modifier = Modifier.padding(top = ArcaneSpacing.XXSmall)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Stepper component for displaying multi-step progress.
 *
 * Supports both horizontal and vertical orientations with visual indicators
 * for completed, active, and pending steps.
 *
 * ## Visual Styling
 * - Completed steps: Primary-colored indicator with checkmark
 * - Active step: Primary-bordered indicator with glow effect
 * - Pending steps: Disabled-colored indicator
 * - Connector lines: Primary when previous step completed, disabled otherwise
 *
 * ## Horizontal Layout
 * - Steps arranged left to right with equal spacing
 * - Connector lines between indicators
 * - Labels below indicators
 * - Description shown only for active step
 *
 * ## Vertical Layout
 * - Steps arranged top to bottom
 * - Vertical connector lines between steps
 * - Labels and descriptions to the right of indicators
 *
 * @param steps List of steps to display in the stepper
 * @param modifier Modifier to be applied to the stepper container
 * @param orientation Layout orientation (Horizontal or Vertical)
 */
@Composable
fun ArcaneStepper(
    steps: List<ArcaneStep>,
    modifier: Modifier = Modifier,
    orientation: ArcaneStepperOrientation = ArcaneStepperOrientation.Horizontal
) {
    if (steps.isEmpty()) return

    when (orientation) {
        is ArcaneStepperOrientation.Horizontal -> {
            HorizontalStepper(steps = steps, modifier = modifier)
        }
        is ArcaneStepperOrientation.Vertical -> {
            VerticalStepper(steps = steps, modifier = modifier)
        }
    }
}
