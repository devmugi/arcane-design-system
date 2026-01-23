# ArcaneDesignSystem Component Mapping

**Rule: Always use Arcane components instead of raw Material 3.**

If you need M3 directly, justify it in a comment explaining why Arcane doesn't cover the use case.

## Controls

| Material 3 | Arcane | Notes |
|------------|--------|-------|
| `Button` | `ArcaneButton` | Styles: `Filled`, `Tonal`, `Outlined`, `Elevated`, `Text` |
| `TextField` | `ArcaneTextField` | Has `label`, `placeholder`, `helperText`, `errorText`, `isPassword` |
| `Switch` | `ArcaneSwitch` | - |
| `Checkbox` | `ArcaneCheckbox` | - |
| `RadioButton` | `ArcaneRadioButton` | - |
| `Slider` | `ArcaneSlider` | - |

### ArcaneButton Styles

```kotlin
// Primary filled button (default)
ArcaneButton(onClick = {}) { Text("Click") }

// With explicit style
ArcaneButton(
    onClick = {},
    style = ArcaneButtonStyle.Outlined()
) { Text("Outlined") }

// Available styles:
// - ArcaneButtonStyle.Filled(containerColor?)
// - ArcaneButtonStyle.Tonal(containerColor?)
// - ArcaneButtonStyle.Outlined(borderColor?)
// - ArcaneButtonStyle.Elevated(containerColor?)
// - ArcaneButtonStyle.Text
```

## Display

| Material 3 | Arcane | Notes |
|------------|--------|-------|
| `Card` | `ArcaneCard` | - |
| `Surface` | `ArcaneSurface` | Use `SurfaceVariant` enum |
| `ListItem` | `ArcaneListItem` | - |
| `Badge` | `ArcaneBadge` | - |
| `Text` | `ArcaneText` | Or use M3 `Text` with `ArcaneTheme.typography` |
| - | `ArcaneAvatar` | No M3 equivalent |
| - | `ArcaneTable` | No M3 equivalent |
| - | `ArcaneTooltip` | Enhanced tooltip |

### ArcaneSurface Variants

```kotlin
// Standard card surface
ArcaneSurface(variant = SurfaceVariant.Container) { }

// Inset/depressed area
ArcaneSurface(variant = SurfaceVariant.ContainerLowest) { }

// Base level
ArcaneSurface(variant = SurfaceVariant.ContainerLow) { }

// Modal overlay
ArcaneSurface(variant = SurfaceVariant.ContainerHigh) { }

// Dialog (highest emphasis)
ArcaneSurface(variant = SurfaceVariant.ContainerHighest) { }
```

## Navigation

| Material 3 | Arcane | Notes |
|------------|--------|-------|
| `TabRow` | `ArcaneTabs` | - |
| - | `ArcaneBreadcrumbs` | No M3 equivalent |
| - | `ArcanePagination` | No M3 equivalent |
| - | `ArcaneStepper` | No M3 equivalent |

## Feedback

| Material 3 | Arcane | Notes |
|------------|--------|-------|
| `CircularProgressIndicator` | `ArcaneSpinner` | Indeterminate loading |
| `CircularProgressIndicator` | `ArcaneCircularProgress` | Determinate progress |
| `LinearProgressIndicator` | `ArcaneLinearProgress` | - |
| `AlertDialog` | `ArcaneConfirmationDialog` | For confirm/cancel actions |
| `AlertDialog` | `ArcaneModal` | For custom content |
| `Snackbar` | `ArcaneToast` | - |
| - | `ArcaneAlertBanner` | Inline alert (no M3 equivalent) |
| - | `ArcaneSkeleton` | Loading placeholder |
| - | `ArcaneSkeletonCard` | Card loading placeholder |
| - | `ArcaneSkeletonListItem` | List item loading placeholder |
| - | `ArcaneSkeletonAvatar` | Avatar loading placeholder |
| `DropdownMenu` | `ArcaneDropdownMenu` | - |
| - | `ArcaneEmptyState` | Empty content placeholder |

## Chat Components (arcane-chat module)

| Purpose | Component | Notes |
|---------|-----------|-------|
| User message bubble | `ArcaneUserMessageBlock` | Right-aligned, primary tint |
| Assistant message bubble | `ArcaneAssistantMessageBlock` | Left-aligned, expandable |
| Message list | `ArcaneChatMessageList` | LazyColumn with auto-scroll |
| Chat input | `ArcaneAgentChatInput` | Multi-row with voice/send buttons |
| Screen layout | `ArcaneChatScreenScaffold` | Scaffold with bottom input slot |
| Suggestion chip | `SuggestionChip` | Animated selection state |
| Suggestions block | `AgentSuggestionsBlockView` | FlowRow of expandable chips |

### Chat Message Models

```kotlin
// User message
ChatMessage.User(
    blocks = listOf(MessageBlock.Text("Hello")),
    timestamp = Clock.System.now()
)

// Assistant message
ChatMessage.Assistant(
    title = "AI Assistant",
    blocks = listOf(
        MessageBlock.Text("Here's my response"),
        MessageBlock.AgentSuggestions(suggestions = listOf(...))
    ),
    isLoading = false
)

// Block types:
// - MessageBlock.Text(text, style: TextStyle)
// - MessageBlock.Image(url, altText?, aspectRatio?)
// - MessageBlock.AgentSuggestions(suggestions, onSuggestionClick)
// - MessageBlock.Custom(content: @Composable)
```

## Theme & Tokens

### Colors

```kotlin
val colors = ArcaneTheme.colors

colors.primary           // Main accent (purple)
colors.text              // Primary text
colors.textSecondary     // Secondary text
colors.textDisabled      // Disabled text
colors.border            // Default border
colors.borderFocused     // Focused input border
colors.error             // Error state
colors.success           // Success state
colors.warning           // Warning state

// Surfaces (darkest to lightest):
colors.surfaceContainerLowest
colors.surfaceContainerLow
colors.surfaceContainer
colors.surfaceContainerHigh
colors.surfaceContainerHighest

// State layers (use with .copy(alpha = ...)):
colors.stateLayerHover   // 8%
colors.stateLayerPressed // 12%
colors.stateLayerFocus   // 12%
colors.stateLayerDragged // 16%
```

### Typography

```kotlin
val typography = ArcaneTheme.typography

typography.headlineLarge
typography.headlineMedium
typography.headlineSmall
typography.titleLarge
typography.titleMedium
typography.titleSmall
typography.bodyLarge
typography.bodyMedium
typography.bodySmall
typography.labelLarge
typography.labelMedium
typography.labelSmall
```

### Spacing

```kotlin
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

ArcaneSpacing.xs   // 4.dp
ArcaneSpacing.sm   // 8.dp
ArcaneSpacing.md   // 16.dp
ArcaneSpacing.lg   // 24.dp
ArcaneSpacing.xl   // 32.dp
ArcaneSpacing.xxl  // 48.dp
```

### Radius

```kotlin
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius

ArcaneRadius.sm    // 4.dp
ArcaneRadius.md    // 8.dp
ArcaneRadius.lg    // 12.dp
ArcaneRadius.xl    // 16.dp
ArcaneRadius.full  // 9999.dp (pill shape)
```

### Border

```kotlin
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneBorder

ArcaneBorder.thin   // 1.dp
ArcaneBorder.medium // 2.dp
ArcaneBorder.thick  // 4.dp
```

## Imports Reference

```kotlin
// Foundation
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.primitives.ArcaneSurface
import io.github.devmugi.arcane.design.foundation.primitives.SurfaceVariant
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneBorder

// Controls
import io.github.devmugi.arcane.design.components.controls.ArcaneButton
import io.github.devmugi.arcane.design.components.controls.ArcaneButtonStyle
import io.github.devmugi.arcane.design.components.controls.ArcaneTextField
import io.github.devmugi.arcane.design.components.controls.ArcaneSwitch
import io.github.devmugi.arcane.design.components.controls.ArcaneCheckbox
import io.github.devmugi.arcane.design.components.controls.ArcaneRadioButton
import io.github.devmugi.arcane.design.components.controls.ArcaneSlider

// Display
import io.github.devmugi.arcane.design.components.display.ArcaneCard
import io.github.devmugi.arcane.design.components.display.ArcaneListItem
import io.github.devmugi.arcane.design.components.display.ArcaneBadge
import io.github.devmugi.arcane.design.components.display.ArcaneAvatar
import io.github.devmugi.arcane.design.components.display.ArcaneTable
import io.github.devmugi.arcane.design.components.display.ArcaneTooltip

// Navigation
import io.github.devmugi.arcane.design.components.navigation.ArcaneTabs
import io.github.devmugi.arcane.design.components.navigation.ArcaneBreadcrumbs
import io.github.devmugi.arcane.design.components.navigation.ArcanePagination
import io.github.devmugi.arcane.design.components.navigation.ArcaneStepper

// Feedback
import io.github.devmugi.arcane.design.components.feedback.ArcaneSpinner
import io.github.devmugi.arcane.design.components.feedback.ArcaneCircularProgress
import io.github.devmugi.arcane.design.components.feedback.ArcaneLinearProgress
import io.github.devmugi.arcane.design.components.feedback.ArcaneToast
import io.github.devmugi.arcane.design.components.feedback.ArcaneAlertBanner
import io.github.devmugi.arcane.design.components.feedback.ArcaneModal
import io.github.devmugi.arcane.design.components.feedback.ArcaneConfirmationDialog
import io.github.devmugi.arcane.design.components.feedback.ArcaneDropdownMenu
import io.github.devmugi.arcane.design.components.feedback.ArcaneEmptyState
import io.github.devmugi.arcane.design.components.feedback.ArcaneSkeleton
import io.github.devmugi.arcane.design.components.feedback.ArcaneSkeletonCard
import io.github.devmugi.arcane.design.components.feedback.ArcaneSkeletonListItem
import io.github.devmugi.arcane.design.components.feedback.ArcaneSkeletonAvatar

// Chat (requires arcane-chat module)
import io.github.devmugi.arcane.chat.models.ChatMessage
import io.github.devmugi.arcane.chat.models.MessageBlock
import io.github.devmugi.arcane.chat.models.Suggestion
import io.github.devmugi.arcane.chat.components.messages.ArcaneUserMessageBlock
import io.github.devmugi.arcane.chat.components.messages.ArcaneAssistantMessageBlock
import io.github.devmugi.arcane.chat.components.messages.ArcaneChatMessageList
import io.github.devmugi.arcane.chat.components.input.ArcaneAgentChatInput
import io.github.devmugi.arcane.chat.components.scaffold.ArcaneChatScreenScaffold
```
