# Chat Screen Components Design

## Overview

Design for AI assistant chat UI components in the Arcane Design System. These components provide a complete chat interface with scaffold, message list, user messages, and assistant messages with truncation/expansion support.

## Components

### 1. ArcaneChatScreenScaffold

Top-level container that switches between empty and conversation states.

**File:** `arcane-components/.../controls/ChatScreenScaffold.kt`

```kotlin
@Composable
fun ArcaneChatScreenScaffold(
    isEmpty: Boolean,
    modifier: Modifier = Modifier,
    emptyState: @Composable () -> Unit,
    content: @Composable () -> Unit
)
```

**Behavior:**
- `isEmpty = true` → renders `emptyState` slot centered
- `isEmpty = false` → renders `content` slot filling available space
- Crossfade animation between states (150ms)
- No opinions on padding/scrolling - pure layout container

---

### 2. ArcaneChatMessageList

Recommended companion component for scrollable message lists.

**File:** `arcane-components/.../controls/ChatMessageList.kt`

```kotlin
@Composable
fun <T> ArcaneChatMessageList(
    messages: List<T>,
    modifier: Modifier = Modifier,
    reverseLayout: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(ArcaneSpacing.Medium),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(ArcaneSpacing.Medium),
    showScrollToBottom: Boolean = true,
    onScrollToBottomClick: (() -> Unit)? = null,
    messageContent: @Composable (T) -> Unit
)
```

**Behavior:**
- Wraps `LazyColumn` with message list patterns
- Auto-scrolls to bottom when new messages added (if already at bottom)
- Shows floating "scroll to bottom" button when user scrolls up
- Generic `<T>` allows any message type

**Scroll-to-bottom FAB:**
- Small circular button with down-arrow icon
- Bottom-center position
- Fades in/out with 150ms animation
- `ArcaneTheme.colors.surfaceRaised` background

---

### 3. ArcaneUserMessageBlock

Right-aligned chat bubble for user messages.

**File:** `arcane-components/.../controls/UserMessageBlock.kt`

```kotlin
@Composable
fun ArcaneUserMessageBlock(
    text: String,
    modifier: Modifier = Modifier,
    timestamp: String? = null
)
```

**Visual design:**
- Right-aligned within parent
- Background: `ArcaneTheme.colors.primary.copy(alpha = 0.15f)`
- Corner radius: 12.dp with top-right squared off (4.dp)
- Padding: 12.dp horizontal, 8.dp vertical
- Text: `typography.bodyMedium`, `colors.text`
- Optional timestamp: `typography.labelSmall`, `colors.textSecondary`
- Max width: ~75% of parent

**Shape:**
```kotlin
RoundedCornerShape(
    topStart = 12.dp,
    topEnd = 4.dp,
    bottomStart = 12.dp,
    bottomEnd = 12.dp
)
```

---

### 4. ArcaneAssistantMessageBlock

Left-aligned message block with title, content, and actions.

**File:** `arcane-components/.../controls/AssistantMessageBlock.kt`

```kotlin
@Composable
fun ArcaneAssistantMessageBlock(
    text: String,
    modifier: Modifier = Modifier,
    title: String? = null,
    isLoading: Boolean = false,
    maxContentHeight: Dp = 160.dp,
    showBottomActions: Boolean = false,
    autoShowWhenTruncated: Boolean = true,
    onCopyClick: (() -> Unit)? = null,
    onShowMoreClick: (() -> Unit)? = null,
    titleActions: @Composable (RowScope.() -> Unit)? = null,
    bottomActions: @Composable (RowScope.() -> Unit)? = null
)
```

**Visual structure:**

1. **Title row** (shown if `title != null` or `isLoading`):
   - Left: Loading indicator (circular, if `isLoading`) + title text
   - Right: Built-in copy icon + `titleActions` slot
   - Height: ~40.dp

2. **Message content:**
   - Text constrained to `maxContentHeight`
   - Fade-out gradient at bottom when truncated
   - `typography.bodyMedium`, `colors.text`

3. **Bottom actions row** (conditional):
   - Shown if `showBottomActions == true` OR (truncated AND `autoShowWhenTruncated`)
   - Default "Show more" action + `bottomActions` slot

**Expansion behavior:**
- Internal state: `isExpanded`, `isTruncated`
- When `onShowMoreClick == null`: toggles expansion internally
- When `onShowMoreClick` provided: calls callback, consumer handles
- Smooth height animation via `animateContentSize()`

**Fade gradient:**
```kotlin
Modifier.drawWithContent {
    drawContent()
    if (isTruncated && !isExpanded) {
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color.Transparent, ArcaneTheme.colors.surface),
                startY = size.height * 0.7f
            )
        )
    }
}
```

---

## Showcase Screen

**File:** `catalog/.../screens/ChatScreen.kt`

**Navigation:** Add `data object Chat : Screen()` to sealed class

**Sections:**

1. **Empty State**
   - ChatScreenScaffold with empty state slot demo

2. **User Messages**
   - Short message
   - Multi-line message

3. **Assistant Messages - Fits**
   - With title, no loading
   - With title + loading indicator
   - With title actions (copy + custom icon)

4. **Assistant Messages - Truncated**
   - Shows fade gradient + "Show more" action
   - Demonstrates inline expansion on click

5. **Assistant Messages - Custom Actions**
   - "Show more" + "Share" icon
   - Custom action buttons

6. **Full Conversation**
   - ArcaneChatMessageList with mixed messages
   - Demonstrates scroll behavior and FAB

---

## Design Decisions

| Decision | Choice | Rationale |
|----------|--------|-----------|
| Default max height | 160.dp | ~6-7 lines, balanced |
| Expansion behavior | Inline by default | Simpler UX, callback for custom |
| Copy action | Built-in | Almost always needed |
| Bottom actions visibility | Boolean flags | Fine-grained control |
| Animation duration | 150ms | Consistent with design system |
| User message alignment | Right | Standard chat pattern |
| Assistant message style | No bubble | Cleaner for long content |

---

## File Locations

```
arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/
├── AgentChatInput.kt           (existing)
├── ChatScreenScaffold.kt       (new)
├── ChatMessageList.kt          (new)
├── UserMessageBlock.kt         (new)
└── AssistantMessageBlock.kt    (new)

catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/
├── App.kt                      (add Chat screen to navigation)
└── screens/
    └── ChatScreen.kt           (new)
```
