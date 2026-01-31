package io.github.devmugi.arcane.design.components.controls

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import io.github.devmugi.arcane.design.testing.TestArcaneTheme
import io.kotest.matchers.shouldBe
import org.junit.Rule
import org.junit.Test

class ToggleControlsUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // ========== ArcaneSwitch Tests ==========

    @Test
    fun `switch displays label text`() {
        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneSwitch(
                    checked = false,
                    onCheckedChange = {},
                    label = "Test Switch"
                )
            }
        }

        composeTestRule.onNodeWithText("Test Switch").assertExists()
    }

    @Test
    fun `switch displays initial state`() {
        var capturedState: Boolean? = null

        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneSwitch(
                    checked = true,
                    onCheckedChange = { capturedState = it },
                    label = "Enabled Switch"
                )
            }
        }

        // Verify the component exists and the initial state is preserved
        composeTestRule.onNodeWithText("Enabled Switch").assertExists()
        // capturedState should be null because no click has happened
        capturedState shouldBe null
    }

    @Test
    fun `switch toggles when clicked`() {
        var isChecked by mutableStateOf(false)

        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneSwitch(
                    checked = isChecked,
                    onCheckedChange = { isChecked = it },
                    label = "Toggle Switch"
                )
            }
        }

        // Initial state
        isChecked shouldBe false

        // Click to toggle on
        composeTestRule.onNodeWithText("Toggle Switch").performClick()
        isChecked shouldBe true

        // Click to toggle off
        composeTestRule.onNodeWithText("Toggle Switch").performClick()
        isChecked shouldBe false
    }

    @Test
    fun `disabled switch does not toggle`() {
        var isChecked by mutableStateOf(false)

        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneSwitch(
                    checked = isChecked,
                    onCheckedChange = { isChecked = it },
                    enabled = false,
                    label = "Disabled Switch"
                )
            }
        }

        composeTestRule.onNodeWithText("Disabled Switch").performClick()

        isChecked shouldBe false
    }

    @Test
    fun `switch callback receives correct value when toggling on`() {
        var receivedValue: Boolean? = null

        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneSwitch(
                    checked = false,
                    onCheckedChange = { receivedValue = it },
                    label = "Callback Switch"
                )
            }
        }

        composeTestRule.onNodeWithText("Callback Switch").performClick()

        receivedValue shouldBe true
    }

    @Test
    fun `switch callback receives correct value when toggling off`() {
        var receivedValue: Boolean? = null

        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneSwitch(
                    checked = true,
                    onCheckedChange = { receivedValue = it },
                    label = "Callback Switch"
                )
            }
        }

        composeTestRule.onNodeWithText("Callback Switch").performClick()

        receivedValue shouldBe false
    }

    // ========== ArcaneCheckbox Tests ==========

    @Test
    fun `checkbox displays label text`() {
        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneCheckbox(
                    checked = false,
                    onCheckedChange = {},
                    label = "Test Checkbox"
                )
            }
        }

        composeTestRule.onNodeWithText("Test Checkbox").assertExists()
    }

    @Test
    fun `checkbox displays initial state`() {
        var capturedState: Boolean? = null

        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneCheckbox(
                    checked = true,
                    onCheckedChange = { capturedState = it },
                    label = "Checked Checkbox"
                )
            }
        }

        // Verify the component exists and the initial state is preserved
        composeTestRule.onNodeWithText("Checked Checkbox").assertExists()
        // capturedState should be null because no click has happened
        capturedState shouldBe null
    }

    @Test
    fun `checkbox toggles when clicked`() {
        var isChecked by mutableStateOf(false)

        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneCheckbox(
                    checked = isChecked,
                    onCheckedChange = { isChecked = it },
                    label = "Toggle Checkbox"
                )
            }
        }

        // Initial state
        isChecked shouldBe false

        // Click to check
        composeTestRule.onNodeWithText("Toggle Checkbox").performClick()
        isChecked shouldBe true

        // Click to uncheck
        composeTestRule.onNodeWithText("Toggle Checkbox").performClick()
        isChecked shouldBe false
    }

    @Test
    fun `disabled checkbox does not toggle`() {
        var isChecked by mutableStateOf(false)

        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneCheckbox(
                    checked = isChecked,
                    onCheckedChange = { isChecked = it },
                    enabled = false,
                    label = "Disabled Checkbox"
                )
            }
        }

        composeTestRule.onNodeWithText("Disabled Checkbox").performClick()

        isChecked shouldBe false
    }

    @Test
    fun `checkbox callback receives correct value when checking`() {
        var receivedValue: Boolean? = null

        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneCheckbox(
                    checked = false,
                    onCheckedChange = { receivedValue = it },
                    label = "Callback Checkbox"
                )
            }
        }

        composeTestRule.onNodeWithText("Callback Checkbox").performClick()

        receivedValue shouldBe true
    }

    @Test
    fun `checkbox callback receives correct value when unchecking`() {
        var receivedValue: Boolean? = null

        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneCheckbox(
                    checked = true,
                    onCheckedChange = { receivedValue = it },
                    label = "Callback Checkbox"
                )
            }
        }

        composeTestRule.onNodeWithText("Callback Checkbox").performClick()

        receivedValue shouldBe false
    }
}
