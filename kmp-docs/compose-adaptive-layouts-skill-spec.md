# Compose Adaptive Layouts Skill Specification

Specification for a Claude Code skill that helps analyze and implement Compose Adaptive Layouts in Kotlin Multiplatform modules.

## Overview

This skill provides two commands:
1. **Analyze** - Assess module readiness for adaptive layout integration
2. **Implement** - Guide step-by-step adaptive layout implementation

## Documentation Reference

This skill depends on: `kmp-docs/compose-adaptive-layouts.md`

The documentation contains:
- Window size class breakpoints and APIs
- Canonical layout patterns (list-detail, feed, supporting pane)
- Adaptive navigation patterns
- Code templates and best practices
- Verification checklist

---

## Skill Metadata

```yaml
name: compose-adaptive-layouts
description: |
  Use when adding adaptive/responsive layouts to Compose Multiplatform modules.
  Triggers on: "add adaptive layout", "make responsive", "support different screen sizes",
  "add window size classes", "adaptive navigation"
commands:
  - compose-adaptive-layouts-analyze
  - compose-adaptive-layouts-implement
```

---

## Feature: Analyze

### Command

`/compose-adaptive-layouts-analyze <module-name>`

### Trigger Phrases

- "analyze adaptive layout readiness for catalog"
- "check if module supports adaptive layouts"
- "assess responsive layout status"

### Input Parameters

| Parameter | Required | Description |
|-----------|----------|-------------|
| `module-name` | Yes | Module path (e.g., `catalog`, `catalog/composeApp`, `:arcane-components`) |

### Analysis Steps

#### Step 1: Locate Module

```
1. Find module by name in project structure
2. Locate build.gradle.kts
3. Identify source directories (commonMain, androidMain, desktopMain, wasmJsMain)
4. If module not found, report error and list available modules
```

#### Step 2: Check Dependencies

Scan `build.gradle.kts` and `libs.versions.toml` for:

| Dependency | Status |
|------------|--------|
| `compose-material3-adaptive` | Required |
| `compose-material3-adaptive-layout` | Optional (for scaffolds) |
| `compose-material3-adaptive-navigation` | Optional (for nav integration) |

#### Step 3: Scan UI Elements

Search source files for:

| Pattern | Significance |
|---------|--------------|
| `@Composable fun.*Screen` | Screen composables to adapt |
| `NavigationBar`, `NavigationRail` | Existing navigation components |
| `ArcaneTabs`, `Tabs` | Tab-based navigation |
| `Column(.*fillMaxSize` | Vertical-only layouts |
| `Row(.*fillMaxWidth` | Horizontal layouts |
| `LazyColumn`, `LazyVerticalGrid` | List/grid content |
| `currentWindowAdaptiveInfo` | Already using window size |
| `WindowSizeClass` | Already adaptive |

#### Step 4: Identify Layout Patterns

Categorize current patterns:

| Pattern | Description | Adaptive Action |
|---------|-------------|-----------------|
| **Single Column** | `Column` with vertical scroll | Add breakpoint-based multi-column |
| **Top Tabs** | `TabRow` at top of screen | Convert to `NavigationSuiteScaffold` |
| **Bottom Nav** | `NavigationBar` at bottom | Keep for compact, add rail for expanded |
| **Hardcoded Sizes** | `Modifier.width(300.dp)` | Replace with responsive sizing |
| **No Window Size** | Missing `currentWindowAdaptiveInfo` | Add window size class handling |

#### Step 5: Assess Complexity

| Complexity | Criteria |
|------------|----------|
| **Simple** | < 3 screens, single navigation pattern, no nested layouts |
| **Moderate** | 3-6 screens, mixed navigation, some nested layouts |
| **Complex** | > 6 screens, multiple navigation patterns, deep nesting |

### Output Format: Readiness Report

```markdown
# Adaptive Layout Readiness Report

## Module: <module-name>
**Complexity:** Simple | Moderate | Complex

## Dependencies Status

| Dependency | Status | Action |
|------------|--------|--------|
| compose-material3-adaptive | ❌ Missing | Add to libs.versions.toml |
| compose-material3-adaptive-layout | ❌ Missing | Add for scaffold support |

## UI Elements Found

### Screens (X found)
- `ControlsScreen.kt` - Column layout, vertical scroll
- `NavigationScreen.kt` - Column layout, vertical scroll
- ...

### Navigation
- **Current:** Top tabs (`ArcaneTabs`)
- **Pattern:** Single-level navigation with 5 destinations

### Layout Issues

| File | Issue | Recommendation |
|------|-------|----------------|
| App.kt | No window size class | Add `currentWindowAdaptiveInfo()` |
| App.kt | Top tabs only | Convert to `NavigationSuiteScaffold` |
| ControlsScreen.kt | Column-only layout | Consider grid for expanded |

## Recommendations

1. Add adaptive dependencies to `libs.versions.toml`
2. Wrap `App()` with window size class provider
3. Replace `ArcaneTabs` with `NavigationSuiteScaffold`
4. Update screens to use responsive layouts

## Next Step

Run `/compose-adaptive-layouts-implement <module-name>` to add adaptive layout support.
```

---

## Feature: Implement

### Command

`/compose-adaptive-layouts-implement <module-name>`

### Trigger Phrases

- "add adaptive layouts to catalog"
- "implement responsive navigation"
- "make catalog support different screen sizes"

### Interactive Questions

#### Question 1: Confirm Module

```
Confirm target module: <module-name>

This will modify:
- gradle/libs.versions.toml
- <module>/build.gradle.kts
- <module>/src/commonMain/kotlin/.../App.kt
- Screen files as needed

Proceed? [Yes/No]
```

#### Question 2: Canonical Layout

```
Select primary layout pattern:

1. **Auto-detect** (Recommended)
   - Analyze existing structure and suggest best fit

2. **Feed Layout**
   - Grid-based content that adapts columns
   - Best for: catalogs, galleries, dashboards

3. **List-Detail Layout**
   - Master list with detail pane
   - Best for: email, settings, file browsers

4. **Supporting Pane Layout**
   - Primary content with secondary panel
   - Best for: editors, media players
```

#### Question 3: Navigation Style

```
Select navigation adaptation:

1. **Auto** (Recommended)
   - NavigationBar (compact) → NavigationRail (medium+) → Drawer (large+)

2. **Bar + Rail only**
   - No drawer, just bar and rail

3. **Persistent Rail**
   - Always show rail, never bar

4. **Keep Current**
   - Don't change navigation, only add window size awareness
```

#### Question 4: Target Platforms

```
Select target platforms (multi-select):

[x] Android - phones, tablets, foldables
[x] Desktop - resizable windows
[x] Web (Wasm) - browser viewport
[ ] iOS - (out of scope)
```

### Implementation Workflow

#### Phase 1: Dependencies

**Action:** Update `gradle/libs.versions.toml`

```toml
[versions]
compose-material3-adaptive = "1.2.0"

[libraries]
compose-material3-adaptive = { module = "org.jetbrains.compose.material3.adaptive:adaptive", version.ref = "compose-material3-adaptive" }
compose-material3-adaptive-layout = { module = "org.jetbrains.compose.material3.adaptive:adaptive-layout", version.ref = "compose-material3-adaptive" }
compose-material3-adaptive-navigation = { module = "org.jetbrains.compose.material3.adaptive:adaptive-navigation", version.ref = "compose-material3-adaptive" }
```

**Action:** Update `<module>/build.gradle.kts`

```kotlin
commonMain.dependencies {
    implementation(libs.compose.material3.adaptive)
    implementation(libs.compose.material3.adaptive.layout)
    implementation(libs.compose.material3.adaptive.navigation)
}
```

#### Phase 2: App Entry Point

**Action:** Wrap main composable with window size class

```kotlin
@Composable
fun App(
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
) {
    val isExpanded = windowSizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
    )
    val isMedium = windowSizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND
    )

    ArcaneTheme {
        AdaptiveScaffold(
            isExpanded = isExpanded,
            isMedium = isMedium,
            // ... navigation and content
        )
    }
}
```

#### Phase 3: Adaptive Navigation

**Action:** Convert navigation to `NavigationSuiteScaffold` pattern

**Before (catalog example):**
```kotlin
Column {
    CatalogTopBar()  // Contains ArcaneTabs
    Box { screenContent() }
}
```

**After:**
```kotlin
val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

// Determine navigation type
val useNavigationRail = windowSizeClass.isWidthAtLeastBreakpoint(
    WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND
)

if (useNavigationRail) {
    Row {
        NavigationRail {
            destinations.forEach { /* rail items */ }
        }
        Column(Modifier.weight(1f)) {
            TopBar()  // Simplified, no tabs
            screenContent()
        }
    }
} else {
    Scaffold(
        bottomBar = {
            NavigationBar {
                destinations.forEach { /* bar items */ }
            }
        }
    ) { padding ->
        Column(Modifier.padding(padding)) {
            TopBar()
            screenContent()
        }
    }
}
```

#### Phase 4: Screen Adaptations

**For Feed-style screens (e.g., ControlsScreen):**

```kotlin
@Composable
fun ControlsScreen(
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
) {
    val columns = when {
        windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_EXPANDED_LOWER_BOUND) -> 3
        windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND) -> 2
        else -> 1
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        // ... content
    )
}
```

**For List-Detail screens:**

```kotlin
@Composable
fun SettingsScreen(
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
) {
    val isExpanded = windowSizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
    )

    if (isExpanded) {
        Row {
            SettingsList(Modifier.width(360.dp))
            SettingsDetail(Modifier.weight(1f))
        }
    } else {
        // Single pane with back navigation
    }
}
```

### Generated Artifacts

| File | Change Type | Description |
|------|-------------|-------------|
| `gradle/libs.versions.toml` | Edit | Add adaptive dependencies |
| `<module>/build.gradle.kts` | Edit | Add implementation dependencies |
| `App.kt` | Edit | Add window size class, adaptive navigation |
| `*Screen.kt` | Edit | Add responsive layout logic |

---

## Integration with ArcaneDesignSystem

### Theme Preservation

All adaptive changes must:
- Keep `ArcaneTheme` as outer wrapper
- Use `ArcaneTheme.colors` and `ArcaneTheme.typography`
- Preserve theme switching functionality

### Component Usage

| Instead of | Use |
|------------|-----|
| `NavigationBar` | Arcane-styled wrapper or M3 with Arcane colors |
| `NavigationRail` | Arcane-styled wrapper or M3 with Arcane colors |
| `Surface` | `ArcaneSurface` with appropriate variant |

### Surface Variants by Context

| Context | Variant |
|---------|---------|
| Navigation rail background | `SurfaceVariant.ContainerLow` |
| Content area | `SurfaceVariant.Container` |
| Elevated panels | `SurfaceVariant.ContainerHigh` |

---

## Verification Checklist

After implementation, verify:

```markdown
## Window Size Class Integration
- [ ] Dependencies added to libs.versions.toml
- [ ] Dependencies added to module build.gradle.kts
- [ ] `currentWindowAdaptiveInfo()` used in App.kt
- [ ] Window size class passed to screens that need it

## Navigation Adaptation
- [ ] Navigation switches between bar/rail based on window width
- [ ] Navigation state preserved when switching
- [ ] Back navigation works correctly

## Layout Responsiveness
- [ ] Compact width (<600dp) uses single column/pane
- [ ] Medium width (600-840dp) shows appropriate layout
- [ ] Expanded width (840dp+) shows multi-pane or grid

## Platform Testing
- [ ] Android: Test portrait, landscape, split-screen
- [ ] Desktop: Test window resizing from small to large
- [ ] Web: Test browser window resizing

## Theme Integration
- [ ] ArcaneTheme still wraps all content
- [ ] Theme switching still works
- [ ] Arcane components used where appropriate
```

---

## Example Usage: Catalog App

### Before

```
catalog/composeApp/
├── App.kt                    # Column + top tabs
├── screens/
│   ├── ControlsScreen.kt     # Vertical scroll only
│   ├── NavigationScreen.kt   # Vertical scroll only
│   └── ...
```

**Current layout:**
- Single column with top `ArcaneTabs`
- Same layout on phone, tablet, desktop
- No window size awareness

### Analysis Command

```
> /compose-adaptive-layouts-analyze catalog

# Adaptive Layout Readiness Report

## Module: catalog/composeApp
**Complexity:** Moderate

## Dependencies Status
| Dependency | Status |
|------------|--------|
| compose-material3-adaptive | ❌ Missing |

## UI Elements Found
- 5 screens (DesignSpec, Controls, Navigation, DataDisplay, Feedback)
- Navigation: Top tabs (ArcaneTabs)
- Layout: Column with vertical scroll

## Next Step
Run `/compose-adaptive-layouts-implement catalog`
```

### Implementation Command

```
> /compose-adaptive-layouts-implement catalog

Confirm target: catalog/composeApp [Yes]
Layout pattern: Auto-detect → Feed Layout
Navigation: Auto (bar → rail → drawer)
Platforms: Android, Desktop, Web

Generating implementation plan...

## Changes to Make

1. libs.versions.toml - Add 3 dependencies
2. catalog/composeApp/build.gradle.kts - Add implementations
3. App.kt - Add window size class, convert to NavigationSuiteScaffold
4. ControlsScreen.kt - Add grid layout for expanded
5. Other screens - Pass window size class

Proceed with implementation? [Yes]
```

### After

```kotlin
// App.kt
@Composable
fun App(
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
) {
    var currentScreen by remember { mutableStateOf(Screen.Controls) }
    var themeVariant by remember { mutableStateOf(ThemeVariant.ARCANE) }

    val colors = themeVariant.toColors()
    val useRail = windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND)

    ArcaneTheme(colors = colors) {
        if (useRail) {
            Row(Modifier.fillMaxSize()) {
                CatalogNavigationRail(
                    currentScreen = currentScreen,
                    onScreenSelected = { currentScreen = it }
                )
                Column(Modifier.weight(1f)) {
                    CatalogTopBar(themeVariant, onThemeChange = { themeVariant = it })
                    ScreenContent(currentScreen, windowSizeClass)
                }
            }
        } else {
            Scaffold(
                topBar = { CatalogTopBar(themeVariant, onThemeChange = { themeVariant = it }) },
                bottomBar = {
                    CatalogNavigationBar(
                        currentScreen = currentScreen,
                        onScreenSelected = { currentScreen = it }
                    )
                }
            ) { padding ->
                ScreenContent(currentScreen, windowSizeClass, Modifier.padding(padding))
            }
        }
    }
}
```

---

## Skill Implementation Notes

When implementing the actual skill (`SKILL.md`):

1. **Read documentation first** - Load `kmp-docs/compose-adaptive-layouts.md` for reference
2. **Use TodoWrite** - Track analysis/implementation steps
3. **Provide code diffs** - Show before/after for clarity
4. **Run build verification** - After changes, run `./gradlew build`
5. **Suggest testing** - Remind user to test on different window sizes
