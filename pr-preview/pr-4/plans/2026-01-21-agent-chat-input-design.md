# AgentChatInput Component - Design Specification

## Overview

A specialized chat input component for AI assistant conversations. Features text input with auto-expand, voice capabilities (transcription and audio recording), and flexible slots for contextual actions like attachments and history access.

**Component:** `ArcaneAgentChatInput`

**Location:** `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/AgentChatInput.kt`

---

## Component Specification

### API Signature

```kotlin
@Composable
fun ArcaneAgentChatInput(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Reply to Claude...",
    enabled: Boolean = true,
    maxLines: Int = 6,
    onVoiceToTextClick: (() -> Unit)? = null,
    onAudioRecordClick: (() -> Unit)? = null,
    addMenuContent: (@Composable () -> Unit)? = null,
    activeItemsContent: (@Composable RowScope.() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
)
```

### Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| `value` | `String` | Current text input value (controlled) |
| `onValueChange` | `(String) -> Unit` | Callback when text changes |
| `onSend` | `() -> Unit` | Callback when user sends message |
| `modifier` | `Modifier` | Standard Compose modifier |
| `placeholder` | `String` | Placeholder text when empty |
| `enabled` | `Boolean` | Enable/disable entire component |
| `maxLines` | `Int` | Maximum lines before scrolling (default 6) |
| `onVoiceToTextClick` | `(() -> Unit)?` | Voice-to-text button callback. Hidden when null |
| `onAudioRecordClick` | `(() -> Unit)?` | Audio recording button callback. Hidden when null |
| `addMenuContent` | `(@Composable () -> Unit)?` | Slot for + button menu content. Button hidden when null |
| `activeItemsContent` | `(@Composable RowScope.() -> Unit)?` | Slot for active items (chips) row |
| `interactionSource` | `MutableInteractionSource` | For tracking focus state |

---

## Visual Design

### Layout Structure

```
┌─────────────────────────────────────────────────────────┐
│  "Reply to Claude..."                                   │  ← Row 1: Text Input
├─────────────────────────────────────────────────────────┤
│  [+]  [chip] [chip]                     [🎤] [🎙️]      │  ← Row 2: Actions (empty state)
│  [+]  [chip] [chip]                           [➤]      │  ← Row 2: Actions (with text)
└─────────────────────────────────────────────────────────┘
```

### Row 1: Text Input Area

- **Background:** `surfaceInset` (`#0F1219`)
- **Corner radius:** `ArcaneRadius.Medium` (8.dp)
- **Padding:** `ArcaneSpacing.Medium` (16.dp) horizontal, `ArcaneSpacing.Small` (12.dp) vertical
- **Typography:** `typography.bodyLarge` (16sp)
- **Placeholder color:** `textSecondary`
- **Behavior:** Single-line, auto-expands up to `maxLines`

### Row 2: Actions Bar

- **Left side:** Add button (+) followed by `activeItemsContent` slot
- **Right side:** Contextual buttons (voice OR send)
- **Horizontal padding:** `ArcaneSpacing.XSmall` (8.dp)
- **Gap between rows:** `ArcaneSpacing.XSmall` (8.dp)

### Overall Container

- **Background:** Transparent (parent controls surface)
- **Corner radius:** `ArcaneRadius.Large` (12.dp)
- **Border:** `ArcaneBorder.Thin` (1.dp) with `colors.border`

---

## Action Buttons

### Add Button (+)

- **Size:** 40.dp touch target
- **Icon size:** 20.dp
- **Icon color:** `textSecondary` (default), `primary` (pressed)
- **Behavior:** Triggers parent's menu via `addMenuContent` slot
- **Visibility:** Hidden when `addMenuContent` is null

### Voice-to-Text Button (Microphone)

- **Size:** 40.dp touch target
- **Icon size:** 20.dp
- **Icon color:** `textSecondary`
- **Visibility:** Shown when `value` is empty AND `onVoiceToTextClick` is not null

### Audio Record Button (Waveform)

- **Size:** 40.dp touch target
- **Icon size:** 20.dp
- **Icon color:** `textSecondary`
- **Visibility:** Shown when `value` is empty AND `onAudioRecordClick` is not null

### Send Button

- **Size:** 40.dp touch target
- **Background:** `primary` (`#8B5CF6`)
- **Icon:** Arrow/send symbol, `colors.text` (white)
- **Visibility:** Shown when `value.isNotBlank()`, replaces both voice buttons
- **Animation:** `AnimatedVisibility` with fade + scale, 150ms

---

## States & Interactions

### Focus State

- **Unfocused:** Border `colors.border` (purple at 40% alpha)
- **Focused:** Border `colors.borderFocused` (solid purple `#8B5CF6`)
- **Transition:** `animateColorAsState` with `tween(150)`
- **Optional:** Subtle glow effect using `colors.glow`

### Disabled State

- All buttons non-interactive
- Colors shift to `textDisabled` with reduced alpha
- Input field not focusable

### Keyboard Handling

- **Enter/Return:** Calls `onSend()` if `value.isNotBlank()`
- **Shift+Enter:** Inserts newline
- **ImeAction:** `ImeAction.Send`

### Auto-Expand Behavior

- Starts at single line height
- Grows with content up to `maxLines`
- `softWrap = true`
- Constrain via `Modifier.heightIn(max = calculatedMaxHeight)`

---

## Accessibility

| Element | Content Description |
|---------|---------------------|
| Input field | Uses placeholder or custom description |
| Add button | "Add attachment or context" |
| Voice-to-text | "Voice to text" |
| Audio record | "Record audio message" |
| Send button | "Send message" |

---

## Active Items Slot

The `activeItemsContent` slot allows parents to render contextual chips. Recommended chip styling:

- **Background:** `surfacePressed`
- **Shape:** `ArcaneRadius.Full` (pill)
- **Content:** Icon + optional label + dismiss X
- **Spacing:** `ArcaneSpacing.XSmall` (8.dp) gaps

Example usage:
```kotlin
ArcaneAgentChatInput(
    // ...
    activeItemsContent = {
        ArcaneChip(
            icon = Icons.Clock,
            label = "Recent",
            onDismiss = { /* remove */ }
        )
        ArcaneChip(
            icon = Icons.File,
            label = "document.pdf",
            onDismiss = { /* remove */ }
        )
    }
)
```

---

## Catalog Integration

Add showcase to `ControlsScreen.kt` with these demo states:

1. **Empty state** - Shows placeholder and voice buttons
2. **With text** - Shows send button
3. **With active chips** - Shows history/attachment chips
4. **Disabled** - Grayed out state

---

## Not Included (YAGNI)

- Built-in menu for + button (slot-based instead)
- Loading/spinner states (parent responsibility)
- Error validation or character limits
- Typing indicators
- Message history management
- Built-in chip components (parent provides via slot)
