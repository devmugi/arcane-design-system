package io.github.devmugi.arcane.design.screenshots

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.captureRoboImage
import io.github.devmugi.arcane.design.foundation.theme.ArcaneColors
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme

/**
 * Captures a screenshot of the given composable in the Arcane theme.
 *
 * @param composeTestRule The Compose test rule
 * @param componentName Base name for the screenshot file (e.g., "Button_Primary")
 * @param isDark Whether to capture dark theme variant
 * @param content The composable content to capture
 */
fun captureArcaneScreenshot(
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

    composeTestRule.onRoot().captureRoboImage(filePath)
}
