# Chat Message Block System Design

**Date:** 2026-01-23
**Status:** Approved
**Scope:** Refactor chat message system to support Slack-style structured blocks with extensibility

## Overview

Refactor the Arcane Chat module from simple text-based messages to a flexible block-based system inspired by Slack Block Kit. This enables rich content (text, images), interactive components (suggestion chips), and extensibility for custom blocks outside the library.

## Goals

1. **Structured blocks** - Support text and image blocks as foundation
2. **Interactive components** - Implement AgentSuggestionsBlock with expandable chip interface
3. **Extensibility** - Allow custom blocks to be injected without modifying library code
4. **Clean migration** - Remove legacy text/content properties entirely

## Architecture

### Block System Foundation

```kotlin
sealed class MessageBlock {
    abstract val id: String

    // Standard library blocks
    data class Text(
        override val id: String,
        val content: String,
        val style: TextStyle = TextStyle.Body
    ) : MessageBlock()

    data class Image(
        override val id: String,
        val url: String,
        val alt: String? = null,
        val aspectRatio: Float? = null
    ) : MessageBlock()

    data class AgentSuggestions(
        override val id: String,
        val suggestions: List<Suggestion>,
        val onSuggestionSelected: (Suggestion) -> Unit
    ) : MessageBlock()

    // Extensibility escape hatch
    data class Custom(
        override val id: String,
        val content: @Composable () -> Unit
    ) : MessageBlock()
}

enum class TextStyle {
    Body, Heading, Code, Caption
}
```

### Message Model Refactor

```kotlin
sealed class ChatMessage {
    abstract val id: String
    abstract val timestamp: String?
    abstract val blocks: List<MessageBlock>

    data class User(
        override val id: String,
        override val blocks: List<MessageBlock>,
        override val timestamp: String? = null
    ) : ChatMessage()

    data class Assistant(
        override val id: String,
        val title: String = "Assistant",
        override val blocks: List<MessageBlock>,
        val isLoading: Boolean = false,
        override val timestamp: String? = null
    ) : ChatMessage()
}
```

**Breaking Changes:**
- Remove `ChatMessage.User.text` property
- Remove `ChatMessage.Assistant.content` property
- All messages now use `blocks: List<MessageBlock>`

### Suggestion System

```kotlin
data class Suggestion(
    val id: String,
    val text: String,
    val icon: @Composable () -> Unit,
    val color: Color,
    val detailsContent: @Composable () -> Unit  // Composable slot
)
```

**Key Features:**
- `detailsContent` is a composable slot injected from ChatScreen
- Enables arbitrary content in expanded details (text, icons, custom layouts)
- Each suggestion has unique color for visual distinction

## Component Design

### Block Rendering System

```kotlin
@Composable
fun MessageBlockRenderer(
    block: MessageBlock,
    modifier: Modifier = Modifier
) {
    when (block) {
        is MessageBlock.Text -> TextBlockView(block, modifier)
        is MessageBlock.Image -> ImageBlockView(block, modifier)
        is MessageBlock.AgentSuggestions -> AgentSuggestionsBlockView(block, modifier)
        is MessageBlock.Custom -> block.content()
    }
}
```

### Updated Message Components

**ArcaneUserMessageBlock:**
```kotlin
@Composable
fun ArcaneUserMessageBlock(
    blocks: List<MessageBlock>,
    timestamp: String? = null,
    modifier: Modifier = Modifier,
    maxWidth: Dp = 280.dp
)
```

**ArcaneAssistantMessageBlock:**
```kotlin
@Composable
fun ArcaneAssistantMessageBlock(
    title: String?,
    blocks: List<MessageBlock>,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier,
    titleActions: @Composable (RowScope.() -> Unit)? = null
)
```

**Simplifications:**
- Remove truncation logic (move to individual block types if needed)
- Remove `bottomActions` slot (suggestions block is self-contained)
- Blocks render sequentially with spacing

### Block View Components

**TextBlockView:**
```kotlin
@Composable
fun TextBlockView(
    block: MessageBlock.Text,
    modifier: Modifier = Modifier
) {
    val style = when (block.style) {
        TextStyle.Body -> typography.bodyMedium
        TextStyle.Heading -> typography.titleMedium
        TextStyle.Code -> typography.bodySmall.copy(fontFamily = FontFamily.Monospace)
        TextStyle.Caption -> typography.labelMedium
    }

    Text(
        text = block.content,
        style = style,
        color = colors.text,
        modifier = modifier
    )
}
```

**ImageBlockView:**
```kotlin
@Composable
fun ImageBlockView(
    block: MessageBlock.Image,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(block.aspectRatio ?: 16f/9f)
            .clip(ArcaneRadius.Medium)
            .background(colors.surfaceContainerLow)
    ) {
        // AsyncImage or placeholder for v1
        Text(
            text = block.alt ?: "Image",
            style = typography.labelSmall,
            color = colors.textDisabled,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
```

**AgentSuggestionsBlockView:**
```kotlin
@Composable
fun AgentSuggestionsBlockView(
    block: MessageBlock.AgentSuggestions,
    modifier: Modifier = Modifier
) {
    var selectedSuggestionId by remember { mutableStateOf<String?>(null) }
    val isExpanded = selectedSuggestionId != null

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(ArcaneRadius.Medium)
            .background(colors.surfaceContainerLowest)
            .padding(ArcaneSpacing.Small)
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
    ) {
        // Chips in FlowRow
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall),
            verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)
        ) {
            block.suggestions.forEach { suggestion ->
                SuggestionChip(
                    suggestion = suggestion,
                    isSelected = selectedSuggestionId == suggestion.id,
                    onClick = {
                        selectedSuggestionId = if (selectedSuggestionId == suggestion.id) {
                            null  // Toggle off
                        } else {
                            suggestion.id
                        }
                        block.onSuggestionSelected(suggestion)
                    }
                )
            }
        }

        // Expanded details
        if (isExpanded) {
            val selectedSuggestion = block.suggestions.find { it.id == selectedSuggestionId }
            selectedSuggestion?.let {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(ArcaneRadius.Small)
                        .background(colors.surfaceContainer)
                        .padding(ArcaneSpacing.Medium)
                ) {
                    it.detailsContent()
                }
            }
        }
    }
}
```

**Behavior:**
- **Collapsed (default)**: Shows chips in flow layout
- **Expanded**: Selected chip highlighted + details area below
- **Toggle**: Click selected chip again to collapse
- **Animation**: `animateContentSize()` for smooth expand/collapse

**SuggestionChip:**
```kotlin
@Composable
fun SuggestionChip(
    suggestion: Suggestion,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) {
            suggestion.color.copy(alpha = 0.3f)
        } else {
            suggestion.color.copy(alpha = 0.15f)
        }
    )

    val borderColor = if (isSelected) {
        suggestion.color.copy(alpha = 0.6f)
    } else {
        Color.Transparent
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(ArcaneRadius.Large))
            .border(1.dp, borderColor, RoundedCornerShape(ArcaneRadius.Large))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = ArcaneSpacing.Small, vertical = ArcaneSpacing.XSmall),
        horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.XXSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(20.dp)) {
            CompositionLocalProvider(LocalContentColor provides suggestion.color) {
                suggestion.icon()
            }
        }
        Text(text = suggestion.text, style = typography.labelMedium, color = colors.text)
    }
}
```

**Visual States:**
- **Not selected**: Subtle background (15% alpha), no border
- **Selected**: Brighter background (30% alpha), colored border (60% alpha)
- **Icon**: 20dp size, tinted with suggestion color
- **Transitions**: Smooth color animations

## File Structure

```
arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat/
├── models/
│   ├── ChatMessage.kt (refactored - breaking changes)
│   ├── MessageBlock.kt (new)
│   └── Suggestion.kt (new)
├── components/
│   ├── blocks/
│   │   ├── MessageBlockRenderer.kt (new)
│   │   ├── TextBlockView.kt (new)
│   │   ├── ImageBlockView.kt (new)
│   │   ├── AgentSuggestionsBlockView.kt (new)
│   │   └── SuggestionChip.kt (new)
│   ├── messages/
│   │   ├── ArcaneUserMessageBlock.kt (refactored - breaking changes)
│   │   ├── ArcaneAssistantMessageBlock.kt (refactored - breaking changes)
│   │   └── ArcaneChatMessageList.kt (unchanged)
│   ├── input/ (unchanged)
│   └── scaffold/ (unchanged)
```

## Migration Guide

### Old API (v0.2.x)
```kotlin
ChatMessage.User(id = "1", text = "Hello", timestamp = "10:30 AM")

ChatMessage.Assistant(
    id = "2",
    content = "Response text",
    title = "Assistant"
)
```

### New API (v0.3.0+)
```kotlin
ChatMessage.User(
    id = "1",
    blocks = listOf(MessageBlock.Text(id = "b1", content = "Hello")),
    timestamp = "10:30 AM"
)

ChatMessage.Assistant(
    id = "2",
    title = "Assistant",
    blocks = listOf(MessageBlock.Text(id = "b2", content = "Response text"))
)
```

### Helper Extensions (Optional Convenience)
```kotlin
// For simple text messages
fun ChatMessage.User(id: String, text: String, timestamp: String? = null) =
    ChatMessage.User(
        id = id,
        blocks = listOf(MessageBlock.Text(id = "${id}_text", content = text)),
        timestamp = timestamp
    )
```

## Mock Data Example

```kotlin
val aiProjectsConversation = listOf(
    ChatMessage.User(
        id = "msg1",
        blocks = listOf(
            MessageBlock.Text(
                id = "b1",
                content = "Show me your key projects and skills related to AI development."
            )
        ),
        timestamp = "10:30 AM"
    ),
    ChatMessage.Assistant(
        id = "msg2",
        title = "Assistant",
        blocks = listOf(
            MessageBlock.Text(
                id = "b2",
                content = "I have extensive experience in AI. My work includes building a scalable recommendation system for retail client and developing an NLP-based chatbot for customer support. Here are the details and relevant skills:"
            ),
            MessageBlock.AgentSuggestions(
                id = "b3",
                suggestions = listOf(
                    Suggestion(
                        id = "rec_engine",
                        text = "Recommendation Engine",
                        icon = { Icon(Icons.Default.Lightbulb, contentDescription = null) },
                        color = Color(0xFF6366F1), // Indigo
                        detailsContent = {
                            Text(
                                "Built a machine learning recommendation system that increased user engagement by 40%. Used collaborative filtering and deep learning techniques.",
                                style = ArcaneTheme.typography.bodySmall,
                                color = ArcaneTheme.colors.text
                            )
                        }
                    ),
                    Suggestion(
                        id = "nlp_chatbot",
                        text = "NLP Chatbot",
                        icon = { Icon(Icons.Default.Chat, contentDescription = null) },
                        color = Color(0xFF8B5CF6), // Purple
                        detailsContent = {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    Icons.Default.Code,
                                    contentDescription = "Code",
                                    tint = Color(0xFF8B5CF6),
                                    modifier = Modifier.size(32.dp)
                                )
                                Icon(
                                    Icons.Default.Translate,
                                    contentDescription = "Language",
                                    tint = Color(0xFF8B5CF6),
                                    modifier = Modifier.size(32.dp)
                                )
                                Icon(
                                    Icons.Default.Psychology,
                                    contentDescription = "AI",
                                    tint = Color(0xFF8B5CF6),
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    ),
                    Suggestion(
                        id = "python",
                        text = "Python",
                        icon = { Icon(Icons.Default.Code, contentDescription = null) },
                        color = Color(0xFFEAB308), // Amber
                        detailsContent = {
                            Text(
                                "Expert in Python with 8+ years experience",
                                style = ArcaneTheme.typography.bodySmall
                            )
                        }
                    ),
                    Suggestion(
                        id = "tensorflow",
                        text = "TensorFlow",
                        icon = { Icon(Icons.Default.Memory, contentDescription = null) },
                        color = Color(0xFF10B981), // Green
                        detailsContent = {
                            Text(
                                "Deep learning framework expertise",
                                style = ArcaneTheme.typography.bodySmall
                            )
                        }
                    ),
                    Suggestion(
                        id = "gcp",
                        text = "Google Cloud Platform",
                        icon = { Icon(Icons.Default.Cloud, contentDescription = null) },
                        color = Color(0xFFEF4444), // Red
                        detailsContent = {
                            Text(
                                "Cloud infrastructure and ML deployment",
                                style = ArcaneTheme.typography.bodySmall
                            )
                        }
                    )
                ),
                onSuggestionSelected = { suggestion ->
                    println("Selected: ${suggestion.text}")
                }
            )
        ),
        timestamp = "10:31 AM"
    )
)
```

## Verification Plan

### Phase 1: Build & Basic Rendering
1. **Build library**: `./gradlew :arcane-chat:build`
2. **Verify compilation**: No errors after refactor
3. **Catalog app builds**: `./gradlew :catalog-chat:composeApp:build`

### Phase 2: Text & Image Blocks
4. **Text blocks render**: Update mock data, verify text displays correctly
5. **Text styles work**: Test Body, Heading, Code, Caption styles
6. **Image placeholders**: Verify image blocks show placeholder UI

### Phase 3: AgentSuggestions Block
7. **Chips display**: Verify flow layout with icons, text, colors
8. **Chip selection**: Click chip → background brightens, border appears
9. **Expand/collapse**: Click selected chip → details show, click again → collapses
10. **Details rendering**: First chip shows text, second shows 3 icons
11. **Multiple suggestions**: All 5 chips from mock data render correctly

### Phase 4: Interactive Chat
12. **Send message**: User messages create TextBlock instances
13. **Receive response**: Assistant responses use TextBlocks
14. **Loading state**: Verify loading spinner still works

### Phase 5: Extensibility
15. **Custom block**: Create `MessageBlock.Custom` with simple composable
16. **Custom renders**: Verify custom block content displays

### Phase 6: Visual Polish
17. **Theme variants**: Test with Arcane, Perplexity, Claude, MTG themes
18. **Device previews**: Test None, Pixel 8, iPhone 16 frames
19. **Animations**: Verify smooth expand/collapse, color transitions

## Future Enhancements

**Phase 2 (Future):**
- Markdown support in TextBlock
- Actual image loading (AsyncImage)
- Code block with syntax highlighting
- Divider block
- File attachment block

**Phase 3 (Future):**
- Button blocks (primary, secondary, danger actions)
- Input blocks (text field, select, date picker in messages)
- Context block (metadata/footer info)
- Rich media (video, audio players)

**Phase 4 (Future):**
- Block validation system
- Accessibility improvements (screen reader support for blocks)
- Analytics events for block interactions
- Block templates/presets

## Technical Notes

- **ID management**: Each block needs unique ID for Compose key tracking
- **State ownership**: AgentSuggestionsBlock owns selection state internally
- **Composable slots**: `detailsContent` allows arbitrary UI injection
- **FlowRow**: Use `androidx.compose.foundation.layout.FlowRow` for chip wrapping
- **Animation**: `animateContentSize()` for expansion, `animateColorAsState()` for chips
- **Breaking changes**: This is a major version bump (v0.2.x → v0.3.0)

## References

- Slack Block Kit: https://api.slack.com/block-kit
- Material 3 Chips: https://m3.material.io/components/chips
- Compose FlowRow: https://developer.android.com/reference/kotlin/androidx/compose/foundation/layout/package-summary#FlowRow
