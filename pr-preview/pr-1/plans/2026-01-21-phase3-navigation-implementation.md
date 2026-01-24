# Phase 3: Navigation Components Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Implement Tabs, Breadcrumbs, Pagination, and Stepper navigation components following the design spec in `docs/plans/2026-01-21-phase3-navigation-design.md`.

**Architecture:** Each component lives in its own file under `arcane-components/.../navigation/`. Components use existing foundation tokens (colors, spacing, radius) and follow established patterns from controls (sealed class styles, animation states, interaction sources).

**Tech Stack:** Kotlin 2.1.0, Compose Multiplatform 1.7.1, Gradle KTS

**Build Command:** `./gradlew :arcane-components:compileKotlinMetadata` (fast compilation check)

**Run Catalog:** `./gradlew :catalog:composeApp:run` (desktop preview)

---

## Task 1: Create Navigation Package Structure

**Files:**
- Create: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/navigation/` (directory)

**Step 1: Create the navigation directory**

```bash
mkdir -p arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/navigation
```

**Step 2: Verify directory exists**

```bash
ls arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/
```

Expected: `controls/` and `navigation/` directories listed

**Step 3: Commit**

```bash
git add .
git commit -m "chore: create navigation package directory"
```

---

## Task 2: Implement ArcaneTabs - Data Classes and Style

**Files:**
- Create: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/navigation/Tabs.kt`

**Step 1: Create Tabs.kt with data classes and style sealed class**

```kotlin
// arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/navigation/Tabs.kt
package io.github.devmugi.arcane.design.components.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable

@Immutable
sealed class ArcaneTabStyle {
    data object Filled : ArcaneTabStyle()
    data object Underline : ArcaneTabStyle()
}

@Immutable
data class ArcaneTab(
    val label: String,
    val icon: (@Composable () -> Unit)? = null,
    val enabled: Boolean = true
)
```

**Step 2: Build to verify compilation**

```bash
./gradlew :arcane-components:compileKotlinMetadata --quiet
```

Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/navigation/Tabs.kt
git commit -m "feat(navigation): add ArcaneTab data class and ArcaneTabStyle"
```

---

## Task 3: Implement ArcaneTabs - Tab Item Composable

**Files:**
- Modify: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/navigation/Tabs.kt`

**Step 1: Add internal ArcaneTabItem composable**

Add after the data classes:

```kotlin
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneIconography
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
internal fun ArcaneTabItem(
    tab: ArcaneTab,
    selected: Boolean,
    onClick: () -> Unit,
    style: ArcaneTabStyle,
    modifier: Modifier = Modifier
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val backgroundColor by animateColorAsState(
        targetValue = when {
            !tab.enabled -> Color.Transparent
            selected && style is ArcaneTabStyle.Filled -> colors.primary
            isPressed -> colors.surfacePressed
            else -> Color.Transparent
        },
        animationSpec = tween(200),
        label = "tabBackground"
    )

    val contentColor by animateColorAsState(
        targetValue = when {
            !tab.enabled -> colors.textDisabled
            selected && style is ArcaneTabStyle.Filled -> colors.surface
            selected -> colors.primary
            else -> colors.textSecondary
        },
        animationSpec = tween(200),
        label = "tabContent"
    )

    val glowAlpha by animateFloatAsState(
        targetValue = if (selected && tab.enabled && style is ArcaneTabStyle.Filled) 0.3f else 0f,
        animationSpec = tween(200),
        label = "tabGlow"
    )

    Box(
        modifier = modifier
            .height(40.dp)
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
                                radius = maxOf(size.width, size.height) * 0.8f
                            )
                        )
                    }
                } else Modifier
            )
            .clip(ArcaneRadius.Medium)
            .background(backgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = tab.enabled,
                role = Role.Tab,
                onClick = onClick
            )
            .padding(horizontal = ArcaneSpacing.Medium),
        contentAlignment = Alignment.Center
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                tab.icon?.let { icon ->
                    Box(modifier = Modifier.height(ArcaneIconography.Small)) {
                        icon()
                    }
                }
                Text(
                    text = tab.label,
                    style = typography.labelLarge,
                    color = contentColor
                )
            }
        }
    }
}
```

**Step 2: Build to verify compilation**

```bash
./gradlew :arcane-components:compileKotlinMetadata --quiet
```

Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/navigation/Tabs.kt
git commit -m "feat(navigation): add ArcaneTabItem internal composable"
```

---

## Task 4: Implement ArcaneTabs - Main Composable

**Files:**
- Modify: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/navigation/Tabs.kt`

**Step 1: Add ArcaneTabs composable (fixed mode)**

Add at the end of the file:

```kotlin
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState

@Composable
fun ArcaneTabs(
    tabs: List<ArcaneTab>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    style: ArcaneTabStyle = ArcaneTabStyle.Filled,
    scrollable: Boolean = false
) {
    if (scrollable) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)
        ) {
            tabs.forEachIndexed { index, tab ->
                ArcaneTabItem(
                    tab = tab,
                    selected = index == selectedIndex,
                    onClick = { onTabSelected(index) },
                    style = style
                )
            }
        }
    } else {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)
        ) {
            tabs.forEachIndexed { index, tab ->
                ArcaneTabItem(
                    tab = tab,
                    selected = index == selectedIndex,
                    onClick = { onTabSelected(index) },
                    style = style,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
```

**Step 2: Build to verify compilation**

```bash
./gradlew :arcane-components:compileKotlinMetadata --quiet
```

Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/navigation/Tabs.kt
git commit -m "feat(navigation): add ArcaneTabs composable with fixed/scrollable modes"
```

---

## Task 5: Implement ArcaneTabLayout

**Files:**
- Modify: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/navigation/Tabs.kt`

**Step 1: Add ArcaneTabLayout composable**

Add at the end of the file:

```kotlin
import androidx.compose.foundation.layout.Column

@Composable
fun ArcaneTabLayout(
    tabs: List<ArcaneTab>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    style: ArcaneTabStyle = ArcaneTabStyle.Filled,
    scrollable: Boolean = false,
    content: @Composable (selectedIndex: Int) -> Unit
) {
    Column(modifier = modifier) {
        ArcaneTabs(
            tabs = tabs,
            selectedIndex = selectedIndex,
            onTabSelected = onTabSelected,
            style = style,
            scrollable = scrollable
        )
        content(selectedIndex)
    }
}
```

**Step 2: Build to verify compilation**

```bash
./gradlew :arcane-components:compileKotlinMetadata --quiet
```

Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/navigation/Tabs.kt
git commit -m "feat(navigation): add ArcaneTabLayout with integrated content"
```

---

## Task 6: Implement ArcaneBreadcrumbs - Data Class

**Files:**
- Create: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/navigation/Breadcrumbs.kt`

**Step 1: Create Breadcrumbs.kt with data class**

```kotlin
// arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/navigation/Breadcrumbs.kt
package io.github.devmugi.arcane.design.components.navigation

import androidx.compose.runtime.Immutable

@Immutable
data class ArcaneBreadcrumb(
    val label: String,
    val onClick: (() -> Unit)? = null
)
```

**Step 2: Build to verify compilation**

```bash
./gradlew :arcane-components:compileKotlinMetadata --quiet
```

Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/navigation/Breadcrumbs.kt
git commit -m "feat(navigation): add ArcaneBreadcrumb data class"
```

---

## Task 7: Implement ArcaneBreadcrumbs - Main Composable

**Files:**
- Modify: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/navigation/Breadcrumbs.kt`

**Step 1: Add ArcaneBreadcrumbs composable**

Replace entire file content:

```kotlin
// arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/navigation/Breadcrumbs.kt
package io.github.devmugi.arcane.design.components.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Immutable
data class ArcaneBreadcrumb(
    val label: String,
    val onClick: (() -> Unit)? = null
)

@Composable
fun ArcaneBreadcrumbs(
    items: List<ArcaneBreadcrumb>,
    modifier: Modifier = Modifier,
    separator: @Composable () -> Unit = { DefaultBreadcrumbSeparator() },
    maxItems: Int = Int.MAX_VALUE,
    collapsedIndicator: @Composable () -> Unit = { DefaultCollapsedIndicator() }
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography

    val displayItems = if (items.size > maxItems && maxItems >= 2) {
        // Show first item, collapsed indicator, then last (maxItems - 1) items
        val lastItemsCount = maxItems - 1
        listOf(items.first()) + listOf(null) + items.takeLast(lastItemsCount)
    } else {
        items.map { it as ArcaneBreadcrumb? }
    }

    Row(
        modifier = modifier.padding(vertical = ArcaneSpacing.XSmall),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        displayItems.forEachIndexed { index, item ->
            if (item == null) {
                // Collapsed indicator
                separator()
                collapsedIndicator()
            } else {
                if (index > 0 && displayItems[index - 1] != null) {
                    separator()
                }

                val isClickable = item.onClick != null
                val isCurrent = item.onClick == null

                Text(
                    text = item.label,
                    style = typography.bodyMedium,
                    fontWeight = if (isCurrent) FontWeight.Medium else FontWeight.Normal,
                    color = if (isClickable) colors.primary else colors.text,
                    modifier = if (isClickable) {
                        Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            role = Role.Button,
                            onClick = { item.onClick?.invoke() }
                        )
                    } else {
                        Modifier
                    }
                )
            }
        }
    }
}

@Composable
private fun DefaultBreadcrumbSeparator() {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography
    Text(
        text = ">",
        style = typography.bodyMedium,
        color = colors.textSecondary,
        modifier = Modifier.padding(horizontal = ArcaneSpacing.XSmall)
    )
}

@Composable
private fun DefaultCollapsedIndicator() {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography
    Text(
        text = "...",
        style = typography.bodyMedium,
        color = colors.primary
    )
}
```

**Step 2: Build to verify compilation**

```bash
./gradlew :arcane-components:compileKotlinMetadata --quiet
```

Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/navigation/Breadcrumbs.kt
git commit -m "feat(navigation): add ArcaneBreadcrumbs with collapsing support"
```

---

## Task 8: Implement ArcanePagination - Page Range Logic

**Files:**
- Create: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/navigation/Pagination.kt`

**Step 1: Create Pagination.kt with page range calculation**

```kotlin
// arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/navigation/Pagination.kt
package io.github.devmugi.arcane.design.components.navigation

import kotlin.math.max
import kotlin.math.min

internal sealed class PaginationItem {
    data class Page(val number: Int) : PaginationItem()
    data object Ellipsis : PaginationItem()
}

internal fun calculatePaginationItems(
    currentPage: Int,
    totalPages: Int,
    siblingCount: Int,
    boundaryCount: Int
): List<PaginationItem> {
    if (totalPages <= 0) return emptyList()
    if (totalPages == 1) return listOf(PaginationItem.Page(1))

    val items = mutableListOf<PaginationItem>()

    // Left boundary pages
    val leftBoundaryEnd = min(boundaryCount, totalPages)
    for (i in 1..leftBoundaryEnd) {
        items.add(PaginationItem.Page(i))
    }

    // Left ellipsis
    val siblingStart = max(currentPage - siblingCount, boundaryCount + 1)
    if (siblingStart > boundaryCount + 1) {
        items.add(PaginationItem.Ellipsis)
    }

    // Sibling pages (and current)
    val siblingEnd = min(currentPage + siblingCount, totalPages - boundaryCount)
    for (i in siblingStart..siblingEnd) {
        if (i > boundaryCount && i <= totalPages - boundaryCount) {
            items.add(PaginationItem.Page(i))
        }
    }

    // Right ellipsis
    val rightBoundaryStart = max(totalPages - boundaryCount + 1, 1)
    if (siblingEnd < rightBoundaryStart - 1) {
        items.add(PaginationItem.Ellipsis)
    }

    // Right boundary pages
    for (i in rightBoundaryStart..totalPages) {
        if (i > boundaryCount) {
            items.add(PaginationItem.Page(i))
        }
    }

    return items.distinctBy {
        when (it) {
            is PaginationItem.Page -> "page-${it.number}"
            is PaginationItem.Ellipsis -> "ellipsis-${items.indexOf(it)}"
        }
    }
}
```

**Step 2: Build to verify compilation**

```bash
./gradlew :arcane-components:compileKotlinMetadata --quiet
```

Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/navigation/Pagination.kt
git commit -m "feat(navigation): add pagination page range calculation logic"
```

---

## Task 9: Implement ArcanePagination - UI Components

**Files:**
- Modify: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/navigation/Pagination.kt`

**Step 1: Add ArcanePagination composable**

Add after the `calculatePaginationItems` function:

```kotlin
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun ArcanePagination(
    currentPage: Int,
    totalPages: Int,
    onPageSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    showPageInfo: Boolean = true,
    siblingCount: Int = 1,
    boundaryCount: Int = 1
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography

    val items = calculatePaginationItems(currentPage, totalPages, siblingCount, boundaryCount)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showPageInfo) {
            Text(
                text = "Page $currentPage of $totalPages",
                style = typography.bodySmall,
                color = colors.textSecondary
            )
            Box(modifier = Modifier.size(ArcaneSpacing.Medium))
        }

        // Previous button
        PaginationButton(
            content = "<",
            enabled = currentPage > 1,
            selected = false,
            onClick = { onPageSelected(currentPage - 1) }
        )

        // Page items
        items.forEach { item ->
            when (item) {
                is PaginationItem.Page -> {
                    PaginationButton(
                        content = item.number.toString(),
                        enabled = true,
                        selected = item.number == currentPage,
                        onClick = { onPageSelected(item.number) }
                    )
                }
                is PaginationItem.Ellipsis -> {
                    Box(
                        modifier = Modifier.size(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "...",
                            style = typography.bodyMedium,
                            color = colors.textSecondary
                        )
                    }
                }
            }
        }

        // Next button
        PaginationButton(
            content = ">",
            enabled = currentPage < totalPages,
            selected = false,
            onClick = { onPageSelected(currentPage + 1) }
        )
    }
}

@Composable
private fun PaginationButton(
    content: String,
    enabled: Boolean,
    selected: Boolean,
    onClick: () -> Unit
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val backgroundColor by animateColorAsState(
        targetValue = when {
            selected -> colors.primary
            isPressed && enabled -> colors.surfacePressed
            else -> Color.Transparent
        },
        animationSpec = tween(150),
        label = "paginationBg"
    )

    val contentColor = when {
        !enabled -> colors.textDisabled
        selected -> colors.surface
        else -> colors.text
    }

    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(ArcaneRadius.Medium)
            .background(backgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                role = Role.Button,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = content,
            style = typography.labelMedium,
            color = contentColor
        )
    }
}
```

**Step 2: Build to verify compilation**

```bash
./gradlew :arcane-components:compileKotlinMetadata --quiet
```

Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/navigation/Pagination.kt
git commit -m "feat(navigation): add ArcanePagination with prev/next and ellipsis"
```

---

## Task 10: Implement ArcaneStepper - Data Classes

**Files:**
- Create: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/navigation/Stepper.kt`

**Step 1: Create Stepper.kt with data classes**

```kotlin
// arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/navigation/Stepper.kt
package io.github.devmugi.arcane.design.components.navigation

import androidx.compose.runtime.Immutable

enum class ArcaneStepState {
    Pending,
    Active,
    Completed
}

@Immutable
data class ArcaneStep(
    val label: String,
    val description: String? = null,
    val state: ArcaneStepState = ArcaneStepState.Pending
)

@Immutable
sealed class ArcaneStepperOrientation {
    data object Horizontal : ArcaneStepperOrientation()
    data object Vertical : ArcaneStepperOrientation()
}
```

**Step 2: Build to verify compilation**

```bash
./gradlew :arcane-components:compileKotlinMetadata --quiet
```

Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/navigation/Stepper.kt
git commit -m "feat(navigation): add ArcaneStep and ArcaneStepperOrientation"
```

---

## Task 11: Implement ArcaneStepper - Step Indicator

**Files:**
- Modify: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/navigation/Stepper.kt`

**Step 1: Add StepIndicator composable**

Add after the data classes:

```kotlin
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneBorder

@Composable
internal fun StepIndicator(
    state: ArcaneStepState,
    stepNumber: Int,
    modifier: Modifier = Modifier
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography

    val backgroundColor by animateColorAsState(
        targetValue = when (state) {
            ArcaneStepState.Completed -> colors.primary
            ArcaneStepState.Active -> colors.surface
            ArcaneStepState.Pending -> colors.surface
        },
        animationSpec = tween(200),
        label = "stepBg"
    )

    val borderColor by animateColorAsState(
        targetValue = when (state) {
            ArcaneStepState.Completed -> colors.primary
            ArcaneStepState.Active -> colors.primary
            ArcaneStepState.Pending -> colors.textDisabled
        },
        animationSpec = tween(200),
        label = "stepBorder"
    )

    val checkmarkScale by animateFloatAsState(
        targetValue = if (state == ArcaneStepState.Completed) 1f else 0f,
        animationSpec = tween(200),
        label = "checkmarkScale"
    )

    val glowAlpha by animateFloatAsState(
        targetValue = if (state == ArcaneStepState.Active) 0.3f else 0f,
        animationSpec = tween(200),
        label = "stepGlow"
    )

    Box(
        modifier = modifier
            .size(24.dp)
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
                                radius = size.width * 1.2f
                            )
                        )
                    }
                } else Modifier
            )
            .clip(CircleShape)
            .background(backgroundColor)
            .border(ArcaneBorder.Medium, borderColor, CircleShape)
            .then(
                if (state == ArcaneStepState.Completed) {
                    Modifier.drawWithContent {
                        drawContent()
                        // Draw checkmark
                        val strokeWidth = 2.dp.toPx()
                        val checkColor = colors.surface
                        val centerX = size.width / 2
                        val centerY = size.height / 2
                        val scale = checkmarkScale

                        drawLine(
                            color = checkColor,
                            start = Offset(centerX - 4.dp.toPx() * scale, centerY),
                            end = Offset(centerX - 1.dp.toPx() * scale, centerY + 3.dp.toPx() * scale),
                            strokeWidth = strokeWidth,
                            cap = StrokeCap.Round
                        )
                        drawLine(
                            color = checkColor,
                            start = Offset(centerX - 1.dp.toPx() * scale, centerY + 3.dp.toPx() * scale),
                            end = Offset(centerX + 4.dp.toPx() * scale, centerY - 3.dp.toPx() * scale),
                            strokeWidth = strokeWidth,
                            cap = StrokeCap.Round
                        )
                    }
                } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        if (state != ArcaneStepState.Completed) {
            val contentColor = when (state) {
                ArcaneStepState.Active -> colors.primary
                ArcaneStepState.Pending -> colors.textDisabled
                else -> colors.textDisabled
            }
            Text(
                text = stepNumber.toString(),
                style = typography.labelSmall,
                color = contentColor
            )
        }
    }
}
```

**Step 2: Build to verify compilation**

```bash
./gradlew :arcane-components:compileKotlinMetadata --quiet
```

Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/navigation/Stepper.kt
git commit -m "feat(navigation): add StepIndicator with animated states"
```

---

## Task 12: Implement ArcaneStepper - Horizontal Layout

**Files:**
- Modify: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/navigation/Stepper.kt`

**Step 1: Add ArcaneStepper composable**

Add at the end of the file:

```kotlin
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.ui.text.font.FontWeight
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun ArcaneStepper(
    steps: List<ArcaneStep>,
    modifier: Modifier = Modifier,
    orientation: ArcaneStepperOrientation = ArcaneStepperOrientation.Horizontal
) {
    when (orientation) {
        is ArcaneStepperOrientation.Horizontal -> HorizontalStepper(steps, modifier)
        is ArcaneStepperOrientation.Vertical -> VerticalStepper(steps, modifier)
    }
}

@Composable
private fun HorizontalStepper(
    steps: List<ArcaneStep>,
    modifier: Modifier = Modifier
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        steps.forEachIndexed { index, step ->
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left connector
                    if (index > 0) {
                        val prevState = steps[index - 1].state
                        val connectorColor = if (prevState == ArcaneStepState.Completed) {
                            colors.primary
                        } else {
                            colors.textDisabled
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(2.dp)
                                .background(connectorColor)
                        )
                    } else {
                        Box(modifier = Modifier.weight(1f))
                    }

                    StepIndicator(
                        state = step.state,
                        stepNumber = index + 1
                    )

                    // Right connector
                    if (index < steps.size - 1) {
                        val connectorColor = if (step.state == ArcaneStepState.Completed) {
                            colors.primary
                        } else {
                            colors.textDisabled
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(2.dp)
                                .background(connectorColor)
                        )
                    } else {
                        Box(modifier = Modifier.weight(1f))
                    }
                }

                // Label
                Text(
                    text = step.label,
                    style = typography.labelMedium,
                    fontWeight = if (step.state == ArcaneStepState.Active) FontWeight.Medium else FontWeight.Normal,
                    color = if (step.state == ArcaneStepState.Pending) colors.textSecondary else colors.text,
                    modifier = Modifier.padding(top = ArcaneSpacing.XSmall)
                )

                // Description (only for active step)
                if (step.state == ArcaneStepState.Active && step.description != null) {
                    Text(
                        text = step.description,
                        style = typography.labelSmall,
                        color = colors.textSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun VerticalStepper(
    steps: List<ArcaneStep>,
    modifier: Modifier = Modifier
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top
    ) {
        steps.forEachIndexed { index, step ->
            Row(
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    StepIndicator(
                        state = step.state,
                        stepNumber = index + 1
                    )

                    // Vertical connector
                    if (index < steps.size - 1) {
                        val connectorColor = if (step.state == ArcaneStepState.Completed) {
                            colors.primary
                        } else {
                            colors.textDisabled
                        }
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .height(32.dp)
                                .background(connectorColor)
                        )
                    }
                }

                Column(
                    modifier = Modifier.padding(start = ArcaneSpacing.Small)
                ) {
                    Text(
                        text = step.label,
                        style = typography.labelMedium,
                        fontWeight = if (step.state == ArcaneStepState.Active) FontWeight.Medium else FontWeight.Normal,
                        color = if (step.state == ArcaneStepState.Pending) colors.textSecondary else colors.text
                    )

                    if (step.state == ArcaneStepState.Active && step.description != null) {
                        Text(
                            text = step.description,
                            style = typography.labelSmall,
                            color = colors.textSecondary
                        )
                    }

                    if (index < steps.size - 1) {
                        Box(modifier = Modifier.height(ArcaneSpacing.Medium))
                    }
                }
            }
        }
    }
}
```

**Step 2: Build to verify compilation**

```bash
./gradlew :arcane-components:compileKotlinMetadata --quiet
```

Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/navigation/Stepper.kt
git commit -m "feat(navigation): add ArcaneStepper with horizontal and vertical layouts"
```

---

## Task 13: Create NavigationScreen for Catalog

**Files:**
- Create: `catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/screens/NavigationScreen.kt`

**Step 1: Create NavigationScreen.kt**

```kotlin
// catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/screens/NavigationScreen.kt
package io.github.devmugi.arcane.catalog.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import io.github.devmugi.arcane.design.components.navigation.ArcaneBreadcrumb
import io.github.devmugi.arcane.design.components.navigation.ArcaneBreadcrumbs
import io.github.devmugi.arcane.design.components.navigation.ArcanePagination
import io.github.devmugi.arcane.design.components.navigation.ArcaneStep
import io.github.devmugi.arcane.design.components.navigation.ArcaneStepState
import io.github.devmugi.arcane.design.components.navigation.ArcaneStepper
import io.github.devmugi.arcane.design.components.navigation.ArcaneTab
import io.github.devmugi.arcane.design.components.navigation.ArcaneTabs
import io.github.devmugi.arcane.design.foundation.primitives.ArcaneSurface
import io.github.devmugi.arcane.design.foundation.primitives.SurfaceVariant
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun NavigationScreen() {
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
            text = "Navigation",
            style = typography.displayMedium,
            color = colors.text
        )

        // Tabs Section
        SectionTitle("Tabs")
        ArcaneSurface(
            variant = SurfaceVariant.Raised,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
            ) {
                var selectedTab by remember { mutableStateOf(0) }
                ArcaneTabs(
                    tabs = listOf(
                        ArcaneTab("Home"),
                        ArcaneTab("Profile"),
                        ArcaneTab("Settings")
                    ),
                    selectedIndex = selectedTab,
                    onTabSelected = { selectedTab = it }
                )
            }
        }

        // Breadcrumbs Section
        SectionTitle("Breadcrumbs")
        ArcaneSurface(
            variant = SurfaceVariant.Raised,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(ArcaneSpacing.Medium)
            ) {
                ArcaneBreadcrumbs(
                    items = listOf(
                        ArcaneBreadcrumb("Home") { },
                        ArcaneBreadcrumb("Products") { },
                        ArcaneBreadcrumb("Categories") { },
                        ArcaneBreadcrumb("Item")
                    )
                )
            }
        }

        // Pagination Section
        SectionTitle("Pagination")
        ArcaneSurface(
            variant = SurfaceVariant.Raised,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(ArcaneSpacing.Medium)
            ) {
                var currentPage by remember { mutableStateOf(1) }
                ArcanePagination(
                    currentPage = currentPage,
                    totalPages = 10,
                    onPageSelected = { currentPage = it }
                )
            }
        }

        // Stepper Section
        SectionTitle("Stepper")
        ArcaneSurface(
            variant = SurfaceVariant.Raised,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(ArcaneSpacing.Medium)
            ) {
                ArcaneStepper(
                    steps = listOf(
                        ArcaneStep("Account", state = ArcaneStepState.Completed),
                        ArcaneStep("Profile", state = ArcaneStepState.Completed),
                        ArcaneStep("Preferences", state = ArcaneStepState.Completed),
                        ArcaneStep("Confirmation", "Review details", state = ArcaneStepState.Active),
                        ArcaneStep("Complete", state = ArcaneStepState.Pending)
                    )
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

**Step 2: Build to verify compilation**

```bash
./gradlew :catalog:composeApp:compileKotlinDesktop --quiet
```

Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/screens/NavigationScreen.kt
git commit -m "feat(catalog): add NavigationScreen showcasing all navigation components"
```

---

## Task 14: Integrate NavigationScreen into App

**Files:**
- Modify: `catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/App.kt`

**Step 1: Update App.kt to show both screens**

Replace entire file content:

```kotlin
// catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/App.kt
package io.github.devmugi.arcane.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import io.github.devmugi.arcane.catalog.screens.ControlsScreen
import io.github.devmugi.arcane.catalog.screens.NavigationScreen
import io.github.devmugi.arcane.design.components.navigation.ArcaneTab
import io.github.devmugi.arcane.design.components.navigation.ArcaneTabs
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme

@Composable
fun App() {
    ArcaneTheme {
        var selectedScreen by remember { mutableStateOf(0) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ArcaneTheme.colors.surface)
        ) {
            ArcaneTabs(
                tabs = listOf(
                    ArcaneTab("Controls"),
                    ArcaneTab("Navigation")
                ),
                selectedIndex = selectedScreen,
                onTabSelected = { selectedScreen = it }
            )

            when (selectedScreen) {
                0 -> ControlsScreen()
                1 -> NavigationScreen()
            }
        }
    }
}
```

**Step 2: Build to verify compilation**

```bash
./gradlew :catalog:composeApp:compileKotlinDesktop --quiet
```

Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/App.kt
git commit -m "feat(catalog): integrate NavigationScreen with tab navigation"
```

---

## Task 15: Final Build Verification

**Step 1: Run full build**

```bash
./gradlew build --quiet
```

Expected: BUILD SUCCESSFUL

**Step 2: Run desktop catalog app (manual verification)**

```bash
./gradlew :catalog:composeApp:run
```

Expected: Desktop window opens showing Controls and Navigation tabs. Navigation tab displays Tabs, Breadcrumbs, Pagination, and Stepper components.

**Step 3: Create summary commit**

```bash
git log --oneline -15
```

Review commits to ensure all Phase 3 work is properly committed.

---

## Summary

| Task | Component | Files |
|------|-----------|-------|
| 1 | Setup | Create navigation directory |
| 2-5 | Tabs | `Tabs.kt` - data classes, TabItem, ArcaneTabs, ArcaneTabLayout |
| 6-7 | Breadcrumbs | `Breadcrumbs.kt` - data class, ArcaneBreadcrumbs |
| 8-9 | Pagination | `Pagination.kt` - page logic, ArcanePagination |
| 10-12 | Stepper | `Stepper.kt` - data classes, StepIndicator, ArcaneStepper |
| 13-14 | Catalog | `NavigationScreen.kt`, App.kt integration |
| 15 | Verification | Full build and visual check |

Total: 15 tasks, ~14 commits
