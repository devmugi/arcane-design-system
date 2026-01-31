# Phase 3: Screenshot Tests with Roborazzi - Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Establish visual regression testing using Roborazzi for Android/JVM screenshot capture

**Architecture:** Create two screenshot modules that capture golden images for all components in light/dark themes. Uses Roborazzi for Android unit tests and JVM tests.

**Tech Stack:** Roborazzi 1.8.0, JUnit4, Compose UI Test

**Reference:** See `docs/plans/2026-01-31-comprehensive-testing-design.md` for full PRD.

---

## Task 1: Add Roborazzi Dependencies to Version Catalog

**Files:**
- Modify: `gradle/libs.versions.toml`

**Step 1: Add Roborazzi version and libraries**

Add to `[versions]` section:
```toml
roborazzi = "1.8.0"
robolectric = "4.14.1"
```

Add to `[libraries]` section:
```toml
# Screenshot testing
roborazzi = { module = "io.github.takahirom.roborazzi:roborazzi", version.ref = "roborazzi" }
roborazzi-compose = { module = "io.github.takahirom.roborazzi:roborazzi-compose", version.ref = "roborazzi" }
roborazzi-junit-rule = { module = "io.github.takahirom.roborazzi:roborazzi-junit-rule", version.ref = "roborazzi" }
robolectric = { module = "org.robolectric:robolectric", version.ref = "robolectric" }
```

Add to `[plugins]` section:
```toml
roborazzi = { id = "io.github.takahirom.roborazzi", version.ref = "roborazzi" }
```

**Step 2: Verify syntax**

Run: `./gradlew --version`
Expected: No errors

**Step 3: Commit**

```bash
git add gradle/libs.versions.toml
git commit -m "build: add Roborazzi dependencies for screenshot testing

Add roborazzi, roborazzi-compose, roborazzi-junit-rule, and robolectric.

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>"
```

---

## Task 2: Configure Git LFS for Golden Images

**Files:**
- Create: `.gitattributes` (if not exists, modify if exists)

**Step 1: Check if Git LFS is installed**

Run: `git lfs version`
If not installed, inform user to install.

**Step 2: Add LFS pattern for golden images**

Add to `.gitattributes`:
```
# Golden images for screenshot testing
**/golden/**/*.png filter=lfs diff=lfs merge=lfs -text
```

**Step 3: Track pattern**

Run: `git lfs track "**/golden/**/*.png"`

**Step 4: Commit**

```bash
git add .gitattributes
git commit -m "build: configure Git LFS for screenshot golden images

Track PNG files in golden directories with Git LFS.

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>"
```

---

## Task 3: Create Screenshot Convention Plugin

**Files:**
- Create: `build-logic/src/main/kotlin/arcane.multiplatform.screenshots.gradle.kts`

**Step 1: Create the convention plugin**

```kotlin
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.android.library")
    id("io.github.takahirom.roborazzi")
}

val libs = versionCatalogs.named("libs")

android {
    namespace = "io.github.devmugi.arcane.design.screenshots"
    compileSdk = 35

    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            all {
                it.systemProperties["robolectric.graphicsMode"] = "NATIVE"
            }
        }
    }
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    jvm("desktop") {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    sourceSets {
        val commonMain by getting

        val androidUnitTest by getting {
            dependencies {
                implementation(libs.findLibrary("kotlin-test").get())
                implementation(libs.findLibrary("kotest-assertions-core").get())
                implementation(libs.findLibrary("compose-ui-test").get())
                implementation(libs.findLibrary("compose-ui-test-junit4").get())
                implementation(libs.findLibrary("roborazzi").get())
                implementation(libs.findLibrary("roborazzi-compose").get())
                implementation(libs.findLibrary("roborazzi-junit-rule").get())
                implementation(libs.findLibrary("robolectric").get())
            }
        }

        val desktopTest by getting {
            dependencies {
                implementation(libs.findLibrary("kotlin-test").get())
                implementation(libs.findLibrary("kotest-assertions-core").get())
                implementation(libs.findLibrary("compose-ui-test").get())
                implementation(libs.findLibrary("compose-ui-test-junit4").get())
                implementation(libs.findLibrary("roborazzi").get())
                implementation(libs.findLibrary("roborazzi-compose").get())
                implementation(libs.findLibrary("roborazzi-junit-rule").get())
                implementation(compose.desktop.currentOs)
            }
        }
    }
}
```

**Step 2: Verify plugin compiles**

Run: `./gradlew :build-logic:build`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add build-logic/
git commit -m "build: add arcane.multiplatform.screenshots convention plugin

Convention plugin for screenshot test modules with Roborazzi on Android and Desktop.

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>"
```

---

## Task 4: Create arcane-components-ui-screenshots Module

**Files:**
- Create: `arcane-components-ui-screenshots/build.gradle.kts`
- Modify: `settings.gradle.kts`

**Step 1: Create module directory**

```bash
mkdir -p arcane-components-ui-screenshots/src/androidUnitTest/kotlin/io/github/devmugi/arcane/design/screenshots
mkdir -p arcane-components-ui-screenshots/src/desktopTest/kotlin/io/github/devmugi/arcane/design/screenshots
mkdir -p arcane-components-ui-screenshots/golden/android/light
mkdir -p arcane-components-ui-screenshots/golden/android/dark
mkdir -p arcane-components-ui-screenshots/golden/desktop/light
mkdir -p arcane-components-ui-screenshots/golden/desktop/dark
```

**Step 2: Create build.gradle.kts**

```kotlin
plugins {
    id("arcane.multiplatform.screenshots")
}

android {
    namespace = "io.github.devmugi.arcane.design.screenshots.components"
}

kotlin {
    sourceSets {
        val androidUnitTest by getting {
            dependencies {
                implementation(project(":arcane-foundation"))
                implementation(project(":arcane-components"))
            }
        }

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
include(":arcane-components-ui-screenshots")
```

**Step 4: Verify module syncs**

Run: `./gradlew :arcane-components-ui-screenshots:tasks`
Expected: Lists available tasks including `recordRoborazzi*` and `verifyRoborazzi*`

**Step 5: Commit**

```bash
git add arcane-components-ui-screenshots/ settings.gradle.kts
git commit -m "build: create arcane-components-ui-screenshots module

Empty screenshot test module structure with golden image directories.

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>"
```

---

## Task 5: Create Screenshot Test Utilities

**Files:**
- Create: `arcane-components-ui-screenshots/src/androidUnitTest/kotlin/io/github/devmugi/arcane/design/screenshots/ScreenshotTestUtils.kt`

**Step 1: Create the test utilities**

```kotlin
package io.github.devmugi.arcane.design.screenshots

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.captureRoboImage
import io.github.devmugi.arcane.design.foundation.theme.ArcaneColors
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme

/**
 * Captures a screenshot of the given composable in both light and dark themes.
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
    val filePath = "golden/android/$theme/${componentName}.png"

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

/**
 * Captures a component in both light and dark themes.
 */
fun captureArcaneScreenshotBothThemes(
    composeTestRule: ComposeContentTestRule,
    componentName: String,
    content: @Composable () -> Unit
) {
    // Capture light theme
    captureArcaneScreenshot(composeTestRule, componentName, isDark = false, content = content)

    // Capture dark theme
    captureArcaneScreenshot(composeTestRule, "${componentName}_Dark", isDark = true, content = content)
}
```

**Step 2: Verify compiles**

Run: `./gradlew :arcane-components-ui-screenshots:compileTestKotlinAndroid`
Expected: BUILD SUCCESSFUL (may have warnings about missing tests, that's OK)

**Step 3: Commit**

```bash
git add arcane-components-ui-screenshots/
git commit -m "test(screenshots): add screenshot capture utilities

Add captureArcaneScreenshot and captureArcaneScreenshotBothThemes helpers.

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>"
```

---

## Task 6: Write Button Screenshot Tests

**Files:**
- Create: `arcane-components-ui-screenshots/src/androidUnitTest/kotlin/io/github/devmugi/arcane/design/screenshots/controls/ButtonScreenshotTest.kt`

**Step 1: Study the Button API**

Read `arcane-components/src/commonMain/kotlin/.../controls/Button.kt` to understand styles.

**Step 2: Create screenshot test**

```kotlin
package io.github.devmugi.arcane.design.screenshots.controls

import androidx.compose.material3.Text
import androidx.compose.ui.test.junit4.createComposeRule
import com.github.takahirom.roborazzi.RoborazziRule
import io.github.devmugi.arcane.design.components.controls.ArcaneButton
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

    // Filled style
    @Test
    fun button_Filled_Light() {
        captureArcaneScreenshot(composeTestRule, "Button_Filled", isDark = false) {
            ArcaneButton(style = ArcaneButtonStyle.Filled, onClick = {}) {
                Text("Filled Button")
            }
        }
    }

    @Test
    fun button_Filled_Dark() {
        captureArcaneScreenshot(composeTestRule, "Button_Filled", isDark = true) {
            ArcaneButton(style = ArcaneButtonStyle.Filled, onClick = {}) {
                Text("Filled Button")
            }
        }
    }

    @Test
    fun button_Filled_Disabled_Light() {
        captureArcaneScreenshot(composeTestRule, "Button_Filled_Disabled", isDark = false) {
            ArcaneButton(style = ArcaneButtonStyle.Filled, onClick = {}, enabled = false) {
                Text("Disabled")
            }
        }
    }

    // Outlined style
    @Test
    fun button_Outlined_Light() {
        captureArcaneScreenshot(composeTestRule, "Button_Outlined", isDark = false) {
            ArcaneButton(style = ArcaneButtonStyle.Outlined(), onClick = {}) {
                Text("Outlined Button")
            }
        }
    }

    @Test
    fun button_Outlined_Dark() {
        captureArcaneScreenshot(composeTestRule, "Button_Outlined", isDark = true) {
            ArcaneButton(style = ArcaneButtonStyle.Outlined(), onClick = {}) {
                Text("Outlined Button")
            }
        }
    }

    // Text style
    @Test
    fun button_Text_Light() {
        captureArcaneScreenshot(composeTestRule, "Button_Text", isDark = false) {
            ArcaneButton(style = ArcaneButtonStyle.Text, onClick = {}) {
                Text("Text Button")
            }
        }
    }

    @Test
    fun button_Text_Dark() {
        captureArcaneScreenshot(composeTestRule, "Button_Text", isDark = true) {
            ArcaneButton(style = ArcaneButtonStyle.Text, onClick = {}) {
                Text("Text Button")
            }
        }
    }
}
```

**Step 3: Record golden images**

Run: `./gradlew :arcane-components-ui-screenshots:recordRoborazziDebug`
Expected: Creates golden PNGs in golden/android/

**Step 4: Verify screenshots**

Run: `./gradlew :arcane-components-ui-screenshots:verifyRoborazziDebug`
Expected: PASS (matches golden)

**Step 5: Commit**

```bash
git add arcane-components-ui-screenshots/
git commit -m "test(screenshots): add Button screenshot tests

Screenshot tests for Filled, Outlined, Text styles in light/dark themes.

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>"
```

---

## Task 7: Write TextField Screenshot Tests

**Files:**
- Create: `arcane-components-ui-screenshots/src/androidUnitTest/kotlin/io/github/devmugi/arcane/design/screenshots/controls/TextFieldScreenshotTest.kt`

**Step 1: Create screenshot test**

```kotlin
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

    @Test
    fun textField_Default_Light() {
        captureArcaneScreenshot(composeTestRule, "TextField_Default", isDark = false) {
            ArcaneTextField(
                value = "Sample text",
                onValueChange = {},
                label = "Label"
            )
        }
    }

    @Test
    fun textField_Default_Dark() {
        captureArcaneScreenshot(composeTestRule, "TextField_Default", isDark = true) {
            ArcaneTextField(
                value = "Sample text",
                onValueChange = {},
                label = "Label"
            )
        }
    }

    @Test
    fun textField_Placeholder_Light() {
        captureArcaneScreenshot(composeTestRule, "TextField_Placeholder", isDark = false) {
            ArcaneTextField(
                value = "",
                onValueChange = {},
                placeholder = "Enter text...",
                label = "Label"
            )
        }
    }

    @Test
    fun textField_Error_Light() {
        captureArcaneScreenshot(composeTestRule, "TextField_Error", isDark = false) {
            ArcaneTextField(
                value = "Invalid input",
                onValueChange = {},
                label = "Email",
                errorText = "Invalid email format"
            )
        }
    }

    @Test
    fun textField_Disabled_Light() {
        captureArcaneScreenshot(composeTestRule, "TextField_Disabled", isDark = false) {
            ArcaneTextField(
                value = "Disabled text",
                onValueChange = {},
                label = "Disabled",
                enabled = false
            )
        }
    }
}
```

**Step 2: Record golden images**

Run: `./gradlew :arcane-components-ui-screenshots:recordRoborazziDebug`

**Step 3: Commit**

```bash
git add arcane-components-ui-screenshots/
git commit -m "test(screenshots): add TextField screenshot tests

Screenshot tests for default, placeholder, error, disabled states.

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>"
```

---

## Task 8: Write Card and ListItem Screenshot Tests

**Files:**
- Create: `arcane-components-ui-screenshots/src/androidUnitTest/kotlin/io/github/devmugi/arcane/design/screenshots/display/DisplayScreenshotTest.kt`

**Step 1: Create screenshot test**

```kotlin
package io.github.devmugi.arcane.design.screenshots.display

import androidx.compose.material3.Text
import androidx.compose.ui.test.junit4.createComposeRule
import com.github.takahirom.roborazzi.RoborazziRule
import io.github.devmugi.arcane.design.components.display.ArcaneCard
import io.github.devmugi.arcane.design.components.display.ArcaneCardContent
import io.github.devmugi.arcane.design.components.display.ArcaneListItem
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
class DisplayScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val roborazziRule = RoborazziRule()

    @Test
    fun card_Default_Light() {
        captureArcaneScreenshot(composeTestRule, "Card_Default", isDark = false) {
            ArcaneCard {
                ArcaneCardContent(
                    title = "Card Title",
                    description = "This is a card description with some sample text."
                )
            }
        }
    }

    @Test
    fun card_Default_Dark() {
        captureArcaneScreenshot(composeTestRule, "Card_Default", isDark = true) {
            ArcaneCard {
                ArcaneCardContent(
                    title = "Card Title",
                    description = "This is a card description with some sample text."
                )
            }
        }
    }

    @Test
    fun listItem_Default_Light() {
        captureArcaneScreenshot(composeTestRule, "ListItem_Default", isDark = false) {
            ArcaneListItem(
                headlineText = "List Item Title",
                supportingText = "Supporting text goes here"
            )
        }
    }

    @Test
    fun listItem_Default_Dark() {
        captureArcaneScreenshot(composeTestRule, "ListItem_Default", isDark = true) {
            ArcaneListItem(
                headlineText = "List Item Title",
                supportingText = "Supporting text goes here"
            )
        }
    }
}
```

**Step 2: Record and commit**

```bash
./gradlew :arcane-components-ui-screenshots:recordRoborazziDebug
git add arcane-components-ui-screenshots/
git commit -m "test(screenshots): add Card and ListItem screenshot tests

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>"
```

---

## Task 9: Write Tabs Screenshot Tests

**Files:**
- Create: `arcane-components-ui-screenshots/src/androidUnitTest/kotlin/io/github/devmugi/arcane/design/screenshots/navigation/TabsScreenshotTest.kt`

**Step 1: Create screenshot test**

```kotlin
package io.github.devmugi.arcane.design.screenshots.navigation

import androidx.compose.ui.test.junit4.createComposeRule
import com.github.takahirom.roborazzi.RoborazziRule
import io.github.devmugi.arcane.design.components.navigation.ArcaneTab
import io.github.devmugi.arcane.design.components.navigation.ArcaneTabs
import io.github.devmugi.arcane.design.components.navigation.ArcaneTabStyle
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
class TabsScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val roborazziRule = RoborazziRule()

    private val tabs = listOf(
        ArcaneTab("Home"),
        ArcaneTab("Profile"),
        ArcaneTab("Settings")
    )

    @Test
    fun tabs_Filled_Light() {
        captureArcaneScreenshot(composeTestRule, "Tabs_Filled", isDark = false) {
            ArcaneTabs(
                tabs = tabs,
                selectedIndex = 0,
                onTabSelected = {},
                style = ArcaneTabStyle.Filled
            )
        }
    }

    @Test
    fun tabs_Filled_Dark() {
        captureArcaneScreenshot(composeTestRule, "Tabs_Filled", isDark = true) {
            ArcaneTabs(
                tabs = tabs,
                selectedIndex = 0,
                onTabSelected = {},
                style = ArcaneTabStyle.Filled
            )
        }
    }

    @Test
    fun tabs_Underline_Light() {
        captureArcaneScreenshot(composeTestRule, "Tabs_Underline", isDark = false) {
            ArcaneTabs(
                tabs = tabs,
                selectedIndex = 1,
                onTabSelected = {},
                style = ArcaneTabStyle.Underline
            )
        }
    }
}
```

**Step 2: Record and commit**

```bash
./gradlew :arcane-components-ui-screenshots:recordRoborazziDebug
git add arcane-components-ui-screenshots/
git commit -m "test(screenshots): add Tabs screenshot tests

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>"
```

---

## Task 10: Create arcane-chat-ui-screenshots Module

**Files:**
- Create: `arcane-chat-ui-screenshots/build.gradle.kts`
- Modify: `settings.gradle.kts`

**Step 1: Create module**

```bash
mkdir -p arcane-chat-ui-screenshots/src/androidUnitTest/kotlin/io/github/devmugi/arcane/chat/screenshots
mkdir -p arcane-chat-ui-screenshots/golden/android/light
mkdir -p arcane-chat-ui-screenshots/golden/android/dark
```

**Step 2: Create build.gradle.kts**

```kotlin
plugins {
    id("arcane.multiplatform.screenshots")
}

android {
    namespace = "io.github.devmugi.arcane.chat.screenshots"
}

kotlin {
    sourceSets {
        val androidUnitTest by getting {
            dependencies {
                implementation(project(":arcane-foundation"))
                implementation(project(":arcane-components"))
                implementation(project(":arcane-chat"))
            }
        }

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

**Step 3: Add to settings.gradle.kts**

```kotlin
include(":arcane-chat-ui-screenshots")
```

**Step 4: Commit**

```bash
git add arcane-chat-ui-screenshots/ settings.gradle.kts
git commit -m "build: create arcane-chat-ui-screenshots module

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>"
```

---

## Task 11: Write Chat Message Screenshot Tests

**Files:**
- Create: `arcane-chat-ui-screenshots/src/androidUnitTest/kotlin/io/github/devmugi/arcane/chat/screenshots/ChatMessageScreenshotTest.kt`

**Step 1: Copy ScreenshotTestUtils**

Copy the screenshot utilities from arcane-components-ui-screenshots to arcane-chat-ui-screenshots.

**Step 2: Create screenshot test**

```kotlin
package io.github.devmugi.arcane.chat.screenshots

import androidx.compose.ui.test.junit4.createComposeRule
import com.github.takahirom.roborazzi.RoborazziRule
import io.github.devmugi.arcane.chat.components.messages.ArcaneAssistantMessageBlock
import io.github.devmugi.arcane.chat.components.messages.ArcaneUserMessageBlock
import io.github.devmugi.arcane.chat.models.MessageBlock
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34])
class ChatMessageScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val roborazziRule = RoborazziRule()

    @Test
    fun userMessage_Light() {
        captureArcaneScreenshot(composeTestRule, "UserMessage", isDark = false) {
            ArcaneUserMessageBlock(
                blocks = listOf(MessageBlock.Text(id = "1", content = "Hello, how can I help you?"))
            )
        }
    }

    @Test
    fun userMessage_Dark() {
        captureArcaneScreenshot(composeTestRule, "UserMessage", isDark = true) {
            ArcaneUserMessageBlock(
                blocks = listOf(MessageBlock.Text(id = "1", content = "Hello, how can I help you?"))
            )
        }
    }

    @Test
    fun assistantMessage_Light() {
        captureArcaneScreenshot(composeTestRule, "AssistantMessage", isDark = false) {
            ArcaneAssistantMessageBlock(
                title = "Assistant",
                blocks = listOf(MessageBlock.Text(id = "1", content = "I can help you with that!"))
            )
        }
    }

    @Test
    fun assistantMessage_Dark() {
        captureArcaneScreenshot(composeTestRule, "AssistantMessage", isDark = true) {
            ArcaneAssistantMessageBlock(
                title = "Assistant",
                blocks = listOf(MessageBlock.Text(id = "1", content = "I can help you with that!"))
            )
        }
    }
}
```

**Step 3: Record and commit**

```bash
./gradlew :arcane-chat-ui-screenshots:recordRoborazziDebug
git add arcane-chat-ui-screenshots/
git commit -m "test(screenshots): add Chat message screenshot tests

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>"
```

---

## Task 12: Run All Screenshot Tests and Verify

**Step 1: Verify all screenshot tests pass**

```bash
./gradlew :arcane-components-ui-screenshots:verifyRoborazziDebug :arcane-chat-ui-screenshots:verifyRoborazziDebug
```

**Step 2: Count tests**

```bash
grep -r "@Test" arcane-*-ui-screenshots/src/ | wc -l
```

**Step 3: List golden images**

```bash
find arcane-*-ui-screenshots/golden -name "*.png" | wc -l
```

---

## Task 13: Final Phase 3 Summary Commit

**Step 1: Update PRD**

Mark Phase 3 complete in `docs/plans/2026-01-31-comprehensive-testing-design.md`.

**Step 2: Commit**

```bash
git add docs/plans/
git commit -m "docs: mark Phase 3 complete in testing PRD

Phase 3 deliverables:
- Roborazzi dependencies and plugin added
- Git LFS configured for golden images
- Convention plugin arcane.multiplatform.screenshots created
- arcane-components-ui-screenshots module with screenshot tests
- arcane-chat-ui-screenshots module with screenshot tests
- Golden images captured for light/dark themes

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>"
```

---

## Phase 3 Complete Checklist

- [ ] Roborazzi dependencies in libs.versions.toml
- [ ] Git LFS configured for golden images
- [ ] Convention plugin arcane.multiplatform.screenshots
- [ ] arcane-components-ui-screenshots module
- [ ] arcane-chat-ui-screenshots module
- [ ] Screenshot test utilities
- [ ] Button screenshot tests
- [ ] TextField screenshot tests
- [ ] Card/ListItem screenshot tests
- [ ] Tabs screenshot tests
- [ ] Chat message screenshot tests
- [ ] All screenshots recorded
- [ ] Verification tests pass
- [ ] PRD updated

---

*Next: Phase 4 - Multiplatform Screenshots (CMP Screenshot Testing)*
