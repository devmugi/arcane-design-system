# Arcane Design System - Comprehensive Testing PRD

**Date:** 2026-01-31
**Branch:** testing
**Status:** Draft

---

## 1. Overview

### 1.1 Objective

Establish a complete testing infrastructure for the Arcane Design System, covering all components with unit tests, UI integration tests, and screenshot tests for visual regression prevention.

### 1.2 Success Criteria

- 80%+ code coverage on component logic
- 100% screenshot coverage (all components × core states × light/dark themes)
- All tests run on every PR (blocking)
- Zero visual regressions reach production

### 1.3 Current State

| Metric | Current |
|--------|---------|
| Test Coverage | ~3% |
| Test Files | 1 |
| Test Cases | 10 |
| Modules with Tests | 1/4 |
| Screenshot Tests | 0 |
| CI Integration | None |

---

## 2. Module Architecture

### 2.1 Final Structure

```
ArcaneDesignSystem/
│
├── arcane-foundation/                    # Design tokens, theme (existing)
├── arcane-foundation-tests/              # Unit tests (colors, spacing, tokens)
│
├── arcane-components/                    # UI components (existing)
├── arcane-components-tests/              # Unit tests (logic)
├── arcane-components-ui-tests/           # Compose UI tests
├── arcane-components-ui-screenshots/     # Visual regression
├── arcane-components-catalog-app/        # Demo app (rename existing)
│
├── arcane-chat/                          # Chat components (existing)
├── arcane-chat-tests/                    # Unit tests
├── arcane-chat-ui-tests/                 # Compose UI tests
├── arcane-chat-ui-screenshots/           # Visual regression
│
└── build-logic/                          # Convention plugins (existing)
```

### 2.2 Module Count

| Type | Existing | New | Total |
|------|----------|-----|-------|
| Production | 3 | 0 | 3 |
| Test | 0 | 9 | 9 |
| App | 1 | 0 | 1 |
| **Total** | 4 | 9 | 13 |

### 2.3 Dependencies

```
arcane-foundation
       ↑
arcane-components ←── arcane-chat
       ↑                    ↑
   [test modules depend on their production counterparts]
```

All test modules depend on production modules but not on each other.

---

## 3. Testing Libraries

### 3.1 libs.versions.toml Additions

```toml
[versions]
kotest = "5.9.0"
turbine = "1.2.0"
roborazzi = "1.8.0"

[libraries]
# Unit testing
kotest-assertions-core = { module = "io.kotest:kotest-assertions-core", version.ref = "kotest" }
kotlinx-coroutines-test = { module = "org.jetbrains.kotlinx:kotlinx-coroutines-test", version.ref = "kotlinx-coroutines" }
turbine = { module = "app.cash.turbine:turbine", version.ref = "turbine" }

# Compose UI testing
compose-ui-test = { module = "org.jetbrains.compose.ui:ui-test", version.ref = "compose-multiplatform" }
compose-ui-test-junit4 = { module = "org.jetbrains.compose.ui:ui-test-junit4", version.ref = "compose-multiplatform" }

# Screenshot testing
roborazzi = { module = "io.github.takahirom.roborazzi:roborazzi", version.ref = "roborazzi" }
roborazzi-compose = { module = "io.github.takahirom.roborazzi:roborazzi-compose", version.ref = "roborazzi" }
roborazzi-junit-rule = { module = "io.github.takahirom.roborazzi:roborazzi-junit-rule", version.ref = "roborazzi" }

[plugins]
roborazzi = { id = "io.github.takahirom.roborazzi", version.ref = "roborazzi" }
```

### 3.2 Library Purposes

| Library | Purpose |
|---------|---------|
| kotlin-test | Base test framework |
| kotest-assertions-core | Fluent assertions |
| turbine | Flow testing |
| kotlinx-coroutines-test | Coroutine testing |
| compose-ui-test | Compose interaction testing |
| roborazzi | Android/JVM screenshot testing |
| CMP Screenshot Testing | iOS/Desktop screenshot testing |

---

## 4. Screenshot Testing Strategy

### 4.1 Libraries by Platform

| Platform | Library | Source Set |
|----------|---------|------------|
| Android | Roborazzi | `androidUnitTest/` |
| JVM/Desktop | Roborazzi | `jvmTest/` |
| iOS | CMP Screenshot Testing | `iosTest/` |
| Desktop Native | CMP Screenshot Testing | `desktopTest/` |
| WASM | CMP Screenshot Testing | `wasmJsTest/` (optional) |

### 4.2 Screenshot Matrix

| Dimension | Values | Count |
|-----------|--------|-------|
| Components | 30 | 30 |
| Core states | default, disabled, error, loading | ~4 avg |
| Themes | light, dark | 2 |
| **Total per platform** | | ~240 |
| **Total all platforms** | | ~400+ |

### 4.3 Golden Image Storage

- **Storage:** Git LFS
- **Location:** `<module>/golden/`
- **Structure:**
  ```
  golden/
  ├── android/
  │   ├── light/
  │   │   ├── Button_Primary_Default.png
  │   │   └── ...
  │   └── dark/
  ├── ios/
  │   ├── light/
  │   └── dark/
  └── desktop/
      ├── light/
      └── dark/
  ```

### 4.4 Git LFS Configuration

`.gitattributes`:
```
**/golden/**/*.png filter=lfs diff=lfs merge=lfs -text
```

---

## 5. Phased Implementation

### Phase 1: Foundation & Unit Tests

**Goal:** Establish test infrastructure and unit test coverage

**Tasks:**
1. Add test libraries to `libs.versions.toml`
2. Create convention plugin `arcane.multiplatform.tests`
3. Create `arcane-foundation-tests` module
4. Create `arcane-components-tests` module
5. Create `arcane-chat-tests` module
6. Write unit tests for all component logic

**Key test targets:**
- Pagination calculation logic
- Stepper validation rules
- Toast auto-dismiss timing
- Badge count formatting
- Table sorting/filtering logic

**Deliverables:**
- 3 new test modules
- ~50-80 unit tests
- CI runs unit tests on every PR

**Exit criteria:**
- All test modules compile on all platforms
- `./gradlew allTests` passes
- Unit tests block PR merge

---

### Phase 2: UI Integration Tests

**Goal:** Cover component interactions and accessibility

**Tasks:**
1. Add Compose UI Test dependencies
2. Create `arcane-components-ui-tests` module
3. Create `arcane-chat-ui-tests` module
4. Create `TestArcaneTheme` utility wrapper
5. Create test helpers (`onArcaneButton()`, etc.)
6. Write UI tests for all 30 components

**Test categories per component:**
| Category | Example |
|----------|---------|
| Rendering | Button displays text, icon positions correctly |
| Interactions | Click triggers callback, disabled blocks clicks |
| State changes | TextField shows error state, Switch toggles |
| Accessibility | Content descriptions, touch targets ≥48dp |

**Sample test:**
```kotlin
@Test
fun `ArcaneButton calls onClick when tapped`() {
    var clicked = false
    composeTestRule.setContent {
        ArcaneTheme {
            ArcaneButton("Submit", onClick = { clicked = true })
        }
    }

    composeTestRule.onNodeWithText("Submit").performClick()
    assertTrue(clicked)
}
```

**Deliverables:**
- 2 new test modules
- ~100-150 UI tests
- CI runs UI tests on every PR

**Exit criteria:**
- All components have interaction tests
- Accessibility assertions pass
- UI tests block PR merge

---

### Phase 3: Screenshot Tests (Roborazzi)

**Goal:** Visual regression prevention for Android/JVM

**Tasks:**
1. Add Roborazzi plugin and dependencies
2. Configure Git LFS for golden images
3. Create `arcane-components-ui-screenshots` module
4. Create `arcane-chat-ui-screenshots` module
5. Create screenshot utilities (`captureArcaneComponent()`)
6. Capture screenshots for all 30 components × core states × light/dark

**Directory structure:**
```
arcane-components-ui-screenshots/
├── src/
│   ├── androidUnitTest/kotlin/.../
│   │   ├── controls/ButtonScreenshotTest.kt
│   │   ├── navigation/TabsScreenshotTest.kt
│   │   └── ...
│   └── jvmTest/kotlin/.../
│       └── (same structure for Desktop)
└── golden/
    ├── android/
    │   ├── light/
    │   └── dark/
    └── jvm/
        ├── light/
        └── dark/
```

**Sample screenshot test:**
```kotlin
@Test
fun buttonPrimaryDefault() {
    composeTestRule.setContent {
        ArcaneTheme(darkTheme = false) {
            ArcaneButton("Submit", style = Primary, onClick = {})
        }
    }
    composeTestRule.onRoot().captureRoboImage("golden/android/light/Button_Primary_Default.png")
}
```

**Deliverables:**
- 2 new screenshot modules
- ~240 golden images (Android + JVM)
- PR diff reports for visual changes

**Exit criteria:**
- All 30 components have screenshot coverage
- Light + dark themes captured
- Screenshot mismatches block PR merge

---

### Phase 4: Multiplatform Screenshots (CMP)

**Goal:** Extend visual regression to iOS and Desktop

**Tasks:**
1. Add CMP Screenshot Testing dependencies
2. Extend `arcane-components-ui-screenshots` with iOS/Desktop source sets
3. Extend `arcane-chat-ui-screenshots` with iOS/Desktop source sets
4. Configure platform-specific golden directories
5. Capture screenshots for iOS and Desktop

**Platform source sets:**
```
arcane-components-ui-screenshots/src/
├── androidUnitTest/   # Roborazzi (existing from Phase 3)
├── jvmTest/           # Roborazzi (existing from Phase 3)
├── iosTest/           # CMP Screenshot Testing (new)
├── desktopTest/       # CMP Screenshot Testing (new)
└── commonTest/        # Shared utilities
```

**Cross-platform considerations:**
- Font rendering differs per platform (expected)
- Each platform maintains own golden set
- Compare within platform, not across
- CI runs platform-specific jobs in parallel

**Deliverables:**
- Extended screenshot modules with iOS/Desktop
- ~160 additional golden images
- Platform-specific CI jobs

**Exit criteria:**
- iOS and Desktop screenshots captured
- All platforms block on mismatches

---

### Phase 5: CI Integration

**Goal:** Full test pipeline with PR blocking

**Tasks:**
1. Configure Git LFS checkout in CI
2. Create unit test job
3. Create UI test job
4. Create screenshot jobs (per platform, parallel)
5. Configure PR comment bot for visual diffs
6. Enable merge blocking on failures

**GitHub Actions workflow:**

```yaml
name: Tests

on: [pull_request]

jobs:
  unit-tests:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: 'temurin'
          java-version: '21'
      - run: ./gradlew :arcane-foundation-tests:allTests
      - run: ./gradlew :arcane-components-tests:allTests
      - run: ./gradlew :arcane-chat-tests:allTests

  ui-tests:
    needs: unit-tests
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: 'temurin'
          java-version: '21'
      - run: ./gradlew :arcane-components-ui-tests:jvmTest
      - run: ./gradlew :arcane-chat-ui-tests:jvmTest

  screenshots-android:
    needs: unit-tests
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
        with:
          lfs: true
      - uses: actions/setup-java@v4
        with:
          distribution: 'temurin'
          java-version: '21'
      - run: ./gradlew :arcane-components-ui-screenshots:verifyRoborazziAndroid
      - run: ./gradlew :arcane-chat-ui-screenshots:verifyRoborazziAndroid
      - uses: actions/upload-artifact@v4
        if: failure()
        with:
          name: screenshot-diffs-android
          path: '**/build/outputs/roborazzi/'

  screenshots-ios:
    needs: unit-tests
    runs-on: macos-latest
    steps:
      - uses: actions/checkout@v4
        with:
          lfs: true
      - uses: actions/setup-java@v4
        with:
          distribution: 'temurin'
          java-version: '21'
      - run: ./gradlew :arcane-components-ui-screenshots:iosTest
      - run: ./gradlew :arcane-chat-ui-screenshots:iosTest

  screenshots-desktop:
    needs: unit-tests
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
        with:
          lfs: true
      - uses: actions/setup-java@v4
        with:
          distribution: 'temurin'
          java-version: '21'
      - run: ./gradlew :arcane-components-ui-screenshots:desktopTest
      - run: ./gradlew :arcane-chat-ui-screenshots:desktopTest
```

**PR blocking behavior:**
- Any test failure → PR cannot merge
- Screenshot diff → uploads comparison artifacts
- Reviewer approves visual changes by updating golden images

**Deliverables:**
- Complete GitHub Actions workflow
- Parallel platform jobs
- Screenshot diff artifacts
- Merge blocking enabled

**Exit criteria:**
- All jobs run on every PR
- Screenshot diffs visible in PR artifacts
- Merge blocked until all jobs green

---

## 6. Success Metrics

| Metric | Current | Phase 1 | Phase 2 | Phase 3 | Phase 4 | Phase 5 |
|--------|---------|---------|---------|---------|---------|---------|
| Unit test coverage | 3% | 60% | 60% | 60% | 60% | 80%+ |
| UI test count | 0 | 0 | 100+ | 100+ | 100+ | 150+ |
| Screenshot count | 0 | 0 | 0 | 240+ | 400+ | 400+ |
| Platforms tested | 0 | 3 | 3 | 2 | 4 | 4 |
| CI integration | No | Yes | Yes | Yes | Yes | Yes |
| PR blocking | No | Unit | Unit+UI | +Android | +iOS/Desktop | Full |

---

## 7. Test Counts Summary

| Module | Unit Tests | UI Tests | Screenshots |
|--------|------------|----------|-------------|
| arcane-foundation-tests | ~15 | - | - |
| arcane-components-tests | ~50 | - | - |
| arcane-components-ui-tests | - | ~120 | - |
| arcane-components-ui-screenshots | - | - | ~320 |
| arcane-chat-tests | ~15 | - | - |
| arcane-chat-ui-tests | - | ~30 | - |
| arcane-chat-ui-screenshots | - | - | ~80 |
| **Total** | **~80** | **~150** | **~400** |

---

## 8. Risks & Mitigations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Roborazzi version incompatibility | Screenshot tests fail | Pin version, test before upgrading |
| Git LFS quota limits | Cannot store golden images | Monitor usage, purge old images |
| Flaky screenshot tests | False failures | Use tolerance thresholds, retry logic |
| CI time increases | Slow feedback | Parallel jobs, caching, conditional runs |
| Font rendering differences | Cross-platform comparison fails | Separate golden sets per platform |
| CMP Screenshot Testing immaturity | Missing features | Fallback to Roborazzi for critical paths |

---

## 9. Open Questions

1. Should WASM screenshots be included in Phase 4 or deferred?
2. What screenshot tolerance threshold for minor rendering differences?
3. Should we auto-update golden images on approved PRs?
4. How often to prune old/unused golden images?

---

## 10. Appendix: Component Coverage Matrix

### Controls (6)
| Component | Unit | UI | Screenshot |
|-----------|------|-----|------------|
| Button | Phase 1 | Phase 2 | Phase 3 |
| TextField | Phase 1 | Phase 2 | Phase 3 |
| Checkbox | Phase 1 | Phase 2 | Phase 3 |
| RadioButton | Phase 1 | Phase 2 | Phase 3 |
| Switch | Phase 1 | Phase 2 | Phase 3 |
| Slider | Phase 1 | Phase 2 | Phase 3 |

### Navigation (4)
| Component | Unit | UI | Screenshot |
|-----------|------|-----|------------|
| Tabs | Phase 1 | Phase 2 | Phase 3 |
| Stepper | Phase 1 | Phase 2 | Phase 3 |
| Breadcrumbs | Phase 1 | Phase 2 | Phase 3 |
| Pagination | Phase 1 | Phase 2 | Phase 3 |

### Display (7)
| Component | Unit | UI | Screenshot |
|-----------|------|-----|------------|
| Card | Phase 1 | Phase 2 | Phase 3 |
| ListItem | Phase 1 | Phase 2 | Phase 3 |
| Badge | Phase 1 | Phase 2 | Phase 3 |
| Avatar | Phase 1 | Phase 2 | Phase 3 |
| Table | Phase 1 | Phase 2 | Phase 3 |
| Text | Phase 1 | Phase 2 | Phase 3 |
| Tooltip | Phase 1 | Phase 2 | Phase 3 |

### Feedback (13)
| Component | Unit | UI | Screenshot |
|-----------|------|-----|------------|
| Toast | Phase 1 | Phase 2 | Phase 3 |
| Modal | Phase 1 | Phase 2 | Phase 3 |
| DropdownMenu | Phase 1 | Phase 2 | Phase 3 |
| AlertBanner | Phase 1 | Phase 2 | Phase 3 |
| ConfirmationDialog | Phase 1 | Phase 2 | Phase 3 |
| Spinner | Phase 1 | Phase 2 | Phase 3 |
| CircularProgress | Phase 1 | Phase 2 | Phase 3 |
| LinearProgress | Phase 1 | Phase 2 | Phase 3 |
| EmptyState | Phase 1 | Phase 2 | Phase 3 |
| Skeleton | Phase 1 | Phase 2 | Phase 3 |
| SkeletonCard | Phase 1 | Phase 2 | Phase 3 |
| SkeletonAvatar | Phase 1 | Phase 2 | Phase 3 |
| SkeletonListItem | Phase 1 | Phase 2 | Phase 3 |

---

*Generated with Claude Code*
