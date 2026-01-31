# Phase 4: Multiplatform Screenshots Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Extend visual regression testing to Desktop (JVM) platform

**Architecture:** Desktop tests use Compose Multiplatform's Skiko-based image capture for screenshot testing. iOS screenshot testing deferred to Phase 4b.

**Tech Stack:** Compose Multiplatform 1.10, Skiko, JUnit4

## Platform Limitations (Discovered)

**Roborazzi:** Android-only library (AAR). Cannot be used for Desktop/JVM tests.

**Desktop Options:**
1. **Skiko ImageIO approach** - Capture ComposeWindow to BufferedImage (recommended)
2. **kotlin-snapshot-testing (Snappy)** - Multiplatform snapshot library
3. **ComposablePreviewScanner** - Preview-based testing (experimental for desktop)

**Chosen approach:** Use Skiko's built-in rendering to capture screenshots on Desktop.

---

## Background

Phase 3 established screenshot testing infrastructure with:
- Roborazzi for Android (79 golden images)
- Convention plugin `arcane.multiplatform.screenshots`
- 2 screenshot modules (components + chat)

Phase 4 extends this to Desktop/JVM platform.

## Scope

**In scope:**
- Desktop (JVM) screenshot tests using Roborazzi
- Shared test utilities between Android and Desktop
- Desktop-specific golden image directories

**Out of scope (Phase 4b):**
- iOS screenshot testing (requires native tooling)
- WASM screenshot testing (experimental)

---

## Task 1: Update Convention Plugin for Desktop Tests

**Files:**
- Modify: `build-logic/src/main/kotlin/arcane.multiplatform.screenshots.gradle.kts`

**Step 1: Add desktop Roborazzi dependencies**

The convention plugin already has `desktopTest` source set but needs Roborazzi dependencies.

Current desktopTest dependencies:
```kotlin
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
```

Verify Roborazzi works on Desktop JVM target.

**Step 2: Verify build**

Run: `./gradlew :arcane-components-ui-screenshots:desktopTest --dry-run`
Expected: Task configuration succeeds

---

## Task 2: Create Desktop Screenshot Utility

**Files:**
- Create: `arcane-components-ui-screenshots/src/desktopTest/kotlin/io/github/devmugi/arcane/design/screenshots/DesktopScreenshotTestUtils.kt`

**Step 1: Write desktop capture utility**

```kotlin
package io.github.devmugi.arcane.design.screenshots

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.captureRoboImage
import io.github.devmugi.arcane.design.foundation.theme.ArcaneColors
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme

fun captureDesktopScreenshot(
    composeTestRule: ComposeContentTestRule,
    componentName: String,
    isDark: Boolean = false,
    content: @Composable () -> Unit
) {
    val theme = if (isDark) "dark" else "light"
    val filePath = "src/desktopTest/resources/golden/desktop/$theme/${componentName}.png"

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
```

---

## Task 3: Create Desktop Golden Directories

**Files:**
- Create: `arcane-components-ui-screenshots/src/desktopTest/resources/golden/desktop/light/.gitkeep`
- Create: `arcane-components-ui-screenshots/src/desktopTest/resources/golden/desktop/dark/.gitkeep`
- Create: `arcane-chat-ui-screenshots/src/desktopTest/resources/golden/desktop/light/.gitkeep`
- Create: `arcane-chat-ui-screenshots/src/desktopTest/resources/golden/desktop/dark/.gitkeep`

---

## Task 4: Write Desktop Button Screenshot Tests

**Files:**
- Create: `arcane-components-ui-screenshots/src/desktopTest/kotlin/io/github/devmugi/arcane/design/screenshots/controls/ButtonDesktopScreenshotTest.kt`

**Step 1: Write desktop button tests**

Mirror the Android tests but use desktop utility:
- Filled style (light/dark)
- Tonal style (light/dark)
- Outlined style (light/dark)
- Disabled state (light/dark)

**Step 2: Run tests to generate golden images**

Run: `./gradlew :arcane-components-ui-screenshots:desktopTest --tests "*ButtonDesktopScreenshotTest"`
Expected: Tests pass, golden images created

---

## Task 5: Write Desktop TextField Screenshot Tests

**Files:**
- Create: `arcane-components-ui-screenshots/src/desktopTest/kotlin/io/github/devmugi/arcane/design/screenshots/controls/TextFieldDesktopScreenshotTest.kt`

Tests:
- Default state (light/dark)
- Placeholder (light/dark)
- Error state (light/dark)
- Disabled state (light/dark)

---

## Task 6: Write Desktop Display Screenshot Tests

**Files:**
- Create: `arcane-components-ui-screenshots/src/desktopTest/kotlin/io/github/devmugi/arcane/design/screenshots/display/DisplayDesktopScreenshotTest.kt`

Tests:
- Card with title (light/dark)
- Card with description (light/dark)
- ListItem simple (light/dark)
- ListItem with supporting text (light/dark)

---

## Task 7: Write Desktop Tabs Screenshot Tests

**Files:**
- Create: `arcane-components-ui-screenshots/src/desktopTest/kotlin/io/github/devmugi/arcane/design/screenshots/navigation/TabsDesktopScreenshotTest.kt`

Tests:
- Filled style (light/dark)
- Underline style (light/dark)
- With disabled tab (light/dark)

---

## Task 8: Create Chat Desktop Screenshot Module Structure

**Files:**
- Create: `arcane-chat-ui-screenshots/src/desktopTest/kotlin/io/github/devmugi/arcane/chat/screenshots/ChatDesktopScreenshotTestUtils.kt`
- Create: `arcane-chat-ui-screenshots/src/desktopTest/resources/golden/desktop/light/.gitkeep`
- Create: `arcane-chat-ui-screenshots/src/desktopTest/resources/golden/desktop/dark/.gitkeep`

---

## Task 9: Write Desktop Chat Message Screenshot Tests

**Files:**
- Create: `arcane-chat-ui-screenshots/src/desktopTest/kotlin/io/github/devmugi/arcane/chat/screenshots/messages/ChatMessageDesktopScreenshotTest.kt`

Tests:
- User message (light/dark)
- Assistant message (light/dark)
- Assistant with title (light/dark)

---

## Task 10: Run All Desktop Tests and Verify

**Step 1: Run all desktop screenshot tests**

Run: `./gradlew :arcane-components-ui-screenshots:desktopTest :arcane-chat-ui-screenshots:desktopTest`
Expected: All tests pass

**Step 2: Verify golden images created**

Check directories for .png files in desktop/light and desktop/dark folders.

---

## Task 11: Update .gitattributes for Desktop Golden Images

**Files:**
- Verify: `.gitattributes` already covers `**/golden/**/*.png`

---

## Task 12: Final Phase 4 Summary Commit

Commit all desktop screenshot tests and golden images.

---

## Expected Deliverables

| Item | Count |
|------|-------|
| Desktop screenshot tests | ~40 |
| Desktop golden images | ~40 |
| New test utility files | 2 |

## Commands Reference

```bash
# Record desktop golden images
./gradlew :arcane-components-ui-screenshots:recordRoborazziDesktop

# Verify desktop screenshots
./gradlew :arcane-components-ui-screenshots:verifyRoborazziDesktop

# Run all desktop tests
./gradlew desktopTest
```

---

*Generated with Claude Code*
