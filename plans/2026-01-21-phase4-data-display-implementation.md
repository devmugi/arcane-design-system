# Phase 4: Data Display Components Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Implement 6 data display components (Badge, Avatar, ListItem, Card, Tooltip, Table) with catalog integration.

**Architecture:** Components live in `arcane-components/.../display/` package. Each component follows established patterns: sealed classes for styles, data classes for configuration, composables with ArcaneTheme integration. Catalog screen demonstrates all components.

**Tech Stack:** Kotlin, Compose Multiplatform 1.7.1, ArcaneTheme tokens

---

## Task 1: Create ArcaneBadge Component

**Files:**
- Create: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/display/Badge.kt`

**Step 1: Create the display package and Badge.kt file**

```kotlin
// arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/display/Badge.kt
package io.github.devmugi.arcane.design.components.display

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Immutable
sealed class ArcaneBadgeStyle {
    data object Default : ArcaneBadgeStyle()
    data object Success : ArcaneBadgeStyle()
    data object Warning : ArcaneBadgeStyle()
    data object Error : ArcaneBadgeStyle()
    data object Neutral : ArcaneBadgeStyle()
    data class Custom(
        val backgroundColor: Color,
        val contentColor: Color
    ) : ArcaneBadgeStyle()
}

@Composable
fun ArcaneBadge(
    text: String,
    modifier: Modifier = Modifier,
    style: ArcaneBadgeStyle = ArcaneBadgeStyle.Default
) {
    val colors = ArcaneTheme.colors

    val (backgroundColor, contentColor) = when (style) {
        is ArcaneBadgeStyle.Default -> colors.primary.copy(alpha = 0.2f) to colors.primary
        is ArcaneBadgeStyle.Success -> colors.success.copy(alpha = 0.2f) to colors.success
        is ArcaneBadgeStyle.Warning -> colors.warning.copy(alpha = 0.2f) to colors.warning
        is ArcaneBadgeStyle.Error -> colors.error.copy(alpha = 0.2f) to colors.error
        is ArcaneBadgeStyle.Neutral -> colors.textSecondary.copy(alpha = 0.2f) to colors.textSecondary
        is ArcaneBadgeStyle.Custom -> style.backgroundColor to style.contentColor
    }

    val borderColor = when (style) {
        is ArcaneBadgeStyle.Custom -> style.contentColor.copy(alpha = 0.5f)
        else -> contentColor.copy(alpha = 0.5f)
    }

    Box(
        modifier = modifier
            .height(20.dp)
            .background(backgroundColor, ArcaneRadius.Small)
            .border(1.dp, borderColor, ArcaneRadius.Small)
            .padding(horizontal = ArcaneSpacing.XSmall),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = ArcaneTheme.typography.labelSmall,
            color = contentColor
        )
    }
}
```

**Step 2: Verify build compiles**

Run: `./gradlew :arcane-components:compileKotlinDesktop`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/display/Badge.kt
git commit -m "feat: add ArcaneBadge component with 5 styles"
```

---

## Task 2: Create ArcaneAvatar Component

**Files:**
- Create: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/display/Avatar.kt`

**Step 1: Create Avatar.kt with size enum and data class**

```kotlin
// arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/display/Avatar.kt
package io.github.devmugi.arcane.design.components.display

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme

@Immutable
enum class ArcaneAvatarSize(val dp: Dp) {
    Small(24.dp),
    Medium(32.dp),
    Large(48.dp)
}

@Immutable
data class ArcaneAvatarData(
    val image: Painter? = null,
    val name: String? = null
)

private fun getInitials(name: String?): String {
    if (name.isNullOrBlank()) return "?"
    val parts = name.trim().split(" ").filter { it.isNotEmpty() }
    return when {
        parts.size >= 2 -> "${parts.first().first()}${parts.last().first()}"
        parts.size == 1 -> parts.first().take(2)
        else -> "?"
    }.uppercase()
}

@Composable
fun ArcaneAvatar(
    modifier: Modifier = Modifier,
    image: Painter? = null,
    name: String? = null,
    size: ArcaneAvatarSize = ArcaneAvatarSize.Medium
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography

    val textStyle = when (size) {
        ArcaneAvatarSize.Small -> typography.labelSmall
        ArcaneAvatarSize.Medium -> typography.labelMedium
        ArcaneAvatarSize.Large -> typography.labelLarge
    }

    Box(
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(colors.surfaceInset)
            .border(2.dp, colors.surface, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (image != null) {
            Image(
                painter = image,
                contentDescription = name,
                modifier = Modifier.size(size.dp),
                contentScale = ContentScale.Crop
            )
        } else {
            Text(
                text = getInitials(name),
                style = textStyle,
                color = colors.text
            )
        }
    }
}

@Composable
fun ArcaneAvatarGroup(
    avatars: List<ArcaneAvatarData>,
    modifier: Modifier = Modifier,
    maxVisible: Int = 3,
    size: ArcaneAvatarSize = ArcaneAvatarSize.Medium,
    overlap: Dp = 8.dp
) {
    val colors = ArcaneTheme.colors
    val visibleAvatars = avatars.take(maxVisible)
    val overflowCount = (avatars.size - maxVisible).coerceAtLeast(0)

    Box(modifier = modifier) {
        visibleAvatars.forEachIndexed { index, avatarData ->
            Box(
                modifier = Modifier
                    .offset(x = (size.dp - overlap) * index)
            ) {
                ArcaneAvatar(
                    image = avatarData.image,
                    name = avatarData.name,
                    size = size
                )
            }
        }

        if (overflowCount > 0) {
            Box(
                modifier = Modifier
                    .offset(x = (size.dp - overlap) * visibleAvatars.size)
                    .size(size.dp)
                    .clip(CircleShape)
                    .background(colors.surfaceRaised)
                    .border(2.dp, colors.surface, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+$overflowCount",
                    style = when (size) {
                        ArcaneAvatarSize.Small -> ArcaneTheme.typography.labelSmall
                        ArcaneAvatarSize.Medium -> ArcaneTheme.typography.labelMedium
                        ArcaneAvatarSize.Large -> ArcaneTheme.typography.labelLarge
                    },
                    color = colors.text
                )
            }
        }
    }
}
```

**Step 2: Verify build compiles**

Run: `./gradlew :arcane-components:compileKotlinDesktop`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/display/Avatar.kt
git commit -m "feat: add ArcaneAvatar and ArcaneAvatarGroup components"
```

---

## Task 3: Create ArcaneListItem Component

**Files:**
- Create: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/display/ListItem.kt`

**Step 1: Create ListItem.kt**

```kotlin
// arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/display/ListItem.kt
package io.github.devmugi.arcane.design.components.display

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneIconography
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun ArcaneListItem(
    headlineText: String,
    modifier: Modifier = Modifier,
    supportingText: String? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .defaultMinSize(minHeight = 56.dp)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick
                    )
                } else Modifier
            )
            .padding(
                horizontal = ArcaneSpacing.Medium,
                vertical = ArcaneSpacing.Small
            ),
        horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leadingContent?.invoke()

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XXSmall)
        ) {
            Text(
                text = headlineText,
                style = typography.bodyLarge,
                color = colors.text
            )
            if (supportingText != null) {
                Text(
                    text = supportingText,
                    style = typography.bodySmall,
                    color = colors.textSecondary
                )
            }
        }

        trailingContent?.invoke()
    }
}

@Composable
fun ArcaneListItemIcon(
    painter: Painter,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    tint: Color = ArcaneTheme.colors.primary
) {
    val colors = ArcaneTheme.colors

    Box(
        modifier = modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(colors.surfaceInset),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier.size(ArcaneIconography.Small),
            tint = tint
        )
    }
}
```

**Step 2: Verify build compiles**

Run: `./gradlew :arcane-components:compileKotlinDesktop`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/display/ListItem.kt
git commit -m "feat: add ArcaneListItem and ArcaneListItemIcon components"
```

---

## Task 4: Create ArcaneCard Component

**Files:**
- Create: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/display/Card.kt`

**Step 1: Create Card.kt with slot components**

```kotlin
// arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/display/Card.kt
package io.github.devmugi.arcane.design.components.display

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import io.github.devmugi.arcane.design.foundation.primitives.ArcaneSurface
import io.github.devmugi.arcane.design.foundation.primitives.SurfaceVariant
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun ArcaneCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = ArcaneTheme.colors
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    val glowAlpha by animateFloatAsState(
        targetValue = when {
            onClick == null -> 0f
            isPressed -> 0.4f
            isHovered -> 0.25f
            else -> 0f
        },
        animationSpec = tween(150),
        label = "cardGlowAlpha"
    )

    Box(
        modifier = modifier
            .then(
                if (glowAlpha > 0f) {
                    Modifier.drawBehind {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    colors.glow.copy(alpha = glowAlpha),
                                    Color.Transparent
                                ),
                                center = Offset(size.width / 2, size.height / 2),
                                radius = maxOf(size.width, size.height) * 0.6f
                            )
                        )
                    }
                } else Modifier
            )
    ) {
        ArcaneSurface(
            variant = SurfaceVariant.Raised,
            modifier = Modifier
                .clip(ArcaneRadius.Large)
                .then(
                    if (onClick != null) {
                        Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = onClick
                        )
                    } else Modifier
                )
        ) {
            Column(content = content)
        }
    }
}

@Composable
fun ArcaneCardImage(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier
            .fillMaxWidth()
            .clip(ArcaneRadius.Large),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun ArcaneCardContent(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography

    Column(
        modifier = modifier.padding(ArcaneSpacing.Medium),
        verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)
    ) {
        Text(
            text = title,
            style = typography.headlineMedium,
            color = colors.text
        )
        if (description != null) {
            Text(
                text = description,
                style = typography.bodyMedium,
                color = colors.textSecondary
            )
        }
    }
}

@Composable
fun ArcaneCardActions(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier.padding(
            start = ArcaneSpacing.Medium,
            end = ArcaneSpacing.Medium,
            bottom = ArcaneSpacing.Medium
        ),
        horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small),
        content = content
    )
}
```

**Step 2: Verify build compiles**

Run: `./gradlew :arcane-components:compileKotlinDesktop`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/display/Card.kt
git commit -m "feat: add ArcaneCard with image, content, and actions slots"
```

---

## Task 5: Create ArcaneTooltip Component

**Files:**
- Create: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/display/Tooltip.kt`

**Step 1: Create Tooltip.kt**

```kotlin
// arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/display/Tooltip.kt
package io.github.devmugi.arcane.design.components.display

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing
import kotlinx.coroutines.delay

@Composable
fun ArcaneTooltipBox(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val colors = ArcaneTheme.colors

    Box(
        modifier = modifier
            .shadow(4.dp, ArcaneRadius.Small)
            .background(colors.surfaceRaised, ArcaneRadius.Small)
            .padding(
                horizontal = ArcaneSpacing.XSmall,
                vertical = ArcaneSpacing.XXSmall
            )
            .widthIn(max = 200.dp)
    ) {
        content()
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ArcaneTooltip(
    tooltip: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    delayMs: Long = 500L,
    content: @Composable () -> Unit
) {
    var isHovered by remember { mutableStateOf(false) }
    var showTooltip by remember { mutableStateOf(false) }

    LaunchedEffect(isHovered, enabled) {
        if (isHovered && enabled) {
            delay(delayMs)
            showTooltip = true
        } else {
            showTooltip = false
        }
    }

    Box(
        modifier = modifier
            .onPointerEvent(PointerEventType.Enter) { isHovered = true }
            .onPointerEvent(PointerEventType.Exit) { isHovered = false }
    ) {
        content()

        if (showTooltip) {
            Popup(
                alignment = Alignment.TopCenter,
                properties = PopupProperties(focusable = false)
            ) {
                AnimatedVisibility(
                    visible = showTooltip,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    ArcaneTooltipBox {
                        tooltip()
                    }
                }
            }
        }
    }
}

@Composable
fun ArcaneTooltip(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    delayMs: Long = 500L,
    content: @Composable () -> Unit
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography

    ArcaneTooltip(
        tooltip = {
            Text(
                text = text,
                style = typography.bodySmall,
                color = colors.text
            )
        },
        modifier = modifier,
        enabled = enabled,
        delayMs = delayMs,
        content = content
    )
}
```

**Step 2: Verify build compiles**

Run: `./gradlew :arcane-components:compileKotlinDesktop`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/display/Tooltip.kt
git commit -m "feat: add ArcaneTooltip with hover delay and popup"
```

---

## Task 6: Create ArcaneTable Component

**Files:**
- Create: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/display/Table.kt`

**Step 1: Create Table.kt with data classes and composables**

```kotlin
// arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/display/Table.kt
package io.github.devmugi.arcane.design.components.display

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Immutable
data class ArcaneTableColumn<T>(
    val header: String,
    val weight: Float = 1f,
    val sortable: Boolean = false,
    val filterable: Boolean = false,
    val content: @Composable (T) -> Unit
)

@Immutable
data class ArcaneTableSortState(
    val columnIndex: Int,
    val ascending: Boolean
)

@Composable
fun <T> ArcaneTable(
    items: List<T>,
    columns: List<ArcaneTableColumn<T>>,
    modifier: Modifier = Modifier,
    onRowClick: ((T) -> Unit)? = null,
    sortState: ArcaneTableSortState? = null,
    onSortChange: ((ArcaneTableSortState) -> Unit)? = null
) {
    Column(
        modifier = modifier
            .clip(ArcaneRadius.Medium)
    ) {
        ArcaneTableHeader(
            columns = columns,
            sortState = sortState,
            onSortChange = onSortChange
        )

        items.forEachIndexed { index, item ->
            ArcaneTableRow(
                onClick = onRowClick?.let { { it(item) } },
                isAlternate = index % 2 == 1
            ) {
                columns.forEach { column ->
                    Box(
                        modifier = Modifier
                            .weight(column.weight)
                            .padding(horizontal = ArcaneSpacing.Small),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        column.content(item)
                    }
                }
            }
        }
    }
}

@Composable
fun <T> ArcaneTableHeader(
    columns: List<ArcaneTableColumn<T>>,
    sortState: ArcaneTableSortState?,
    onSortChange: ((ArcaneTableSortState) -> Unit)?
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surfaceInset)
            .defaultMinSize(minHeight = 40.dp)
            .padding(vertical = ArcaneSpacing.XSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        columns.forEachIndexed { index, column ->
            val isCurrentSort = sortState?.columnIndex == index
            val interactionSource = remember { MutableInteractionSource() }

            Row(
                modifier = Modifier
                    .weight(column.weight)
                    .padding(horizontal = ArcaneSpacing.Small)
                    .then(
                        if (column.sortable && onSortChange != null) {
                            Modifier.clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                val newState = if (isCurrentSort) {
                                    ArcaneTableSortState(index, !sortState.ascending)
                                } else {
                                    ArcaneTableSortState(index, true)
                                }
                                onSortChange(newState)
                            }
                        } else Modifier
                    ),
                horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.XXSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = column.header,
                    style = typography.labelMedium,
                    color = colors.textSecondary
                )

                if (column.sortable) {
                    val sortIndicator = when {
                        !isCurrentSort -> "\u2195" // ↕
                        sortState.ascending -> "\u2191" // ↑
                        else -> "\u2193" // ↓
                    }
                    Text(
                        text = sortIndicator,
                        style = typography.labelSmall,
                        color = if (isCurrentSort) colors.primary else colors.textSecondary
                    )
                }

                if (column.filterable) {
                    Text(
                        text = "\u25BC", // ▼ funnel-like
                        style = typography.labelSmall,
                        color = colors.textSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun ArcaneTableRow(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    isAlternate: Boolean = false,
    content: @Composable RowScope.() -> Unit
) {
    val colors = ArcaneTheme.colors
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val backgroundColor = when {
        isHovered && onClick != null -> colors.surfacePressed
        isAlternate -> colors.surface.copy(alpha = 0.5f)
        else -> Color.Transparent
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
            .background(backgroundColor)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick
                    )
                } else Modifier
            )
            .padding(vertical = ArcaneSpacing.Small),
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}
```

**Step 2: Verify build compiles**

Run: `./gradlew :arcane-components:compileKotlinDesktop`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/display/Table.kt
git commit -m "feat: add ArcaneTable with sortable columns and row hover"
```

---

## Task 7: Create DataDisplayScreen Catalog

**Files:**
- Create: `catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/screens/DataDisplayScreen.kt`

**Step 1: Create DataDisplayScreen.kt showcasing all components**

```kotlin
// catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/screens/DataDisplayScreen.kt
package io.github.devmugi.arcane.catalog.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import io.github.devmugi.arcane.design.components.controls.ArcaneTextButton
import io.github.devmugi.arcane.design.components.controls.ArcaneButtonStyle
import io.github.devmugi.arcane.design.components.display.ArcaneAvatar
import io.github.devmugi.arcane.design.components.display.ArcaneAvatarData
import io.github.devmugi.arcane.design.components.display.ArcaneAvatarGroup
import io.github.devmugi.arcane.design.components.display.ArcaneAvatarSize
import io.github.devmugi.arcane.design.components.display.ArcaneBadge
import io.github.devmugi.arcane.design.components.display.ArcaneBadgeStyle
import io.github.devmugi.arcane.design.components.display.ArcaneCard
import io.github.devmugi.arcane.design.components.display.ArcaneCardActions
import io.github.devmugi.arcane.design.components.display.ArcaneCardContent
import io.github.devmugi.arcane.design.components.display.ArcaneListItem
import io.github.devmugi.arcane.design.components.display.ArcaneTable
import io.github.devmugi.arcane.design.components.display.ArcaneTableColumn
import io.github.devmugi.arcane.design.components.display.ArcaneTableSortState
import io.github.devmugi.arcane.design.components.display.ArcaneTooltip
import io.github.devmugi.arcane.design.foundation.primitives.ArcaneSurface
import io.github.devmugi.arcane.design.foundation.primitives.SurfaceVariant
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun DataDisplayScreen() {
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
        Text(
            text = "Data Display",
            style = typography.displayMedium,
            color = colors.text
        )

        // Badges Section
        SectionTitle("Badges")
        ArcaneSurface(
            variant = SurfaceVariant.Raised,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
            ) {
                ArcaneBadge("New", style = ArcaneBadgeStyle.Success)
                ArcaneBadge("Featured", style = ArcaneBadgeStyle.Default)
                ArcaneBadge("Sale", style = ArcaneBadgeStyle.Warning)
                ArcaneBadge("Error", style = ArcaneBadgeStyle.Error)
                ArcaneBadge("Neutral", style = ArcaneBadgeStyle.Neutral)
            }
        }

        // Avatars Section
        SectionTitle("Avatars")
        ArcaneSurface(
            variant = SurfaceVariant.Raised,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Medium)
            ) {
                // Size variants
                Text("Sizes", style = typography.labelMedium, color = colors.textSecondary)
                Row(horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)) {
                    ArcaneAvatar(name = "John Doe", size = ArcaneAvatarSize.Small)
                    ArcaneAvatar(name = "Jane Smith", size = ArcaneAvatarSize.Medium)
                    ArcaneAvatar(name = "Bob Wilson", size = ArcaneAvatarSize.Large)
                }

                // Avatar group
                Text("Avatar Group", style = typography.labelMedium, color = colors.textSecondary)
                ArcaneAvatarGroup(
                    avatars = listOf(
                        ArcaneAvatarData(name = "Alice"),
                        ArcaneAvatarData(name = "Bob"),
                        ArcaneAvatarData(name = "Charlie"),
                        ArcaneAvatarData(name = "Diana"),
                        ArcaneAvatarData(name = "Eve")
                    ),
                    maxVisible = 3
                )
            }
        }

        // List Items Section
        SectionTitle("List Items")
        ArcaneSurface(
            variant = SurfaceVariant.Raised,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                ArcaneListItem(
                    headlineText = "Meeting Tomorrow",
                    supportingText = "10:00 AM - 11:00 AM, Room A"
                )
                ArcaneListItem(
                    headlineText = "Project Review",
                    supportingText = "2:00 PM - 3:00 PM, Virtual",
                    trailingContent = {
                        ArcaneBadge("New", style = ArcaneBadgeStyle.Success)
                    }
                )
                ArcaneListItem(
                    headlineText = "Team Standup",
                    supportingText = "9:00 AM - 9:15 AM, Daily",
                    onClick = { }
                )
            }
        }

        // Cards Section
        SectionTitle("Cards")
        ArcaneCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            ArcaneCardContent(
                title = "Project Phoenix",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit."
            )
            ArcaneCardActions {
                ArcaneTextButton(
                    text = "View Project",
                    onClick = { },
                    style = ArcaneButtonStyle.Secondary
                )
            }
        }

        // Tooltip Section
        SectionTitle("Tooltip")
        ArcaneSurface(
            variant = SurfaceVariant.Raised,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Medium)
            ) {
                ArcaneTooltip(text = "This is helpful information") {
                    ArcaneTextButton(
                        text = "Hover me",
                        onClick = { },
                        style = ArcaneButtonStyle.Secondary
                    )
                }
            }
        }

        // Table Section
        SectionTitle("Table")
        var sortState by remember { mutableStateOf<ArcaneTableSortState?>(null) }

        data class TableItem(val name: String, val status: String, val date: String)
        val items = listOf(
            TableItem("Project Alpha", "Active", "Jan 15"),
            TableItem("Project Beta", "Pending", "Jan 18"),
            TableItem("Project Gamma", "Complete", "Jan 20")
        )

        ArcaneTable(
            items = items,
            columns = listOf(
                ArcaneTableColumn(
                    header = "Name",
                    weight = 1.5f,
                    sortable = true
                ) { item ->
                    Text(item.name, style = typography.bodyMedium, color = colors.text)
                },
                ArcaneTableColumn(
                    header = "Status",
                    filterable = true
                ) { item ->
                    val badgeStyle = when (item.status) {
                        "Active" -> ArcaneBadgeStyle.Success
                        "Pending" -> ArcaneBadgeStyle.Warning
                        else -> ArcaneBadgeStyle.Neutral
                    }
                    ArcaneBadge(item.status, style = badgeStyle)
                },
                ArcaneTableColumn(
                    header = "Date",
                    sortable = true
                ) { item ->
                    Text(item.date, style = typography.bodyMedium, color = colors.textSecondary)
                }
            ),
            sortState = sortState,
            onSortChange = { sortState = it },
            modifier = Modifier.fillMaxWidth()
        )

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

**Step 2: Verify build compiles**

Run: `./gradlew :catalog:composeApp:compileKotlinDesktop`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/screens/DataDisplayScreen.kt
git commit -m "feat: add DataDisplayScreen showcasing all data display components"
```

---

## Task 8: Integrate DataDisplayScreen with App Navigation

**Files:**
- Modify: `catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/App.kt`

**Step 1: Add Data Display tab to App.kt**

In `App.kt`, update the tabs list and when block:

```kotlin
// Replace existing ArcaneTabs line:
ArcaneTabs(
    tabs = listOf(
        ArcaneTab("Controls"),
        ArcaneTab("Navigation"),
        ArcaneTab("Data Display")
    ),
    selectedIndex = selectedScreen,
    onTabSelected = { selectedScreen = it }
)

// Replace existing when block:
when (selectedScreen) {
    0 -> ControlsScreen()
    1 -> NavigationScreen()
    2 -> DataDisplayScreen()
}
```

Also add the import at the top:

```kotlin
import io.github.devmugi.arcane.catalog.screens.DataDisplayScreen
```

**Step 2: Verify build compiles**

Run: `./gradlew :catalog:composeApp:compileKotlinDesktop`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/App.kt
git commit -m "feat: integrate DataDisplayScreen with main app navigation"
```

---

## Task 9: Full Build Verification

**Step 1: Run full project build**

Run: `./gradlew build`
Expected: BUILD SUCCESSFUL

**Step 2: Run desktop app to verify visually**

Run: `./gradlew :catalog:composeApp:run`
Expected: App launches, all three tabs work, Data Display components render correctly

**Step 3: Final commit if any fixes needed**

If fixes were required, commit them:
```bash
git add -A
git commit -m "fix: resolve build issues in Phase 4 components"
```

---

## Summary

| Task | Component | Files |
|------|-----------|-------|
| 1 | ArcaneBadge | `display/Badge.kt` |
| 2 | ArcaneAvatar, ArcaneAvatarGroup | `display/Avatar.kt` |
| 3 | ArcaneListItem, ArcaneListItemIcon | `display/ListItem.kt` |
| 4 | ArcaneCard, slots | `display/Card.kt` |
| 5 | ArcaneTooltip, ArcaneTooltipBox | `display/Tooltip.kt` |
| 6 | ArcaneTable, ArcaneTableHeader, ArcaneTableRow | `display/Table.kt` |
| 7 | DataDisplayScreen | `screens/DataDisplayScreen.kt` |
| 8 | App integration | `App.kt` |
| 9 | Build verification | - |

**Total: 9 tasks, 7 new files, 1 modified file**
