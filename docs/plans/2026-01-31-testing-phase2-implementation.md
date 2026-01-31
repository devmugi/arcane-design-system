# Phase 2: UI Integration Tests - Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Create UI integration test modules with Compose UI Test to verify component interactions, rendering, and accessibility.

**Architecture:** Create two new modules (`arcane-components-ui-tests`, `arcane-chat-ui-tests`) that use Compose UI Test for interaction testing. Build on the test convention plugin from Phase 1.

**Tech Stack:** compose-ui-test, compose-ui-test-junit4, JUnit4 TestRule

**Reference:** See `docs/plans/2026-01-31-comprehensive-testing-design.md` for full PRD.

---

## Task 1: Add Compose UI Test Dependencies

**Files:**
- Modify: `gradle/libs.versions.toml`

**Step 1: Add UI test library definitions**

Add to `[libraries]` section:

```toml
# Compose UI Testing
compose-ui-test = { module = "org.jetbrains.compose.ui:ui-test", version.ref = "compose-multiplatform" }
compose-ui-test-junit4 = { module = "org.jetbrains.compose.ui:ui-test-junit4", version.ref = "compose-multiplatform" }
```

**Step 2: Verify syntax**

Run: `./gradlew --version`
Expected: No errors

**Step 3: Commit**

```bash
git add gradle/libs.versions.toml
git commit -m "build: add Compose UI Test dependencies to version catalog

Add compose-ui-test and compose-ui-test-junit4 for UI integration testing.

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>"
```

---

## Task 2: Create UI Test Convention Plugin

**Files:**
- Create: `build-logic/src/main/kotlin/arcane.multiplatform.uitests.gradle.kts`

**Step 1: Create the convention plugin**

This plugin extends the test plugin with Compose UI Test dependencies:

```kotlin
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

val libs = versionCatalogs.named("libs")

kotlin {
    jvm("desktop") {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    sourceSets {
        val desktopTest by getting {
            dependencies {
                implementation(libs.findLibrary("kotlin-test").get())
                implementation(libs.findLibrary("kotest-assertions-core").get())
                implementation(libs.findLibrary("compose-ui-test").get())
                implementation(libs.findLibrary("compose-ui-test-junit4").get())
                implementation(compose.desktop.currentOs)
            }
        }
    }
}
```

**Note:** UI tests only run on Desktop/JVM for Phase 2. Android instrumented tests and iOS tests are more complex and can be added later.

**Step 2: Verify plugin compiles**

Run: `./gradlew :build-logic:build`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add build-logic/src/main/kotlin/arcane.multiplatform.uitests.gradle.kts
git commit -m "build: add arcane.multiplatform.uitests convention plugin

Convention plugin for UI test modules with Compose UI Test on Desktop/JVM.

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>"
```

---

## Task 3: Create arcane-components-ui-tests Module

**Files:**
- Create: `arcane-components-ui-tests/build.gradle.kts`
- Modify: `settings.gradle.kts`

**Step 1: Create module directory**

```bash
mkdir -p arcane-components-ui-tests/src/desktopTest/kotlin/io/github/devmugi/arcane/design/components
```

**Step 2: Create build.gradle.kts**

```kotlin
plugins {
    id("arcane.multiplatform.uitests")
}

kotlin {
    sourceSets {
        val desktopTest by getting {
            dependencies {
                implementation(project(":arcane-foundation"))
                implementation(project(":arcane-components"))
            }
        }
    }
}
```

**Step 3: Add module to settings.gradle.kts**

```kotlin
include(":arcane-components-ui-tests")
```

**Step 4: Verify module syncs**

Run: `./gradlew :arcane-components-ui-tests:tasks`
Expected: Lists available tasks

**Step 5: Commit**

```bash
git add arcane-components-ui-tests/ settings.gradle.kts
git commit -m "build: create arcane-components-ui-tests module

Empty UI test module structure for component integration tests.

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>"
```

---

## Task 4: Create TestArcaneTheme Utility

**Files:**
- Create: `arcane-components-ui-tests/src/desktopTest/kotlin/io/github/devmugi/arcane/design/testing/TestArcaneTheme.kt`

**Step 1: Create the test theme wrapper**

```kotlin
package io.github.devmugi.arcane.design.testing

import androidx.compose.runtime.Composable
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme

/**
 * Wrapper for tests that provides ArcaneTheme context.
 * Use this in all UI tests to ensure components have proper theme.
 */
@Composable
fun TestArcaneTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    ArcaneTheme(
        colors = if (darkTheme) {
            io.github.devmugi.arcane.design.foundation.theme.ArcaneColors.dark()
        } else {
            io.github.devmugi.arcane.design.foundation.theme.ArcaneColors.default()
        },
        content = content
    )
}
```

**Step 2: Verify compiles**

Run: `./gradlew :arcane-components-ui-tests:compileTestKotlinDesktop`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add arcane-components-ui-tests/
git commit -m "test(ui): add TestArcaneTheme wrapper utility

Provides consistent theme context for all UI tests.

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>"
```

---

## Task 5: Write First UI Test (ArcaneButton)

**Files:**
- Create: `arcane-components-ui-tests/src/desktopTest/kotlin/io/github/devmugi/arcane/design/components/controls/ArcaneButtonUiTest.kt`
- Reference: `arcane-components/src/commonMain/kotlin/.../controls/Button.kt`

**Step 1: Write the UI test**

```kotlin
package io.github.devmugi.arcane.design.components.controls

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import io.github.devmugi.arcane.design.testing.TestArcaneTheme
import io.kotest.matchers.shouldBe
import org.junit.Rule
import org.junit.Test

class ArcaneButtonUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `button displays text content`() {
        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneButton(
                    text = "Submit",
                    onClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Submit").assertIsDisplayed()
    }

    @Test
    fun `button triggers onClick when clicked`() {
        var clicked = false

        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneButton(
                    text = "Click Me",
                    onClick = { clicked = true }
                )
            }
        }

        composeTestRule.onNodeWithText("Click Me").performClick()
        clicked shouldBe true
    }

    @Test
    fun `disabled button does not trigger onClick`() {
        var clicked = false

        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneButton(
                    text = "Disabled",
                    onClick = { clicked = true },
                    enabled = false
                )
            }
        }

        composeTestRule.onNodeWithText("Disabled")
            .assertIsNotEnabled()
            .performClick()

        clicked shouldBe false
    }

    @Test
    fun `button is clickable by default`() {
        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneButton(
                    text = "Active",
                    onClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Active")
            .assertIsEnabled()
            .assertHasClickAction()
    }
}
```

**Step 2: Run test**

Run: `./gradlew :arcane-components-ui-tests:desktopTest --tests "*.ArcaneButtonUiTest"`
Expected: All tests PASS

**Step 3: Commit**

```bash
git add arcane-components-ui-tests/
git commit -m "test(ui): add ArcaneButton UI tests

Test button rendering, click handling, and disabled state.

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>"
```

---

## Task 6: Write TextField UI Tests

**Files:**
- Create: `arcane-components-ui-tests/src/desktopTest/kotlin/io/github/devmugi/arcane/design/components/controls/ArcaneTextFieldUiTest.kt`

**Step 1: Write the UI test**

```kotlin
package io.github.devmugi.arcane.design.components.controls

import androidx.compose.runtime.*
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import io.github.devmugi.arcane.design.testing.TestArcaneTheme
import io.kotest.matchers.shouldBe
import org.junit.Rule
import org.junit.Test

class ArcaneTextFieldUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `textfield displays placeholder when empty`() {
        composeTestRule.setContent {
            TestArcaneTheme {
                var text by remember { mutableStateOf("") }
                ArcaneTextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = "Enter text"
                )
            }
        }

        composeTestRule.onNodeWithText("Enter text").assertIsDisplayed()
    }

    @Test
    fun `textfield accepts input`() {
        var capturedText = ""

        composeTestRule.setContent {
            TestArcaneTheme {
                var text by remember { mutableStateOf("") }
                ArcaneTextField(
                    value = text,
                    onValueChange = {
                        text = it
                        capturedText = it
                    }
                )
            }
        }

        composeTestRule.onNode(hasSetTextAction())
            .performTextInput("Hello World")

        capturedText shouldBe "Hello World"
    }

    @Test
    fun `textfield displays label`() {
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
    fun `disabled textfield does not accept input`() {
        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneTextField(
                    value = "Fixed",
                    onValueChange = {},
                    enabled = false
                )
            }
        }

        composeTestRule.onNodeWithText("Fixed")
            .assertIsNotEnabled()
    }
}
```

**Step 2: Run test**

Run: `./gradlew :arcane-components-ui-tests:desktopTest --tests "*.ArcaneTextFieldUiTest"`
Expected: PASS

**Step 3: Commit**

```bash
git add arcane-components-ui-tests/
git commit -m "test(ui): add ArcaneTextField UI tests

Test text input, placeholder, label, and disabled state.

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>"
```

---

## Task 7: Write Switch/Checkbox UI Tests

**Files:**
- Create: `arcane-components-ui-tests/src/desktopTest/kotlin/io/github/devmugi/arcane/design/components/controls/ToggleControlsUiTest.kt`

**Step 1: Write tests for Switch and Checkbox**

```kotlin
package io.github.devmugi.arcane.design.components.controls

import androidx.compose.runtime.*
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import io.github.devmugi.arcane.design.testing.TestArcaneTheme
import io.kotest.matchers.shouldBe
import org.junit.Rule
import org.junit.Test

class ToggleControlsUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Switch tests
    @Test
    fun `switch toggles when clicked`() {
        var checked = false

        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneSwitch(
                    checked = checked,
                    onCheckedChange = { checked = it }
                )
            }
        }

        composeTestRule.onNode(isToggleable())
            .performClick()

        checked shouldBe true
    }

    @Test
    fun `switch displays initial state`() {
        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneSwitch(
                    checked = true,
                    onCheckedChange = {}
                )
            }
        }

        composeTestRule.onNode(isToggleable())
            .assertIsOn()
    }

    // Checkbox tests
    @Test
    fun `checkbox toggles when clicked`() {
        var checked = false

        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneCheckbox(
                    checked = checked,
                    onCheckedChange = { checked = it }
                )
            }
        }

        composeTestRule.onNode(isToggleable())
            .performClick()

        checked shouldBe true
    }

    @Test
    fun `disabled checkbox does not toggle`() {
        var checked = false

        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneCheckbox(
                    checked = checked,
                    onCheckedChange = { checked = it },
                    enabled = false
                )
            }
        }

        composeTestRule.onNode(isToggleable())
            .assertIsNotEnabled()
            .performClick()

        checked shouldBe false
    }
}
```

**Step 2: Run test**

Run: `./gradlew :arcane-components-ui-tests:desktopTest --tests "*.ToggleControlsUiTest"`
Expected: PASS

**Step 3: Commit**

```bash
git add arcane-components-ui-tests/
git commit -m "test(ui): add Switch and Checkbox UI tests

Test toggle behavior and disabled states.

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>"
```

---

## Task 8: Write Card and ListItem UI Tests

**Files:**
- Create: `arcane-components-ui-tests/src/desktopTest/kotlin/io/github/devmugi/arcane/design/components/display/DisplayComponentsUiTest.kt`

**Step 1: Write tests**

```kotlin
package io.github.devmugi.arcane.design.components.display

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import io.github.devmugi.arcane.design.testing.TestArcaneTheme
import org.junit.Rule
import org.junit.Test

class DisplayComponentsUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `card displays content`() {
        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneCard {
                    Text("Card Content")
                }
            }
        }

        composeTestRule.onNodeWithText("Card Content").assertIsDisplayed()
    }

    @Test
    fun `clickable card triggers onClick`() {
        var clicked = false

        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneCard(
                    onClick = { clicked = true }
                ) {
                    Text("Clickable Card")
                }
            }
        }

        composeTestRule.onNodeWithText("Clickable Card").performClick()
        assert(clicked)
    }

    @Test
    fun `list item displays title`() {
        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneListItem(
                    headlineContent = { Text("Item Title") }
                )
            }
        }

        composeTestRule.onNodeWithText("Item Title").assertIsDisplayed()
    }

    @Test
    fun `list item displays supporting text`() {
        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneListItem(
                    headlineContent = { Text("Title") },
                    supportingContent = { Text("Description") }
                )
            }
        }

        composeTestRule.onNodeWithText("Description").assertIsDisplayed()
    }
}
```

**Step 2: Run test**

Run: `./gradlew :arcane-components-ui-tests:desktopTest --tests "*.DisplayComponentsUiTest"`
Expected: PASS

**Step 3: Commit**

```bash
git add arcane-components-ui-tests/
git commit -m "test(ui): add Card and ListItem UI tests

Test content rendering and click interactions.

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>"
```

---

## Task 9: Write Tabs UI Tests

**Files:**
- Create: `arcane-components-ui-tests/src/desktopTest/kotlin/io/github/devmugi/arcane/design/components/navigation/TabsUiTest.kt`

**Step 1: Write tests**

```kotlin
package io.github.devmugi.arcane.design.components.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import io.github.devmugi.arcane.design.testing.TestArcaneTheme
import io.kotest.matchers.shouldBe
import org.junit.Rule
import org.junit.Test

class TabsUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `tabs display all tab labels`() {
        val tabs = listOf("Home", "Profile", "Settings")

        composeTestRule.setContent {
            TestArcaneTheme {
                var selectedIndex by remember { mutableIntStateOf(0) }
                ArcaneTabs(
                    tabs = tabs,
                    selectedIndex = selectedIndex,
                    onTabSelected = { selectedIndex = it }
                )
            }
        }

        tabs.forEach { tab ->
            composeTestRule.onNodeWithText(tab).assertIsDisplayed()
        }
    }

    @Test
    fun `tab selection changes on click`() {
        var selectedIndex = 0
        val tabs = listOf("Tab 1", "Tab 2", "Tab 3")

        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneTabs(
                    tabs = tabs,
                    selectedIndex = selectedIndex,
                    onTabSelected = { selectedIndex = it }
                )
            }
        }

        composeTestRule.onNodeWithText("Tab 2").performClick()
        selectedIndex shouldBe 1
    }

    @Test
    fun `selected tab is indicated`() {
        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneTabs(
                    tabs = listOf("Selected", "Not Selected"),
                    selectedIndex = 0,
                    onTabSelected = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Selected")
            .assertIsSelected()
    }
}
```

**Step 2: Run test**

Run: `./gradlew :arcane-components-ui-tests:desktopTest --tests "*.TabsUiTest"`
Expected: PASS

**Step 3: Commit**

```bash
git add arcane-components-ui-tests/
git commit -m "test(ui): add Tabs UI tests

Test tab rendering and selection behavior.

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>"
```

---

## Task 10: Create arcane-chat-ui-tests Module

**Files:**
- Create: `arcane-chat-ui-tests/build.gradle.kts`
- Modify: `settings.gradle.kts`

**Step 1: Create module directory**

```bash
mkdir -p arcane-chat-ui-tests/src/desktopTest/kotlin/io/github/devmugi/arcane/chat
```

**Step 2: Create build.gradle.kts**

```kotlin
plugins {
    id("arcane.multiplatform.uitests")
}

kotlin {
    sourceSets {
        val desktopTest by getting {
            dependencies {
                implementation(project(":arcane-foundation"))
                implementation(project(":arcane-components"))
                implementation(project(":arcane-chat"))
            }
        }
    }
}
```

**Step 3: Add module to settings.gradle.kts**

```kotlin
include(":arcane-chat-ui-tests")
```

**Step 4: Verify module syncs**

Run: `./gradlew :arcane-chat-ui-tests:tasks`
Expected: Lists available tasks

**Step 5: Commit**

```bash
git add arcane-chat-ui-tests/ settings.gradle.kts
git commit -m "build: create arcane-chat-ui-tests module

Empty UI test module structure for chat component integration tests.

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>"
```

---

## Task 11: Write Chat Message UI Tests

**Files:**
- Create: `arcane-chat-ui-tests/src/desktopTest/kotlin/io/github/devmugi/arcane/chat/messages/ChatMessageUiTest.kt`

**Step 1: Write tests for chat message components**

```kotlin
package io.github.devmugi.arcane.chat.messages

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import io.github.devmugi.arcane.chat.components.messages.*
import io.github.devmugi.arcane.chat.models.*
import io.github.devmugi.arcane.design.testing.TestArcaneTheme
import org.junit.Rule
import org.junit.Test

class ChatMessageUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `user message displays content`() {
        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneUserMessageBlock(
                    message = ChatMessage.User(
                        blocks = listOf(MessageBlock.Text("Hello, World!"))
                    )
                )
            }
        }

        composeTestRule.onNodeWithText("Hello, World!").assertIsDisplayed()
    }

    @Test
    fun `assistant message displays title and content`() {
        composeTestRule.setContent {
            TestArcaneTheme {
                ArcaneAssistantMessageBlock(
                    message = ChatMessage.Assistant(
                        title = "Assistant",
                        blocks = listOf(MessageBlock.Text("I can help you!"))
                    )
                )
            }
        }

        composeTestRule.onNodeWithText("I can help you!").assertExists()
    }
}
```

**Step 2: Run test**

Run: `./gradlew :arcane-chat-ui-tests:desktopTest --tests "*.ChatMessageUiTest"`
Expected: PASS

**Step 3: Commit**

```bash
git add arcane-chat-ui-tests/
git commit -m "test(ui): add Chat message UI tests

Test user and assistant message rendering.

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>"
```

---

## Task 12: Run All UI Tests and Verify

**Files:**
- None (verification only)

**Step 1: Run all UI tests**

```bash
./gradlew :arcane-components-ui-tests:desktopTest :arcane-chat-ui-tests:desktopTest
```
Expected: All tests PASS

**Step 2: Count tests**

```bash
grep -r "@Test" arcane-*-ui-tests/src/ | wc -l
```
Expected: 20+ tests

**Step 3: Verify test reports**

```bash
ls -la arcane-*-ui-tests/build/reports/tests/
```
Expected: Reports exist

---

## Task 13: Final Phase 2 Summary Commit

**Files:**
- Modify: `docs/plans/2026-01-31-comprehensive-testing-design.md`

**Step 1: Update PRD status**

Mark Phase 2 complete in the PRD.

**Step 2: Commit**

```bash
git add docs/plans/
git commit -m "docs: mark Phase 2 complete in testing PRD

Phase 2 deliverables:
- Compose UI Test dependencies added
- Convention plugin arcane.multiplatform.uitests created
- arcane-components-ui-tests module with UI tests
- arcane-chat-ui-tests module with UI tests
- TestArcaneTheme utility wrapper

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>"
```

---

## Phase 2 Complete Checklist

- [ ] Compose UI Test dependencies in libs.versions.toml
- [ ] Convention plugin arcane.multiplatform.uitests
- [ ] arcane-components-ui-tests module
- [ ] arcane-chat-ui-tests module
- [ ] TestArcaneTheme utility
- [ ] ArcaneButton UI tests
- [ ] ArcaneTextField UI tests
- [ ] Switch/Checkbox UI tests
- [ ] Card/ListItem UI tests
- [ ] Tabs UI tests
- [ ] Chat message UI tests
- [ ] All tests pass
- [ ] PRD updated

---

*Next: Phase 3 - Screenshot Tests with Roborazzi*
