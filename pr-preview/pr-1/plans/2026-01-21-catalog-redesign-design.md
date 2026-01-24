# Catalog Redesign: Single Design Spec Screen

## Overview
Replace tab-based navigation with a single scrollable "Design Spec" screen showing all components in a dense grid layout. Section titles are clickable to open detailed screens.

## Files to Modify
- `catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/App.kt`
- Create: `catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/screens/DesignSpecScreen.kt`
- Keep existing: `ControlsScreen.kt`, `NavigationScreen.kt`, `DataDisplayScreen.kt`, `FeedbackScreen.kt`

## Navigation Flow
```
DesignSpecScreen (main)
    ↓ click section title
DetailScreen (Controls/Navigation/DataDisplay/Feedback)
    ↓ back button
DesignSpecScreen
```

## Layout Structure

### Foundation Section (no click)
Row 1: Surfaces | Elevation | Iconography
Row 2: Border Thickness | Radius Scale

### CONTROLS Section (clickable → ControlsScreen)
```
Row {
    Buttons (column)     | Text Field (column) | Tactile+Switch+Slider (column)
    - Primary            | - Placeholder       | - Checkbox
    - Secondary          | - Helper Text       | - Radio Button
    - Pressed            | - Password          | - Switch ON/OFF
    - Loading            | - Focused           | - Slider
    - Disabled           | - Error             |
}
```

### NAVIGATION Section (clickable → NavigationScreen)
```
Row 1: Tabs | Breadcrumbs | 5-Step Stepper
Row 2: Pagination
```

### DATA DISPLAY Section (clickable → DataDisplayScreen)
```
Row 1: Cards | List Items | Badges | Avatars | Tooltip
Row 2: Compact Table Header
```

### FEEDBACK Section (clickable → FeedbackScreen)
```
Row 1: Progress | Spinner | Skeletons
Row 2: Alert Banners | Empty State
Row 3: Modal buttons | Toast buttons
```

## Implementation Notes

### Clickable Section Title Component
```kotlin
@Composable
fun SectionHeader(
    title: String,
    onClick: () -> Unit
) {
    Text(
        text = title,
        modifier = Modifier.clickable { onClick() },
        style = typography.displaySmall,
        color = colors.primary
    )
}
```

### Navigation State in App.kt
```kotlin
sealed class Screen {
    data object DesignSpec : Screen()
    data object Controls : Screen()
    data object Navigation : Screen()
    data object DataDisplay : Screen()
    data object Feedback : Screen()
}

var currentScreen by remember { mutableStateOf<Screen>(Screen.DesignSpec) }
```

### Grid Layout Pattern
```kotlin
Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Medium)
) {
    Column(modifier = Modifier.weight(1f)) { /* content */ }
    Column(modifier = Modifier.weight(1f)) { /* content */ }
    Column(modifier = Modifier.weight(1f)) { /* content */ }
}
```

## Verification
1. Run `./gradlew :catalog:composeApp:run`
2. Verify single scrollable screen shows all sections
3. Verify Foundation tokens display correctly
4. Click each section title → opens detail screen
5. Back button returns to overview
6. Layout matches Figma reference image
