# Compose Adaptive Layouts

Reference documentation for implementing adaptive layouts in Compose Multiplatform projects.

## References

- [Compose Multiplatform Adaptive Layouts](https://kotlinlang.org/docs/multiplatform/compose-adaptive-layouts.html)
- [Jetpack Compose Adaptive Layouts](https://developer.android.com/develop/ui/compose/layouts/adaptive)
- [Window Size Classes](https://developer.android.com/develop/ui/compose/layouts/adaptive/use-window-size-classes)
- [Canonical Layouts](https://developer.android.com/develop/ui/compose/layouts/adaptive/canonical-layouts)
- [Adaptive Navigation](https://developer.android.com/develop/ui/compose/layouts/adaptive/build-adaptive-navigation)
- [List-Detail Layouts](https://developer.android.com/develop/ui/compose/layouts/adaptive/list-detail)
- [Adaptive Do's and Don'ts](https://developer.android.com/develop/ui/compose/layouts/adaptive/adaptive-dos-and-donts)

---

## Compose Adaptive Layout Overview

Adaptive layouts adapt UI to different display sizes, orientations, and input modes to provide consistent user experiences across devices.

### Core Concept: Window Size Classes

Window size classes are opinionated viewport breakpoints that categorize display space. They are NOT device-based - they reflect available window space which changes dynamically due to:

- Device orientation changes
- Multi-window/split-screen mode
- Folding/unfolding (foldables)
- Window resizing (desktop/ChromeOS)

### Key Principle

**Base layout decisions on available window space, not device type.**

```kotlin
// CORRECT: Use window size class
val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
if (windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_EXPANDED_LOWER_BOUND)) {
    TwoPaneLayout()
} else {
    SinglePaneLayout()
}

// WRONG: Check for device type
if (isTablet()) { // Don't do this
    TwoPaneLayout()
}
```

---

## Compose Multiplatform

### Design Guidelines

1. **Use canonical layout patterns**: List-detail, feed, and supporting pane layouts
2. **Maintain consistency**: Reuse shared styles for padding, typography, and design elements
3. **Follow platform conventions**: Keep navigation consistent while respecting platform guidelines
4. **Break into reusable composables**: Enhance flexibility and modularity
5. **Account for screen density and orientation**: Adjust layouts accordingly

### Dependency

```toml
# libs.versions.toml
[versions]
compose-material3-adaptive = "1.2.0"

[libraries]
compose-material3-adaptive = { module = "org.jetbrains.compose.material3.adaptive:adaptive", version.ref = "compose-material3-adaptive" }
```

```kotlin
// build.gradle.kts
commonMain.dependencies {
    implementation(libs.compose.material3.adaptive)
}
```

### Core API

```kotlin
@Composable
fun MyApp(
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
) {
    val showTopAppBar = windowSizeClass.isHeightAtLeastBreakpoint(
        WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND
    )

    MyScreen(showTopAppBar = showTopAppBar)
}
```

**Key APIs:**
- `currentWindowAdaptiveInfo()` - Get current window adaptive info
- `WindowSizeClass` - Contains width and height classifications
- `isHeightAtLeastBreakpoint()` - Check if height meets threshold
- `isWidthAtLeastBreakpoint()` - Check if width meets threshold

---

## Jetpack Compose (Detailed)

### Window Size Classes

#### Width Breakpoints

| Size Class | Breakpoint | Typical Devices |
|------------|------------|-----------------|
| **Compact** | width < 600dp | 99.96% of phones in portrait |
| **Medium** | 600dp <= width < 840dp | 93.73% of tablets in portrait, large foldables |
| **Expanded** | 840dp <= width < 1200dp | 97.22% of tablets in landscape |
| **Large** | 1200dp <= width < 1600dp | Large tablet displays |
| **Extra Large** | width >= 1600dp | Desktop displays |

#### Height Breakpoints

| Size Class | Breakpoint | Typical Devices |
|------------|------------|-----------------|
| **Compact** | height < 480dp | 99.78% of phones in landscape |
| **Medium** | 480dp <= height < 900dp | 96.56% of tablets in landscape, 97.59% of phones in portrait |
| **Expanded** | height >= 900dp | 94.25% of tablets in portrait |

#### Breakpoint Constants

```kotlin
// Width breakpoints (in dp)
WindowSizeClass.WIDTH_DP_COMPACT_LOWER_BOUND   // 0dp
WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND    // 600dp
WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND  // 840dp
WindowSizeClass.WIDTH_DP_LARGE_LOWER_BOUND     // 1200dp
WindowSizeClass.WIDTH_DP_XLARGE_LOWER_BOUND    // 1600dp

// Height breakpoints (in dp)
WindowSizeClass.HEIGHT_DP_COMPACT_LOWER_BOUND  // 0dp
WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND   // 480dp
WindowSizeClass.HEIGHT_DP_EXPANDED_LOWER_BOUND // 900dp
```

#### Usage Example

```kotlin
@Composable
fun MyApp(
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo(
        supportLargeAndXLargeWidth = true
    ).windowSizeClass
) {
    val useNavigationRail = windowSizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND
    )

    val showTopAppBar = windowSizeClass.isHeightAtLeastBreakpoint(
        WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND
    )

    MyScreen(
        useNavigationRail = useNavigationRail,
        showTopAppBar = showTopAppBar
    )
}
```

### Canonical Layouts

#### 1. List-Detail Layout

Master-detail pattern with two panes: list and detail.

**Responsive behavior:**
- **Expanded width**: Both panes visible side-by-side
- **Compact/Medium width**: Single pane, switch between list and detail

```kotlin
@Composable
fun ListDetailScreen(
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,
    selectedItem: Item?,
    onItemSelected: (Item) -> Unit,
    onBackToList: () -> Unit
) {
    val isExpanded = windowSizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
    )

    if (isExpanded) {
        // Two-pane layout
        Row {
            ListPane(
                modifier = Modifier.weight(0.4f),
                onItemClick = onItemSelected
            )
            DetailPane(
                modifier = Modifier.weight(0.6f),
                item = selectedItem
            )
        }
    } else {
        // Single pane - show list or detail
        if (selectedItem != null) {
            DetailPane(item = selectedItem)
            BackHandler { onBackToList() }
        } else {
            ListPane(onItemClick = onItemSelected)
        }
    }
}
```

**Using ListDetailPaneScaffold:**

```kotlin
val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator<MyItem>()
val scope = rememberCoroutineScope()

NavigableListDetailPaneScaffold(
    navigator = scaffoldNavigator,
    listPane = {
        AnimatedPane {
            MyList(
                onItemClick = { item ->
                    scope.launch {
                        scaffoldNavigator.navigateTo(
                            ListDetailPaneScaffoldRole.Detail,
                            item
                        )
                    }
                }
            )
        }
    },
    detailPane = {
        AnimatedPane {
            scaffoldNavigator.currentDestination?.contentKey?.let {
                MyDetails(it)
            }
        }
    }
)
```

#### 2. Feed Layout

Grid-based content that adapts columns based on available space.

```kotlin
@Composable
fun FeedScreen() {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(feedItems) { item ->
            FeedCard(item)
        }

        // Full-width section header
        item(span = { GridItemSpan(maxLineSpan) }) {
            SectionHeader("Featured")
        }
    }
}
```

#### 3. Supporting Pane Layout

Primary content (67%) with supporting content (33%).

```kotlin
@Composable
fun SupportingPaneScreen(
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
) {
    val isExpanded = windowSizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
    )
    val isMedium = windowSizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND
    )

    when {
        isExpanded -> {
            Row {
                MainContent(modifier = Modifier.weight(0.67f))
                SupportingPane(modifier = Modifier.weight(0.33f))
            }
        }
        isMedium -> {
            Row {
                MainContent(modifier = Modifier.weight(0.5f))
                SupportingPane(modifier = Modifier.weight(0.5f))
            }
        }
        else -> {
            Column {
                MainContent(modifier = Modifier.weight(1f))
                // Use bottom sheet for supporting content
            }
        }
    }
}
```

### Adaptive Navigation

`NavigationSuiteScaffold` automatically switches between navigation types:

- **Compact windows**: Navigation Bar (bottom)
- **Expanded windows**: Navigation Rail (side)
- **Extra large / custom**: Navigation Drawer

```kotlin
enum class AppDestinations(
    val label: String,
    val icon: ImageVector
) {
    HOME("Home", Icons.Default.Home),
    SEARCH("Search", Icons.Default.Search),
    SETTINGS("Settings", Icons.Default.Settings)
}

@Composable
fun AdaptiveNavigation() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach { destination ->
                item(
                    icon = { Icon(destination.icon, contentDescription = destination.label) },
                    label = { Text(destination.label) },
                    selected = destination == currentDestination,
                    onClick = { currentDestination = destination }
                )
            }
        }
    ) {
        when (currentDestination) {
            AppDestinations.HOME -> HomeScreen()
            AppDestinations.SEARCH -> SearchScreen()
            AppDestinations.SETTINGS -> SettingsScreen()
        }
    }
}
```

**Customizing navigation type:**

```kotlin
val adaptiveInfo = currentWindowAdaptiveInfo()
val customNavSuiteType = with(adaptiveInfo) {
    if (windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_LARGE_LOWER_BOUND)) {
        NavigationSuiteType.NavigationDrawer
    } else {
        NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo)
    }
}

NavigationSuiteScaffold(
    layoutType = customNavSuiteType,
    navigationSuiteItems = { /* ... */ }
) { /* content */ }
```

### Implementation Patterns

#### State Hoisting for Window Size

Pass window size class as parameter to enable testing and previews:

```kotlin
@Composable
fun MyScreen(
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
) {
    // Use windowSizeClass for layout decisions
}

// In previews:
@Preview
@Composable
fun MyScreenCompactPreview() {
    MyScreen(windowSizeClass = WindowSizeClass(400.dp, 800.dp))
}

@Preview
@Composable
fun MyScreenExpandedPreview() {
    MyScreen(windowSizeClass = WindowSizeClass(1200.dp, 800.dp))
}
```

#### Component-Level Adaptation with BoxWithConstraints

For components that need to adapt based on their own constraints:

```kotlin
@Composable
fun AdaptiveCard(
    imageUrl: String,
    title: String,
    description: String
) {
    BoxWithConstraints {
        if (maxWidth < 400.dp) {
            // Vertical layout for narrow containers
            Column {
                AsyncImage(imageUrl)
                Text(title)
            }
        } else {
            // Horizontal layout for wide containers
            Row {
                Column(modifier = Modifier.weight(1f)) {
                    Text(title)
                    Text(description)
                }
                AsyncImage(imageUrl)
            }
        }
    }
}
```

**Note:** `BoxWithConstraints` defers composition to layout phase - use sparingly.

#### Always Provide All Data

Don't conditionally load data based on display size:

```kotlin
// CORRECT: Always provide all data
@Composable
fun ItemCard(
    title: String,
    subtitle: String,     // Always provided
    description: String,  // Always provided, shown conditionally
    imageUrl: String
)

// WRONG: Conditionally omit data
@Composable
fun ItemCard(
    title: String,
    description: String? = null  // Don't do this based on screen size
)
```

---

## Adaptive Layout by Platform

### Desktop

| Consideration | Details |
|---------------|---------|
| **Window Resizing** | Support all sizes, windows are freely resizable |
| **Input** | Keyboard navigation, mouse hover states, keyboard shortcuts |
| **Width Classes** | Commonly Medium, Expanded, Large, Extra Large |
| **Navigation** | NavigationRail or NavigationDrawer preferred |
| **Multi-window** | Users may have multiple app windows open |

```kotlin
// Enable large/extra-large width classes for desktop
val windowSizeClass = currentWindowAdaptiveInfo(
    supportLargeAndXLargeWidth = true
).windowSizeClass
```

### Android

| Consideration | Details |
|---------------|---------|
| **Orientation** | Support both portrait and landscape |
| **Multi-window** | Support split-screen and freeform modes |
| **Foldables** | Consider fold state and hinge position |
| **Width Classes** | All classes possible depending on device/mode |
| **Navigation** | NavigationBar (compact), NavigationRail (medium+) |

**Android 16+ Note:** System ignores orientation/aspect ratio restrictions on devices >= 600dp smallest width.

### Web (Wasm/JS)

| Consideration | Details |
|---------------|---------|
| **Viewport** | Responds to browser window resizing |
| **Browser Chrome** | Account for browser UI reducing available space |
| **Responsive** | Same principles as desktop windowing |
| **Input** | Keyboard, mouse, and touch (hybrid devices) |

### iOS

Out of scope for now due to nightmare tooling support, Xcode sucks.

---

## Best Practices

### Do's

- **Build with Compose** and Material 3 Adaptive library
- **Base layouts on window size classes**, not device type
- **Create multi-pane layouts** for expanded widths
- **Make app resizable** - never set `resizeableActivity="false"`
- **Support all orientations** - remove `screenOrientation` restrictions
- **Support input devices** - keyboard, mouse, stylus
- **Use modern APIs** - `WindowManager.getCurrentWindowMetrics()`
- **Test at all breakpoints** - Compact, Medium, Expanded

### Don'ts

- **Don't lock orientation** - causes letterboxing
- **Don't restrict aspect ratio** - use `minAspectRatio`/`maxAspectRatio`
- **Don't use deprecated APIs**:
  - `Display.getSize()` (deprecated API 30)
  - `Display.getMetrics()` (deprecated API 30)
  - `Display.getRealSize()` (deprecated API 31)
  - `Display.getRealMetrics()` (deprecated API 31)
- **Don't just stretch UI** - use content panes instead
- **Don't reinvent navigation** - use NavigationSuiteScaffold
- **Don't ignore multi-window mode**

### Responsive vs Adaptive

| Type | Description | When to Use |
|------|-------------|-------------|
| **Responsive** | Small adjustments that fill available space | Within components, flexible sizing |
| **Adaptive** | Complete layout replacement based on size | App-level, canonical layout switching |

---

## Integration with ArcaneDesignSystem

### Using Window Size Classes with ArcaneTheme

```kotlin
@Composable
fun ArcaneApp() {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    ArcaneTheme {
        when {
            windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_EXPANDED_LOWER_BOUND) -> {
                ExpandedLayout()
            }
            windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND) -> {
                MediumLayout()
            }
            else -> {
                CompactLayout()
            }
        }
    }
}
```

### Surface Variants by Window Size

```kotlin
@Composable
fun AdaptiveContainer(
    windowSizeClass: WindowSizeClass,
    content: @Composable () -> Unit
) {
    val surfaceVariant = when {
        windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_EXPANDED_LOWER_BOUND) ->
            SurfaceVariant.ContainerHigh
        else ->
            SurfaceVariant.Container
    }

    ArcaneSurface(variant = surfaceVariant) {
        content()
    }
}
```

---

## Dependencies

Add to `libs.versions.toml`:

```toml
[versions]
compose-material3-adaptive = "1.2.0"

[libraries]
# Core adaptive
compose-material3-adaptive = { module = "org.jetbrains.compose.material3.adaptive:adaptive", version.ref = "compose-material3-adaptive" }

# Layout scaffolds (list-detail, supporting pane)
compose-material3-adaptive-layout = { module = "org.jetbrains.compose.material3.adaptive:adaptive-layout", version.ref = "compose-material3-adaptive" }

# Navigation integration
compose-material3-adaptive-navigation = { module = "org.jetbrains.compose.material3.adaptive:adaptive-navigation", version.ref = "compose-material3-adaptive" }

# Navigation suite scaffold (Android-specific, check KMP availability)
# compose-material3-navigation-suite = { module = "androidx.compose.material3:material3-adaptive-navigation-suite", version.ref = "..." }
```

Usage in `build.gradle.kts`:

```kotlin
commonMain.dependencies {
    implementation(libs.compose.material3.adaptive)
    implementation(libs.compose.material3.adaptive.layout)
    implementation(libs.compose.material3.adaptive.navigation)
}
```

---

## Code Templates

### Basic Adaptive Screen

```kotlin
@Composable
fun AdaptiveScreen(
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
) {
    val isExpanded = windowSizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
    )

    ArcaneTheme {
        if (isExpanded) {
            Row {
                NavigationRail { /* nav items */ }
                Content(modifier = Modifier.weight(1f))
            }
        } else {
            Scaffold(
                bottomBar = { NavigationBar { /* nav items */ } }
            ) { padding ->
                Content(modifier = Modifier.padding(padding))
            }
        }
    }
}
```

### List-Detail Template

```kotlin
@Composable
fun ListDetailTemplate(
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
) {
    var selectedItem by remember { mutableStateOf<Item?>(null) }

    val isExpanded = windowSizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
    )

    ArcaneTheme {
        if (isExpanded) {
            Row {
                ArcaneSurface(
                    variant = SurfaceVariant.ContainerLow,
                    modifier = Modifier.width(360.dp)
                ) {
                    ItemList(
                        selectedItem = selectedItem,
                        onItemClick = { selectedItem = it }
                    )
                }
                ArcaneSurface(
                    variant = SurfaceVariant.Container,
                    modifier = Modifier.weight(1f)
                ) {
                    selectedItem?.let { ItemDetail(it) }
                        ?: EmptyDetailPlaceholder()
                }
            }
        } else {
            if (selectedItem != null) {
                ItemDetail(selectedItem!!)
                BackHandler { selectedItem = null }
            } else {
                ItemList(onItemClick = { selectedItem = it })
            }
        }
    }
}
```

---

## Verification Checklist

Use this checklist when implementing adaptive layouts:

```
## Window Size Class Integration
[ ] Added adaptive dependencies to libs.versions.toml
[ ] Using currentWindowAdaptiveInfo().windowSizeClass
[ ] Window size class passed as parameter (for testing)

## Breakpoint Testing
[ ] Tested at Compact width (<600dp)
[ ] Tested at Medium width (600-840dp)
[ ] Tested at Expanded width (840-1200dp)
[ ] Tested at Large width (1200-1600dp) if applicable
[ ] Tested height breakpoints for top app bar visibility

## Navigation
[ ] Navigation adapts correctly (bar/rail/drawer)
[ ] Back navigation works in single-pane mode
[ ] Navigation state preserved across configuration changes

## Layout
[ ] Multi-pane layout for expanded widths
[ ] Content not stretched unnaturally
[ ] Proper spacing and padding at all sizes

## State Management
[ ] State preserved across configuration changes
[ ] State hoisted appropriately
[ ] All data provided regardless of display size

## Platform-Specific
[ ] Keyboard navigation works (desktop)
[ ] Mouse hover states work (desktop)
[ ] Multi-window mode works (Android)
[ ] Orientation changes work (Android)
[ ] Browser resize works (Web)

## Arcane Integration
[ ] Using ArcaneTheme wrapper
[ ] Using appropriate SurfaceVariant per context
[ ] Arcane components used instead of raw Material
```
