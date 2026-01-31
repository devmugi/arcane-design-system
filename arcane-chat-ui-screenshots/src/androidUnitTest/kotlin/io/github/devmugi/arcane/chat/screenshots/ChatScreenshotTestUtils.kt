package io.github.devmugi.arcane.chat.screenshots

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.captureRoboImage
import io.github.devmugi.arcane.design.foundation.theme.ArcaneColors
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme

/**
 * Roborazzi options with tolerance for minor rendering differences.
 * Allows 1% pixel difference to account for font anti-aliasing variations
 * between local development (macOS) and CI (Ubuntu) environments.
 */
private val roborazziOptions = RoborazziOptions(
    compareOptions = RoborazziOptions.CompareOptions(
        changeThreshold = 0.01f // Allow 1% pixel difference
    )
)

/**
 * Captures a screenshot of the given composable content wrapped in ArcaneTheme.
 *
 * @param composeTestRule The compose test rule to use
 * @param componentName The name of the component (used in the file path)
 * @param isDark Whether to use dark theme
 * @param content The composable content to capture
 */
fun captureChatScreenshot(
    composeTestRule: ComposeContentTestRule,
    componentName: String,
    isDark: Boolean = false,
    content: @Composable () -> Unit
) {
    val theme = if (isDark) "dark" else "light"
    val filePath = "src/androidUnitTest/resources/golden/android/$theme/${componentName}.png"

    composeTestRule.setContent {
        ArcaneTheme(
            isDark = isDark,
            colors = if (isDark) ArcaneColors.dark() else ArcaneColors.default()
        ) {
            content()
        }
    }

    composeTestRule.waitForIdle()
    composeTestRule.onRoot().captureRoboImage(
        filePath = filePath,
        roborazziOptions = roborazziOptions
    )
}
