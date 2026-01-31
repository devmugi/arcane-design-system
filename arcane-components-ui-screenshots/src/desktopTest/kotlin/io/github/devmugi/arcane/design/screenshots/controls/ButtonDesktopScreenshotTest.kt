package io.github.devmugi.arcane.design.screenshots.controls

import androidx.compose.material3.Text
import io.github.devmugi.arcane.design.components.controls.ArcaneButton
import io.github.devmugi.arcane.design.components.controls.ArcaneButtonStyle
import io.github.devmugi.arcane.design.screenshots.captureDesktopScreenshot
import kotlin.test.Test

class ButtonDesktopScreenshotTest {

    // ========================================================================
    // Filled Style
    // ========================================================================

    @Test
    fun button_Filled_Light() {
        captureDesktopScreenshot("controls", "Button_Filled", isDark = false) {
            ArcaneButton(style = ArcaneButtonStyle.Filled(), onClick = {}) {
                Text("Filled Button")
            }
        }
    }

    @Test
    fun button_Filled_Dark() {
        captureDesktopScreenshot("controls", "Button_Filled", isDark = true) {
            ArcaneButton(style = ArcaneButtonStyle.Filled(), onClick = {}) {
                Text("Filled Button")
            }
        }
    }

    @Test
    fun button_Filled_Disabled_Light() {
        captureDesktopScreenshot("controls", "Button_Filled_Disabled", isDark = false) {
            ArcaneButton(style = ArcaneButtonStyle.Filled(), onClick = {}, enabled = false) {
                Text("Disabled")
            }
        }
    }

    @Test
    fun button_Filled_Disabled_Dark() {
        captureDesktopScreenshot("controls", "Button_Filled_Disabled", isDark = true) {
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
        captureDesktopScreenshot("controls", "Button_Tonal", isDark = false) {
            ArcaneButton(style = ArcaneButtonStyle.Tonal(), onClick = {}) {
                Text("Tonal Button")
            }
        }
    }

    @Test
    fun button_Tonal_Dark() {
        captureDesktopScreenshot("controls", "Button_Tonal", isDark = true) {
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
        captureDesktopScreenshot("controls", "Button_Outlined", isDark = false) {
            ArcaneButton(style = ArcaneButtonStyle.Outlined(), onClick = {}) {
                Text("Outlined Button")
            }
        }
    }

    @Test
    fun button_Outlined_Dark() {
        captureDesktopScreenshot("controls", "Button_Outlined", isDark = true) {
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
        captureDesktopScreenshot("controls", "Button_Elevated", isDark = false) {
            ArcaneButton(style = ArcaneButtonStyle.Elevated(), onClick = {}) {
                Text("Elevated Button")
            }
        }
    }

    @Test
    fun button_Elevated_Dark() {
        captureDesktopScreenshot("controls", "Button_Elevated", isDark = true) {
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
        captureDesktopScreenshot("controls", "Button_Text", isDark = false) {
            ArcaneButton(style = ArcaneButtonStyle.Text, onClick = {}) {
                Text("Text Button")
            }
        }
    }

    @Test
    fun button_Text_Dark() {
        captureDesktopScreenshot("controls", "Button_Text", isDark = true) {
            ArcaneButton(style = ArcaneButtonStyle.Text, onClick = {}) {
                Text("Text Button")
            }
        }
    }
}
