# Chat Module Extraction Design

**Date:** 2026-01-22
**Status:** Approved

## Overview

Extract all chat-related components from `arcane-components` into a new `arcane-chat` module for better modularity and faster prototyping. Create a dedicated `catalog-chat` application with device preview capabilities, theme switching, and persistent state management.

## Goals

1. Separate chat components into dedicated `arcane-chat` module
2. Create `catalog-chat` app for rapid chat UI prototyping
3. Implement device preview (Pixel 8, iPhone 16) with toggle
4. Add theme switching with persistent state
5. Provide comprehensive mock data for testing

## Module Structure

### arcane-chat

New library module containing all chat components:

```
arcane-chat/
├── build.gradle.kts (uses arcane.multiplatform.library convention)
├── src/commonMain/kotlin/io/github/devmugi/arcane/chat/
│   ├── components/
│   │   ├── messages/
│   │   │   ├── ArcaneUserMessageBlock.kt
│   │   │   ├── ArcaneAssistantMessageBlock.kt
│   │   │   └── ArcaneChatMessageList.kt
│   │   ├── scaffold/
│   │   │   └── ArcaneChatScreenScaffold.kt
│   │   └── input/
│   │       └── (future: chat input components)
│   └── models/
│       └── ChatMessage.kt
```

**Dependencies:**
- `arcane-foundation` - Theme, colors, typography, tokens
- `arcane-components` - Basic controls (buttons, textfields, surfaces)

**Components to migrate:**
- `ArcaneUserMessageBlock.kt` (from arcane-components/controls/)
- `ArcaneAssistantMessageBlock.kt` (from arcane-components/controls/)
- `ArcaneChatMessageList.kt` (from arcane-components/controls/)
- `ArcaneChatScreenScaffold.kt` (from arcane-components/controls/)

### catalog-chat

New catalog application for chat prototyping:

```
catalog-chat/
├── build.gradle.kts (uses arcane.multiplatform.application convention)
├── composeApp/
│   ├── src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/
│   │   ├── App.kt
│   │   ├── navigation/
│   │   │   └── ChatCatalogNavigation.kt
│   │   ├── screens/
│   │   │   ├── ChatScreen.kt
│   │   │   ├── MessageBlocksScreen.kt
│   │   │   └── ChatInputScreen.kt
│   │   ├── components/
│   │   │   ├── DevicePreview.kt
│   │   │   ├── CatalogTopBar.kt
│   │   │   ├── DeviceSelector.kt
│   │   │   └── ThemeToggleButton.kt
│   │   └── data/
│   │       └── MockChatData.kt
```

**Dependencies:**
- `arcane-chat` - All chat components
- `arcane-components` - Basic controls
- `arcane-foundation` - Theme system

## Device Preview Architecture

### DeviceType Enum

```kotlin
enum class DeviceType {
    None,      // No device frame, content displayed directly
    Pixel8,    // Google Pixel 8 (412x915dp, 32dp corners, punch-hole)
    iPhone16   // Apple iPhone 16 (393x852dp, 55dp corners, Dynamic Island)
}
```

### DevicePreview Component

```kotlin
@Composable
fun DevicePreview(
    deviceType: DeviceType,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    when (deviceType) {
        DeviceType.None -> content()
        DeviceType.Pixel8 -> DeviceFrame(
            screenWidth = 412.dp,
            screenHeight = 915.dp,
            cornerRadius = 32.dp,
            notchType = NotchType.PunchHole(centerX = 206.dp, y = 24.dp),
            content = content
        )
        DeviceType.iPhone16 -> DeviceFrame(
            screenWidth = 393.dp,
            screenHeight = 852.dp,
            cornerRadius = 55.dp,
            notchType = NotchType.DynamicIsland(centerX = 196.5.dp, y = 20.dp),
            content = content
        )
    }
}
```

### DeviceFrame Implementation

```kotlin
sealed class NotchType {
    data class PunchHole(val centerX: Dp, val y: Dp, val radius: Dp = 6.dp) : NotchType()
    data class DynamicIsland(val centerX: Dp, val y: Dp, val width: Dp = 126.dp, val height: Dp = 37.dp) : NotchType()
}

@Composable
private fun DeviceFrame(
    screenWidth: Dp,
    screenHeight: Dp,
    cornerRadius: Dp,
    notchType: NotchType,
    content: @Composable () -> Unit
) {
    // Implementation:
    // 1. Box with device dimensions + bezel padding
    // 2. ArcaneSurface with elevation for device shadow
    // 3. Clip content to rounded corners
    // 4. Overlay notch/dynamic island on top
}
```

### Screen Integration

Every catalog screen wraps its content in `DevicePreview`:

```kotlin
@Composable
fun ChatScreen(deviceType: DeviceType) {
    DevicePreview(deviceType = deviceType) {
        // Actual chat UI renders here
        ArcaneChatScreenScaffold(...)
    }
}
```

## App Structure & Navigation

### Root Composable (App.kt)

```kotlin
@Composable
fun App() {
    var isDarkTheme by rememberSaveable { mutableStateOf(false) }
    var deviceType by rememberSaveable(stateSaver = DeviceTypeSaver) {
        mutableStateOf(DeviceType.None)
    }
    var selectedTab by rememberSaveable(stateSaver = CatalogTabSaver) {
        mutableStateOf(CatalogTab.Chat)
    }

    ArcaneTheme(isDark = isDarkTheme) {
        Column(Modifier.fillMaxSize()) {
            CatalogTopBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                deviceType = deviceType,
                onDeviceTypeSelected = { deviceType = it },
                isDarkTheme = isDarkTheme,
                onThemeToggle = { isDarkTheme = !isDarkTheme }
            )

            when (selectedTab) {
                CatalogTab.Chat -> ChatScreen(deviceType)
                CatalogTab.MessageBlocks -> MessageBlocksScreen(deviceType)
                CatalogTab.ChatInput -> ChatInputScreen(deviceType)
            }
        }
    }
}
```

### CatalogTopBar

```kotlin
enum class CatalogTab {
    Chat,
    MessageBlocks,
    ChatInput
}

@Composable
fun CatalogTopBar(
    selectedTab: CatalogTab,
    onTabSelected: (CatalogTab) -> Unit,
    deviceType: DeviceType,
    onDeviceTypeSelected: (DeviceType) -> Unit,
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(ArcaneTheme.colors.surfaceContainerLow)
            .padding(ArcaneSpacing.Medium),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left side: Tab navigation
        Row(horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)) {
            TabButton(
                text = "Chat",
                selected = selectedTab == CatalogTab.Chat,
                onClick = { onTabSelected(CatalogTab.Chat) }
            )
            TabButton(
                text = "Message Blocks",
                selected = selectedTab == CatalogTab.MessageBlocks,
                onClick = { onTabSelected(CatalogTab.MessageBlocks) }
            )
            TabButton(
                text = "Chat Input",
                selected = selectedTab == CatalogTab.ChatInput,
                onClick = { onTabSelected(CatalogTab.ChatInput) }
            )
        }

        // Right side: Device selector + Theme toggle
        Row(horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)) {
            DeviceSelector(
                deviceType = deviceType,
                onDeviceSelected = onDeviceTypeSelected
            )
            ThemeToggleButton(
                isDark = isDarkTheme,
                onToggle = onThemeToggle
            )
        }
    }
}
```

### DeviceSelector Component

Dropdown menu with three options:

```kotlin
@Composable
fun DeviceSelector(
    deviceType: DeviceType,
    onDeviceSelected: (DeviceType) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        ArcaneButton(
            text = deviceType.displayName,
            style = ArcaneButtonStyle.Outlined(),
            onClick = { expanded = true }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("No Preview") },
                onClick = {
                    onDeviceSelected(DeviceType.None)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Pixel 8") },
                onClick = {
                    onDeviceSelected(DeviceType.Pixel8)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("iPhone 16") },
                onClick = {
                    onDeviceSelected(DeviceType.iPhone16)
                    expanded = false
                }
            )
        }
    }
}
```

### Navigation Strategy

- **Simple when statement** - No navigation library needed for 3 tabs
- **State hoisting** - All state managed in App.kt
- **Stateless screens** - Screens receive state via parameters
- **Persistent selection** - Tab selection survives app restarts

## Theme Switching & State Persistence

### ArcaneTheme Enhancement

Add `isDark` parameter to existing theme:

```kotlin
@Composable
fun ArcaneTheme(
    isDark: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (isDark) ArcaneDarkColors else ArcaneLightColors

    CompositionLocalProvider(
        LocalArcaneColors provides colors,
        LocalArcaneTypography provides ArcaneTypography
    ) {
        content()
    }
}
```

### Color Schemes

**Light mode (existing):**
- `ArcaneLightColors` - Current color palette

**Dark mode (new):**
```kotlin
private val ArcaneDarkColors = ArcaneColors(
    // Surface colors (M3 tonal elevation)
    surfaceContainerLowest = Color(0xFF0F0F1A),   // Darkest
    surfaceContainerLow = Color(0xFF16162A),
    surfaceContainer = Color(0xFF1D1D38),
    surfaceContainerHigh = Color(0xFF242446),
    surfaceContainerHighest = Color(0xFF2B2B54),   // Lightest

    // Deprecated (compatibility)
    surface = Color(0xFF16162A),
    surfaceRaised = Color(0xFF1D1D38),
    surfaceInset = Color(0xFF0F0F1A),

    // Text colors
    text = Color(0xFFE6E6FF),
    textSecondary = Color(0xFFB3B3CC),
    textDisabled = Color(0xFF666680),

    // Primary accent
    primary = Color(0xFFB19EFF),
    primaryVariant = Color(0xFF8B72FF),
    onPrimary = Color(0xFF1A1A2E),

    // Additional colors...
)
```

### State Persistence

Use `rememberSaveable` with custom `Saver` for enums:

```kotlin
// DeviceType saver
private val DeviceTypeSaver = Saver<DeviceType, String>(
    save = { it.name },
    restore = { DeviceType.valueOf(it) }
)

// CatalogTab saver
private val CatalogTabSaver = Saver<CatalogTab, String>(
    save = { it.name },
    restore = { CatalogTab.valueOf(it) }
)

// Usage in App.kt
var deviceType by rememberSaveable(stateSaver = DeviceTypeSaver) {
    mutableStateOf(DeviceType.None)
}
var selectedTab by rememberSaveable(stateSaver = CatalogTabSaver) {
    mutableStateOf(CatalogTab.Chat)
}
var isDarkTheme by rememberSaveable {
    mutableStateOf(false)
}
```

**Benefits:**
- State survives configuration changes (rotation, window resize)
- State survives process death (on Android)
- No external dependencies required

## Mock Data Patterns

### ChatMessage Model

```kotlin
// arcane-chat/src/commonMain/.../models/ChatMessage.kt
sealed class ChatMessage {
    abstract val id: String
    abstract val timestamp: String?

    data class User(
        override val id: String,
        val text: String,
        override val timestamp: String? = null
    ) : ChatMessage()

    data class Assistant(
        override val id: String,
        val title: String = "Claude",
        val content: String,
        val isLoading: Boolean = false,
        override val timestamp: String? = null
    ) : ChatMessage()
}
```

### MockChatData Provider

```kotlin
// catalog-chat/.../data/MockChatData.kt
object MockChatData {
    val sampleConversation = listOf(
        ChatMessage.User(
            id = "1",
            text = "Hello, Claude!",
            timestamp = "2:30 PM"
        ),
        ChatMessage.Assistant(
            id = "2",
            content = "Hello! I'd be happy to help you with that.",
            timestamp = "2:30 PM"
        ),
        ChatMessage.User(
            id = "3",
            text = "Can you help me understand how to implement a chat interface in Compose?",
            timestamp = "2:34 PM"
        ),
        ChatMessage.Assistant(
            id = "4",
            content = """
                I'd be happy to help you understand chat interfaces in Compose! Here are the key concepts:

                1. **LazyColumn for messages**: Use LazyColumn to efficiently render message lists. It only composes visible items.

                2. **State management**: Keep your messages in a ViewModel or state holder. Use mutableStateListOf for reactive updates.

                3. **Message alignment**: User messages typically align right, assistant messages align left.

                4. **Auto-scroll behavior**: When new messages arrive, scroll to bottom if user was already at bottom. Show a "scroll to bottom" button if scrolled up.

                5. **Input handling**: Use BasicTextField with keyboard actions for the input area.

                6. **Loading states**: Show typing indicators or progress when waiting for responses.

                Would you like me to elaborate on any of these points?
            """.trimIndent(),
            timestamp = "2:34 PM"
        )
    )

    val shortConversation = listOf(
        ChatMessage.User(id = "1", text = "Hi!"),
        ChatMessage.Assistant(id = "2", content = "Hello! How can I help?")
    )

    val emptyConversation = emptyList<ChatMessage>()

    val loadingConversation = sampleConversation + ChatMessage.Assistant(
        id = "loading",
        content = "Thinking...",
        isLoading = true
    )

    // Individual message samples for MessageBlocksScreen
    val sampleUserMessage = ChatMessage.User(
        id = "sample-user",
        text = "This is a sample user message",
        timestamp = "2:34 PM"
    )

    val sampleAssistantMessage = ChatMessage.Assistant(
        id = "sample-assistant",
        content = "This is a sample assistant message with some helpful information.",
        timestamp = "2:34 PM"
    )

    val longAssistantMessage = ChatMessage.Assistant(
        id = "sample-long",
        content = """
            This is a much longer assistant message that will demonstrate the truncation behavior.

            It contains multiple paragraphs and should trigger the "Show more" functionality
            when the content exceeds the maximum height threshold.

            You can use this to test how the message block handles overflow content
            and whether the fade gradient appears correctly.
        """.trimIndent()
    )
}
```

### Screen Usage Example

```kotlin
// ChatScreen.kt
@Composable
fun ChatScreen(deviceType: DeviceType) {
    val messages = remember { MockChatData.sampleConversation }

    DevicePreview(deviceType = deviceType) {
        ArcaneChatScreenScaffold(
            isEmpty = messages.isEmpty(),
            emptyState = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
                ) {
                    Text(
                        text = "No messages yet",
                        style = ArcaneTheme.typography.bodyLarge,
                        color = ArcaneTheme.colors.textSecondary
                    )
                    Text(
                        text = "Start a conversation",
                        style = ArcaneTheme.typography.labelMedium,
                        color = ArcaneTheme.colors.textDisabled
                    )
                }
            }
        ) {
            ArcaneChatMessageList(
                messages = messages,
                messageKey = { it.id }
            ) { message ->
                when (message) {
                    is ChatMessage.User -> ArcaneUserMessageBlock(
                        text = message.text,
                        timestamp = message.timestamp
                    )
                    is ChatMessage.Assistant -> ArcaneAssistantMessageBlock(
                        title = message.title,
                        isLoading = message.isLoading
                    ) {
                        Text(
                            text = message.content,
                            style = ArcaneTheme.typography.bodyMedium,
                            color = ArcaneTheme.colors.text
                        )
                    }
                }
            }
        }
    }
}
```

## Screen Breakdown

### Chat Screen
- Full chat experience inside device preview
- Uses `ArcaneChatScreenScaffold` with empty state
- Displays `sampleConversation` mock data
- Shows message list with scroll-to-bottom FAB
- Demonstrates complete chat flow

### Message Blocks Screen
- Individual message block showcase
- Similar to current `catalog/ChatScreen.kt` layout
- Sections: User messages, Assistant messages (fits), Truncated messages, Custom actions
- Each section demonstrates different message block configurations
- Wrappable in device preview for mobile testing

### Chat Input Screen
- Placeholder for future chat input components
- Will demonstrate text field, send button, attachments, voice input
- Currently shows "Coming soon" placeholder

## Implementation Phases

### Phase 1: Module Setup
1. Create `arcane-chat` module with convention plugin
2. Create `catalog-chat` module with convention plugin
3. Update settings.gradle.kts to include both modules
4. Set up dependency chains

### Phase 2: Component Migration
1. Copy chat components from `arcane-components` to `arcane-chat`
2. Update package names (`io.github.devmugi.arcane.chat.components`)
3. Update imports in migrated components
4. Delete old components from `arcane-components`
5. Create `ChatMessage` model

### Phase 3: Catalog App Structure
1. Implement `App.kt` with state management
2. Create `CatalogTopBar` with tabs and controls
3. Implement `DeviceSelector` and `ThemeToggleButton`
4. Set up navigation with `when` statement
5. Add state persistence with `rememberSaveable`

### Phase 4: Device Preview
1. Create `DeviceType` enum
2. Implement `DeviceFrame` with notch support
3. Build `DevicePreview` wrapper component
4. Test with both device types

### Phase 5: Theme Support
1. Add `isDark` parameter to `ArcaneTheme`
2. Create `ArcaneDarkColors` palette
3. Wire up theme toggle to state
4. Test all components in both themes

### Phase 6: Mock Data & Screens
1. Create `MockChatData` object with sample conversations
2. Implement `ChatScreen` with device preview
3. Implement `MessageBlocksScreen` (migrate from current catalog)
4. Implement `ChatInputScreen` placeholder
5. Test all screens with device preview toggle

## File Migrations

### From arcane-components to arcane-chat:
- `controls/UserMessageBlock.kt` → `chat/components/messages/ArcaneUserMessageBlock.kt`
- `controls/AssistantMessageBlock.kt` → `chat/components/messages/ArcaneAssistantMessageBlock.kt`
- `controls/ChatMessageList.kt` → `chat/components/messages/ArcaneChatMessageList.kt`
- `controls/ChatScreenScaffold.kt` → `chat/components/scaffold/ArcaneChatScreenScaffold.kt`

### Update main catalog app:
- Update `catalog/composeApp/src/commonMain/.../screens/ChatScreen.kt` to import from `arcane-chat`
- Or remove it entirely if `catalog-chat` replaces it

## Success Criteria

- [ ] `arcane-chat` module builds and publishes successfully
- [ ] All chat components work in new module with correct imports
- [ ] `catalog-chat` runs on Desktop, Android, and iOS
- [ ] Device preview renders correctly for Pixel 8 and iPhone 16
- [ ] Theme switching works and persists across app restarts
- [ ] Tab navigation works and persists selection
- [ ] Mock chat data displays correctly in all screens
- [ ] No build errors or dependency conflicts

## Future Enhancements

- Chat input components (text field, send button, attachments)
- Message editing and deletion
- Message reactions
- Typing indicators
- Read receipts
- Message threading
- Search functionality
- More device types (Pixel 9, iPhone 15, tablets)
- Custom device frame editor
