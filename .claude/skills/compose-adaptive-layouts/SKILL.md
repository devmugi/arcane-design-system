---
name: compose-adaptive-layouts
description: Use when adding adaptive/responsive layouts to Compose Multiplatform modules. Triggers on "add adaptive layout", "make responsive", "support different screen sizes", "add window size classes", "adaptive navigation", "NavigationSuiteScaffold".
---

# Compose Adaptive Layouts Skill

Add adaptive layout support to Compose Multiplatform modules for responsive UI across phones, tablets, and desktop.

## Documentation Reference

**Read first:** `kmp-docs/compose-adaptive-layouts.md`

This documentation contains:
- Window size class breakpoints and APIs
- Canonical layout patterns
- Adaptive navigation patterns
- Code templates and best practices

## Commands

This skill provides two workflows:
1. **Analyze** - Assess module readiness
2. **Implement** - Add adaptive layout support

---

## Analyze Workflow

Use when: User asks to "check", "analyze", "assess" adaptive layout readiness

### Step 1: Get Module Name

Ask: "Which module should I analyze for adaptive layout readiness?"

Common modules in ArcaneDesignSystem:
- `catalog` (catalog/composeApp)
- `arcane-components`
- `arcane-chat`

### Step 2: Locate and Scan Module

```
1. Find module build.gradle.kts
2. Find source directories (commonMain, etc.)
3. If not found, list available modules
```

### Step 3: Check Dependencies

Search `libs.versions.toml` and module `build.gradle.kts` for:

| Dependency | Key |
|------------|-----|
| `compose-material3-adaptive` | Core adaptive APIs |
| `compose-material3-adaptive-layout` | Scaffold components |
| `compose-material3-adaptive-navigation` | Navigation integration |
| `compose-material3-windowsizeclass` | WindowSizeClass type (required) |

### Step 4: Scan UI Elements

Search source files for these patterns:

| Pattern | What It Means |
|---------|---------------|
| `currentWindowAdaptiveInfo` | Already using adaptive |
| `WindowSizeClass` | Already using adaptive |
| `NavigationBar` | Has bottom navigation |
| `NavigationRail` | Has side navigation |
| `ArcaneTabs`, `TabRow` | Tab-based navigation |
| `Column(.*fillMaxSize` | Vertical-only layout |
| `@Composable fun.*Screen` | Screen composables |

### Step 5: Generate Report

Output format:

```markdown
# Adaptive Layout Readiness Report

## Module: {module-name}
**Complexity:** Simple | Moderate | Complex

## Dependencies
| Dependency | Status |
|------------|--------|
| compose-material3-adaptive | Present/Missing |
| compose-material3-adaptive-layout | Present/Missing |

## Screens Found
- {ScreenName}.kt - {layout pattern}
- ...

## Current Navigation
- Type: {tabs/bottom bar/none}
- Location: {file path}

## Issues Found
1. {issue description}
2. ...

## Recommendations
1. Add adaptive dependencies
2. Wrap App with window size class
3. Convert navigation to NavigationSuiteScaffold pattern

## Next Step
Run adaptive layout implementation to add support.
```

---

## Implement Workflow

Use when: User asks to "add", "implement", "enable" adaptive layouts

### Step 1: Confirm Module

Ask: "Which module should I add adaptive layouts to?"

Then confirm:
```
I will modify:
- gradle/libs.versions.toml (add dependencies)
- {module}/build.gradle.kts (add implementations)
- {module}/src/commonMain/.../App.kt (add window size class)
- Screen files as needed

Proceed?
```

### Step 2: Ask Layout Questions

**Question 1: Layout Pattern**

```
Select layout pattern for screens:

1. Feed Layout (Recommended for catalog)
   - Grid that adapts columns based on width
   - Best for: component showcases, galleries

2. List-Detail Layout
   - Master list with detail pane
   - Best for: settings, file browsers

3. Keep Simple
   - Just add navigation adaptation, no screen changes
```

**Question 2: Navigation Style**

```
Select navigation adaptation:

1. Auto (Recommended)
   - Compact: Bottom NavigationBar
   - Medium+: Side NavigationRail

2. Keep Current Navigation
   - Only add window size awareness, don't change navigation
```

### Step 3: Add Dependencies

**Edit `gradle/libs.versions.toml`:**

Add to `[versions]`:
```toml
compose-material3-adaptive = "1.2.0"
```

Add to `[libraries]`:
```toml
compose-material3-adaptive = { module = "org.jetbrains.compose.material3.adaptive:adaptive", version.ref = "compose-material3-adaptive" }
compose-material3-adaptive-layout = { module = "org.jetbrains.compose.material3.adaptive:adaptive-layout", version.ref = "compose-material3-adaptive" }
compose-material3-adaptive-navigation = { module = "org.jetbrains.compose.material3.adaptive:adaptive-navigation", version.ref = "compose-material3-adaptive" }
# Note: Use compose-material3 version, not compose-material3-adaptive
compose-material3-windowsizeclass = { module = "org.jetbrains.compose.material3:material3-window-size-class", version.ref = "compose-material3" }
```

**Edit module `build.gradle.kts`:**

Add to `commonMain.dependencies`:
```kotlin
implementation(libs.compose.material3.adaptive)
implementation(libs.compose.material3.adaptive.layout)
implementation(libs.compose.material3.adaptive.navigation)
implementation(libs.compose.material3.windowsizeclass)
```

### Step 3.5: Verify Theme Properties

Before using navigation colors, verify these properties exist in your theme (e.g., `ArcaneColors.kt`):

| Property | Purpose | Fallback if Missing |
|----------|---------|---------------------|
| `secondaryContainer` | Navigation indicator background | Use `primary.copy(alpha = 0.12f)` |
| `primary` | Selected icon/text color | Required |
| `textSecondary` | Unselected icon/text color | Use `onSurface.copy(alpha = 0.6f)` |
| `surfaceContainerLow` | Navigation container background | Use `surface` |

**Note:** ArcaneColors does NOT have `primaryContainer`. Use `secondaryContainer` instead.

### Step 4: Update App Entry Point

**Add window size class parameter:**

```kotlin
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.window.core.layout.WindowSizeClass

@Composable
fun App() {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    // Pass to screens and layout logic
}
```

**Add adaptive navigation logic:**

```kotlin
@Composable
fun App() {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    var currentScreen by remember { mutableStateOf(Screen.Home) }

    // Use MEDIUM (600dp) to show rail on tablets in portrait
    val useNavigationRail = windowSizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND
    )

    ArcaneTheme {
        if (useNavigationRail) {
            // Expanded layout with rail
            Row(Modifier.fillMaxSize()) {
                AppNavigationRail(
                    currentScreen = currentScreen,
                    onNavigate = { currentScreen = it }
                )
                Column(Modifier.weight(1f)) {
                    TopBar()
                    ScreenContent(currentScreen, windowSizeClass)
                }
            }
        } else {
            // Compact layout with bottom bar
            Scaffold(
                topBar = { TopBar() },
                bottomBar = {
                    AppNavigationBar(
                        currentScreen = currentScreen,
                        onNavigate = { currentScreen = it }
                    )
                }
            ) { padding ->
                ScreenContent(currentScreen, windowSizeClass, Modifier.padding(padding))
            }
        }
    }
}
```

### Step 5: Create Navigation Components

**NavigationRail component:**

```kotlin
@Composable
fun AppNavigationRail(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationRail(
        modifier = modifier,
        containerColor = ArcaneTheme.colors.surfaceContainerLow
    ) {
        Screen.entries.forEach { screen ->
            NavigationRailItem(
                selected = currentScreen == screen,
                onClick = { onNavigate(screen) },
                icon = { Icon(screen.icon, contentDescription = screen.label) },
                label = { Text(screen.label) },
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = ArcaneTheme.colors.primary,
                    selectedTextColor = ArcaneTheme.colors.primary,
                    indicatorColor = ArcaneTheme.colors.secondaryContainer
                )
            )
        }
    }
}
```

**NavigationBar component:**

```kotlin
@Composable
fun AppNavigationBar(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = ArcaneTheme.colors.surfaceContainerLow
    ) {
        Screen.entries.forEach { screen ->
            NavigationBarItem(
                selected = currentScreen == screen,
                onClick = { onNavigate(screen) },
                icon = { Icon(screen.icon, contentDescription = screen.label) },
                label = { Text(screen.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = ArcaneTheme.colors.primary,
                    selectedTextColor = ArcaneTheme.colors.primary,
                    indicatorColor = ArcaneTheme.colors.secondaryContainer
                )
            )
        }
    }
}
```

### Step 6: Update Screens (if Feed Layout selected)

**Screen parameter pattern:**

Screens receive `WindowSizeClass` from the parent `App.kt` rather than calling `currentWindowAdaptiveInfo()` themselves. This ensures a single source of truth.

```kotlin
// In App.kt - single call, pass to screens
val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
ScreenContent(currentScreen, windowSizeClass)

// In screens - receive from parent, optional for compatibility
@Composable
fun ComponentScreen(windowSizeClass: WindowSizeClass? = null) {
    val columns = when {
        windowSizeClass?.isWidthAtLeastBreakpoint(WIDTH_DP_EXPANDED_LOWER_BOUND) == true -> 3
        windowSizeClass?.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND) == true -> 2
        else -> 1
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        contentPadding = PaddingValues(ArcaneSpacing.Medium),
        horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Medium),
        verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Medium)
    ) {
        // Grid items
    }
}
```

### Step 7: Run Verification

After implementation, run:

```bash
./gradlew :module-name:build
```

Then test:
- Desktop: Resize window from narrow to wide
- Android: Test portrait, landscape, split-screen
- Web: Resize browser window

---

## Verification Checklist

After implementing, verify:

```
## Dependencies
- [ ] compose-material3-adaptive in libs.versions.toml
- [ ] compose-material3-windowsizeclass in libs.versions.toml (uses compose-material3 version)
- [ ] All 4 dependencies added to module build.gradle.kts
- [ ] Build succeeds

## Window Size Class
- [ ] App.kt uses currentWindowAdaptiveInfo()
- [ ] WindowSizeClass passed to screens that need it

## Navigation
- [ ] NavigationRail shows on expanded widths (>=600dp)
- [ ] NavigationBar shows on compact widths (<600dp)
- [ ] Navigation state preserved when resizing

## Layout
- [ ] Compact: Single column/pane layout
- [ ] Expanded: Multi-column or multi-pane layout

## Theme Integration
- [ ] ArcaneTheme still wraps all content
- [ ] Navigation uses Arcane colors
- [ ] Theme switching still works (if applicable)
```

---

## Quick Reference

### Window Size Breakpoints

| Class | Width | Use |
|-------|-------|-----|
| Compact | < 600dp | Phone portrait |
| Medium | 600-840dp | Tablet portrait, phone landscape |
| Expanded | 840-1200dp | Tablet landscape |
| Large | 1200-1600dp | Desktop |
| Extra Large | >= 1600dp | Large desktop |

### Key Imports

```kotlin
// Adaptive API
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo

// WindowSizeClass - from window.core, NOT material3.adaptive
import androidx.window.core.layout.WindowSizeClass

// Navigation components
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
```

### Breakpoint Selection

| Breakpoint | Width | When to Use |
|------------|-------|-------------|
| `WIDTH_DP_MEDIUM_LOWER_BOUND` | 600dp | **Recommended** - Shows rail on tablets in portrait |
| `WIDTH_DP_EXPANDED_LOWER_BOUND` | 840dp | Only for large screens (tablets landscape+) |

```kotlin
// Recommended: Use MEDIUM for earlier rail appearance
val useNavigationRail = windowSizeClass.isWidthAtLeastBreakpoint(
    WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND  // 600dp
)

// Alternative: Use EXPANDED for later rail appearance
val useNavigationRail = windowSizeClass.isWidthAtLeastBreakpoint(
    WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND  // 840dp
)
```
