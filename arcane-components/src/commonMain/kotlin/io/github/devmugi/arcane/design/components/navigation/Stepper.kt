package io.github.devmugi.arcane.design.components.navigation

import androidx.compose.runtime.Immutable

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
