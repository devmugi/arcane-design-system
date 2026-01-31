package io.github.devmugi.arcane.design.foundation.tokens

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.runtime.Immutable

/**
 * Motion tokens for consistent animation timing across the design system.
 * Use these durations with tween() or other animation specs.
 */
@Immutable
object ArcaneMotion {
    // Duration tokens (in milliseconds)

    /** Fast: Quick feedback animations (150ms) - button press, toggle, state changes */
    const val Fast: Int = 150

    /** Medium: Standard transitions (300ms) - modals, color transitions, expand/collapse */
    const val Medium: Int = 300

    /** Slow: Emphasis animations (500ms) - complex transitions, page changes */
    const val Slow: Int = 500

    // Easing tokens

    /** EaseOut: Decelerating motion - elements entering view, button press release */
    val EaseOut: Easing = CubicBezierEasing(0.0f, 0.0f, 0.2f, 1.0f)

    /** EaseInOut: Smooth acceleration and deceleration - general purpose transitions */
    val EaseInOut: Easing = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)

    /** EaseIn: Accelerating motion - elements leaving view */
    val EaseIn: Easing = CubicBezierEasing(0.4f, 0.0f, 1.0f, 1.0f)

    /** Linear: Constant speed - progress indicators, looping animations */
    val Linear: Easing = CubicBezierEasing(0.0f, 0.0f, 1.0f, 1.0f)

    // Spring tokens (for expressive interactions)

    /** Standard spring damping ratio - natural bounce */
    const val SpringDampingStandard: Float = 0.75f

    /** Bouncy spring damping ratio - playful interactions */
    const val SpringDampingBouncy: Float = 0.6f

    /** Stiff spring damping ratio - quick, controlled */
    const val SpringDampingStiff: Float = 0.9f
}
