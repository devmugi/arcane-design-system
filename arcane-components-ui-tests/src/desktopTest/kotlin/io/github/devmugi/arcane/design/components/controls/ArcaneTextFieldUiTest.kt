package io.github.devmugi.arcane.design.components.controls

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import io.github.devmugi.arcane.design.testing.TestArcaneTheme
import io.kotest.matchers.shouldBe
import org.junit.Rule
import org.junit.Test

class ArcaneTextFieldUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `TextField displays placeholder when empty`() {
        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = "Enter text here"
                )
            }
        }

        composeTestRule.onNodeWithText("Enter text here").assertExists()
    }

    @Test
    fun `TextField hides placeholder when value is not empty`() {
        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneTextField(
                    value = "Some text",
                    onValueChange = {},
                    placeholder = "Enter text here"
                )
            }
        }

        composeTestRule.onNodeWithText("Enter text here").assertDoesNotExist()
        composeTestRule.onNodeWithText("Some text").assertExists()
    }

    @Test
    fun `TextField accepts input`() {
        var textValue by mutableStateOf("")

        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneTextField(
                    value = textValue,
                    onValueChange = { textValue = it },
                    placeholder = "Type something"
                )
            }
        }

        composeTestRule.onNodeWithText("Type something").performTextInput("Hello World")

        textValue shouldBe "Hello World"
    }

    @Test
    fun `TextField displays label`() {
        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneTextField(
                    value = "",
                    onValueChange = {},
                    label = "Username"
                )
            }
        }

        composeTestRule.onNodeWithText("Username").assertExists()
    }

    @Test
    fun `TextField displays label and placeholder together`() {
        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneTextField(
                    value = "",
                    onValueChange = {},
                    label = "Email",
                    placeholder = "Enter your email"
                )
            }
        }

        composeTestRule.onNodeWithText("Email").assertExists()
        composeTestRule.onNodeWithText("Enter your email").assertExists()
    }

    @Test
    fun `disabled TextField does not accept input`() {
        var textValue by mutableStateOf("")

        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneTextField(
                    value = textValue,
                    onValueChange = { textValue = it },
                    enabled = false,
                    placeholder = "Disabled field"
                )
            }
        }

        composeTestRule.onNodeWithText("Disabled field").assertIsNotEnabled()
        textValue shouldBe ""
    }

    @Test
    fun `TextField displays helper text`() {
        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneTextField(
                    value = "",
                    onValueChange = {},
                    helperText = "This field is required"
                )
            }
        }

        composeTestRule.onNodeWithText("This field is required").assertExists()
    }

    @Test
    fun `TextField displays error text`() {
        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneTextField(
                    value = "",
                    onValueChange = {},
                    errorText = "Invalid input"
                )
            }
        }

        composeTestRule.onNodeWithText("Invalid input").assertExists()
    }

    @Test
    fun `TextField is enabled by default`() {
        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = "Default field"
                )
            }
        }

        composeTestRule.onNodeWithText("Default field").assertIsEnabled()
    }
}
