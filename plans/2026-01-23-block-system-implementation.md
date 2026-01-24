# Chat Message Block System Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Refactor chat message system to support Slack-style structured blocks with text, images, interactive AgentSuggestions, and custom block extensibility.

**Architecture:** Replace simple text-based messages with a flexible block-based system. Messages contain lists of blocks (Text, Image, AgentSuggestions, Custom). Each block type has its own renderer. AgentSuggestionsBlock provides expandable chip interface with composable detail slots.

**Tech Stack:** Kotlin Compose Multiplatform, Material 3, FlowRow for chip layout, animateContentSize for smooth expand/collapse.

---

## Task 1: Create MessageBlock Model

**Files:**
- Create: `arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat/models/MessageBlock.kt`

**Step 1: Create MessageBlock sealed class with TextStyle enum**

Create the file with the following content:

```kotlin
package io.github.devmugi.arcane.chat.models

import androidx.compose.runtime.Composable

/**
 * Represents a content block within a chat message.
 * Supports text, images, interactive suggestions, and custom composables.
 */
sealed class MessageBlock {
    abstract val id: String

    /**
     * Text content block with optional styling.
     */
    data class Text(
        override val id: String,
        val content: String,
        val style: TextStyle = TextStyle.Body
    ) : MessageBlock()

    /**
     * Image block with URL and optional metadata.
     */
    data class Image(
        override val id: String,
        val url: String,
        val alt: String? = null,
        val aspectRatio: Float? = null
    ) : MessageBlock()

    /**
     * Interactive suggestion chips with expandable details.
     */
    data class AgentSuggestions(
        override val id: String,
        val suggestions: List<Suggestion>,
        val onSuggestionSelected: (Suggestion) -> Unit
    ) : MessageBlock()

    /**
     * Custom composable block for extensibility.
     * Allows external code to inject any composable without modifying the library.
     */
    data class Custom(
        override val id: String,
        val content: @Composable () -> Unit
    ) : MessageBlock()
}

/**
 * Text styling options for Text blocks.
 */
enum class TextStyle {
    Body,
    Heading,
    Code,
    Caption
}
```

**Step 2: Build library module to verify**

Run: `./gradlew :arcane-chat:build`
Expected: SUCCESS (compilation passes)

**Step 3: Commit**

```bash
git add arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat/models/MessageBlock.kt
git commit -m "feat(chat): add MessageBlock sealed class with Text, Image, AgentSuggestions, and Custom types"
```

---

## Task 2: Create Suggestion Model

**Files:**
- Create: `arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat/models/Suggestion.kt`

**Step 1: Create Suggestion data class**

Create the file with the following content:

```kotlin
package io.github.devmugi.arcane.chat.models

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Represents a suggestion chip with expandable details.
 * Used within AgentSuggestionsBlock.
 *
 * @param id Unique identifier for the suggestion
 * @param text Display text shown on the chip
 * @param icon Composable icon displayed on the chip
 * @param color Tint color for the chip background and icon
 * @param detailsContent Composable slot for expanded detail content
 */
data class Suggestion(
    val id: String,
    val text: String,
    val icon: @Composable () -> Unit,
    val color: Color,
    val detailsContent: @Composable () -> Unit
)
```

**Step 2: Build library module to verify**

Run: `./gradlew :arcane-chat:build`
Expected: SUCCESS

**Step 3: Commit**

```bash
git add arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat/models/Suggestion.kt
git commit -m "feat(chat): add Suggestion data class for expandable chip interface"
```

---

## Task 3: Refactor ChatMessage Model (Breaking Changes)

**Files:**
- Modify: `arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat/models/ChatMessage.kt`

**Step 1: Replace ChatMessage model with block-based version**

Replace the entire file content with:

```kotlin
package io.github.devmugi.arcane.chat.models

/**
 * Represents a message in a chat conversation.
 * Messages contain blocks of content rather than simple text.
 *
 * Breaking changes from v0.2.x:
 * - Removed ChatMessage.User.text property
 * - Removed ChatMessage.Assistant.content property
 * - Added blocks: List<MessageBlock> to both types
 */
sealed class ChatMessage {
    abstract val id: String
    abstract val timestamp: String?
    abstract val blocks: List<MessageBlock>

    /**
     * Message sent by the user.
     */
    data class User(
        override val id: String,
        override val blocks: List<MessageBlock>,
        override val timestamp: String? = null
    ) : ChatMessage()

    /**
     * Message sent by the assistant/AI.
     */
    data class Assistant(
        override val id: String,
        val title: String = "Assistant",
        override val blocks: List<MessageBlock>,
        val isLoading: Boolean = false,
        override val timestamp: String? = null
    ) : ChatMessage()
}
```

**Step 2: Attempt to build (expect failures)**

Run: `./gradlew :arcane-chat:build`
Expected: FAILURE with compilation errors in components using old ChatMessage API
(This is expected - we'll fix these in subsequent tasks)

**Step 3: Commit the breaking change**

```bash
git add arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat/models/ChatMessage.kt
git commit -m "feat(chat)!: refactor ChatMessage to use blocks instead of text/content

BREAKING CHANGE: ChatMessage.User.text and ChatMessage.Assistant.content
properties removed. All messages now use blocks: List<MessageBlock>."
```

---

## Task 4: Create TextBlockView Component

**Files:**
- Create: `arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat/components/blocks/TextBlockView.kt`

**Step 1: Create blocks directory**

Run: `mkdir -p arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat/components/blocks`

**Step 2: Create TextBlockView composable**

Create the file with the following content:

```kotlin
package io.github.devmugi.arcane.chat.components.blocks

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import io.github.devmugi.arcane.chat.models.MessageBlock
import io.github.devmugi.arcane.chat.models.TextStyle
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme

/**
 * Renders a text block with appropriate styling.
 *
 * @param block The text block to render
 * @param modifier Optional modifier for the text component
 */
@Composable
fun TextBlockView(
    block: MessageBlock.Text,
    modifier: Modifier = Modifier
) {
    val typography = ArcaneTheme.typography
    val colors = ArcaneTheme.colors

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

**Step 3: Build library module**

Run: `./gradlew :arcane-chat:build`
Expected: Still failing due to message components not updated, but TextBlockView itself should compile

**Step 4: Commit**

```bash
git add arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat/components/blocks/TextBlockView.kt
git commit -m "feat(chat): add TextBlockView component for rendering text blocks"
```

---

## Task 5: Create ImageBlockView Component

**Files:**
- Create: `arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat/components/blocks/ImageBlockView.kt`

**Step 1: Create ImageBlockView composable**

Create the file with the following content:

```kotlin
package io.github.devmugi.arcane.chat.components.blocks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import io.github.devmugi.arcane.chat.models.MessageBlock
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

/**
 * Renders an image block.
 * Currently shows a placeholder with alt text until AsyncImage is integrated.
 *
 * @param block The image block to render
 * @param modifier Optional modifier for the image container
 */
@Composable
fun ImageBlockView(
    block: MessageBlock.Image,
    modifier: Modifier = Modifier
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography

    Box(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (block.aspectRatio != null) {
                    Modifier.aspectRatio(block.aspectRatio)
                } else {
                    Modifier
                }
            )
            .clip(ArcaneRadius.Medium)
            .background(colors.surfaceContainerLow),
        contentAlignment = Alignment.Center
    ) {
        // Placeholder for v1 - will be replaced with AsyncImage later
        Text(
            text = block.alt ?: "Image",
            style = typography.labelSmall,
            color = colors.textDisabled,
            modifier = Modifier.padding(ArcaneSpacing.Medium)
        )
    }
}
```

**Step 2: Build library module**

Run: `./gradlew :arcane-chat:build`
Expected: Still failing, but ImageBlockView compiles

**Step 3: Commit**

```bash
git add arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat/components/blocks/ImageBlockView.kt
git commit -m "feat(chat): add ImageBlockView component with placeholder UI"
```

---

## Task 6: Create SuggestionChip Component

**Files:**
- Create: `arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat/components/blocks/SuggestionChip.kt`

**Step 1: Create SuggestionChip composable**

Create the file with the following content:

```kotlin
package io.github.devmugi.arcane.chat.components.blocks

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.chat.models.Suggestion
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

/**
 * Renders a suggestion chip with icon and text.
 * Visual state changes based on selection (background brightness, border).
 *
 * @param suggestion The suggestion data to display
 * @param isSelected Whether this chip is currently selected
 * @param onClick Callback when chip is clicked
 * @param modifier Optional modifier
 */
@Composable
fun SuggestionChip(
    suggestion: Suggestion,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography

    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) {
            suggestion.color.copy(alpha = 0.3f)  // Brighter when selected
        } else {
            suggestion.color.copy(alpha = 0.15f)  // Subtle when not selected
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
            .border(
                width = if (isSelected) 1.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(ArcaneRadius.Large)
            )
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(
                horizontal = ArcaneSpacing.Small,
                vertical = ArcaneSpacing.XSmall
            ),
        horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.XXSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon with color tint
        Box(modifier = Modifier.size(20.dp)) {
            CompositionLocalProvider(LocalContentColor provides suggestion.color) {
                suggestion.icon()
            }
        }

        Text(
            text = suggestion.text,
            style = typography.labelMedium,
            color = colors.text
        )
    }
}
```

**Step 2: Build library module**

Run: `./gradlew :arcane-chat:build`
Expected: Still failing, but SuggestionChip compiles

**Step 3: Commit**

```bash
git add arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat/components/blocks/SuggestionChip.kt
git commit -m "feat(chat): add SuggestionChip component with selection states and animations"
```

---

## Task 7: Create AgentSuggestionsBlockView Component

**Files:**
- Create: `arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat/components/blocks/AgentSuggestionsBlockView.kt`

**Step 1: Create AgentSuggestionsBlockView composable**

Create the file with the following content:

```kotlin
package io.github.devmugi.arcane.chat.components.blocks

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import io.github.devmugi.arcane.chat.models.MessageBlock
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

/**
 * Renders an interactive suggestions block with expandable chips.
 *
 * Behavior:
 * - Collapsed (default): Shows chips in flow layout
 * - Expanded: Selected chip highlighted + details area below
 * - Toggle: Click selected chip again to collapse
 *
 * @param block The agent suggestions block to render
 * @param modifier Optional modifier
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AgentSuggestionsBlockView(
    block: MessageBlock.AgentSuggestions,
    modifier: Modifier = Modifier
) {
    val colors = ArcaneTheme.colors

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
                            null  // Toggle off if already selected
                        } else {
                            suggestion.id  // Select new chip
                        }
                        block.onSuggestionSelected(suggestion)
                    }
                )
            }
        }

        // Expanded details area
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
                    it.detailsContent()  // Render injected composable slot
                }
            }
        }
    }
}
```

**Step 2: Build library module**

Run: `./gradlew :arcane-chat:build`
Expected: Still failing, but AgentSuggestionsBlockView compiles

**Step 3: Commit**

```bash
git add arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat/components/blocks/AgentSuggestionsBlockView.kt
git commit -m "feat(chat): add AgentSuggestionsBlockView with expandable chip interface"
```

---

## Task 8: Create MessageBlockRenderer

**Files:**
- Create: `arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat/components/blocks/MessageBlockRenderer.kt`

**Step 1: Create MessageBlockRenderer composable**

Create the file with the following content:

```kotlin
package io.github.devmugi.arcane.chat.components.blocks

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.devmugi.arcane.chat.models.MessageBlock

/**
 * Renders a message block based on its type.
 * Delegates to specific block view components.
 *
 * @param block The block to render
 * @param modifier Optional modifier applied to the block view
 */
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

**Step 2: Build library module**

Run: `./gradlew :arcane-chat:build`
Expected: Still failing due to message components, but renderer compiles

**Step 3: Commit**

```bash
git add arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat/components/blocks/MessageBlockRenderer.kt
git commit -m "feat(chat): add MessageBlockRenderer for polymorphic block rendering"
```

---

## Task 9: Refactor ArcaneUserMessageBlock

**Files:**
- Modify: `arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat/components/messages/ArcaneUserMessageBlock.kt`

**Step 1: Replace ArcaneUserMessageBlock with block-based version**

Replace the entire file content with:

```kotlin
package io.github.devmugi.arcane.chat.components.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.chat.components.blocks.MessageBlockRenderer
import io.github.devmugi.arcane.chat.models.MessageBlock
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

private val UserMessageShape = RoundedCornerShape(
    topStart = 12.dp,
    topEnd = 4.dp,
    bottomStart = 12.dp,
    bottomEnd = 12.dp
)

/**
 * Renders a user message block with right alignment.
 * Now accepts a list of blocks instead of plain text.
 *
 * @param blocks List of content blocks to render
 * @param timestamp Optional timestamp to display
 * @param modifier Optional modifier
 * @param maxWidth Maximum width of the message bubble
 * @param backgroundColor Background color of the message bubble
 */
@Composable
fun ArcaneUserMessageBlock(
    blocks: List<MessageBlock>,
    modifier: Modifier = Modifier,
    timestamp: String? = null,
    maxWidth: Dp = 280.dp,
    backgroundColor: Color = ArcaneTheme.colors.primary.copy(alpha = 0.15f)
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = maxWidth)
                .clip(UserMessageShape)
                .background(backgroundColor)
                .padding(
                    horizontal = ArcaneSpacing.Small,
                    vertical = ArcaneSpacing.XSmall
                ),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)
        ) {
            // Render all blocks
            blocks.forEach { block ->
                MessageBlockRenderer(block)
            }

            // Timestamp
            if (timestamp != null) {
                Text(
                    text = timestamp,
                    style = typography.labelSmall,
                    color = colors.textSecondary,
                    modifier = Modifier.padding(top = ArcaneSpacing.XXSmall)
                )
            }
        }
    }
}
```

**Step 2: Build library module**

Run: `./gradlew :arcane-chat:build`
Expected: Still failing due to assistant message block and usage sites

**Step 3: Commit**

```bash
git add arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat/components/messages/ArcaneUserMessageBlock.kt
git commit -m "refactor(chat)!: update ArcaneUserMessageBlock to render blocks

BREAKING CHANGE: ArcaneUserMessageBlock now takes blocks: List<MessageBlock>
instead of text: String parameter."
```

---

## Task 10: Refactor ArcaneAssistantMessageBlock

**Files:**
- Modify: `arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat/components/messages/ArcaneAssistantMessageBlock.kt`

**Step 1: Replace ArcaneAssistantMessageBlock with simplified block-based version**

Replace the entire file content with:

```kotlin
package io.github.devmugi.arcane.chat.components.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.chat.components.blocks.MessageBlockRenderer
import io.github.devmugi.arcane.chat.models.MessageBlock
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

private val AssistantMessageShape = RoundedCornerShape(
    topStart = 4.dp,
    topEnd = 12.dp,
    bottomStart = 12.dp,
    bottomEnd = 12.dp
)

/**
 * Renders an assistant message block with left alignment.
 * Now accepts a list of blocks instead of content lambda.
 *
 * Removed features from previous version:
 * - Content truncation (move to individual block types if needed)
 * - Bottom actions slot (suggestions block is self-contained)
 *
 * @param title Optional title for the message (e.g., "Assistant")
 * @param blocks List of content blocks to render
 * @param isLoading Whether to show loading spinner
 * @param modifier Optional modifier
 * @param titleActions Optional composable for actions in the title row
 */
@Composable
fun ArcaneAssistantMessageBlock(
    title: String?,
    blocks: List<MessageBlock>,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    titleActions: @Composable (RowScope.() -> Unit)? = null
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography

    val showTitleRow = title != null || isLoading

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(AssistantMessageShape)
            .background(colors.surfaceContainer)
            .padding(ArcaneSpacing.Small),
        verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)
    ) {
        // Title Row
        if (showTitleRow) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left side: Loading + Title
                Row(
                    horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = colors.primary,
                            strokeWidth = 2.dp
                        )
                    }
                    if (title != null) {
                        Text(
                            text = title,
                            style = typography.labelLarge,
                            color = colors.textSecondary
                        )
                    }
                }

                // Right side: custom actions
                if (titleActions != null) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.XXSmall),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        titleActions()
                    }
                }
            }
        }

        // Render all blocks
        blocks.forEach { block ->
            MessageBlockRenderer(block, Modifier.fillMaxWidth())
        }
    }
}
```

**Step 2: Build library module**

Run: `./gradlew :arcane-chat:build`
Expected: Still failing due to ChatScreen and MockChatData usage

**Step 3: Commit**

```bash
git add arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat/components/messages/ArcaneAssistantMessageBlock.kt
git commit -m "refactor(chat)!: simplify ArcaneAssistantMessageBlock to render blocks

BREAKING CHANGE: ArcaneAssistantMessageBlock now takes blocks: List<MessageBlock>
instead of content: @Composable () -> Unit. Removed truncation and bottomActions."
```

---

## Task 11: Update MockChatData

**Files:**
- Modify: `catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/data/MockChatData.kt`

**Step 1: Read current MockChatData to understand structure**

Run: `cat catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/data/MockChatData.kt`

**Step 2: Replace MockChatData with block-based version**

Replace the entire file content with:

```kotlin
package io.github.devmugi.arcane.catalog.chat.data

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.chat.models.ChatMessage
import io.github.devmugi.arcane.chat.models.MessageBlock
import io.github.devmugi.arcane.chat.models.Suggestion
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme

object MockChatData {
    /**
     * Sample conversation with agent suggestions block.
     */
    val sampleConversation = listOf(
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
                                    text = "Built a machine learning recommendation system that increased user engagement by 40%. Used collaborative filtering and deep learning techniques.",
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
                                    text = "Expert in Python with 8+ years experience",
                                    style = ArcaneTheme.typography.bodySmall,
                                    color = ArcaneTheme.colors.text
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
                                    text = "Deep learning framework expertise",
                                    style = ArcaneTheme.typography.bodySmall,
                                    color = ArcaneTheme.colors.text
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
                                    text = "Cloud infrastructure and ML deployment",
                                    style = ArcaneTheme.typography.bodySmall,
                                    color = ArcaneTheme.colors.text
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

    val shortConversation = listOf(
        ChatMessage.User(
            id = "msg1",
            blocks = listOf(MessageBlock.Text(id = "b1", content = "Hello!")),
            timestamp = "9:00 AM"
        ),
        ChatMessage.Assistant(
            id = "msg2",
            title = "Assistant",
            blocks = listOf(MessageBlock.Text(id = "b2", content = "Hi there! How can I help you today?")),
            timestamp = "9:00 AM"
        )
    )

    val emptyConversation = emptyList<ChatMessage>()

    val loadingConversation = sampleConversation + ChatMessage.Assistant(
        id = "msg_loading",
        title = "Assistant",
        blocks = listOf(MessageBlock.Text(id = "b_loading", content = "")),
        isLoading = true,
        timestamp = null
    )
}
```

**Step 3: Build catalog app**

Run: `./gradlew :catalog-chat:composeApp:build`
Expected: Still failing due to ChatScreen not updated

**Step 4: Commit**

```bash
git add catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/data/MockChatData.kt
git commit -m "refactor(catalog-chat): update MockChatData to use block-based messages with AgentSuggestions"
```

---

## Task 12: Update ChatScreen

**Files:**
- Modify: `catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/screens/ChatScreen.kt`

**Step 1: Replace ChatScreen with block-based version**

Replace the entire file content with:

```kotlin
package io.github.devmugi.arcane.catalog.chat.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import io.github.devmugi.arcane.catalog.chat.components.ComponentPreview
import io.github.devmugi.arcane.catalog.chat.components.DeviceType
import io.github.devmugi.arcane.catalog.chat.data.MockChatData
import io.github.devmugi.arcane.chat.components.input.ArcaneAgentChatInput
import io.github.devmugi.arcane.chat.components.messages.ArcaneAssistantMessageBlock
import io.github.devmugi.arcane.chat.components.messages.ArcaneChatMessageList
import io.github.devmugi.arcane.chat.components.messages.ArcaneUserMessageBlock
import io.github.devmugi.arcane.chat.components.scaffold.ArcaneChatScreenScaffold
import io.github.devmugi.arcane.chat.models.ChatMessage
import io.github.devmugi.arcane.chat.models.MessageBlock
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun ChatScreen(deviceType: DeviceType) {
    var messages by remember { mutableStateOf<List<ChatMessage>>(MockChatData.sampleConversation) }
    var inputText by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    fun handleSendMessage() {
        if (inputText.isBlank()) return

        // Add user message
        val userMessage = ChatMessage.User(
            id = generateId(),
            blocks = listOf(
                MessageBlock.Text(
                    id = generateId(),
                    content = inputText
                )
            ),
            timestamp = getCurrentTimestamp()
        )
        messages = messages + userMessage
        val userInput = inputText
        inputText = ""

        // Add loading message
        val loadingId = generateId()
        val loadingMessage = ChatMessage.Assistant(
            id = loadingId,
            title = "Assistant",
            blocks = listOf(
                MessageBlock.Text(
                    id = generateId(),
                    content = ""
                )
            ),
            isLoading = true,
            timestamp = null
        )
        messages = messages + loadingMessage

        // Simulate response delay
        scope.launch {
            delay(700)
            messages = messages.filterNot { it.id == loadingId }
            val response = ChatMessage.Assistant(
                id = generateId(),
                title = "Assistant",
                blocks = listOf(
                    MessageBlock.Text(
                        id = generateId(),
                        content = "Response to \"$userInput\""
                    )
                ),
                isLoading = false,
                timestamp = getCurrentTimestamp()
            )
            messages = messages + response
        }
    }

    ComponentPreview(deviceType = deviceType) {
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
            },
            bottomBar = {
                ArcaneAgentChatInput(
                    value = inputText,
                    onValueChange = { inputText = it },
                    onSend = { handleSendMessage() },
                    placeholder = "Type a message...",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        ) {
            ArcaneChatMessageList(
                messages = messages,
                messageKey = { it.id }
            ) { message ->
                when (message) {
                    is ChatMessage.User -> ArcaneUserMessageBlock(
                        blocks = message.blocks,
                        timestamp = message.timestamp
                    )
                    is ChatMessage.Assistant -> ArcaneAssistantMessageBlock(
                        title = message.title,
                        blocks = message.blocks,
                        isLoading = message.isLoading
                    )
                }
            }
        }
    }
}

private var messageIdCounter = 0
private fun generateId(): String = "msg_${messageIdCounter++}"

private fun getCurrentTimestamp(): String {
    // Simple timestamp for demo purposes
    val hour = (System.currentTimeMillis() / 3600000 % 24).toInt()
    val minute = (System.currentTimeMillis() / 60000 % 60).toInt()
    val formattedHour = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
    val amPm = if (hour < 12) "AM" else "PM"
    return String.format("%d:%02d %s", formattedHour, minute, amPm)
}
```

**Step 2: Build catalog app**

Run: `./gradlew :catalog-chat:composeApp:build`
Expected: SUCCESS

**Step 3: Commit**

```bash
git add catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/screens/ChatScreen.kt
git commit -m "refactor(catalog-chat): update ChatScreen to use block-based message system"
```

---

## Task 13: Build and Verify Compilation

**Step 1: Clean build entire project**

Run: `./gradlew clean build`
Expected: SUCCESS - all modules compile

**Step 2: Verify library module specifically**

Run: `./gradlew :arcane-chat:build`
Expected: SUCCESS

**Step 3: Verify catalog app builds**

Run: `./gradlew :catalog-chat:composeApp:build`
Expected: SUCCESS

**Step 4: Commit checkpoint**

```bash
git add -A
git commit -m "chore: verify all modules build successfully after block system refactor"
```

---

## Task 14: Manual Testing - Basic Functionality

**Step 1: Run the catalog app**

Run: `./gradlew :catalog-chat:composeApp:run`
Expected: App launches without errors

**Step 2: Navigate to Chat tab**

Manual action: Click "Chat" tab in the app
Expected:
- Two messages visible (user question + assistant response)
- Assistant message shows text block + suggestions block

**Step 3: Test AgentSuggestions block - collapsed state**

Manual verification:
- [ ] Five suggestion chips visible in flow layout
- [ ] Each chip has icon, text, and colored background
- [ ] Colors: Indigo (Rec Engine), Purple (NLP), Amber (Python), Green (TensorFlow), Red (GCP)
- [ ] No details visible (collapsed state)

**Step 4: Test chip selection**

Manual action: Click "Recommendation Engine" chip
Expected:
- [ ] Chip background brightens (15% → 30% alpha)
- [ ] Colored border appears around chip
- [ ] Details area expands below chips with text content
- [ ] Smooth animation

**Step 5: Test chip deselection**

Manual action: Click "Recommendation Engine" chip again
Expected:
- [ ] Chip returns to subtle appearance
- [ ] Details area collapses
- [ ] Smooth animation

**Step 6: Test different chip selection**

Manual action: Click "NLP Chatbot" chip
Expected:
- [ ] Previous chip deselects
- [ ] New chip selects (brightens, border appears)
- [ ] Details show 3 icons (Code, Translate, Psychology)
- [ ] Icons are purple-tinted (matching chip color)

**Step 7: Test interactive chat**

Manual action: Type "test message" and press Enter
Expected:
- [ ] User message appears with TextBlock
- [ ] Loading spinner shows briefly
- [ ] After 700ms, assistant responds with "Response to 'test message'"

**Step 8: Document any issues**

If any issues found, create GitHub issues or note for fixing.

---

## Task 15: Manual Testing - Visual Polish

**Step 1: Test theme variants**

Manual actions:
- Switch to Perplexity theme
- Switch to Claude theme
- Switch to MTG theme
- Switch back to Arcane theme

Expected for each:
- [ ] Suggestion chips update colors appropriately
- [ ] Text colors update
- [ ] Surface colors update

**Step 2: Test device previews**

Manual actions:
- Select "None" device → verify full width
- Select "Pixel 8" → verify fits in device frame
- Select "iPhone 16" → verify fits with safe areas

Expected:
- [ ] All suggestions visible
- [ ] Chat input respects safe areas
- [ ] No content overflow

**Step 3: Test animations**

Manual verification:
- [ ] Chip color transition is smooth when selecting/deselecting
- [ ] Details expand/collapse smoothly (animateContentSize)
- [ ] No jarring layout shifts

---

## Task 16: Optional - Test Custom Block

**Step 1: Add test custom block to MockChatData**

Add after the AgentSuggestions block in sampleConversation:

```kotlin
MessageBlock.Custom(
    id = "custom1",
    content = {
        Text(
            "This is a custom block!",
            style = ArcaneTheme.typography.titleSmall,
            color = Color.Magenta
        )
    }
)
```

**Step 2: Run app and verify**

Run: `./gradlew :catalog-chat:composeApp:run`
Expected:
- [ ] Custom block renders below suggestions
- [ ] Magenta text displays correctly
- [ ] No errors

**Step 3: Commit test**

```bash
git add catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/data/MockChatData.kt
git commit -m "test: add custom block example to verify extensibility"
```

---

## Task 17: Update MessageBlocksScreen (Optional)

**Files:**
- Modify: `catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/screens/MessageBlocksScreen.kt`

**Note:** This screen may need updates to work with the new block system. If time permits, update it to showcase individual blocks. Otherwise, it can be addressed in a follow-up task.

**Step 1: Check if MessageBlocksScreen still compiles**

Run: `./gradlew :catalog-chat:composeApp:build`

If it fails, consider:
- Option 1: Fix it now following the same block pattern
- Option 2: Comment it out and create a TODO
- Option 3: Remove it entirely if redundant

---

## Task 18: Final Verification

**Step 1: Run full test suite (if tests exist)**

Run: `./gradlew :arcane-chat:allTests`
Expected: All tests pass (or create issues for failures)

**Step 2: Final build verification**

Run: `./gradlew build`
Expected: SUCCESS

**Step 3: Create final commit**

```bash
git add -A
git commit -m "feat: complete chat message block system implementation

Implemented Slack-style block system with:
- Text, Image, AgentSuggestions, and Custom block types
- Interactive suggestion chips with expandable details
- Block-based message rendering
- Full backward-incompatible migration from text-based messages

Verified functionality:
- Suggestion chips display and select correctly
- Details expand/collapse smoothly
- Interactive chat creates blocks
- Custom blocks render
- All themes work
- Device previews work"
```

**Step 4: Push to remote (if applicable)**

Run: `git push origin <branch-name>`

---

## Verification Checklist

After completing all tasks, verify:

**Core Functionality:**
- [ ] Library builds without errors
- [ ] Catalog app builds without errors
- [ ] App launches and displays chat
- [ ] Suggestion chips render with icons and colors
- [ ] Chips can be selected (visual change + border)
- [ ] Details expand when chip selected
- [ ] Details collapse when chip clicked again
- [ ] Only one chip selected at a time
- [ ] Text details render (Recommendation Engine chip)
- [ ] Icon details render (NLP Chatbot chip - 3 icons)
- [ ] Interactive chat works (send message → response)
- [ ] Custom blocks can be added and render

**Visual Polish:**
- [ ] Animations are smooth (color transitions, expand/collapse)
- [ ] All theme variants work
- [ ] All device previews work
- [ ] No layout issues or overflow

**Code Quality:**
- [ ] All commits have clear messages
- [ ] No dead code remains
- [ ] Breaking changes documented in commits

---

## Notes

- **Breaking Changes:** This is a major version bump (v0.2.x → v0.3.0)
- **FlowRow:** Uses `androidx.compose.foundation.layout.FlowRow` (ExperimentalLayoutApi)
- **Future Work:** MessageBlocksScreen may need updating, AsyncImage for actual image loading
