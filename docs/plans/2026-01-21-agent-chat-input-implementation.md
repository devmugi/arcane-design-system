# AgentChatInput Component - Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Implement a specialized chat input component for AI assistant conversations with text auto-expand, voice capabilities, and slot-based customization.

**Architecture:** Single-file component in `controls/` following existing TextField and Button patterns. Uses `BasicTextField` with custom decoration, `AnimatedVisibility` for contextual buttons, and slot lambdas for extensibility. Catalog showcase added to ControlsScreen.

**Tech Stack:** Kotlin, Compose Multiplatform, Material Icons, Arcane foundation tokens

---

## Task 1: Create AgentChatInput Component File

**Files:**
- Create: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/AgentChatInput.kt`

**Step 1: Create the file with imports and function signature**

```kotlin
// arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/AgentChatInput.kt
package io.github.devmugi.arcane.design.components.controls

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneBorder
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

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
) {
    // Implementation goes here
}
```

**Step 2: Verify file compiles**

Run: `./gradlew :arcane-components:compileKotlinDesktop --quiet`
Expected: Build succeeds (warnings OK)

**Step 3: Commit**

```bash
git add arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/AgentChatInput.kt
git commit -m "feat(controls): add AgentChatInput component skeleton"
```

---

## Task 2: Implement Core Layout Structure

**Files:**
- Modify: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/AgentChatInput.kt`

**Step 1: Add theme access and focus state tracking**

Replace the `// Implementation goes here` comment with:

```kotlin
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography
    val isFocused by interactionSource.collectIsFocusedAsState()
    val hasText = value.isNotBlank()

    val borderColor by animateColorAsState(
        targetValue = when {
            !enabled -> colors.textDisabled.copy(alpha = 0.3f)
            isFocused -> colors.borderFocused
            else -> colors.border
        },
        animationSpec = tween(150),
        label = "borderColor"
    )

    val contentColor = if (enabled) colors.textSecondary else colors.textDisabled

    // Calculate max height for auto-expand (approximate line height * maxLines)
    val lineHeight = with(LocalDensity.current) { typography.bodyLarge.lineHeight.toDp() }
    val verticalPadding = ArcaneSpacing.Small * 2
    val maxHeight = lineHeight * maxLines + verticalPadding

    Column(
        modifier = modifier
            .clip(ArcaneRadius.Large)
            .border(ArcaneBorder.Thin, borderColor, ArcaneRadius.Large)
            .padding(ArcaneSpacing.XSmall),
        verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)
    ) {
        // Row 1: Text Input
        // Row 2: Actions Bar
    }
```

**Step 2: Add Row 1 - Text Input Area**

Replace `// Row 1: Text Input` with:

```kotlin
        // Row 1: Text Input
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 40.dp, max = maxHeight)
                .clip(ArcaneRadius.Medium)
                .background(colors.surfaceInset)
                .padding(horizontal = ArcaneSpacing.Medium, vertical = ArcaneSpacing.Small)
                .onPreviewKeyEvent { keyEvent ->
                    if (keyEvent.type == KeyEventType.KeyDown &&
                        keyEvent.key == Key.Enter &&
                        !keyEvent.isShiftPressed &&
                        hasText
                    ) {
                        onSend()
                        true
                    } else {
                        false
                    }
                },
            enabled = enabled,
            textStyle = typography.bodyLarge.copy(color = colors.text),
            cursorBrush = SolidColor(colors.primary),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = { if (hasText) onSend() }),
            interactionSource = interactionSource,
            decorationBox = { innerTextField ->
                Box {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = typography.bodyLarge,
                            color = colors.textSecondary
                        )
                    }
                    innerTextField()
                }
            }
        )
```

**Step 3: Add Row 2 - Actions Bar placeholder**

Replace `// Row 2: Actions Bar` with:

```kotlin
        // Row 2: Actions Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = ArcaneSpacing.XSmall),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side: Add button + active items
            Row(
                horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Add button placeholder
                // Active items slot placeholder
            }

            // Right side: Voice buttons or Send button
            Row(
                horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Action buttons placeholder
            }
        }
```

**Step 4: Verify compilation**

Run: `./gradlew :arcane-components:compileKotlinDesktop --quiet`
Expected: Build succeeds

**Step 5: Commit**

```bash
git add -A
git commit -m "feat(controls): implement AgentChatInput core layout structure"
```

---

## Task 3: Implement Action Buttons

**Files:**
- Modify: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/AgentChatInput.kt`

**Step 1: Add private IconButton helper composable**

Add this after the main function, before the closing of the file:

```kotlin
@Composable
private fun ChatInputIconButton(
    onClick: () -> Unit,
    enabled: Boolean,
    contentDescription: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val colors = ArcaneTheme.colors
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(ArcaneRadius.Full)
            .clickable(enabled = enabled, onClick = onClick)
            .semantics { this.contentDescription = contentDescription },
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
private fun SendButton(
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    val colors = ArcaneTheme.colors
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(ArcaneRadius.Full)
            .background(if (enabled) colors.primary else colors.textDisabled)
            .clickable(enabled = enabled, onClick = onClick)
            .semantics { contentDescription = "Send message" },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Send,
            contentDescription = null,
            tint = colors.text,
            modifier = Modifier.size(20.dp)
        )
    }
}
```

**Step 2: Replace Add button placeholder**

Find `// Add button placeholder` and replace with:

```kotlin
                // Add button
                if (addMenuContent != null) {
                    Box {
                        ChatInputIconButton(
                            onClick = { /* Menu shown via slot */ },
                            enabled = enabled,
                            contentDescription = "Add attachment or context"
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = contentColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        addMenuContent()
                    }
                }
```

**Step 3: Replace Active items slot placeholder**

Find `// Active items slot placeholder` and replace with:

```kotlin
                // Active items slot
                if (activeItemsContent != null) {
                    activeItemsContent()
                }
```

**Step 4: Replace Action buttons placeholder**

Find `// Action buttons placeholder` and replace with:

```kotlin
                // Voice buttons (shown when no text)
                AnimatedVisibility(
                    visible = !hasText,
                    enter = fadeIn(tween(150)) + scaleIn(tween(150)),
                    exit = fadeOut(tween(150)) + scaleOut(tween(150))
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (onVoiceToTextClick != null) {
                            ChatInputIconButton(
                                onClick = onVoiceToTextClick,
                                enabled = enabled,
                                contentDescription = "Voice to text"
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Mic,
                                    contentDescription = null,
                                    tint = contentColor,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        if (onAudioRecordClick != null) {
                            ChatInputIconButton(
                                onClick = onAudioRecordClick,
                                enabled = enabled,
                                contentDescription = "Record audio message"
                            ) {
                                Icon(
                                    imageVector = Icons.Default.GraphicEq,
                                    contentDescription = null,
                                    tint = contentColor,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                // Send button (shown when has text)
                AnimatedVisibility(
                    visible = hasText,
                    enter = fadeIn(tween(150)) + scaleIn(tween(150)),
                    exit = fadeOut(tween(150)) + scaleOut(tween(150))
                ) {
                    SendButton(
                        onClick = onSend,
                        enabled = enabled
                    )
                }
```

**Step 5: Verify compilation**

Run: `./gradlew :arcane-components:compileKotlinDesktop --quiet`
Expected: Build succeeds

**Step 6: Commit**

```bash
git add -A
git commit -m "feat(controls): implement AgentChatInput action buttons with animations"
```

---

## Task 4: Add Catalog Showcase

**Files:**
- Modify: `catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/screens/ControlsScreen.kt`

**Step 1: Add import for ArcaneAgentChatInput**

Add this import near the other control imports:

```kotlin
import io.github.devmugi.arcane.design.components.controls.ArcaneAgentChatInput
```

**Step 2: Add AgentChatInput section before the final Spacer**

Find `Spacer(modifier = Modifier.height(ArcaneSpacing.XLarge))` and add before it:

```kotlin
        // AgentChatInput Section
        SectionTitle("Agent Chat Input")
        ArcaneSurface(
            variant = SurfaceVariant.Raised,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Medium)
            ) {
                // Empty state with voice buttons
                var chatText1 by remember { mutableStateOf("") }
                Text(
                    text = "Empty (with voice)",
                    style = typography.labelMedium,
                    color = colors.textSecondary
                )
                ArcaneAgentChatInput(
                    value = chatText1,
                    onValueChange = { chatText1 = it },
                    onSend = { chatText1 = "" },
                    onVoiceToTextClick = {},
                    onAudioRecordClick = {}
                )

                // With text (shows send button)
                var chatText2 by remember { mutableStateOf("Hello, Claude!") }
                Text(
                    text = "With text (send button)",
                    style = typography.labelMedium,
                    color = colors.textSecondary
                )
                ArcaneAgentChatInput(
                    value = chatText2,
                    onValueChange = { chatText2 = it },
                    onSend = { chatText2 = "" },
                    onVoiceToTextClick = {},
                    onAudioRecordClick = {}
                )

                // Disabled state
                Text(
                    text = "Disabled",
                    style = typography.labelMedium,
                    color = colors.textSecondary
                )
                ArcaneAgentChatInput(
                    value = "",
                    onValueChange = {},
                    onSend = {},
                    enabled = false,
                    onVoiceToTextClick = {},
                    onAudioRecordClick = {}
                )
            }
        }

```

**Step 3: Verify catalog compiles**

Run: `./gradlew :catalog:composeApp:compileKotlinDesktop --quiet`
Expected: Build succeeds

**Step 4: Commit**

```bash
git add -A
git commit -m "feat(catalog): add AgentChatInput showcase to ControlsScreen"
```

---

## Task 5: Run Desktop Catalog for Visual Verification

**Step 1: Run the catalog app**

Run: `./gradlew :catalog:composeApp:run`
Expected: Desktop app launches, navigate to Controls screen, scroll to "Agent Chat Input" section

**Step 2: Verify visually**

Check:
- Empty input shows placeholder "Reply to Claude..."
- Empty input shows microphone and waveform icons on right
- Typing text makes voice icons disappear and send button appear
- Send button is purple with white arrow
- Clicking send (or pressing Enter) clears the text
- Disabled state shows grayed out UI
- Focus state shows brighter purple border

**Step 3: Commit any fixes if needed**

If everything looks correct, proceed. Otherwise, fix issues and commit.

---

## Task 6: Final Cleanup and Documentation

**Step 1: Verify all builds pass**

Run: `./gradlew :arcane-components:compileKotlinDesktop :catalog:composeApp:compileKotlinDesktop --quiet`
Expected: Both succeed

**Step 2: Final commit with feature complete message**

```bash
git add -A
git commit -m "feat(controls): complete ArcaneAgentChatInput component

Implements specialized chat input for AI assistant conversations:
- Auto-expanding text input (up to maxLines)
- Contextual action buttons (voice when empty, send when has text)
- Animated transitions between states
- Slot-based customization (addMenuContent, activeItemsContent)
- Full accessibility support
- Catalog showcase with multiple demo states"
```

---

## Verification Checklist

After completing all tasks, verify:

- [ ] `./gradlew :arcane-components:compileKotlinDesktop` passes
- [ ] `./gradlew :catalog:composeApp:compileKotlinDesktop` passes
- [ ] Desktop catalog app runs and shows AgentChatInput section
- [ ] Empty state shows voice buttons
- [ ] Text entry shows send button (voice buttons hidden)
- [ ] Enter key sends message (when text present)
- [ ] Shift+Enter adds newline
- [ ] Disabled state is visually distinct
- [ ] Focus state shows bright purple border
