# Phase 3: Navigation Components Design

## Overview

This document defines the design specification for Phase 3 navigation components: Tabs, Breadcrumbs, Pagination, and Stepper. These components follow the established Arcane Design System patterns with sci-fi aesthetic (teal glow on dark surfaces).

## File Location

All navigation components will be placed in:
```
arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/navigation/
├── Tabs.kt
├── Breadcrumbs.kt
├── Pagination.kt
└── Stepper.kt
```

---

## 1. Tabs

### API Structure

```kotlin
sealed class ArcaneTabStyle {
    data object Filled : ArcaneTabStyle()    // Teal pill background (primary)
    data object Underline : ArcaneTabStyle() // Future variant
}

data class ArcaneTab(
    val label: String,
    val icon: (@Composable () -> Unit)? = null,
    val enabled: Boolean = true
)

// State-only tabs (flexible)
@Composable
fun ArcaneTabs(
    tabs: List<ArcaneTab>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    style: ArcaneTabStyle = ArcaneTabStyle.Filled,
    scrollable: Boolean = false
)

// Integrated tabs + content (convenient)
@Composable
fun ArcaneTabLayout(
    tabs: List<ArcaneTab>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    style: ArcaneTabStyle = ArcaneTabStyle.Filled,
    scrollable: Boolean = false,
    content: @Composable (selectedIndex: Int) -> Unit
)
```

### Visual Styling

**Selected tab (Filled style):**
- Background: `colors.primary` with `ArcaneRadius.Medium` (6dp)
- Text: `colors.surface` for contrast
- Subtle glow effect using `colors.glow`

**Unselected tab:**
- Background: Transparent
- Text: `colors.textSecondary`
- On hover/press: `colors.surfacePressed` background

**Disabled tab:**
- Text: `colors.textDisabled`
- No interaction response

### Animations

- Selection indicator: `animateDpAsState` (200ms tween)
- Background color: `animateColorAsState`
- Selected background slides to new position

### Sizing & Spacing

- Tab height: 40dp
- Horizontal padding: `ArcaneSpacing.Medium` (16dp)
- Gap between tabs: `ArcaneSpacing.XSmall` (8dp)
- Icon size: `ArcaneIconography.Small` (15dp)
- Icon-to-text spacing: `ArcaneSpacing.XSmall` (8dp)

### Scrollable Behavior

**Fixed mode (`scrollable = false`):**
- Tabs fill available width equally via `Modifier.weight(1f)`
- Best for 2-5 tabs

**Scrollable mode (`scrollable = true`):**
- Tabs use intrinsic width
- Horizontal scroll with `LazyRow`
- Selected tab auto-scrolls into view
- Optional fade edges (8dp gradient)

---

## 2. Breadcrumbs

### API Structure

```kotlin
data class ArcaneBreadcrumb(
    val label: String,
    val onClick: (() -> Unit)? = null  // null = current/non-clickable
)

@Composable
fun ArcaneBreadcrumbs(
    items: List<ArcaneBreadcrumb>,
    modifier: Modifier = Modifier,
    separator: @Composable () -> Unit = { DefaultSeparator() },
    maxItems: Int = Int.MAX_VALUE,
    collapsedIndicator: @Composable () -> Unit = { Text("...") }
)
```

### Visual Styling

**Clickable items (with onClick):**
- Text: `colors.primary` (teal)
- Style: `typography.bodyMedium`
- On hover/press: Underline, `colors.primaryVariant`

**Current item (no onClick):**
- Text: `colors.text`
- Style: `typography.bodyMedium` with `FontWeight.Medium`

**Separator:**
- Default: `>` character
- Color: `colors.textSecondary`
- Padding: `ArcaneSpacing.XSmall` (8dp) each side

**Collapsed indicator:**
- Text: `...`
- Color: `colors.primary` (teal)
- Optionally clickable to expand

### Collapsing Behavior

When `items.size > maxItems`:
```
Home > ... > Categories > Item
```
First item + collapsed indicator + last N items shown.

### Sizing

- Vertical padding: `ArcaneSpacing.XSmall` (8dp)
- Single row, horizontally scrollable if needed

---

## 3. Pagination

### API Structure

```kotlin
@Composable
fun ArcanePagination(
    currentPage: Int,              // 1-indexed
    totalPages: Int,
    onPageSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    showPageInfo: Boolean = true,  // "Page 1 of 10"
    siblingCount: Int = 1,         // Pages on each side of current
    boundaryCount: Int = 1         // Pages at start/end
)
```

### Page Number Logic

With `siblingCount=1, boundaryCount=1`:
```
Page 1:   < [1] 2 3 ... 10 >
Page 5:   < 1 ... 4 [5] 6 ... 10 >
Page 10:  < 1 ... 8 9 [10] >
```

### Visual Styling

**Page info label:**
- Text: `colors.textSecondary`
- Style: `typography.bodySmall`
- Position: Left of page buttons

**Current page button:**
- Background: `colors.primary` with `ArcaneRadius.Medium`
- Text: `colors.surface`
- Subtle glow effect

**Other page buttons:**
- Background: Transparent
- Text: `colors.text`
- On hover/press: `colors.surfacePressed`

**Arrow buttons:**
- Icon: Chevron left/right, `ArcaneIconography.Small`
- Enabled: `colors.text`
- Disabled (at boundary): `colors.textDisabled`
- Hover background: `colors.surfacePressed`

**Ellipsis:**
- Text: `...`
- Color: `colors.textSecondary`
- Non-interactive

### Sizing & Spacing

- Button size: 32dp x 32dp
- Gap between buttons: `ArcaneSpacing.XSmall` (8dp)
- Gap page info to buttons: `ArcaneSpacing.Medium` (16dp)

---

## 4. Stepper

### API Structure

```kotlin
enum class ArcaneStepState {
    Pending,
    Active,
    Completed
}

data class ArcaneStep(
    val label: String,
    val description: String? = null,
    val state: ArcaneStepState = ArcaneStepState.Pending
)

@Composable
fun ArcaneStepper(
    steps: List<ArcaneStep>,
    modifier: Modifier = Modifier,
    orientation: ArcaneStepperOrientation = ArcaneStepperOrientation.Horizontal
)

sealed class ArcaneStepperOrientation {
    data object Horizontal : ArcaneStepperOrientation()
    data object Vertical : ArcaneStepperOrientation()
}
```

### Visual Styling

**Step indicators (circles):**
- Size: 24dp diameter
- Border: `ArcaneBorder.Medium` (2dp)

**Completed step:**
- Background: `colors.primary` (filled)
- Border: `colors.primary`
- Icon: Checkmark in `colors.surface`
- Checkmark animates in (scale)

**Active step:**
- Background: `colors.surface`
- Border: `colors.primary`
- Inner: Small teal dot or step number
- Subtle glow effect

**Pending step:**
- Background: `colors.surface`
- Border: `colors.textDisabled`
- Optional step number in `colors.textDisabled`

**Connector lines:**
- Height: 2dp
- Completed: `colors.primary`
- Pending: `colors.textDisabled`
- Animated fill on completion

**Labels & descriptions:**
- Label: `typography.labelMedium`
- Active label: `colors.text`, `FontWeight.Medium`
- Other labels: `colors.textSecondary`
- Description: `typography.labelSmall`, `colors.textSecondary`, active step only

### Spacing

- Connector: Flexible, fills available space
- Circle-to-label: `ArcaneSpacing.XSmall` (8dp)

---

## Implementation Order

1. **Tabs** - Most critical, foundational for content organization
2. **Breadcrumbs** - Simple, quick win
3. **Pagination** - Moderate complexity
4. **Stepper** - Most complex, multi-state with animations

## Catalog Integration

Add `NavigationScreen.kt` to catalog app showcasing all navigation components with interactive examples.
