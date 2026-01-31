package io.github.devmugi.arcane.design.screenshots.controls

import androidx.compose.material3.Text
import androidx.compose.ui.test.junit4.createComposeRule
import com.github.takahirom.roborazzi.RoborazziRule
import io.github.devmugi.arcane.design.components.controls.ArcaneButton
import io.github.devmugi.arcane.design.components.controls.ArcaneButtonSize
import io.github.devmugi.arcane.design.components.controls.ArcaneButtonStyle
import io.github.devmugi.arcane.design.screenshots.captureArcaneScreenshot
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34])
class ButtonScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val roborazziRule = RoborazziRule()

    // ========================================================================
    // Filled Style
    // ========================================================================

    @Test
    fun button_Filled_Light() {
        captureArcaneScreenshot(composeTestRule, "controls", "Button_Filled", isDark = false) {
            ArcaneButton(style = ArcaneButtonStyle.Filled(), onClick = {}) {
                Text("Filled Button")
            }
        }
    }

    @Test
    fun button_Filled_Dark() {
        captureArcaneScreenshot(composeTestRule, "controls", "Button_Filled", isDark = true) {
            ArcaneButton(style = ArcaneButtonStyle.Filled(), onClick = {}) {
                Text("Filled Button")
            }
        }
    }

    @Test
    fun button_Filled_Disabled_Light() {
        captureArcaneScreenshot(composeTestRule, "controls", "Button_Filled_Disabled", isDark = false) {
            ArcaneButton(style = ArcaneButtonStyle.Filled(), onClick = {}, enabled = false) {
                Text("Disabled")
            }
        }
    }

    @Test
    fun button_Filled_Disabled_Dark() {
        captureArcaneScreenshot(composeTestRule, "controls", "Button_Filled_Disabled", isDark = true) {
            ArcaneButton(style = ArcaneButtonStyle.Filled(), onClick = {}, enabled = false) {
                Text("Disabled")
            }
        }
    }

    // ========================================================================
    // Tonal Style
    // ========================================================================

    @Test
    fun button_Tonal_Light() {
        captureArcaneScreenshot(composeTestRule, "controls", "Button_Tonal", isDark = false) {
            ArcaneButton(style = ArcaneButtonStyle.Tonal(), onClick = {}) {
                Text("Tonal Button")
            }
        }
    }

    @Test
    fun button_Tonal_Dark() {
        captureArcaneScreenshot(composeTestRule, "controls", "Button_Tonal", isDark = true) {
            ArcaneButton(style = ArcaneButtonStyle.Tonal(), onClick = {}) {
                Text("Tonal Button")
            }
        }
    }

    // ========================================================================
    // Outlined Style
    // ========================================================================

    @Test
    fun button_Outlined_Light() {
        captureArcaneScreenshot(composeTestRule, "controls", "Button_Outlined", isDark = false) {
            ArcaneButton(style = ArcaneButtonStyle.Outlined(), onClick = {}) {
                Text("Outlined Button")
            }
        }
    }

    @Test
    fun button_Outlined_Dark() {
        captureArcaneScreenshot(composeTestRule, "controls", "Button_Outlined", isDark = true) {
            ArcaneButton(style = ArcaneButtonStyle.Outlined(), onClick = {}) {
                Text("Outlined Button")
            }
        }
    }

    // ========================================================================
    // Elevated Style
    // ========================================================================

    @Test
    fun button_Elevated_Light() {
        captureArcaneScreenshot(composeTestRule, "controls", "Button_Elevated", isDark = false) {
            ArcaneButton(style = ArcaneButtonStyle.Elevated(), onClick = {}) {
                Text("Elevated Button")
            }
        }
    }

    @Test
    fun button_Elevated_Dark() {
        captureArcaneScreenshot(composeTestRule, "controls", "Button_Elevated", isDark = true) {
            ArcaneButton(style = ArcaneButtonStyle.Elevated(), onClick = {}) {
                Text("Elevated Button")
            }
        }
    }

    // ========================================================================
    // Text Style
    // ========================================================================

    @Test
    fun button_Text_Light() {
        captureArcaneScreenshot(composeTestRule, "controls", "Button_Text", isDark = false) {
            ArcaneButton(style = ArcaneButtonStyle.Text, onClick = {}) {
                Text("Text Button")
            }
        }
    }

    @Test
    fun button_Text_Dark() {
        captureArcaneScreenshot(composeTestRule, "controls", "Button_Text", isDark = true) {
            ArcaneButton(style = ArcaneButtonStyle.Text, onClick = {}) {
                Text("Text Button")
            }
        }
    }

    // ========================================================================
    // Button Sizes
    // ========================================================================

    @Test
    fun button_Size_ExtraSmall_Light() {
        captureArcaneScreenshot(composeTestRule, "controls", "Button_Size_ExtraSmall", isDark = false) {
            ArcaneButton(
                style = ArcaneButtonStyle.Filled(),
                size = ArcaneButtonSize.ExtraSmall,
                onClick = {}
            ) {
                Text("XS")
            }
        }
    }

    @Test
    fun button_Size_Small_Light() {
        captureArcaneScreenshot(composeTestRule, "controls", "Button_Size_Small", isDark = false) {
            ArcaneButton(
                style = ArcaneButtonStyle.Filled(),
                size = ArcaneButtonSize.Small,
                onClick = {}
            ) {
                Text("Small")
            }
        }
    }

    @Test
    fun button_Size_Medium_Light() {
        captureArcaneScreenshot(composeTestRule, "controls", "Button_Size_Medium", isDark = false) {
            ArcaneButton(
                style = ArcaneButtonStyle.Filled(),
                size = ArcaneButtonSize.Medium,
                onClick = {}
            ) {
                Text("Medium")
            }
        }
    }

    // ========================================================================
    // Loading State
    // ========================================================================

    @Test
    fun button_Loading_Light() {
        captureArcaneScreenshot(composeTestRule, "controls", "Button_Loading", isDark = false) {
            ArcaneButton(style = ArcaneButtonStyle.Filled(), onClick = {}, loading = true) {
                Text("Loading")
            }
        }
    }

    @Test
    fun button_Loading_Dark() {
        captureArcaneScreenshot(composeTestRule, "controls", "Button_Loading", isDark = true) {
            ArcaneButton(style = ArcaneButtonStyle.Filled(), onClick = {}, loading = true) {
                Text("Loading")
            }
        }
    }
}
