package io.github.devmugi.arcane.design.screenshots.controls

import io.github.devmugi.arcane.design.components.controls.ArcaneTextField
import io.github.devmugi.arcane.design.screenshots.captureDesktopScreenshot
import kotlin.test.Test

class TextFieldDesktopScreenshotTest {

    // ========================================================================
    // Default State
    // ========================================================================

    @Test
    fun textField_Default_Light() {
        captureDesktopScreenshot("TextField_Default", isDark = false) {
            ArcaneTextField(
                value = "Sample text",
                onValueChange = {}
            )
        }
    }

    @Test
    fun textField_Default_Dark() {
        captureDesktopScreenshot("TextField_Default", isDark = true) {
            ArcaneTextField(
                value = "Sample text",
                onValueChange = {}
            )
        }
    }

    // ========================================================================
    // Placeholder State
    // ========================================================================

    @Test
    fun textField_Placeholder_Light() {
        captureDesktopScreenshot("TextField_Placeholder", isDark = false) {
            ArcaneTextField(
                value = "",
                onValueChange = {},
                placeholder = "Enter your text"
            )
        }
    }

    @Test
    fun textField_Placeholder_Dark() {
        captureDesktopScreenshot("TextField_Placeholder", isDark = true) {
            ArcaneTextField(
                value = "",
                onValueChange = {},
                placeholder = "Enter your text"
            )
        }
    }

    // ========================================================================
    // With Label
    // ========================================================================

    @Test
    fun textField_WithLabel_Light() {
        captureDesktopScreenshot("TextField_WithLabel", isDark = false, height = 80) {
            ArcaneTextField(
                value = "Input value",
                onValueChange = {},
                label = "Email"
            )
        }
    }

    @Test
    fun textField_WithLabel_Dark() {
        captureDesktopScreenshot("TextField_WithLabel", isDark = true, height = 80) {
            ArcaneTextField(
                value = "Input value",
                onValueChange = {},
                label = "Email"
            )
        }
    }

    // ========================================================================
    // Error State
    // ========================================================================

    @Test
    fun textField_Error_Light() {
        captureDesktopScreenshot("TextField_Error", isDark = false, height = 100) {
            ArcaneTextField(
                value = "invalid",
                onValueChange = {},
                label = "Email",
                errorText = "Please enter a valid email"
            )
        }
    }

    @Test
    fun textField_Error_Dark() {
        captureDesktopScreenshot("TextField_Error", isDark = true, height = 100) {
            ArcaneTextField(
                value = "invalid",
                onValueChange = {},
                label = "Email",
                errorText = "Please enter a valid email"
            )
        }
    }

    // ========================================================================
    // Disabled State
    // ========================================================================

    @Test
    fun textField_Disabled_Light() {
        captureDesktopScreenshot("TextField_Disabled", isDark = false) {
            ArcaneTextField(
                value = "Disabled text",
                onValueChange = {},
                enabled = false
            )
        }
    }

    @Test
    fun textField_Disabled_Dark() {
        captureDesktopScreenshot("TextField_Disabled", isDark = true) {
            ArcaneTextField(
                value = "Disabled text",
                onValueChange = {},
                enabled = false
            )
        }
    }
}
