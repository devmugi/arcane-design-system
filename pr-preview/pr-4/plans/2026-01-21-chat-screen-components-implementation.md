# Chat Screen Components Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Implement chat UI components (scaffold, message list, user/assistant message blocks) and showcase screen for the Arcane Design System.

**Architecture:** Four composable components in `controls/` directory following existing patterns (slot-based APIs, 150ms animations, theme tokens). A new ChatScreen in the catalog demonstrates all component states.

**Tech Stack:** Kotlin Multiplatform, Jetpack Compose, Material Icons

---

## Task 1: ChatScreenScaffold

**Files:**
- Create: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/ChatScreenScaffold.kt`

**Step 1: Create the component file**

```kotlin
// arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/ChatScreenScaffold.kt
package io.github.devmugi.arcane.design.components.controls

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ArcaneChatScreenScaffold(
    isEmpty: Boolean,
    modifier: Modifier = Modifier,
    emptyState: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Crossfade(
            targetState = isEmpty,
            animationSpec = tween(150),
            label = "ChatScaffoldCrossfade"
        ) { showEmpty ->
            if (showEmpty) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    emptyState()
                }
            } else {
                Box(modifier = Modifier.fillMaxSize()) {
                    content()
                }
            }
        }
    }
}
```

**Step 2: Verify compilation**

Run: `./gradlew arcane-components:compileKotlinDesktop`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/ChatScreenScaffold.kt
git commit -m "feat(controls): add ArcaneChatScreenScaffold component"
```

---

## Task 2: UserMessageBlock

**Files:**
- Create: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/UserMessageBlock.kt`

**Step 1: Create the component file**

```kotlin
// arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/UserMessageBlock.kt
package io.github.devmugi.arcane.design.components.controls

import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

private val UserMessageShape = RoundedCornerShape(
    topStart = 12.dp,
    topEnd = 4.dp,
    bottomStart = 12.dp,
    bottomEnd = 12.dp
)

@Composable
fun ArcaneUserMessageBlock(
    text: String,
    modifier: Modifier = Modifier,
    timestamp: String? = null
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(UserMessageShape)
                .background(colors.primary.copy(alpha = 0.15f))
                .padding(
                    horizontal = ArcaneSpacing.Small,
                    vertical = ArcaneSpacing.XSmall
                ),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = text,
                style = typography.bodyMedium,
                color = colors.text
            )
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

**Step 2: Verify compilation**

Run: `./gradlew arcane-components:compileKotlinDesktop`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/UserMessageBlock.kt
git commit -m "feat(controls): add ArcaneUserMessageBlock component"
```

---

## Task 3: AssistantMessageBlock

**Files:**
- Create: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/AssistantMessageBlock.kt`

**Step 1: Create the component file with imports and signature**

```kotlin
// arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/AssistantMessageBlock.kt
package io.github.devmugi.arcane.design.components.controls

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

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
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography
    val density = LocalDensity.current

    var isExpanded by remember { mutableStateOf(false) }
    var isTruncated by remember { mutableStateOf(false) }

    val showTitleRow = title != null || isLoading
    val shouldShowBottomActions = showBottomActions || (autoShowWhenTruncated && isTruncated && !isExpanded)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(animationSpec = tween(150)),
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

                // Right side: Copy + custom actions
                Row(
                    horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.XXSmall),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (onCopyClick != null) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(ArcaneRadius.Full)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = onCopyClick
                                )
                                .semantics { contentDescription = "Copy message" },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ContentCopy,
                                contentDescription = null,
                                tint = colors.textSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    titleActions?.invoke(this)
                }
            }
        }

        // Message Content
        val maxHeightPx = with(density) { maxContentHeight.toPx() }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (!isExpanded) {
                        Modifier.heightIn(max = maxContentHeight)
                    } else {
                        Modifier
                    }
                )
                .onSizeChanged { size ->
                    if (!isExpanded) {
                        isTruncated = size.height >= maxHeightPx.toInt()
                    }
                }
                .drawWithContent {
                    drawContent()
                    if (isTruncated && !isExpanded) {
                        drawRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, colors.surface),
                                startY = size.height * 0.6f,
                                endY = size.height
                            )
                        )
                    }
                }
        ) {
            Text(
                text = text,
                style = typography.bodyMedium,
                color = colors.text,
                overflow = if (!isExpanded) TextOverflow.Clip else TextOverflow.Visible
            )
        }

        // Bottom Actions Row
        if (shouldShowBottomActions) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Show more/less button
                if (isTruncated || isExpanded) {
                    Text(
                        text = if (isExpanded) "Show less" else "Show more",
                        style = typography.labelMedium,
                        color = colors.primary,
                        modifier = Modifier
                            .clip(ArcaneRadius.Small)
                            .clickable {
                                if (onShowMoreClick != null) {
                                    onShowMoreClick()
                                } else {
                                    isExpanded = !isExpanded
                                }
                            }
                            .padding(
                                horizontal = ArcaneSpacing.XSmall,
                                vertical = ArcaneSpacing.XXSmall
                            )
                    )
                }
                bottomActions?.invoke(this)
            }
        }
    }
}
```

**Step 2: Verify compilation**

Run: `./gradlew arcane-components:compileKotlinDesktop`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/AssistantMessageBlock.kt
git commit -m "feat(controls): add ArcaneAssistantMessageBlock component"
```

---

## Task 4: ChatMessageList

**Files:**
- Create: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/ChatMessageList.kt`

**Step 1: Create the component file**

```kotlin
// arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/ChatMessageList.kt
package io.github.devmugi.arcane.design.components.controls

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing
import kotlinx.coroutines.launch

@Composable
fun <T> ArcaneChatMessageList(
    messages: List<T>,
    modifier: Modifier = Modifier,
    reverseLayout: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(ArcaneSpacing.Medium),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(ArcaneSpacing.Medium),
    showScrollToBottom: Boolean = true,
    listState: LazyListState = rememberLazyListState(),
    messageKey: ((T) -> Any)? = null,
    messageContent: @Composable (T) -> Unit
) {
    val colors = ArcaneTheme.colors
    val coroutineScope = rememberCoroutineScope()

    // Determine if we should show scroll-to-bottom button
    val showScrollButton by remember {
        derivedStateOf {
            if (!showScrollToBottom || messages.isEmpty()) {
                false
            } else {
                val lastVisibleIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                val totalItems = listState.layoutInfo.totalItemsCount
                // Show button if not at bottom (with some threshold)
                totalItems > 0 && lastVisibleIndex < totalItems - 1
            }
        }
    }

    // Auto-scroll to bottom when new messages added (if already at bottom)
    val isAtBottom by remember {
        derivedStateOf {
            val lastVisibleIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = listState.layoutInfo.totalItemsCount
            totalItems == 0 || lastVisibleIndex >= totalItems - 2
        }
    }

    LaunchedEffect(messages.size, isAtBottom) {
        if (messages.isNotEmpty() && isAtBottom) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPadding,
            verticalArrangement = verticalArrangement,
            reverseLayout = reverseLayout
        ) {
            items(
                items = messages,
                key = messageKey
            ) { message ->
                messageContent(message)
            }
        }

        // Scroll to bottom FAB
        AnimatedVisibility(
            visible = showScrollButton,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = ArcaneSpacing.Medium),
            enter = fadeIn(tween(150)),
            exit = fadeOut(tween(150))
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(ArcaneRadius.Full)
                    .background(colors.surfaceRaised)
                    .clickable {
                        coroutineScope.launch {
                            listState.animateScrollToItem(messages.size - 1)
                        }
                    }
                    .semantics { contentDescription = "Scroll to bottom" },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = colors.text,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
```

**Step 2: Verify compilation**

Run: `./gradlew arcane-components:compileKotlinDesktop`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/ChatMessageList.kt
git commit -m "feat(controls): add ArcaneChatMessageList component"
```

---

## Task 5: Update App.kt Navigation

**Files:**
- Modify: `catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/App.kt`

**Step 1: Add Chat screen to sealed class and navigation**

Add import at top:
```kotlin
import io.github.devmugi.arcane.catalog.screens.ChatScreen
```

Add to `sealed class Screen`:
```kotlin
data object Chat : Screen()
```

Add to `when (currentScreen)` block:
```kotlin
Screen.Chat -> ChatScreen(
    onBack = { currentScreen = Screen.DesignSpec }
)
```

**Step 2: Verify compilation**

Run: `./gradlew catalog:composeApp:compileKotlinDesktop`
Expected: BUILD SUCCESSFUL (will fail until ChatScreen exists - that's OK)

**Step 3: Commit (after Task 6)**

Will commit together with ChatScreen.

---

## Task 6: Create ChatScreen Showcase

**Files:**
- Create: `catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/screens/ChatScreen.kt`

**Step 1: Create the showcase screen**

```kotlin
// catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/screens/ChatScreen.kt
package io.github.devmugi.arcane.catalog.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.components.controls.ArcaneAssistantMessageBlock
import io.github.devmugi.arcane.design.components.controls.ArcaneChatScreenScaffold
import io.github.devmugi.arcane.design.components.controls.ArcaneUserMessageBlock
import io.github.devmugi.arcane.design.foundation.primitives.ArcaneSurface
import io.github.devmugi.arcane.design.foundation.primitives.SurfaceVariant
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun ChatScreen(onBack: () -> Unit = {}) {
    val typography = ArcaneTheme.typography
    val colors = ArcaneTheme.colors
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(ArcaneSpacing.Medium),
        verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Large)
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = colors.primary,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onBack() }
            )
            Text(
                text = "Chat",
                style = typography.displayMedium,
                color = colors.text
            )
        }

        // Empty State Section
        SectionTitle("Empty State")
        ArcaneSurface(
            variant = SurfaceVariant.Raised,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(ArcaneSpacing.Medium)
            ) {
                ArcaneChatScreenScaffold(
                    isEmpty = true,
                    emptyState = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
                        ) {
                            Text(
                                text = "No messages yet",
                                style = typography.bodyLarge,
                                color = colors.textSecondary
                            )
                            Text(
                                text = "Start a conversation",
                                style = typography.labelMedium,
                                color = colors.textDisabled
                            )
                        }
                    },
                    content = {}
                )
            }
        }

        // User Messages Section
        SectionTitle("User Messages")
        ArcaneSurface(
            variant = SurfaceVariant.Raised,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Medium)
            ) {
                Text(
                    text = "Short message",
                    style = typography.labelMedium,
                    color = colors.textSecondary
                )
                ArcaneUserMessageBlock(
                    text = "Hello, Claude!"
                )

                Text(
                    text = "Multi-line message",
                    style = typography.labelMedium,
                    color = colors.textSecondary
                )
                ArcaneUserMessageBlock(
                    text = "Can you help me understand how to implement a chat interface in Compose? I'm looking for best practices.",
                    timestamp = "2:34 PM"
                )
            }
        }

        // Assistant Messages - Fits Section
        SectionTitle("Assistant Messages - Fits")
        ArcaneSurface(
            variant = SurfaceVariant.Raised,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Large)
            ) {
                Text(
                    text = "With title, no loading",
                    style = typography.labelMedium,
                    color = colors.textSecondary
                )
                ArcaneAssistantMessageBlock(
                    text = "Hello! I'd be happy to help you with that.",
                    title = "Claude",
                    onCopyClick = {}
                )

                Text(
                    text = "With title + loading",
                    style = typography.labelMedium,
                    color = colors.textSecondary
                )
                ArcaneAssistantMessageBlock(
                    text = "Thinking...",
                    title = "Claude",
                    isLoading = true,
                    onCopyClick = {}
                )

                Text(
                    text = "With title actions",
                    style = typography.labelMedium,
                    color = colors.textSecondary
                )
                ArcaneAssistantMessageBlock(
                    text = "Here's a helpful response with extra actions.",
                    title = "Claude",
                    onCopyClick = {},
                    titleActions = {
                        Icon(
                            imageVector = Icons.Outlined.Share,
                            contentDescription = "Share",
                            tint = colors.textSecondary,
                            modifier = Modifier
                                .size(18.dp)
                                .clickable { }
                        )
                    }
                )
            }
        }

        // Assistant Messages - Truncated Section
        SectionTitle("Assistant Messages - Truncated")
        ArcaneSurface(
            variant = SurfaceVariant.Raised,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Large)
            ) {
                val longText = """
                    I'd be happy to help you understand chat interfaces in Compose! Here are the key concepts:

                    1. **LazyColumn for messages**: Use LazyColumn to efficiently render message lists. It only composes visible items.

                    2. **State management**: Keep your messages in a ViewModel or state holder. Use mutableStateListOf for reactive updates.

                    3. **Message alignment**: User messages typically align right, assistant messages align left.

                    4. **Auto-scroll behavior**: When new messages arrive, scroll to bottom if user was already at bottom. Show a "scroll to bottom" button if scrolled up.

                    5. **Input handling**: Use BasicTextField with keyboard actions for the input area.

                    6. **Loading states**: Show typing indicators or progress when waiting for responses.

                    Would you like me to elaborate on any of these points?
                """.trimIndent()

                Text(
                    text = "Long message (click Show more)",
                    style = typography.labelMedium,
                    color = colors.textSecondary
                )
                ArcaneAssistantMessageBlock(
                    text = longText,
                    title = "Claude",
                    maxContentHeight = 160.dp,
                    onCopyClick = {}
                )
            }
        }

        // Assistant Messages - Custom Actions Section
        SectionTitle("Assistant Messages - Custom Actions")
        ArcaneSurface(
            variant = SurfaceVariant.Raised,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Large)
            ) {
                Text(
                    text = "With bottom actions (always shown)",
                    style = typography.labelMedium,
                    color = colors.textSecondary
                )
                ArcaneAssistantMessageBlock(
                    text = "Here's a response with custom bottom actions always visible.",
                    title = "Claude",
                    showBottomActions = true,
                    autoShowWhenTruncated = false,
                    onCopyClick = {},
                    bottomActions = {
                        Icon(
                            imageVector = Icons.Outlined.Share,
                            contentDescription = "Share",
                            tint = colors.primary,
                            modifier = Modifier
                                .size(18.dp)
                                .clickable { }
                        )
                    }
                )

                val longTextWithShare = """
                    This is a longer response that will be truncated and show both the "Show more" action and a share icon in the bottom actions row.

                    The bottom actions appear automatically when content is truncated (default behavior), or can be forced to always show via the showBottomActions parameter.

                    This flexibility allows you to customize the UX based on your needs.
                """.trimIndent()

                Text(
                    text = "Truncated with share action",
                    style = typography.labelMedium,
                    color = colors.textSecondary
                )
                ArcaneAssistantMessageBlock(
                    text = longTextWithShare,
                    title = "Claude",
                    maxContentHeight = 100.dp,
                    onCopyClick = {},
                    bottomActions = {
                        Icon(
                            imageVector = Icons.Outlined.Share,
                            contentDescription = "Share",
                            tint = colors.primary,
                            modifier = Modifier
                                .size(18.dp)
                                .clickable { }
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(ArcaneSpacing.XLarge))
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = ArcaneTheme.typography.headlineLarge,
        color = ArcaneTheme.colors.textSecondary
    )
}
```

**Step 2: Verify full build**

Run: `./gradlew catalog:composeApp:compileKotlinDesktop`
Expected: BUILD SUCCESSFUL

**Step 3: Commit App.kt and ChatScreen together**

```bash
git add catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/App.kt
git add catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/screens/ChatScreen.kt
git commit -m "feat(catalog): add Chat showcase screen with navigation"
```

---

## Task 7: Add Chat Navigation Link to DesignSpecScreen

**Files:**
- Modify: `catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/screens/DesignSpecScreen.kt`

**Step 1: Add onNavigateToChat parameter**

Update function signature:
```kotlin
@Composable
fun DesignSpecScreen(
    onNavigateToControls: () -> Unit,
    onNavigateToNavigation: () -> Unit,
    onNavigateToDataDisplay: () -> Unit,
    onNavigateToFeedback: () -> Unit,
    onNavigateToChat: () -> Unit  // Add this
)
```

**Step 2: Add Chat category card** (similar pattern to other navigation cards in the file)

Find where other category cards are rendered and add a Chat card following the same pattern.

**Step 3: Update App.kt to pass the callback**

In `App.kt`, update the DesignSpecScreen call:
```kotlin
Screen.DesignSpec -> DesignSpecScreen(
    onNavigateToControls = { currentScreen = Screen.Controls },
    onNavigateToNavigation = { currentScreen = Screen.Navigation },
    onNavigateToDataDisplay = { currentScreen = Screen.DataDisplay },
    onNavigateToFeedback = { currentScreen = Screen.Feedback },
    onNavigateToChat = { currentScreen = Screen.Chat }
)
```

**Step 4: Verify compilation**

Run: `./gradlew catalog:composeApp:compileKotlinDesktop`
Expected: BUILD SUCCESSFUL

**Step 5: Commit**

```bash
git add catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/screens/DesignSpecScreen.kt
git add catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/App.kt
git commit -m "feat(catalog): add Chat navigation link to DesignSpecScreen"
```

---

## Task 8: Run Full Build and Test UI

**Step 1: Run full Gradle build**

Run: `./gradlew build`
Expected: BUILD SUCCESSFUL

**Step 2: Run desktop app to verify UI**

Run: `./gradlew catalog:composeApp:run`
Expected: App launches, Chat screen accessible from home, all components render correctly

**Step 3: Final commit if any fixes needed**

Fix any issues discovered during manual testing and commit.

---

## Summary

| Task | Component | Files |
|------|-----------|-------|
| 1 | ChatScreenScaffold | `controls/ChatScreenScaffold.kt` |
| 2 | UserMessageBlock | `controls/UserMessageBlock.kt` |
| 3 | AssistantMessageBlock | `controls/AssistantMessageBlock.kt` |
| 4 | ChatMessageList | `controls/ChatMessageList.kt` |
| 5-6 | Navigation + ChatScreen | `App.kt`, `screens/ChatScreen.kt` |
| 7 | DesignSpec nav link | `screens/DesignSpecScreen.kt` |
| 8 | Build verification | - |
