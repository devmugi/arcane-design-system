# Chat Module Extraction Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Extract chat components into arcane-chat module and create catalog-chat app with device preview, theme switching, and persistent state.

**Architecture:** Clean module separation with arcane-chat (library) and catalog-chat (catalog app). Device preview wraps content in device frames (Pixel 8, iPhone 16). Theme supports light/dark modes with persistent state using rememberSaveable.

**Tech Stack:** Kotlin Multiplatform, Compose Multiplatform 1.10.0, Material 3, Convention plugins

---

## Task 1: Create arcane-chat Module Structure

**Files:**
- Create: `arcane-chat/build.gradle.kts`
- Modify: `settings.gradle.kts`

**Step 1: Create arcane-chat directory and build file**

Create the module directory:
```bash
mkdir -p arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat
```

Create `arcane-chat/build.gradle.kts`:
```kotlin
plugins {
    id("arcane.multiplatform.library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":arcane-foundation"))
            implementation(project(":arcane-components"))
        }
    }
}
```

**Step 2: Register module in settings.gradle.kts**

Modify `settings.gradle.kts` (after line 33):
```kotlin
include(":arcane-foundation")
include(":arcane-components")
include(":arcane-chat")
include(":catalog:composeApp")
```

**Step 3: Verify module builds**

Run: `./gradlew :arcane-chat:build`
Expected: BUILD SUCCESSFUL

**Step 4: Commit**

```bash
git add arcane-chat/build.gradle.kts settings.gradle.kts
git commit -m "feat(arcane-chat): create arcane-chat module"
```

---

## Task 2: Create catalog-chat Module Structure

**Files:**
- Create: `catalog-chat/build.gradle.kts`
- Create: `catalog-chat/composeApp/build.gradle.kts`
- Modify: `settings.gradle.kts`

**Step 1: Create catalog-chat directory structure**

```bash
mkdir -p catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat
mkdir -p catalog-chat/composeApp/src/androidMain/kotlin/io/github/devmugi/arcane/catalog/chat
mkdir -p catalog-chat/composeApp/src/desktopMain/kotlin/io/github/devmugi/arcane/catalog/chat
mkdir -p catalog-chat/composeApp/src/iosMain/kotlin/io/github/devmugi/arcane/catalog/chat
```

**Step 2: Create root build.gradle.kts**

Create `catalog-chat/build.gradle.kts`:
```kotlin
// Empty root build file for catalog-chat
```

**Step 3: Create composeApp build.gradle.kts**

Create `catalog-chat/composeApp/build.gradle.kts`:
```kotlin
plugins {
    id("arcane.multiplatform.application")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":arcane-foundation"))
            implementation(project(":arcane-components"))
            implementation(project(":arcane-chat"))
        }
    }
}

android {
    namespace = "io.github.devmugi.arcane.catalog.chat"

    defaultConfig {
        applicationId = "io.github.devmugi.arcane.catalog.chat"
    }
}

compose.desktop {
    application {
        mainClass = "io.github.devmugi.arcane.catalog.chat.MainKt"
    }
}
```

**Step 4: Register module in settings.gradle.kts**

Modify `settings.gradle.kts` (after arcane-chat):
```kotlin
include(":arcane-foundation")
include(":arcane-components")
include(":arcane-chat")
include(":catalog:composeApp")
include(":catalog-chat:composeApp")
```

**Step 5: Verify module builds**

Run: `./gradlew :catalog-chat:composeApp:build`
Expected: BUILD SUCCESSFUL (may have warnings about missing main class - that's OK for now)

**Step 6: Commit**

```bash
git add catalog-chat/ settings.gradle.kts
git commit -m "feat(catalog-chat): create catalog-chat module structure"
```

---

## Task 3: Add Dark Theme Support to ArcaneTheme

**Files:**
- Modify: `arcane-foundation/src/commonMain/kotlin/io/github/devmugi/arcane/design/foundation/theme/ArcaneTheme.kt`
- Modify: `arcane-foundation/src/commonMain/kotlin/io/github/devmugi/arcane/design/foundation/theme/ArcaneColors.kt`

**Step 1: Add dark color scheme to ArcaneColors.kt**

Add this function to the `companion object` in `ArcaneColors.kt` (after the `mtg()` function):
```kotlin
/**
 * Dark theme variant with inverted brightness and adjusted purple accent.
 * Maintains the sci-fi aesthetic with deeper backgrounds and brighter text.
 */
fun dark(): ArcaneColors = ArcaneColors(
    primary = Color(0xFFB19EFF),                        // Lighter purple for dark bg
    primaryVariant = Color(0xFF8B72FF),                 // Medium purple
    onPrimary = Color(0xFF1A1A2E),                      // Dark text on purple

    secondaryContainer = Color(0xFF3D2F5C),             // Muted purple container
    onSecondaryContainer = Color(0xFFB19EFF),           // Light purple text

    // Material 3 Surface Containers (dark mode tonal elevation)
    surfaceContainerLowest = Color(0xFF0F0F1A),         // Darkest
    surfaceContainerLow = Color(0xFF16162A),            // Base level
    surfaceContainer = Color(0xFF1D1D38),               // Standard cards
    surfaceContainerHigh = Color(0xFF242446),           // Elevated modals
    surfaceContainerHighest = Color(0xFF2B2B54),        // Maximum emphasis

    glow = Color(0xFFB19EFF).copy(alpha = 0.3f),        // Purple glow
    glowStrong = Color(0xFFB19EFF).copy(alpha = 0.6f),  // Strong purple glow

    text = Color(0xFFE6E6FF),                           // Very light purple-white
    textSecondary = Color(0xFFB3B3CC),                  // Medium purple-gray
    textDisabled = Color(0xFF666680),                   // Darker purple-gray

    border = Color(0xFFB19EFF).copy(alpha = 0.4f),      // Purple border
    borderFocused = Color(0xFFB19EFF),                  // Bright purple when focused

    error = Color(0xFFFF8A80),                          // Light red for dark bg
    success = Color(0xFFB19EFF),                        // Purple for success
    warning = Color(0xFFFFD54F),                        // Light yellow for dark bg
)
```

**Step 2: Update ArcaneTheme to support isDark parameter**

Modify `ArcaneTheme.kt` - replace the entire `ArcaneTheme` function:
```kotlin
@Composable
fun ArcaneTheme(
    isDark: Boolean = false,
    colors: ArcaneColors = if (isDark) ArcaneColors.dark() else ArcaneColors.default(),
    typography: ArcaneTypography = ArcaneTypography(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalArcaneColors provides colors,
        LocalArcaneTypography provides typography,
        content = content
    )
}
```

**Step 3: Build and verify**

Run: `./gradlew :arcane-foundation:build`
Expected: BUILD SUCCESSFUL

**Step 4: Commit**

```bash
git add arcane-foundation/src/commonMain/kotlin/io/github/devmugi/arcane/design/foundation/theme/
git commit -m "feat(theme): add dark theme support with isDark parameter"
```

---

## Task 4: Create ChatMessage Model

**Files:**
- Create: `arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat/models/ChatMessage.kt`

**Step 1: Create models directory**

```bash
mkdir -p arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat/models
```

**Step 2: Write ChatMessage sealed class**

Create `arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat/models/ChatMessage.kt`:
```kotlin
package io.github.devmugi.arcane.chat.models

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

**Step 3: Build and verify**

Run: `./gradlew :arcane-chat:build`
Expected: BUILD SUCCESSFUL

**Step 4: Commit**

```bash
git add arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat/models/
git commit -m "feat(arcane-chat): add ChatMessage model"
```

---

## Task 5: Migrate UserMessageBlock to arcane-chat

**Files:**
- Create: `arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat/components/messages/ArcaneUserMessageBlock.kt`
- Delete: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/UserMessageBlock.kt`

**Step 1: Create messages directory**

```bash
mkdir -p arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat/components/messages
```

**Step 2: Copy UserMessageBlock with updated package**

Create `arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat/components/messages/ArcaneUserMessageBlock.kt`:
```kotlin
package io.github.devmugi.arcane.chat.components.messages

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
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
    timestamp: String? = null,
    maxWidth: Dp = 280.dp,
    backgroundColor: Color = ArcaneTheme.colors.primary.copy(alpha = 0.15f),
    textStyle: TextStyle = ArcaneTheme.typography.bodyMedium
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
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = text,
                style = textStyle,
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

**Step 3: Build and verify**

Run: `./gradlew :arcane-chat:build`
Expected: BUILD SUCCESSFUL

**Step 4: Delete old file**

```bash
git rm arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/UserMessageBlock.kt
```

**Step 5: Commit**

```bash
git add arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat/components/messages/ArcaneUserMessageBlock.kt
git commit -m "feat(arcane-chat): migrate ArcaneUserMessageBlock from arcane-components"
```

---

## Task 6: Migrate AssistantMessageBlock to arcane-chat

**Files:**
- Create: `arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat/components/messages/ArcaneAssistantMessageBlock.kt`
- Delete: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/AssistantMessageBlock.kt`

**Step 1: Copy AssistantMessageBlock with updated package**

Create `arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat/components/messages/ArcaneAssistantMessageBlock.kt`:
```kotlin
package io.github.devmugi.arcane.chat.components.messages

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

private val AssistantMessageShape = RoundedCornerShape(
    topStart = 4.dp,
    topEnd = 12.dp,
    bottomStart = 12.dp,
    bottomEnd = 12.dp
)

@Composable
fun ArcaneAssistantMessageBlock(
    modifier: Modifier = Modifier,
    title: String? = null,
    isLoading: Boolean = false,
    maxContentHeight: Dp = 160.dp,
    enableTruncation: Boolean = true,
    showBottomActions: Boolean = false,
    autoShowWhenTruncated: Boolean = true,
    onShowMoreClick: (() -> Unit)? = null,
    titleActions: @Composable (RowScope.() -> Unit)? = null,
    bottomActions: @Composable (RowScope.() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography
    val density = LocalDensity.current

    var isExpanded by remember { mutableStateOf(false) }
    var isTruncated by remember { mutableStateOf(false) }

    val showTitleRow = title != null || isLoading
    val shouldShowBottomActions = showBottomActions ||
        (enableTruncation && autoShowWhenTruncated && isTruncated && !isExpanded)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(AssistantMessageShape)
            .background(colors.surfaceContainer)
            .padding(ArcaneSpacing.Small)
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

                // Right side: custom actions
                Row(
                    horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.XXSmall),
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                    if (enableTruncation && !isExpanded) {
                        Modifier.heightIn(max = maxContentHeight)
                    } else {
                        Modifier
                    }
                )
                .onSizeChanged { size ->
                    if (enableTruncation && !isExpanded) {
                        isTruncated = size.height >= maxHeightPx.toInt()
                    }
                }
                .drawWithContent {
                    drawContent()
                    if (enableTruncation && isTruncated && !isExpanded) {
                        drawRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, colors.surfaceContainer),
                                startY = size.height * 0.6f,
                                endY = size.height
                            )
                        )
                    }
                }
        ) {
            content()
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

**Step 2: Build and verify**

Run: `./gradlew :arcane-chat:build`
Expected: BUILD SUCCESSFUL

**Step 3: Delete old file**

```bash
git rm arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/AssistantMessageBlock.kt
```

**Step 4: Commit**

```bash
git add arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat/components/messages/ArcaneAssistantMessageBlock.kt
git commit -m "feat(arcane-chat): migrate ArcaneAssistantMessageBlock from arcane-components"
```

---

## Task 7: Migrate ChatMessageList to arcane-chat

**Files:**
- Create: `arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat/components/messages/ArcaneChatMessageList.kt`
- Delete: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/ChatMessageList.kt`

**Step 1: Copy ChatMessageList with updated package**

Create `arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat/components/messages/ArcaneChatMessageList.kt`:
```kotlin
package io.github.devmugi.arcane.chat.components.messages

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
                    .background(colors.surfaceContainer)
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

**Step 2: Build and verify**

Run: `./gradlew :arcane-chat:build`
Expected: BUILD SUCCESSFUL

**Step 3: Delete old file**

```bash
git rm arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/ChatMessageList.kt
```

**Step 4: Commit**

```bash
git add arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat/components/messages/ArcaneChatMessageList.kt
git commit -m "feat(arcane-chat): migrate ArcaneChatMessageList from arcane-components"
```

---

## Task 8: Migrate ChatScreenScaffold to arcane-chat

**Files:**
- Create: `arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat/components/scaffold/ArcaneChatScreenScaffold.kt`
- Delete: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/ChatScreenScaffold.kt`

**Step 1: Create scaffold directory**

```bash
mkdir -p arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat/components/scaffold
```

**Step 2: Copy ChatScreenScaffold with updated package**

Create `arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat/components/scaffold/ArcaneChatScreenScaffold.kt`:
```kotlin
package io.github.devmugi.arcane.chat.components.scaffold

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

**Step 3: Build and verify**

Run: `./gradlew :arcane-chat:build`
Expected: BUILD SUCCESSFUL

**Step 4: Delete old file**

```bash
git rm arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/ChatScreenScaffold.kt
```

**Step 5: Commit**

```bash
git add arcane-chat/src/commonMain/kotlin/io/github/devmugi/arcane/chat/components/scaffold/ArcaneChatScreenScaffold.kt
git commit -m "feat(arcane-chat): migrate ArcaneChatScreenScaffold from arcane-components"
```

---

## Task 9: Update main catalog ChatScreen imports

**Files:**
- Modify: `catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/screens/ChatScreen.kt`

**Step 1: Update imports in catalog ChatScreen**

Replace the import statements (lines 26-28) in `catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/screens/ChatScreen.kt`:

Old imports:
```kotlin
import io.github.devmugi.arcane.design.components.controls.ArcaneAssistantMessageBlock
import io.github.devmugi.arcane.design.components.controls.ArcaneChatScreenScaffold
import io.github.devmugi.arcane.design.components.controls.ArcaneUserMessageBlock
```

New imports:
```kotlin
import io.github.devmugi.arcane.chat.components.messages.ArcaneAssistantMessageBlock
import io.github.devmugi.arcane.chat.components.messages.ArcaneUserMessageBlock
import io.github.devmugi.arcane.chat.components.scaffold.ArcaneChatScreenScaffold
```

**Step 2: Add arcane-chat dependency to catalog**

Modify `catalog/composeApp/build.gradle.kts` - add to commonMain.dependencies:
```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":arcane-foundation"))
            implementation(project(":arcane-components"))
            implementation(project(":arcane-chat"))
        }
    }
}
```

**Step 3: Build and verify**

Run: `./gradlew :catalog:composeApp:build`
Expected: BUILD SUCCESSFUL

**Step 4: Commit**

```bash
git add catalog/composeApp/build.gradle.kts catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/screens/ChatScreen.kt
git commit -m "refactor(catalog): update imports to use arcane-chat module"
```

---

## Task 10: Create Device Preview Components

**Files:**
- Create: `catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/components/DevicePreview.kt`

**Step 1: Create components directory**

```bash
mkdir -p catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/components
```

**Step 2: Create DevicePreview.kt**

Create `catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/components/DevicePreview.kt`:
```kotlin
package io.github.devmugi.arcane.catalog.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.primitives.ArcaneSurface
import io.github.devmugi.arcane.design.foundation.primitives.SurfaceVariant

enum class DeviceType {
    None,
    Pixel8,
    iPhone16;

    val displayName: String
        get() = when (this) {
            None -> "No Preview"
            Pixel8 -> "Pixel 8"
            iPhone16 -> "iPhone 16"
        }
}

sealed class NotchType {
    data class PunchHole(
        val centerX: Dp,
        val y: Dp,
        val radius: Dp = 6.dp
    ) : NotchType()

    data class DynamicIsland(
        val centerX: Dp,
        val y: Dp,
        val width: Dp = 126.dp,
        val height: Dp = 37.dp
    ) : NotchType()
}

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
            modifier = modifier,
            content = content
        )
        DeviceType.iPhone16 -> DeviceFrame(
            screenWidth = 393.dp,
            screenHeight = 852.dp,
            cornerRadius = 55.dp,
            notchType = NotchType.DynamicIsland(centerX = 196.5.dp, y = 20.dp),
            modifier = modifier,
            content = content
        )
    }
}

@Composable
private fun DeviceFrame(
    screenWidth: Dp,
    screenHeight: Dp,
    cornerRadius: Dp,
    notchType: NotchType,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Device bezel with shadow
        ArcaneSurface(
            variant = SurfaceVariant.ContainerHigh,
            modifier = Modifier
                .size(width = screenWidth + 16.dp, height = screenHeight + 16.dp)
                .clip(RoundedCornerShape(cornerRadius + 4.dp))
                .border(
                    width = 2.dp,
                    color = Color.White.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(cornerRadius + 4.dp)
                )
        ) {
            // Screen content area
            Box(
                modifier = Modifier
                    .size(width = screenWidth, height = screenHeight)
                    .clip(RoundedCornerShape(cornerRadius))
                    .align(Alignment.Center)
            ) {
                content()

                // Notch overlay
                when (notchType) {
                    is NotchType.PunchHole -> {
                        Box(
                            modifier = Modifier
                                .size(notchType.radius * 2)
                                .align(Alignment.TopCenter)
                                .offset(x = notchType.centerX - screenWidth / 2, y = notchType.y)
                                .clip(CircleShape)
                                .background(Color.Black)
                                .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                        )
                    }
                    is NotchType.DynamicIsland -> {
                        Box(
                            modifier = Modifier
                                .size(width = notchType.width, height = notchType.height)
                                .align(Alignment.TopCenter)
                                .offset(
                                    x = notchType.centerX - screenWidth / 2,
                                    y = notchType.y
                                )
                                .clip(RoundedCornerShape(notchType.height / 2))
                                .background(Color.Black)
                                .border(
                                    1.dp,
                                    Color.White.copy(alpha = 0.2f),
                                    RoundedCornerShape(notchType.height / 2)
                                )
                        )
                    }
                }
            }
        }
    }
}

// Extension function for offset (needed for notch positioning)
private fun Modifier.offset(x: Dp = 0.dp, y: Dp = 0.dp): Modifier {
    return this.then(
        androidx.compose.foundation.layout.offset(x = x, y = y)
    )
}
```

**Step 3: Build and verify**

Run: `./gradlew :catalog-chat:composeApp:build`
Expected: BUILD SUCCESSFUL

**Step 4: Commit**

```bash
git add catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/components/DevicePreview.kt
git commit -m "feat(catalog-chat): add device preview with Pixel 8 and iPhone 16 frames"
```

---

## Task 11: Create CatalogTopBar Components

**Files:**
- Create: `catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/components/CatalogTopBar.kt`

**Step 1: Create CatalogTopBar.kt**

Create `catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/components/CatalogTopBar.kt`:
```kotlin
package io.github.devmugi.arcane.catalog.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.components.controls.ArcaneButton
import io.github.devmugi.arcane.design.components.controls.ArcaneButtonStyle
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

enum class CatalogTab {
    Chat,
    MessageBlocks,
    ChatInput;

    val displayName: String
        get() = when (this) {
            Chat -> "Chat"
            MessageBlocks -> "Message Blocks"
            ChatInput -> "Chat Input"
        }
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
            CatalogTab.entries.forEach { tab ->
                ArcaneButton(
                    text = tab.displayName,
                    style = if (selectedTab == tab) {
                        ArcaneButtonStyle.Primary
                    } else {
                        ArcaneButtonStyle.Outlined()
                    },
                    onClick = { onTabSelected(tab) }
                )
            }
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

@Composable
private fun DeviceSelector(
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
            DeviceType.entries.forEach { device ->
                DropdownMenuItem(
                    text = { Text(device.displayName) },
                    onClick = {
                        onDeviceSelected(device)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun ThemeToggleButton(
    isDark: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onToggle,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
            contentDescription = if (isDark) "Switch to light mode" else "Switch to dark mode",
            tint = ArcaneTheme.colors.text,
            modifier = Modifier.size(24.dp)
        )
    }
}
```

**Step 2: Build and verify**

Run: `./gradlew :catalog-chat:composeApp:build`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/components/CatalogTopBar.kt
git commit -m "feat(catalog-chat): add CatalogTopBar with tabs, device selector, and theme toggle"
```

---

## Task 12: Create Mock Chat Data

**Files:**
- Create: `catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/data/MockChatData.kt`

**Step 1: Create data directory**

```bash
mkdir -p catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/data
```

**Step 2: Create MockChatData.kt**

Create `catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/data/MockChatData.kt`:
```kotlin
package io.github.devmugi.arcane.catalog.chat.data

import io.github.devmugi.arcane.chat.models.ChatMessage

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

**Step 3: Build and verify**

Run: `./gradlew :catalog-chat:composeApp:build`
Expected: BUILD SUCCESSFUL

**Step 4: Commit**

```bash
git add catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/data/MockChatData.kt
git commit -m "feat(catalog-chat): add mock chat data for testing and prototyping"
```

---

## Task 13: Create Chat Screen

**Files:**
- Create: `catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/screens/ChatScreen.kt`

**Step 1: Create screens directory**

```bash
mkdir -p catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/screens
```

**Step 2: Create ChatScreen.kt**

Create `catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/screens/ChatScreen.kt`:
```kotlin
package io.github.devmugi.arcane.catalog.chat.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.devmugi.arcane.catalog.chat.components.DevicePreview
import io.github.devmugi.arcane.catalog.chat.components.DeviceType
import io.github.devmugi.arcane.catalog.chat.data.MockChatData
import io.github.devmugi.arcane.chat.components.messages.ArcaneAssistantMessageBlock
import io.github.devmugi.arcane.chat.components.messages.ArcaneChatMessageList
import io.github.devmugi.arcane.chat.components.messages.ArcaneUserMessageBlock
import io.github.devmugi.arcane.chat.components.scaffold.ArcaneChatScreenScaffold
import io.github.devmugi.arcane.chat.models.ChatMessage
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

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

**Step 3: Build and verify**

Run: `./gradlew :catalog-chat:composeApp:build`
Expected: BUILD SUCCESSFUL

**Step 4: Commit**

```bash
git add catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/screens/ChatScreen.kt
git commit -m "feat(catalog-chat): add ChatScreen with full chat experience"
```

---

## Task 14: Create Message Blocks Screen

**Files:**
- Create: `catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/screens/MessageBlocksScreen.kt`

**Step 1: Create MessageBlocksScreen.kt**

Create `catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/screens/MessageBlocksScreen.kt`:
```kotlin
package io.github.devmugi.arcane.catalog.chat.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.catalog.chat.components.DevicePreview
import io.github.devmugi.arcane.catalog.chat.components.DeviceType
import io.github.devmugi.arcane.chat.components.messages.ArcaneAssistantMessageBlock
import io.github.devmugi.arcane.chat.components.messages.ArcaneUserMessageBlock
import io.github.devmugi.arcane.design.foundation.primitives.ArcaneSurface
import io.github.devmugi.arcane.design.foundation.primitives.SurfaceVariant
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun MessageBlocksScreen(deviceType: DeviceType) {
    val typography = ArcaneTheme.typography
    val colors = ArcaneTheme.colors

    DevicePreview(deviceType = deviceType) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(ArcaneSpacing.Medium),
            verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Large)
        ) {
            // User Messages Section
            SectionTitle("User Messages")
            ArcaneSurface(
                variant = SurfaceVariant.Container,
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
                variant = SurfaceVariant.Container,
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
                        title = "Claude"
                    ) {
                        Text(
                            text = "Hello! I'd be happy to help you with that.",
                            style = typography.bodyMedium,
                            color = colors.text
                        )
                    }

                    Text(
                        text = "With title + loading",
                        style = typography.labelMedium,
                        color = colors.textSecondary
                    )
                    ArcaneAssistantMessageBlock(
                        title = "Claude",
                        isLoading = true
                    ) {
                        Text(
                            text = "Thinking...",
                            style = typography.bodyMedium,
                            color = colors.text
                        )
                    }

                    Text(
                        text = "With title actions",
                        style = typography.labelMedium,
                        color = colors.textSecondary
                    )
                    ArcaneAssistantMessageBlock(
                        title = "Claude",
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
                    ) {
                        Text(
                            text = "Here's a helpful response with extra actions.",
                            style = typography.bodyMedium,
                            color = colors.text
                        )
                    }
                }
            }

            // Assistant Messages - Truncated Section
            SectionTitle("Assistant Messages - Truncated")
            ArcaneSurface(
                variant = SurfaceVariant.Container,
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
                        title = "Claude",
                        maxContentHeight = 160.dp
                    ) {
                        Text(
                            text = longText,
                            style = typography.bodyMedium,
                            color = colors.text
                        )
                    }
                }
            }

            // Assistant Messages - Custom Actions Section
            SectionTitle("Assistant Messages - Custom Actions")
            ArcaneSurface(
                variant = SurfaceVariant.Container,
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
                        title = "Claude",
                        showBottomActions = true,
                        autoShowWhenTruncated = false,
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
                    ) {
                        Text(
                            text = "Here's a response with custom bottom actions always visible.",
                            style = typography.bodyMedium,
                            color = colors.text
                        )
                    }

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
                        title = "Claude",
                        maxContentHeight = 100.dp,
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
                    ) {
                        Text(
                            text = longTextWithShare,
                            style = typography.bodyMedium,
                            color = colors.text
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(ArcaneSpacing.XLarge))
        }
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

**Step 2: Build and verify**

Run: `./gradlew :catalog-chat:composeApp:build`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/screens/MessageBlocksScreen.kt
git commit -m "feat(catalog-chat): add MessageBlocksScreen with individual component showcase"
```

---

## Task 15: Create Chat Input Screen (Placeholder)

**Files:**
- Create: `catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/screens/ChatInputScreen.kt`

**Step 1: Create ChatInputScreen.kt**

Create `catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/screens/ChatInputScreen.kt`:
```kotlin
package io.github.devmugi.arcane.catalog.chat.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.devmugi.arcane.catalog.chat.components.DevicePreview
import io.github.devmugi.arcane.catalog.chat.components.DeviceType
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun ChatInputScreen(deviceType: DeviceType) {
    DevicePreview(deviceType = deviceType) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(ArcaneSpacing.Large),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Chat Input Components",
                style = ArcaneTheme.typography.headlineLarge,
                color = ArcaneTheme.colors.text
            )
            Text(
                text = "Coming soon...",
                style = ArcaneTheme.typography.bodyLarge,
                color = ArcaneTheme.colors.textSecondary
            )
        }
    }
}
```

**Step 2: Build and verify**

Run: `./gradlew :catalog-chat:composeApp:build`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/screens/ChatInputScreen.kt
git commit -m "feat(catalog-chat): add ChatInputScreen placeholder"
```

---

## Task 16: Create App.kt with Navigation and State

**Files:**
- Create: `catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/App.kt`

**Step 1: Create App.kt**

Create `catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/App.kt`:
```kotlin
package io.github.devmugi.arcane.catalog.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import io.github.devmugi.arcane.catalog.chat.components.CatalogTab
import io.github.devmugi.arcane.catalog.chat.components.CatalogTopBar
import io.github.devmugi.arcane.catalog.chat.components.DeviceType
import io.github.devmugi.arcane.catalog.chat.screens.ChatInputScreen
import io.github.devmugi.arcane.catalog.chat.screens.ChatScreen
import io.github.devmugi.arcane.catalog.chat.screens.MessageBlocksScreen
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme

// Custom savers for enums
private val DeviceTypeSaver = Saver<DeviceType, String>(
    save = { it.name },
    restore = { DeviceType.valueOf(it) }
)

private val CatalogTabSaver = Saver<CatalogTab, String>(
    save = { it.name },
    restore = { CatalogTab.valueOf(it) }
)

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

**Step 2: Build and verify**

Run: `./gradlew :catalog-chat:composeApp:build`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/App.kt
git commit -m "feat(catalog-chat): add App with navigation and persistent state management"
```

---

## Task 17: Create Platform Entry Points

**Files:**
- Create: `catalog-chat/composeApp/src/desktopMain/kotlin/io/github/devmugi/arcane/catalog/chat/Main.kt`
- Create: `catalog-chat/composeApp/src/androidMain/kotlin/io/github/devmugi/arcane/catalog/chat/MainActivity.kt`
- Create: `catalog-chat/composeApp/src/androidMain/AndroidManifest.xml`

**Step 1: Create Desktop main function**

Create `catalog-chat/composeApp/src/desktopMain/kotlin/io/github/devmugi/arcane/catalog/chat/Main.kt`:
```kotlin
package io.github.devmugi.arcane.catalog.chat

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Arcane Chat Catalog"
    ) {
        App()
    }
}
```

**Step 2: Create Android MainActivity**

Create `catalog-chat/composeApp/src/androidMain/kotlin/io/github/devmugi/arcane/catalog/chat/MainActivity.kt`:
```kotlin
package io.github.devmugi.arcane.catalog.chat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App()
        }
    }
}
```

**Step 3: Create AndroidManifest.xml**

```bash
mkdir -p catalog-chat/composeApp/src/androidMain
```

Create `catalog-chat/composeApp/src/androidMain/AndroidManifest.xml`:
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    <application
        android:allowBackup="true"
        android:icon="@android:drawable/ic_dialog_info"
        android:label="Arcane Chat Catalog"
        android:supportsRtl="true"
        android:theme="@android:style/Theme.Material.NoActionBar">
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:configChanges="orientation|screenSize|screenLayout|keyboardHidden">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
</manifest>
```

**Step 4: Build and verify all platforms**

Run: `./gradlew :catalog-chat:composeApp:build`
Expected: BUILD SUCCESSFUL

**Step 5: Commit**

```bash
git add catalog-chat/composeApp/src/desktopMain/ catalog-chat/composeApp/src/androidMain/
git commit -m "feat(catalog-chat): add platform entry points for Desktop and Android"
```

---

## Task 18: Test Desktop Application

**Files:**
- None (verification only)

**Step 1: Run desktop app**

Run: `./gradlew :catalog-chat:composeApp:run`
Expected: Application window opens showing Chat tab

**Step 2: Test navigation**

Manual testing:
1. Click "Message Blocks" tab - should switch to Message Blocks screen
2. Click "Chat Input" tab - should show "Coming soon" placeholder
3. Click "Chat" tab - should switch back to Chat screen

**Step 3: Test device preview**

Manual testing:
1. Click device selector dropdown
2. Select "Pixel 8" - should wrap content in Pixel 8 frame with punch-hole notch
3. Select "iPhone 16" - should wrap content in iPhone 16 frame with Dynamic Island
4. Select "No Preview" - should show content without frame

**Step 4: Test theme toggle**

Manual testing:
1. Click theme toggle icon (moon/sun)
2. Interface should switch between light and dark theme
3. Close and reopen app - theme preference should persist

**Step 5: Verify and document**

Create test notes documenting any issues found.

---

## Task 19: Final Build Verification

**Files:**
- None (verification only)

**Step 1: Clean build all modules**

Run: `./gradlew clean build`
Expected: BUILD SUCCESSFUL for all modules

**Step 2: Verify arcane-chat builds**

Run: `./gradlew :arcane-chat:build`
Expected: BUILD SUCCESSFUL

**Step 3: Verify catalog-chat builds**

Run: `./gradlew :catalog-chat:composeApp:build`
Expected: BUILD SUCCESSFUL

**Step 4: Verify main catalog still builds**

Run: `./gradlew :catalog:composeApp:build`
Expected: BUILD SUCCESSFUL (with arcane-chat imports)

**Step 5: Create final commit**

```bash
git add -A
git commit -m "chore: verify all modules build successfully after chat extraction"
```

---

## Success Criteria Checklist

Verify all criteria are met:

- [ ] `arcane-chat` module builds and publishes successfully
- [ ] All chat components work in new module with correct imports
- [ ] `catalog-chat` runs on Desktop (test: `./gradlew :catalog-chat:composeApp:run`)
- [ ] Device preview renders correctly for Pixel 8 and iPhone 16
- [ ] Theme switching works (toggle between light/dark)
- [ ] Theme persists across app restarts
- [ ] Tab navigation works (Chat, Message Blocks, Chat Input)
- [ ] Tab selection persists across app restarts
- [ ] Mock chat data displays correctly in all screens
- [ ] No build errors or dependency conflicts
- [ ] Main catalog app still works with updated imports

---

## Notes

**DRY Principle:** Components are defined once in arcane-chat and reused in catalog-chat. No code duplication.

**YAGNI Principle:** Only implementing what's in the design doc. No extra features added.

**Testing:** Manual testing for UI components. Focus on visual verification and interaction testing.

**Commits:** Frequent, atomic commits after each logical step. Each commit is self-contained and buildable.

**Module Architecture:** Clean dependency graph:
- arcane-foundation (base)
- arcane-components (depends on foundation)
- arcane-chat (depends on components + foundation)
- catalog-chat (depends on chat + components + foundation)

**Future Work:** Chat input components, iOS testing, Android device testing, additional device frames.
