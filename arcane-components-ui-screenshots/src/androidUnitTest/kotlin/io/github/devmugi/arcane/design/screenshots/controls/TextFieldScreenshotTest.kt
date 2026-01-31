package io.github.devmugi.arcane.design.screenshots.controls

import androidx.compose.ui.test.junit4.createComposeRule
import com.github.takahirom.roborazzi.RoborazziRule
import io.github.devmugi.arcane.design.components.controls.ArcaneTextField
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
class TextFieldScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val roborazziRule = RoborazziRule()

    // ========================================================================
    // Default State (with value)
    // ========================================================================

    @Test
    fun textField_Default_Light() {
        captureArcaneScreenshot(composeTestRule, "TextField_Default", isDark = false) {
            ArcaneTextField(
                value = "Sample text",
                onValueChange = {}
            )
        }
    }

    @Test
    fun textField_Default_Dark() {
        captureArcaneScreenshot(composeTestRule, "TextField_Default", isDark = true) {
            ArcaneTextField(
                value = "Sample text",
                onValueChange = {}
            )
        }
    }

    // ========================================================================
    // Placeholder State (empty)
    // ========================================================================

    @Test
    fun textField_Placeholder_Light() {
        captureArcaneScreenshot(composeTestRule, "TextField_Placeholder", isDark = false) {
            ArcaneTextField(
                value = "",
                onValueChange = {},
                placeholder = "Enter your text"
            )
        }
    }

    @Test
    fun textField_Placeholder_Dark() {
        captureArcaneScreenshot(composeTestRule, "TextField_Placeholder", isDark = true) {
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
        captureArcaneScreenshot(composeTestRule, "TextField_WithLabel", isDark = false) {
            ArcaneTextField(
                value = "Input value",
                onValueChange = {},
                label = "Email"
            )
        }
    }

    @Test
    fun textField_WithLabel_Dark() {
        captureArcaneScreenshot(composeTestRule, "TextField_WithLabel", isDark = true) {
            ArcaneTextField(
                value = "Input value",
                onValueChange = {},
                label = "Email"
            )
        }
    }

    // ========================================================================
    // With Helper Text
    // ========================================================================

    @Test
    fun textField_WithHelperText_Light() {
        captureArcaneScreenshot(composeTestRule, "TextField_WithHelperText", isDark = false) {
            ArcaneTextField(
                value = "user@example.com",
                onValueChange = {},
                label = "Email",
                helperText = "We'll never share your email"
            )
        }
    }

    @Test
    fun textField_WithHelperText_Dark() {
        captureArcaneScreenshot(composeTestRule, "TextField_WithHelperText", isDark = true) {
            ArcaneTextField(
                value = "user@example.com",
                onValueChange = {},
                label = "Email",
                helperText = "We'll never share your email"
            )
        }
    }

    // ========================================================================
    // Error State
    // ========================================================================

    @Test
    fun textField_Error_Light() {
        captureArcaneScreenshot(composeTestRule, "TextField_Error", isDark = false) {
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
        captureArcaneScreenshot(composeTestRule, "TextField_Error", isDark = true) {
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
        captureArcaneScreenshot(composeTestRule, "TextField_Disabled", isDark = false) {
            ArcaneTextField(
                value = "Disabled text",
                onValueChange = {},
                enabled = false
            )
        }
    }

    @Test
    fun textField_Disabled_Dark() {
        captureArcaneScreenshot(composeTestRule, "TextField_Disabled", isDark = true) {
            ArcaneTextField(
                value = "Disabled text",
                onValueChange = {},
                enabled = false
            )
        }
    }

    // ========================================================================
    // Disabled with Label
    // ========================================================================

    @Test
    fun textField_DisabledWithLabel_Light() {
        captureArcaneScreenshot(composeTestRule, "TextField_DisabledWithLabel", isDark = false) {
            ArcaneTextField(
                value = "Disabled content",
                onValueChange = {},
                label = "Username",
                enabled = false
            )
        }
    }

    @Test
    fun textField_DisabledWithLabel_Dark() {
        captureArcaneScreenshot(composeTestRule, "TextField_DisabledWithLabel", isDark = true) {
            ArcaneTextField(
                value = "Disabled content",
                onValueChange = {},
                label = "Username",
                enabled = false
            )
        }
    }

    // ========================================================================
    // Password Field
    // ========================================================================

    @Test
    fun textField_Password_Light() {
        captureArcaneScreenshot(composeTestRule, "TextField_Password", isDark = false) {
            ArcaneTextField(
                value = "secretpass",
                onValueChange = {},
                label = "Password",
                isPassword = true
            )
        }
    }

    @Test
    fun textField_Password_Dark() {
        captureArcaneScreenshot(composeTestRule, "TextField_Password", isDark = true) {
            ArcaneTextField(
                value = "secretpass",
                onValueChange = {},
                label = "Password",
                isPassword = true
            )
        }
    }
}
