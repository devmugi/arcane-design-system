# Phase 5: Feedback Components Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Implement 12 Feedback components (Modal, Toast, AlertBanner, Progress, Spinner, Skeleton, EmptyState) following established Arcane Design System patterns.

**Architecture:** Components live in `arcane-components/.../feedback/` package. Each component follows existing patterns: sealed class styles, theme tokens, glow effects. Toast uses composition local state holder. Verification via Gradle build and catalog app.

**Tech Stack:** Kotlin, Compose Multiplatform, Gradle

---

## Pre-Implementation Setup

### Task 0: Create Feedback Package Directory

**Files:**
- Create: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/feedback/` (directory)

**Step 1: Create the directory structure**

```bash
mkdir -p arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/feedback
```

**Step 2: Verify directory exists**

```bash
ls -la arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/feedback
```
Expected: Empty directory exists

**Step 3: Commit**

```bash
git add -A && git commit -m "chore: create feedback components package directory"
```

---

## Task 1: ArcaneSpinner

Simple indeterminate loading spinner. No dependencies on other feedback components.

**Files:**
- Create: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/feedback/Spinner.kt`

**Step 1: Implement ArcaneSpinner**

```kotlin
package io.github.devmugi.arcane.design.components.feedback

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme

@Immutable
sealed class ArcaneSpinnerSize(val dp: Dp) {
    data object Small : ArcaneSpinnerSize(16.dp)
    data object Medium : ArcaneSpinnerSize(24.dp)
    data object Large : ArcaneSpinnerSize(48.dp)
}

@Composable
fun ArcaneSpinner(
    modifier: Modifier = Modifier,
    size: ArcaneSpinnerSize = ArcaneSpinnerSize.Medium,
    color: Color = ArcaneTheme.colors.primary
) {
    val infiniteTransition = rememberInfiniteTransition(label = "spinnerTransition")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing)
        ),
        label = "spinnerRotation"
    )

    val strokeWidth = when (size) {
        ArcaneSpinnerSize.Small -> 2.dp
        ArcaneSpinnerSize.Medium -> 3.dp
        ArcaneSpinnerSize.Large -> 4.dp
    }

    Canvas(
        modifier = modifier.size(size.dp)
    ) {
        val sweepAngle = 270f
        val startAngle = rotation

        drawArc(
            color = color.copy(alpha = 0.2f),
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
        )

        drawArc(
            color = color,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
        )
    }
}
```

**Step 2: Verify compilation**

```bash
cd /Users/den/IdeaProjects/ArcaneDesignSystem && ./gradlew :arcane-components:compileKotlinDesktop --quiet
```
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/feedback/Spinner.kt && git commit -m "feat(feedback): add ArcaneSpinner component"
```

---

## Task 2: ArcaneCircularProgress

Determinate circular progress indicator with optional label.

**Files:**
- Create: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/feedback/CircularProgress.kt`

**Step 1: Implement ArcaneCircularProgress**

```kotlin
package io.github.devmugi.arcane.design.components.feedback

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import kotlin.math.roundToInt

@Composable
fun ArcaneCircularProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    strokeWidth: Dp = 4.dp,
    trackColor: Color = ArcaneTheme.colors.surfaceInset,
    progressColor: Color = ArcaneTheme.colors.primary,
    showLabel: Boolean = false
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(300),
        label = "circularProgress"
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            // Track
            drawArc(
                color = trackColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )

            // Progress
            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = animatedProgress * 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }

        if (showLabel) {
            Text(
                text = "${(animatedProgress * 100).roundToInt()}%",
                style = ArcaneTheme.typography.labelSmall,
                color = ArcaneTheme.colors.text
            )
        }
    }
}
```

**Step 2: Verify compilation**

```bash
cd /Users/den/IdeaProjects/ArcaneDesignSystem && ./gradlew :arcane-components:compileKotlinDesktop --quiet
```
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/feedback/CircularProgress.kt && git commit -m "feat(feedback): add ArcaneCircularProgress component"
```

---

## Task 3: ArcaneLinearProgress

Determinate horizontal progress bar.

**Files:**
- Create: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/feedback/LinearProgress.kt`

**Step 1: Implement ArcaneLinearProgress**

```kotlin
package io.github.devmugi.arcane.design.components.feedback

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme

@Composable
fun ArcaneLinearProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    height: Dp = 8.dp,
    trackColor: Color = ArcaneTheme.colors.surfaceInset,
    progressColor: Color = ArcaneTheme.colors.primary
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(300),
        label = "linearProgress"
    )

    val shape = RoundedCornerShape(height / 2)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(shape)
            .background(trackColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(animatedProgress)
                .height(height)
                .clip(shape)
                .background(progressColor)
        )
    }
}
```

**Step 2: Verify compilation**

```bash
cd /Users/den/IdeaProjects/ArcaneDesignSystem && ./gradlew :arcane-components:compileKotlinDesktop --quiet
```
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/feedback/LinearProgress.kt && git commit -m "feat(feedback): add ArcaneLinearProgress component"
```

---

## Task 4: ArcaneSkeleton

Base skeleton shapes with shimmer animation.

**Files:**
- Create: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/feedback/Skeleton.kt`

**Step 1: Implement ArcaneSkeleton**

```kotlin
package io.github.devmugi.arcane.design.components.feedback

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Immutable
sealed class ArcaneSkeletonShape {
    data class Text(val lines: Int = 1, val lastLineWidth: Float = 0.7f) : ArcaneSkeletonShape()
    data class Circle(val size: Dp = 40.dp) : ArcaneSkeletonShape()
    data class Rectangle(
        val width: Dp = Dp.Unspecified,
        val height: Dp = 100.dp,
        val radius: Shape = ArcaneRadius.Medium
    ) : ArcaneSkeletonShape()
}

@Composable
fun ArcaneSkeleton(
    modifier: Modifier = Modifier,
    shape: ArcaneSkeletonShape = ArcaneSkeletonShape.Text()
) {
    when (shape) {
        is ArcaneSkeletonShape.Text -> SkeletonText(
            modifier = modifier,
            lines = shape.lines,
            lastLineWidth = shape.lastLineWidth
        )
        is ArcaneSkeletonShape.Circle -> SkeletonCircle(
            modifier = modifier,
            size = shape.size
        )
        is ArcaneSkeletonShape.Rectangle -> SkeletonRectangle(
            modifier = modifier,
            width = shape.width,
            height = shape.height,
            radius = shape.radius
        )
    }
}

@Composable
private fun SkeletonText(
    modifier: Modifier = Modifier,
    lines: Int,
    lastLineWidth: Float
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)
    ) {
        repeat(lines) { index ->
            val isLastLine = index == lines - 1
            SkeletonBox(
                modifier = Modifier
                    .fillMaxWidth(if (isLastLine) lastLineWidth else 1f)
                    .height(16.dp),
                shape = RoundedCornerShape(4.dp)
            )
        }
    }
}

@Composable
private fun SkeletonCircle(
    modifier: Modifier = Modifier,
    size: Dp
) {
    SkeletonBox(
        modifier = modifier.size(size),
        shape = CircleShape
    )
}

@Composable
private fun SkeletonRectangle(
    modifier: Modifier = Modifier,
    width: Dp,
    height: Dp,
    radius: Shape
) {
    SkeletonBox(
        modifier = modifier
            .then(if (width == Dp.Unspecified) Modifier.fillMaxWidth() else Modifier.size(width, height))
            .height(height),
        shape = radius
    )
}

@Composable
internal fun SkeletonBox(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(4.dp)
) {
    val colors = ArcaneTheme.colors
    val infiniteTransition = rememberInfiniteTransition(label = "skeletonShimmer")

    val shimmerProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing)
        ),
        label = "shimmerProgress"
    )

    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            colors.surfaceInset,
            colors.surfaceRaised,
            colors.surfaceInset
        ),
        start = Offset(shimmerProgress * 1000f - 500f, 0f),
        end = Offset(shimmerProgress * 1000f + 500f, 0f)
    )

    Box(
        modifier = modifier
            .clip(shape)
            .background(shimmerBrush)
    )
}
```

**Step 2: Verify compilation**

```bash
cd /Users/den/IdeaProjects/ArcaneDesignSystem && ./gradlew :arcane-components:compileKotlinDesktop --quiet
```
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/feedback/Skeleton.kt && git commit -m "feat(feedback): add ArcaneSkeleton component with shimmer animation"
```

---

## Task 5: Component-Specific Skeletons

Skeleton variants that match existing component layouts.

**Files:**
- Create: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/feedback/SkeletonListItem.kt`
- Create: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/feedback/SkeletonCard.kt`
- Create: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/feedback/SkeletonAvatar.kt`

**Step 1: Implement ArcaneSkeletonListItem**

```kotlin
package io.github.devmugi.arcane.design.components.feedback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun ArcaneSkeletonListItem(
    modifier: Modifier = Modifier,
    showLeadingIcon: Boolean = true,
    showTrailingContent: Boolean = false
) {
    Row(
        modifier = modifier
            .defaultMinSize(minHeight = 56.dp)
            .padding(
                horizontal = ArcaneSpacing.Medium,
                vertical = ArcaneSpacing.Small
            ),
        horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showLeadingIcon) {
            SkeletonBox(
                modifier = Modifier
                    .defaultMinSize(32.dp, 32.dp),
                shape = CircleShape
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XXSmall)
        ) {
            SkeletonBox(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(16.dp),
                shape = RoundedCornerShape(4.dp)
            )
            SkeletonBox(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(12.dp),
                shape = RoundedCornerShape(4.dp)
            )
        }

        if (showTrailingContent) {
            SkeletonBox(
                modifier = Modifier
                    .width(48.dp)
                    .height(20.dp),
                shape = RoundedCornerShape(4.dp)
            )
        }
    }
}
```

**Step 2: Implement ArcaneSkeletonCard**

```kotlin
package io.github.devmugi.arcane.design.components.feedback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.primitives.ArcaneSurface
import io.github.devmugi.arcane.design.foundation.primitives.SurfaceVariant
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun ArcaneSkeletonCard(
    modifier: Modifier = Modifier,
    showImage: Boolean = true,
    showActions: Boolean = true
) {
    ArcaneSurface(
        variant = SurfaceVariant.Raised,
        modifier = modifier,
        shape = ArcaneRadius.Large
    ) {
        Column {
            if (showImage) {
                SkeletonBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                )
            }

            Column(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall)
            ) {
                SkeletonBox(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(20.dp),
                    shape = RoundedCornerShape(4.dp)
                )
                SkeletonBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp),
                    shape = RoundedCornerShape(4.dp)
                )
                SkeletonBox(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(14.dp),
                    shape = RoundedCornerShape(4.dp)
                )
            }

            if (showActions) {
                Row(
                    modifier = Modifier.padding(
                        start = ArcaneSpacing.Medium,
                        end = ArcaneSpacing.Medium,
                        bottom = ArcaneSpacing.Medium
                    ),
                    horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
                ) {
                    SkeletonBox(
                        modifier = Modifier
                            .width(100.dp)
                            .height(36.dp),
                        shape = RoundedCornerShape(18.dp)
                    )
                }
            }
        }
    }
}
```

**Step 3: Implement ArcaneSkeletonAvatar**

```kotlin
package io.github.devmugi.arcane.design.components.feedback

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.devmugi.arcane.design.components.display.ArcaneAvatarSize

@Composable
fun ArcaneSkeletonAvatar(
    modifier: Modifier = Modifier,
    size: ArcaneAvatarSize = ArcaneAvatarSize.Medium
) {
    SkeletonBox(
        modifier = modifier.size(size.dp),
        shape = CircleShape
    )
}
```

**Step 4: Verify compilation**

```bash
cd /Users/den/IdeaProjects/ArcaneDesignSystem && ./gradlew :arcane-components:compileKotlinDesktop --quiet
```
Expected: BUILD SUCCESSFUL

**Step 5: Commit**

```bash
git add arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/feedback/SkeletonListItem.kt arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/feedback/SkeletonCard.kt arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/feedback/SkeletonAvatar.kt && git commit -m "feat(feedback): add component-specific skeleton variants"
```

---

## Task 6: ArcaneEmptyState

Flexible container for empty content scenarios.

**Files:**
- Create: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/feedback/EmptyState.kt`

**Step 1: Implement ArcaneEmptyState**

```kotlin
package io.github.devmugi.arcane.design.components.feedback

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.primitives.ArcaneSurface
import io.github.devmugi.arcane.design.foundation.primitives.SurfaceVariant
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun ArcaneEmptyState(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = ArcaneTheme.colors

    ArcaneSurface(
        variant = SurfaceVariant.Inset,
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = colors.border.copy(alpha = 0.5f),
                shape = ArcaneRadius.Medium
            ),
        showBorder = false
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ArcaneSpacing.XLarge),
            horizontalAlignment = contentAlignment,
            content = content
        )
    }
}
```

**Step 2: Verify compilation**

```bash
cd /Users/den/IdeaProjects/ArcaneDesignSystem && ./gradlew :arcane-components:compileKotlinDesktop --quiet
```
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/feedback/EmptyState.kt && git commit -m "feat(feedback): add ArcaneEmptyState component"
```

---

## Task 7: ArcaneAlertBanner

Inline persistent alerts with semantic variants.

**Files:**
- Create: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/feedback/AlertBanner.kt`

**Step 1: Implement ArcaneAlertBanner**

```kotlin
package io.github.devmugi.arcane.design.components.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Immutable
sealed class ArcaneAlertStyle {
    data object Info : ArcaneAlertStyle()
    data object Success : ArcaneAlertStyle()
    data object Warning : ArcaneAlertStyle()
    data object Error : ArcaneAlertStyle()
}

@Immutable
data class ArcaneAlertAction(
    val label: String,
    val onClick: () -> Unit
)

@Composable
fun ArcaneAlertBanner(
    message: String,
    modifier: Modifier = Modifier,
    style: ArcaneAlertStyle = ArcaneAlertStyle.Info,
    icon: (@Composable () -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
    action: ArcaneAlertAction? = null
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography

    val (accentColor, defaultIcon) = when (style) {
        is ArcaneAlertStyle.Info -> colors.primary to Icons.Default.Info
        is ArcaneAlertStyle.Success -> colors.success to Icons.Default.Check
        is ArcaneAlertStyle.Warning -> colors.warning to Icons.Default.Warning
        is ArcaneAlertStyle.Error -> colors.error to Icons.Default.Warning
    }

    val shape = RoundedCornerShape(8.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(accentColor.copy(alpha = 0.1f))
            .border(
                width = 1.dp,
                color = accentColor.copy(alpha = 0.3f),
                shape = shape
            )
    ) {
        // Left accent border
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .background(accentColor)
                .size(width = 3.dp, height = 56.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = ArcaneSpacing.Medium, end = ArcaneSpacing.Small)
                .padding(vertical = ArcaneSpacing.Small),
            horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            if (icon != null) {
                icon()
            } else {
                Icon(
                    imageVector = defaultIcon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = accentColor
                )
            }

            // Message
            Text(
                text = message,
                style = typography.bodyMedium,
                color = colors.text,
                modifier = Modifier.weight(1f)
            )

            // Action button
            if (action != null) {
                Text(
                    text = action.label,
                    style = typography.labelMedium,
                    color = accentColor,
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = action.onClick
                        )
                        .padding(horizontal = ArcaneSpacing.Small)
                )
            }

            // Dismiss button
            if (onDismiss != null) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Dismiss",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onDismiss
                        ),
                    tint = colors.textSecondary
                )
            }
        }
    }
}
```

**Step 2: Verify compilation**

```bash
cd /Users/den/IdeaProjects/ArcaneDesignSystem && ./gradlew :arcane-components:compileKotlinDesktop --quiet
```
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/feedback/AlertBanner.kt && git commit -m "feat(feedback): add ArcaneAlertBanner component with semantic variants"
```

---

## Task 8: ArcaneModal

Generic modal container with backdrop.

**Files:**
- Create: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/feedback/Modal.kt`

**Step 1: Implement ArcaneModal**

```kotlin
package io.github.devmugi.arcane.design.components.feedback

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.github.devmugi.arcane.design.foundation.primitives.ArcaneSurface
import io.github.devmugi.arcane.design.foundation.primitives.SurfaceVariant
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun ArcaneModal(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    dismissOnBackdropClick: Boolean = true,
    dismissOnBackPress: Boolean = true,
    content: @Composable () -> Unit
) {
    if (visible) {
        Dialog(
            onDismissRequest = {
                if (dismissOnBackPress) {
                    onDismissRequest()
                }
            },
            properties = DialogProperties(
                dismissOnBackPress = dismissOnBackPress,
                dismissOnClickOutside = dismissOnBackdropClick,
                usePlatformDefaultWidth = false
            )
        ) {
            val colors = ArcaneTheme.colors

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
                    .then(
                        if (dismissOnBackdropClick) {
                            Modifier.clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onDismissRequest
                            )
                        } else Modifier
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { /* Consume click to prevent backdrop dismiss */ }
                        )
                        .drawBehind {
                            drawCircle(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        colors.glow.copy(alpha = 0.3f),
                                        Color.Transparent
                                    ),
                                    center = Offset(size.width / 2, size.height / 2),
                                    radius = maxOf(size.width, size.height) * 0.8f
                                )
                            )
                        }
                ) {
                    ArcaneSurface(
                        variant = SurfaceVariant.Raised,
                        shape = ArcaneRadius.Large
                    ) {
                        Box(modifier = Modifier.padding(ArcaneSpacing.Large)) {
                            content()
                        }
                    }
                }
            }
        }
    }
}
```

**Step 2: Verify compilation**

```bash
cd /Users/den/IdeaProjects/ArcaneDesignSystem && ./gradlew :arcane-components:compileKotlinDesktop --quiet
```
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/feedback/Modal.kt && git commit -m "feat(feedback): add ArcaneModal component"
```

---

## Task 9: ArcaneConfirmationDialog

Pre-built confirmation dialog using ArcaneModal.

**Files:**
- Create: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/feedback/ConfirmationDialog.kt`

**Step 1: Implement ArcaneConfirmationDialog**

```kotlin
package io.github.devmugi.arcane.design.components.feedback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.components.controls.ArcaneButton
import io.github.devmugi.arcane.design.components.controls.ArcaneButtonStyle
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Immutable
sealed class ArcaneConfirmationStyle {
    data object Default : ArcaneConfirmationStyle()
    data object Destructive : ArcaneConfirmationStyle()
}

@Composable
fun ArcaneConfirmationDialog(
    visible: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    icon: (@Composable () -> Unit)? = null,
    confirmText: String = "Confirm",
    cancelText: String = "Cancel",
    style: ArcaneConfirmationStyle = ArcaneConfirmationStyle.Default
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography

    val iconColor = when (style) {
        is ArcaneConfirmationStyle.Default -> colors.primary
        is ArcaneConfirmationStyle.Destructive -> colors.error
    }

    ArcaneModal(
        visible = visible,
        onDismissRequest = onDismiss,
        modifier = modifier.width(280.dp),
        dismissOnBackdropClick = true
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icon
            if (icon != null) {
                icon()
            } else {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = iconColor
                )
            }

            Spacer(modifier = Modifier.height(ArcaneSpacing.Medium))

            // Title
            Text(
                text = title,
                style = typography.headlineMedium,
                color = colors.text,
                textAlign = TextAlign.Center
            )

            // Description
            if (description != null) {
                Spacer(modifier = Modifier.height(ArcaneSpacing.XSmall))
                Text(
                    text = description,
                    style = typography.bodyMedium,
                    color = colors.textSecondary,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(ArcaneSpacing.Large))

            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
            ) {
                ArcaneButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    style = ArcaneButtonStyle.Secondary
                ) {
                    Text(cancelText)
                }

                ArcaneButton(
                    onClick = {
                        onConfirm()
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f),
                    style = ArcaneButtonStyle.Primary
                ) {
                    Text(
                        text = confirmText,
                        color = if (style is ArcaneConfirmationStyle.Destructive) {
                            colors.error
                        } else {
                            ArcaneTheme.colors.surface
                        }
                    )
                }
            }
        }
    }
}
```

**Step 2: Verify compilation**

```bash
cd /Users/den/IdeaProjects/ArcaneDesignSystem && ./gradlew :arcane-components:compileKotlinDesktop --quiet
```
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/feedback/ConfirmationDialog.kt && git commit -m "feat(feedback): add ArcaneConfirmationDialog component"
```

---

## Task 10: Toast System

Toast data, state holder, and host composable.

**Files:**
- Create: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/feedback/Toast.kt`

**Step 1: Implement Toast System**

```kotlin
package io.github.devmugi.arcane.design.components.feedback

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing
import kotlinx.coroutines.delay

@Immutable
sealed class ArcaneToastStyle {
    data object Default : ArcaneToastStyle()
    data object Success : ArcaneToastStyle()
    data object Warning : ArcaneToastStyle()
    data object Error : ArcaneToastStyle()
}

enum class ArcaneToastPosition {
    TopStart, TopCenter, TopEnd,
    BottomStart, BottomCenter, BottomEnd
}

@Immutable
data class ArcaneToastData(
    val id: Long = System.currentTimeMillis(),
    val message: String,
    val style: ArcaneToastStyle = ArcaneToastStyle.Default,
    val durationMs: Long = 4000L
)

class ArcaneToastState {
    internal val toasts: SnapshotStateList<ArcaneToastData> = mutableStateListOf()

    fun show(message: String, style: ArcaneToastStyle = ArcaneToastStyle.Default, durationMs: Long = 4000L) {
        show(ArcaneToastData(message = message, style = style, durationMs = durationMs))
    }

    fun show(toast: ArcaneToastData) {
        // Limit to 3 visible toasts
        if (toasts.size >= 3) {
            toasts.removeAt(0)
        }
        toasts.add(toast)
    }

    fun dismiss(id: Long) {
        toasts.removeAll { it.id == id }
    }

    fun dismissAll() {
        toasts.clear()
    }
}

@Composable
fun rememberArcaneToastState(): ArcaneToastState {
    return remember { ArcaneToastState() }
}

val LocalArcaneToastState = compositionLocalOf<ArcaneToastState> {
    error("No ArcaneToastState provided. Wrap your content with ArcaneToastHost.")
}

@Composable
fun ArcaneToastHost(
    state: ArcaneToastState,
    modifier: Modifier = Modifier,
    position: ArcaneToastPosition = ArcaneToastPosition.BottomCenter
) {
    val alignment = when (position) {
        ArcaneToastPosition.TopStart -> Alignment.TopStart
        ArcaneToastPosition.TopCenter -> Alignment.TopCenter
        ArcaneToastPosition.TopEnd -> Alignment.TopEnd
        ArcaneToastPosition.BottomStart -> Alignment.BottomStart
        ArcaneToastPosition.BottomCenter -> Alignment.BottomCenter
        ArcaneToastPosition.BottomEnd -> Alignment.BottomEnd
    }

    val isTop = position in listOf(
        ArcaneToastPosition.TopStart,
        ArcaneToastPosition.TopCenter,
        ArcaneToastPosition.TopEnd
    )

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = alignment
    ) {
        Column(
            modifier = Modifier.padding(ArcaneSpacing.Medium),
            verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small),
            horizontalAlignment = when (position) {
                ArcaneToastPosition.TopStart, ArcaneToastPosition.BottomStart -> Alignment.Start
                ArcaneToastPosition.TopCenter, ArcaneToastPosition.BottomCenter -> Alignment.CenterHorizontally
                ArcaneToastPosition.TopEnd, ArcaneToastPosition.BottomEnd -> Alignment.End
            }
        ) {
            state.toasts.forEach { toast ->
                key(toast.id) {
                    ArcaneToastItem(
                        toast = toast,
                        isTop = isTop,
                        onDismiss = { state.dismiss(toast.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ArcaneToastItem(
    toast: ArcaneToastData,
    isTop: Boolean,
    onDismiss: () -> Unit
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography

    val accentColor = when (toast.style) {
        is ArcaneToastStyle.Default -> colors.primary
        is ArcaneToastStyle.Success -> colors.success
        is ArcaneToastStyle.Warning -> colors.warning
        is ArcaneToastStyle.Error -> colors.error
    }

    LaunchedEffect(toast.id) {
        delay(toast.durationMs)
        onDismiss()
    }

    val shape = RoundedCornerShape(8.dp)

    AnimatedVisibility(
        visible = true,
        enter = slideInVertically(
            initialOffsetY = { if (isTop) -it else it },
            animationSpec = tween(300)
        ) + fadeIn(animationSpec = tween(300)),
        exit = slideOutVertically(
            targetOffsetY = { if (isTop) -it else it },
            animationSpec = tween(200)
        ) + fadeOut(animationSpec = tween(200))
    ) {
        Row(
            modifier = Modifier
                .widthIn(min = 200.dp, max = 400.dp)
                .clip(shape)
                .background(colors.surfaceRaised)
                .border(1.dp, accentColor.copy(alpha = 0.5f), shape)
                .padding(
                    start = ArcaneSpacing.Medium,
                    end = ArcaneSpacing.Small,
                    top = ArcaneSpacing.Small,
                    bottom = ArcaneSpacing.Small
                ),
            horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = toast.message,
                style = typography.bodyMedium,
                color = colors.text,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Dismiss",
                modifier = Modifier
                    .size(18.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onDismiss
                    ),
                tint = colors.textSecondary
            )
        }
    }
}

@Composable
private fun key(id: Long, content: @Composable () -> Unit) {
    androidx.compose.runtime.key(id) {
        content()
    }
}
```

**Step 2: Verify compilation**

```bash
cd /Users/den/IdeaProjects/ArcaneDesignSystem && ./gradlew :arcane-components:compileKotlinDesktop --quiet
```
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/feedback/Toast.kt && git commit -m "feat(feedback): add Toast system with state holder and host"
```

---

## Task 11: Catalog FeedbackScreen

Create the catalog showcase screen.

**Files:**
- Create: `catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/screens/FeedbackScreen.kt`

**Step 1: Implement FeedbackScreen**

```kotlin
package io.github.devmugi.arcane.catalog.screens

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Add
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
import io.github.devmugi.arcane.design.components.controls.ArcaneButton
import io.github.devmugi.arcane.design.components.controls.ArcaneButtonStyle
import io.github.devmugi.arcane.design.components.controls.ArcaneTextButton
import io.github.devmugi.arcane.design.components.feedback.ArcaneAlertAction
import io.github.devmugi.arcane.design.components.feedback.ArcaneAlertBanner
import io.github.devmugi.arcane.design.components.feedback.ArcaneAlertStyle
import io.github.devmugi.arcane.design.components.feedback.ArcaneCircularProgress
import io.github.devmugi.arcane.design.components.feedback.ArcaneConfirmationDialog
import io.github.devmugi.arcane.design.components.feedback.ArcaneConfirmationStyle
import io.github.devmugi.arcane.design.components.feedback.ArcaneEmptyState
import io.github.devmugi.arcane.design.components.feedback.ArcaneLinearProgress
import io.github.devmugi.arcane.design.components.feedback.ArcaneSkeletonCard
import io.github.devmugi.arcane.design.components.feedback.ArcaneSkeletonListItem
import io.github.devmugi.arcane.design.components.feedback.ArcaneSpinner
import io.github.devmugi.arcane.design.components.feedback.ArcaneSpinnerSize
import io.github.devmugi.arcane.design.components.feedback.ArcaneToastHost
import io.github.devmugi.arcane.design.components.feedback.ArcaneToastPosition
import io.github.devmugi.arcane.design.components.feedback.ArcaneToastStyle
import io.github.devmugi.arcane.design.components.feedback.rememberArcaneToastState
import io.github.devmugi.arcane.design.foundation.primitives.ArcaneSurface
import io.github.devmugi.arcane.design.foundation.primitives.SurfaceVariant
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun FeedbackScreen() {
    val typography = ArcaneTheme.typography
    val colors = ArcaneTheme.colors
    val scrollState = rememberScrollState()
    val toastState = rememberArcaneToastState()

    var showDefaultDialog by remember { mutableStateOf(false) }
    var showDestructiveDialog by remember { mutableStateOf(false) }

    // Confirmation Dialogs
    ArcaneConfirmationDialog(
        visible = showDefaultDialog,
        onDismiss = { showDefaultDialog = false },
        onConfirm = { toastState.show("Confirmed!", ArcaneToastStyle.Success) },
        title = "Confirm Action",
        description = "Are you sure you want to proceed with this action?"
    )

    ArcaneConfirmationDialog(
        visible = showDestructiveDialog,
        onDismiss = { showDestructiveDialog = false },
        onConfirm = { toastState.show("Item deleted", ArcaneToastStyle.Error) },
        title = "Delete Item?",
        description = "Are you sure you want to delete this item?",
        confirmText = "Delete",
        cancelText = "Cancel",
        style = ArcaneConfirmationStyle.Destructive
    )

    // Toast Host
    ArcaneToastHost(
        state = toastState,
        position = ArcaneToastPosition.BottomCenter
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(ArcaneSpacing.Medium),
        verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Large)
    ) {
        Text(
            text = "Feedback",
            style = typography.displayMedium,
            color = colors.text
        )

        // Modals Section
        SectionTitle("Modals")
        ArcaneSurface(
            variant = SurfaceVariant.Raised,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
            ) {
                ArcaneTextButton(
                    text = "Confirmation",
                    onClick = { showDefaultDialog = true },
                    style = ArcaneButtonStyle.Secondary
                )
                ArcaneTextButton(
                    text = "Destructive",
                    onClick = { showDestructiveDialog = true },
                    style = ArcaneButtonStyle.Secondary
                )
            }
        }

        // Toasts Section
        SectionTitle("Toasts")
        ArcaneSurface(
            variant = SurfaceVariant.Raised,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
            ) {
                ArcaneTextButton(
                    text = "Default",
                    onClick = { toastState.show("This is a default toast") },
                    style = ArcaneButtonStyle.Secondary
                )
                ArcaneTextButton(
                    text = "Success",
                    onClick = { toastState.show("Operation successful!", ArcaneToastStyle.Success) },
                    style = ArcaneButtonStyle.Secondary
                )
                ArcaneTextButton(
                    text = "Warning",
                    onClick = { toastState.show("Please review your input", ArcaneToastStyle.Warning) },
                    style = ArcaneButtonStyle.Secondary
                )
                ArcaneTextButton(
                    text = "Error",
                    onClick = { toastState.show("Something went wrong", ArcaneToastStyle.Error) },
                    style = ArcaneButtonStyle.Secondary
                )
            }
        }

        // Alert Banners Section
        SectionTitle("Alert Banners")
        Column(verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)) {
            ArcaneAlertBanner(
                message = "This is an informational message.",
                style = ArcaneAlertStyle.Info,
                onDismiss = { }
            )
            ArcaneAlertBanner(
                message = "Operation completed successfully!",
                style = ArcaneAlertStyle.Success
            )
            ArcaneAlertBanner(
                message = "Server is experiencing high load.",
                style = ArcaneAlertStyle.Warning,
                action = ArcaneAlertAction("Retry") { }
            )
            ArcaneAlertBanner(
                message = "Connection failed. Please try again.",
                style = ArcaneAlertStyle.Error,
                onDismiss = { },
                action = ArcaneAlertAction("Retry") { }
            )
        }

        // Progress Section
        SectionTitle("Progress")
        ArcaneSurface(
            variant = SurfaceVariant.Raised,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Medium)
            ) {
                Text("Circular Progress", style = typography.labelMedium, color = colors.textSecondary)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Large),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ArcaneCircularProgress(progress = 0.25f)
                    ArcaneCircularProgress(progress = 0.5f, showLabel = true)
                    ArcaneCircularProgress(progress = 0.75f, size = 64.dp, showLabel = true)
                }

                Text("Linear Progress", style = typography.labelMedium, color = colors.textSecondary)
                Column(verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)) {
                    ArcaneLinearProgress(progress = 0.3f)
                    ArcaneLinearProgress(progress = 0.6f)
                    ArcaneLinearProgress(progress = 0.9f)
                }
            }
        }

        // Spinner Section
        SectionTitle("Spinner")
        ArcaneSurface(
            variant = SurfaceVariant.Raised,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Large),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    ArcaneSpinner(size = ArcaneSpinnerSize.Small)
                    Spacer(modifier = Modifier.height(ArcaneSpacing.XSmall))
                    Text("Small", style = typography.labelSmall, color = colors.textSecondary)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    ArcaneSpinner(size = ArcaneSpinnerSize.Medium)
                    Spacer(modifier = Modifier.height(ArcaneSpacing.XSmall))
                    Text("Medium", style = typography.labelSmall, color = colors.textSecondary)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    ArcaneSpinner(size = ArcaneSpinnerSize.Large)
                    Spacer(modifier = Modifier.height(ArcaneSpacing.XSmall))
                    Text("Large", style = typography.labelSmall, color = colors.textSecondary)
                }
            }
        }

        // Skeletons Section
        SectionTitle("Skeletons")
        ArcaneSurface(
            variant = SurfaceVariant.Raised,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Medium)
            ) {
                Text("List Item Skeleton", style = typography.labelMedium, color = colors.textSecondary)
                ArcaneSkeletonListItem()
                ArcaneSkeletonListItem(showTrailingContent = true)

                Text("Card Skeleton", style = typography.labelMedium, color = colors.textSecondary)
                ArcaneSkeletonCard(modifier = Modifier.fillMaxWidth())
            }
        }

        // Empty State Section
        SectionTitle("Empty State")
        ArcaneEmptyState {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = colors.textDisabled
            )
            Spacer(modifier = Modifier.height(ArcaneSpacing.Medium))
            Text(
                text = "No items found",
                style = typography.headlineMedium,
                color = colors.text
            )
            Spacer(modifier = Modifier.height(ArcaneSpacing.XSmall))
            Text(
                text = "Start by adding a new project.",
                style = typography.bodyMedium,
                color = colors.textSecondary
            )
            Spacer(modifier = Modifier.height(ArcaneSpacing.Large))
            ArcaneButton(onClick = { }) {
                Text("Add Project")
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

**Step 2: Verify compilation**

```bash
cd /Users/den/IdeaProjects/ArcaneDesignSystem && ./gradlew :catalog:composeApp:compileKotlinDesktop --quiet
```
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/screens/FeedbackScreen.kt && git commit -m "feat(catalog): add FeedbackScreen showcasing all feedback components"
```

---

## Task 12: Update App.kt Navigation

Add Feedback tab to catalog navigation.

**Files:**
- Modify: `catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/App.kt`

**Step 1: Update App.kt**

Add import:
```kotlin
import io.github.devmugi.arcane.catalog.screens.FeedbackScreen
```

Update tabs list:
```kotlin
ArcaneTabs(
    tabs = listOf(
        ArcaneTab("Controls"),
        ArcaneTab("Navigation"),
        ArcaneTab("Data Display"),
        ArcaneTab("Feedback")
    ),
    selectedIndex = selectedScreen,
    onTabSelected = { selectedScreen = it }
)
```

Update when block:
```kotlin
when (selectedScreen) {
    0 -> ControlsScreen()
    1 -> NavigationScreen()
    2 -> DataDisplayScreen()
    3 -> FeedbackScreen()
}
```

**Step 2: Verify full build**

```bash
cd /Users/den/IdeaProjects/ArcaneDesignSystem && ./gradlew :catalog:composeApp:compileKotlinDesktop --quiet
```
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/App.kt && git commit -m "feat(catalog): add Feedback tab to navigation"
```

---

## Task 13: Final Build Verification

Run full project build to verify everything compiles.

**Step 1: Clean and build**

```bash
cd /Users/den/IdeaProjects/ArcaneDesignSystem && ./gradlew clean build --quiet
```
Expected: BUILD SUCCESSFUL

**Step 2: Run desktop catalog (visual verification)**

```bash
cd /Users/den/IdeaProjects/ArcaneDesignSystem && ./gradlew :catalog:composeApp:run
```
Expected: Catalog app opens with Feedback tab visible and all components render correctly

**Step 3: Final commit (if any fixes were needed)**

```bash
git status
```
If clean: No commit needed
If changes: Commit with appropriate message

---

## Summary

**Total Tasks:** 13 (including setup and verification)

**Components Implemented:**
1. ArcaneSpinner
2. ArcaneCircularProgress
3. ArcaneLinearProgress
4. ArcaneSkeleton (with Text, Circle, Rectangle shapes)
5. ArcaneSkeletonListItem
6. ArcaneSkeletonCard
7. ArcaneSkeletonAvatar
8. ArcaneEmptyState
9. ArcaneAlertBanner
10. ArcaneModal
11. ArcaneConfirmationDialog
12. Toast System (ArcaneToastData, ArcaneToastState, ArcaneToastHost)

**Files Created:** 12 component files + 1 catalog screen
**Files Modified:** 1 (App.kt)
