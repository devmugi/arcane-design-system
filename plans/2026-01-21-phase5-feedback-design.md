# Phase 5: Feedback Components - Design Specification

## Overview

Expanding the Arcane Design System with a new **Feedback** component category. These components handle user notifications, loading states, confirmations, and empty content scenarios.

**Components:** 11 total (plus 2 additional skeleton variants)

---

## Component Inventory

| Component | File | Purpose |
|-----------|------|---------|
| `ArcaneModal` | `Modal.kt` | Generic modal container with backdrop |
| `ArcaneConfirmationDialog` | `ConfirmationDialog.kt` | Pre-built confirm/delete dialog |
| `ArcaneToast` + `ArcaneToastHost` | `Toast.kt` | Dismissible notifications with queue |
| `ArcaneAlertBanner` | `AlertBanner.kt` | Persistent inline alerts with variants |
| `ArcaneCircularProgress` | `CircularProgress.kt` | Determinate ring progress |
| `ArcaneLinearProgress` | `LinearProgress.kt` | Determinate horizontal bar |
| `ArcaneSpinner` | `Spinner.kt` | Indeterminate loading animation |
| `ArcaneSkeleton` | `Skeleton.kt` | Base shapes (text, circle, rectangle) |
| `ArcaneSkeletonListItem` | `SkeletonListItem.kt` | Matches ArcaneListItem layout |
| `ArcaneSkeletonCard` | `SkeletonCard.kt` | Matches ArcaneCard layout |
| `ArcaneSkeletonAvatar` | `SkeletonAvatar.kt` | Matches ArcaneAvatar layout |
| `ArcaneEmptyState` | `EmptyState.kt` | Flexible empty content container |

**Location:** `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/feedback/`

---

## Component Specifications

### 1. ArcaneModal

Generic container for any modal content.

```kotlin
@Composable
fun ArcaneModal(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    dismissOnBackdropClick: Boolean = true,
    dismissOnBackPress: Boolean = true,
    content: @Composable () -> Unit
)
```

**Visual Design:**
- Dark semi-transparent backdrop with subtle glow effect
- Content renders in centered `ArcaneSurface(variant = Raised)`
- Fade + scale animation on enter/exit
- Handles back button on Android

---

### 2. ArcaneConfirmationDialog

Pre-built dialog for confirm/delete scenarios.

```kotlin
@Composable
fun ArcaneConfirmationDialog(
    visible: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    icon: (@Composable () -> Unit)? = null,
    confirmText: String = "Confirm",
    cancelText: String = "Cancel",
    style: ArcaneConfirmationStyle = ArcaneConfirmationStyle.Default
)

sealed class ArcaneConfirmationStyle {
    data object Default : ArcaneConfirmationStyle()      // Primary confirm button
    data object Destructive : ArcaneConfirmationStyle()  // Error-colored confirm
}
```

**Visual Design:**
- Uses `ArcaneModal` internally
- Icon centered above title (like reference "Delete Item?" with warning icon)
- Cancel = Secondary button, Confirm = Primary (or Error for Destructive)

---

### 3. Toast System

**Data Classes:**

```kotlin
@Immutable
data class ArcaneToastData(
    val id: Long = System.currentTimeMillis(),
    val message: String,
    val style: ArcaneToastStyle = ArcaneToastStyle.Default,
    val durationMs: Long = 4000L
)

sealed class ArcaneToastStyle {
    data object Default : ArcaneToastStyle()   // Primary border/accent
    data object Success : ArcaneToastStyle()   // Green accent
    data object Warning : ArcaneToastStyle()   // Orange accent
    data object Error : ArcaneToastStyle()     // Red accent
}

enum class ArcaneToastPosition {
    TopStart, TopCenter, TopEnd,
    BottomStart, BottomCenter, BottomEnd
}
```

**State Holder:**

```kotlin
class ArcaneToastState {
    fun show(message: String, style: ArcaneToastStyle = Default)
    fun show(toast: ArcaneToastData)
    fun dismiss(id: Long)
    fun dismissAll()
}

val LocalArcaneToastState = compositionLocalOf<ArcaneToastState> { ... }
```

**Host Composable:**

```kotlin
@Composable
fun ArcaneToastHost(
    state: ArcaneToastState,
    modifier: Modifier = Modifier,
    position: ArcaneToastPosition = ArcaneToastPosition.BottomCenter
)
```

**Behavior:**
- Configurable position (default: BottomCenter)
- Always auto-dismiss after duration (default 4 seconds)
- Manual dismiss via X button
- New toasts slide in, auto-dismiss with fade out
- Max 3 visible, older ones pushed out

---

### 4. ArcaneAlertBanner

Inline persistent alerts with semantic variants.

```kotlin
@Composable
fun ArcaneAlertBanner(
    message: String,
    modifier: Modifier = Modifier,
    style: ArcaneAlertStyle = ArcaneAlertStyle.Info,
    icon: (@Composable () -> Unit)? = null,  // Default icon per style if null
    onDismiss: (() -> Unit)? = null,         // Shows X button if provided
    action: ArcaneAlertAction? = null
)

@Immutable
data class ArcaneAlertAction(
    val label: String,
    val onClick: () -> Unit
)

sealed class ArcaneAlertStyle {
    data object Info : ArcaneAlertStyle()      // Primary teal
    data object Success : ArcaneAlertStyle()   // Green
    data object Warning : ArcaneAlertStyle()   // Orange
    data object Error : ArcaneAlertStyle()     // Red
}
```

**Visual Design:**
- Full-width horizontal banner
- Left: Icon (default per style - info circle, checkmark, warning triangle, error circle)
- Center: Message text
- Right: Optional action button + optional dismiss X
- Background: Style color at low opacity (10-15%)
- Left border accent: 3dp solid style color
- Uses `ArcaneSurface(variant = Inset)` as base

---

### 5. ArcaneCircularProgress

Determinate ring indicator.

```kotlin
@Composable
fun ArcaneCircularProgress(
    progress: Float,  // 0f to 1f
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    strokeWidth: Dp = 4.dp,
    trackColor: Color = ArcaneTheme.colors.surfaceInset,
    progressColor: Color = ArcaneTheme.colors.primary,
    showLabel: Boolean = false  // Shows percentage in center
)
```

**Visual Design:**
- Rounded caps on progress arc
- Subtle glow on progress color
- Track (background) uses inset color

---

### 6. ArcaneLinearProgress

Determinate horizontal bar.

```kotlin
@Composable
fun ArcaneLinearProgress(
    progress: Float,  // 0f to 1f
    modifier: Modifier = Modifier,
    height: Dp = 8.dp,
    trackColor: Color = ArcaneTheme.colors.surfaceInset,
    progressColor: Color = ArcaneTheme.colors.primary
)
```

**Visual Design:**
- Rounded caps on progress bar
- Subtle glow on progress color
- Full-width by default (fillMaxWidth)

---

### 7. ArcaneSpinner

Indeterminate loading animation.

```kotlin
@Composable
fun ArcaneSpinner(
    modifier: Modifier = Modifier,
    size: ArcaneSpinnerSize = ArcaneSpinnerSize.Medium,
    color: Color = ArcaneTheme.colors.primary
)

sealed class ArcaneSpinnerSize(val dp: Dp) {
    data object Small : ArcaneSpinnerSize(16.dp)   // Inline/button use
    data object Medium : ArcaneSpinnerSize(24.dp)  // Default
    data object Large : ArcaneSpinnerSize(48.dp)   // Centered loading
}
```

**Visual Design:**
- Rotating arc (270° sweep) with fade gradient tail
- Smooth infinite rotation animation

---

### 8. ArcaneSkeleton

Base skeleton shapes for loading placeholders.

```kotlin
@Composable
fun ArcaneSkeleton(
    modifier: Modifier = Modifier,
    shape: ArcaneSkeletonShape = ArcaneSkeletonShape.Text()
)

sealed class ArcaneSkeletonShape {
    data class Text(val lines: Int = 1, val lastLineWidth: Float = 0.7f) : ArcaneSkeletonShape()
    data class Circle(val size: Dp = 40.dp) : ArcaneSkeletonShape()
    data class Rectangle(val width: Dp = Dp.Unspecified, val height: Dp = 100.dp, val radius: Dp = ArcaneRadius.Medium) : ArcaneSkeletonShape()
}
```

**Visual Design:**
- Base color: `colors.surfaceInset`
- Shimmer animation: Gradient sweep left-to-right using `colors.surfaceRaised`
- Animation: Infinite, 1.5s duration, ease-in-out

---

### 9. Component-Specific Skeletons

```kotlin
@Composable
fun ArcaneSkeletonListItem(
    modifier: Modifier = Modifier,
    showLeadingIcon: Boolean = true,
    showTrailingContent: Boolean = false
)

@Composable
fun ArcaneSkeletonCard(
    modifier: Modifier = Modifier,
    showImage: Boolean = true,
    showActions: Boolean = true
)

@Composable
fun ArcaneSkeletonAvatar(
    modifier: Modifier = Modifier,
    size: ArcaneAvatarSize = ArcaneAvatarSize.Medium
)
```

**Design Principle:**
- Match exact dimensions/spacing of corresponding real components
- Drop-in replacements while data is loading

---

### 10. ArcaneEmptyState

Flexible container for empty content scenarios.

```kotlin
@Composable
fun ArcaneEmptyState(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable ColumnScope.() -> Unit
)
```

**Visual Design:**
- Centered column layout by default
- Uses `ArcaneSurface(variant = Inset)` with dashed border (`colors.border` at 50% opacity)
- Default padding: `ArcaneSpacing.XLarge` (32.dp)
- Content uses `colors.textSecondary` for muted appearance

**Example Usage:**

```kotlin
ArcaneEmptyState {
    Icon(
        imageVector = Icons.Outlined.CloudOff,
        contentDescription = null,
        modifier = Modifier.size(64.dp),
        tint = ArcaneTheme.colors.textDisabled
    )
    Spacer(Modifier.height(ArcaneSpacing.Medium))
    Text(
        text = "No items found.",
        style = ArcaneTheme.typography.headlineMedium,
        color = ArcaneTheme.colors.text
    )
    Spacer(Modifier.height(ArcaneSpacing.XSmall))
    Text(
        text = "Start by adding a new project.",
        style = ArcaneTheme.typography.bodyMedium,
        color = ArcaneTheme.colors.textSecondary
    )
    Spacer(Modifier.height(ArcaneSpacing.Large))
    ArcaneButton(onClick = { /* add */ }) {
        Text("Add Project")
    }
}
```

---

## Catalog Integration

**New Screen:** `FeedbackScreen.kt`

Sections:
- **Modals** - Buttons to trigger ConfirmationDialog (default + destructive)
- **Toasts** - Buttons to show each toast style
- **Alert Banners** - All 4 variants displayed, some with actions
- **Progress** - Circular + Linear at various percentages
- **Spinner** - All 3 sizes side-by-side
- **Skeletons** - Base shapes + component-specific variants
- **Empty State** - Example with icon, text, and action button

**App.kt:** Add "Feedback" tab alongside existing Controls, Navigation, Data Display tabs.

---

## Implementation Notes

### Patterns to Follow

All components should follow established patterns from Phases 1-4:

1. **Sealed classes** for style variants with `@Immutable` annotation
2. **Theme access** via `ArcaneTheme.colors` and `ArcaneTheme.typography`
3. **Token usage** for spacing, radius, borders
4. **Glow effects** using `animateFloatAsState` with `tween(150)`
5. **Interaction handling** via `MutableInteractionSource`

### Animation Specs

| Animation | Spec |
|-----------|------|
| Modal enter/exit | `tween(200)` fade + scale |
| Toast slide | `tween(300)` with `FastOutSlowIn` |
| Toast fade out | `tween(200)` |
| Spinner rotation | `infiniteRepeatable`, `linearEasing`, 1s duration |
| Skeleton shimmer | `infiniteRepeatable`, `tween(1500)`, ease-in-out |
| Progress changes | `animateFloatAsState`, `tween(300)` |

### Accessibility

- Modal: Focus trap, announce title on open
- Toast: `LiveRegion.Polite` for screen reader announcements
- Alert Banner: Semantic role based on style (warning, error, etc.)
- Progress: `progressSemantics()` modifier with current value
- Spinner: "Loading" content description

---

## Summary

**Total Component Count After Phase 5:**

| Category | Components |
|----------|------------|
| Foundation | Tokens, Theme, Surface |
| Controls | 6 |
| Navigation | 4 |
| Data Display | 7 |
| **Feedback** | **12** |

**Grand Total: 29+ components**
