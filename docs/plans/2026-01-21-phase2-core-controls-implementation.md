# Phase 2: Core Controls - Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Implement the six core control components (Button, TextField, Checkbox, RadioButton, Switch, Slider) with all variants shown in the design reference.

**Architecture:** All components go in `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/`. Each component uses foundation tokens and follows a consistent API pattern with sealed class styles. Catalog app gets updated with a ControlsScreen to showcase all components.

**Tech Stack:** Kotlin Multiplatform, Compose Multiplatform 1.7.x, foundation tokens from arcane-foundation

---

## Task 1: Create ArcaneButton Component

**Files:**
- Create: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/Button.kt`

**Step 1: Create Button.kt with all variants**

```kotlin
// arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/Button.kt
package io.github.devmugi.arcane.design.components.controls

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneBorder
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

sealed class ArcaneButtonStyle {
    data object Primary : ArcaneButtonStyle()
    data object Secondary : ArcaneButtonStyle()
}

@Composable
fun ArcaneButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    style: ArcaneButtonStyle = ArcaneButtonStyle.Primary,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = ArcaneSpacing.Medium,
        vertical = ArcaneSpacing.Small
    ),
    content: @Composable RowScope.() -> Unit
) {
    val colors = ArcaneTheme.colors
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val backgroundColor = when {
        !enabled -> colors.surfaceInset
        loading -> colors.surfacePressed
        isPressed -> colors.surfacePressed
        style is ArcaneButtonStyle.Primary -> colors.primary
        else -> colors.surface
    }

    val contentColor = when {
        !enabled -> colors.textDisabled
        style is ArcaneButtonStyle.Primary && !isPressed && !loading -> colors.surface
        else -> colors.text
    }

    val borderColor = when {
        !enabled -> colors.textDisabled.copy(alpha = 0.3f)
        else -> colors.border
    }

    val glowAlpha by animateFloatAsState(
        targetValue = if (enabled && !loading && style is ArcaneButtonStyle.Primary) 0.3f else 0f,
        animationSpec = tween(150),
        label = "glowAlpha"
    )

    Box(
        modifier = modifier
            .defaultMinSize(minHeight = 44.dp)
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
            .clip(ArcaneRadius.Full)
            .background(backgroundColor, ArcaneRadius.Full)
            .border(ArcaneBorder.Title, borderColor, ArcaneRadius.Full)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled && !loading,
                role = Role.Button,
                onClick = onClick
            )
            .padding(contentPadding),
        contentAlignment = Alignment.Center
    ) {
        val contentAlpha by animateFloatAsState(
            targetValue = if (loading) 0f else 1f,
            animationSpec = tween(150),
            label = "contentAlpha"
        )

        Row(
            modifier = Modifier.alpha(contentAlpha),
            horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.XSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            androidx.compose.runtime.CompositionLocalProvider(
                androidx.compose.material3.LocalContentColor provides contentColor
            ) {
                content()
            }
        }

        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = colors.primary,
                strokeWidth = 2.dp
            )
        }
    }
}

@Composable
fun ArcaneTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    style: ArcaneButtonStyle = ArcaneButtonStyle.Primary,
) {
    ArcaneButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        loading = loading,
        style = style
    ) {
        Text(
            text = text,
            style = ArcaneTheme.typography.labelLarge,
            color = androidx.compose.material3.LocalContentColor.current
        )
    }
}
```

**Step 2: Verify build compiles**

Run: `./gradlew :arcane-components:compileKotlinDesktop`

Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add arcane-components/
git commit -m "feat(components): add ArcaneButton with Primary/Secondary/Loading/Disabled states"
```

---

## Task 2: Create ArcaneTextField Component

**Files:**
- Create: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/TextField.kt`

**Step 1: Create TextField.kt with all variants**

```kotlin
// arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/TextField.kt
package io.github.devmugi.arcane.design.components.controls

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneBorder
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun ArcaneTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    helperText: String? = null,
    errorText: String? = null,
    isError: Boolean = errorText != null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isPassword: Boolean = false,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography
    val isFocused by interactionSource.collectIsFocusedAsState()

    val borderColor by animateColorAsState(
        targetValue = when {
            isError -> colors.error
            isFocused -> colors.borderFocused
            !enabled -> colors.textDisabled.copy(alpha = 0.3f)
            else -> colors.border
        },
        animationSpec = tween(150),
        label = "borderColor"
    )

    val backgroundColor = when {
        !enabled -> colors.surfaceInset.copy(alpha = 0.5f)
        else -> colors.surfaceInset
    }

    Column(modifier = modifier) {
        if (label != null) {
            Text(
                text = label,
                style = typography.labelMedium,
                color = if (isError) colors.error else colors.textSecondary,
                modifier = Modifier.padding(bottom = ArcaneSpacing.XXSmall)
            )
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .clip(ArcaneRadius.Medium)
                .background(backgroundColor)
                .border(ArcaneBorder.Title, borderColor, ArcaneRadius.Medium)
                .padding(ArcaneSpacing.Small),
            enabled = enabled,
            readOnly = readOnly,
            textStyle = typography.bodyMedium.copy(color = colors.text),
            cursorBrush = SolidColor(colors.primary),
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            interactionSource = interactionSource,
            decorationBox = { innerTextField ->
                Box {
                    if (value.isEmpty() && placeholder != null) {
                        Text(
                            text = placeholder,
                            style = typography.bodyMedium,
                            color = colors.textDisabled
                        )
                    }
                    innerTextField()
                }
            }
        )

        val supportingText = errorText ?: helperText
        if (supportingText != null) {
            Text(
                text = supportingText,
                style = typography.labelSmall,
                color = if (isError) colors.error else colors.textSecondary,
                modifier = Modifier.padding(top = ArcaneSpacing.XXSmall)
            )
        }
    }
}
```

**Step 2: Verify build compiles**

Run: `./gradlew :arcane-components:compileKotlinDesktop`

Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add arcane-components/
git commit -m "feat(components): add ArcaneTextField with label/placeholder/helper/error states"
```

---

## Task 3: Create ArcaneCheckbox Component

**Files:**
- Create: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/Checkbox.kt`

**Step 1: Create Checkbox.kt**

```kotlin
// arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/Checkbox.kt
package io.github.devmugi.arcane.design.components.controls

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneBorder
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun ArcaneCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography

    val backgroundColor by animateColorAsState(
        targetValue = when {
            !enabled -> colors.surfaceInset.copy(alpha = 0.5f)
            checked -> colors.primary
            else -> colors.surfaceInset
        },
        animationSpec = tween(150),
        label = "backgroundColor"
    )

    val borderColor by animateColorAsState(
        targetValue = when {
            !enabled -> colors.textDisabled.copy(alpha = 0.3f)
            checked -> colors.primary
            else -> colors.border
        },
        animationSpec = tween(150),
        label = "borderColor"
    )

    val checkmarkColor = if (checked) colors.surface else Color.Transparent

    Row(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                role = Role.Checkbox,
                onClick = { onCheckedChange(!checked) }
            ),
        horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(ArcaneRadius.Small)
                .background(backgroundColor)
                .border(ArcaneBorder.Title, borderColor, ArcaneRadius.Small)
                .drawWithContent {
                    drawContent()
                    if (checked) {
                        val strokeWidth = 2.dp.toPx()
                        val padding = 5.dp.toPx()
                        // Draw checkmark
                        drawLine(
                            color = checkmarkColor,
                            start = Offset(padding, size.height / 2),
                            end = Offset(size.width / 2 - 1.dp.toPx(), size.height - padding),
                            strokeWidth = strokeWidth,
                            cap = StrokeCap.Round
                        )
                        drawLine(
                            color = checkmarkColor,
                            start = Offset(size.width / 2 - 1.dp.toPx(), size.height - padding),
                            end = Offset(size.width - padding, padding),
                            strokeWidth = strokeWidth,
                            cap = StrokeCap.Round
                        )
                    }
                },
            contentAlignment = Alignment.Center
        ) {}

        if (label != null) {
            Text(
                text = label,
                style = typography.bodyMedium,
                color = if (enabled) colors.text else colors.textDisabled
            )
        }
    }
}
```

**Step 2: Verify build compiles**

Run: `./gradlew :arcane-components:compileKotlinDesktop`

Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add arcane-components/
git commit -m "feat(components): add ArcaneCheckbox with checked/unchecked states"
```

---

## Task 4: Create ArcaneRadioButton Component

**Files:**
- Create: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/RadioButton.kt`

**Step 1: Create RadioButton.kt**

```kotlin
// arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/RadioButton.kt
package io.github.devmugi.arcane.design.components.controls

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneBorder
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun ArcaneRadioButton(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography

    val borderColor by animateColorAsState(
        targetValue = when {
            !enabled -> colors.textDisabled.copy(alpha = 0.3f)
            selected -> colors.primary
            else -> colors.border
        },
        animationSpec = tween(150),
        label = "borderColor"
    )

    val innerDotSize by animateDpAsState(
        targetValue = if (selected) 10.dp else 0.dp,
        animationSpec = tween(150),
        label = "innerDotSize"
    )

    val innerDotColor by animateColorAsState(
        targetValue = if (selected && enabled) colors.primary else Color.Transparent,
        animationSpec = tween(150),
        label = "innerDotColor"
    )

    Row(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                role = Role.RadioButton,
                onClick = onClick
            ),
        horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(colors.surfaceInset)
                .border(ArcaneBorder.Title, borderColor, CircleShape)
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(innerDotSize)
                    .clip(CircleShape)
                    .background(innerDotColor)
            )
        }

        if (label != null) {
            Text(
                text = label,
                style = typography.bodyMedium,
                color = if (enabled) colors.text else colors.textDisabled
            )
        }
    }
}
```

**Step 2: Verify build compiles**

Run: `./gradlew :arcane-components:compileKotlinDesktop`

Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add arcane-components/
git commit -m "feat(components): add ArcaneRadioButton with selected/unselected states"
```

---

## Task 5: Create ArcaneSwitch Component

**Files:**
- Create: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/Switch.kt`

**Step 1: Create Switch.kt**

```kotlin
// arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/Switch.kt
package io.github.devmugi.arcane.design.components.controls

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneBorder
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun ArcaneSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography

    val trackWidth = 48.dp
    val trackHeight = 26.dp
    val thumbSize = 20.dp
    val thumbPadding = 3.dp

    val thumbOffset by animateDpAsState(
        targetValue = if (checked) trackWidth - thumbSize - thumbPadding * 2 else 0.dp,
        animationSpec = tween(200),
        label = "thumbOffset"
    )

    val trackColor by animateColorAsState(
        targetValue = when {
            !enabled -> colors.surfaceInset.copy(alpha = 0.5f)
            checked -> colors.primary
            else -> colors.surfaceInset
        },
        animationSpec = tween(200),
        label = "trackColor"
    )

    val borderColor by animateColorAsState(
        targetValue = when {
            !enabled -> colors.textDisabled.copy(alpha = 0.3f)
            checked -> colors.primary
            else -> colors.border
        },
        animationSpec = tween(200),
        label = "borderColor"
    )

    val thumbColor by animateColorAsState(
        targetValue = when {
            !enabled -> colors.textDisabled
            checked -> colors.surface
            else -> colors.textSecondary
        },
        animationSpec = tween(200),
        label = "thumbColor"
    )

    Row(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                role = Role.Switch,
                onClick = { onCheckedChange(!checked) }
            ),
        horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(trackWidth)
                .height(trackHeight)
                .clip(ArcaneRadius.Full)
                .background(trackColor)
                .border(ArcaneBorder.Title, borderColor, ArcaneRadius.Full)
                .padding(thumbPadding),
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .offset(x = thumbOffset)
                    .size(thumbSize)
                    .clip(CircleShape)
                    .background(thumbColor)
            )
        }

        if (label != null) {
            Text(
                text = label,
                style = typography.bodyMedium,
                color = if (enabled) colors.text else colors.textDisabled
            )
        }
    }
}
```

**Step 2: Verify build compiles**

Run: `./gradlew :arcane-components:compileKotlinDesktop`

Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add arcane-components/
git commit -m "feat(components): add ArcaneSwitch with on/off states"
```

---

## Task 6: Create ArcaneSlider Component

**Files:**
- Create: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/Slider.kt`

**Step 1: Create Slider.kt with tooltip**

```kotlin
// arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/Slider.kt
package io.github.devmugi.arcane.design.components.controls

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneBorder
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing
import kotlin.math.roundToInt

@Composable
fun ArcaneSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    showTooltip: Boolean = true,
    valueLabel: (Float) -> String = { "${(it * 100).roundToInt()}%" },
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val colors = ArcaneTheme.colors
    val typography = ArcaneTheme.typography
    val density = LocalDensity.current

    val trackHeight = 6.dp
    val thumbSize = 20.dp

    var trackWidthPx by remember { mutableStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }

    val normalizedValue = ((value - valueRange.start) / (valueRange.endInclusive - valueRange.start))
        .coerceIn(0f, 1f)

    val trackColor = if (enabled) colors.surfaceInset else colors.surfaceInset.copy(alpha = 0.5f)
    val activeTrackColor = if (enabled) colors.primary else colors.textDisabled
    val thumbColor = if (enabled) colors.primary else colors.textDisabled
    val borderColor = if (enabled) colors.border else colors.textDisabled.copy(alpha = 0.3f)

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(thumbSize + 40.dp), // Extra space for tooltip
            contentAlignment = Alignment.BottomStart
        ) {
            // Tooltip
            AnimatedVisibility(
                visible = showTooltip && isDragging,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .offset {
                        val thumbCenterPx = normalizedValue * trackWidthPx
                        IntOffset(
                            x = (thumbCenterPx - with(density) { 24.dp.toPx() }).roundToInt(),
                            y = 0
                        )
                    }
            ) {
                Box(
                    modifier = Modifier
                        .width(48.dp)
                        .clip(ArcaneRadius.Small)
                        .background(colors.surfaceRaised)
                        .border(ArcaneBorder.Title, colors.border, ArcaneRadius.Small)
                        .padding(horizontal = ArcaneSpacing.XSmall, vertical = ArcaneSpacing.XXSmall),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = valueLabel(value),
                        style = typography.labelSmall,
                        color = colors.text
                    )
                }
            }

            // Track and thumb container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(thumbSize)
                    .align(Alignment.BottomStart)
                    .onSizeChanged { size ->
                        trackWidthPx = size.width.toFloat()
                    }
                    .pointerInput(enabled, valueRange) {
                        if (!enabled) return@pointerInput
                        detectTapGestures { offset ->
                            val newNormalized = (offset.x / trackWidthPx).coerceIn(0f, 1f)
                            val newValue = valueRange.start + newNormalized * (valueRange.endInclusive - valueRange.start)
                            onValueChange(newValue)
                        }
                    }
                    .pointerInput(enabled, valueRange) {
                        if (!enabled) return@pointerInput
                        detectDragGestures(
                            onDragStart = { isDragging = true },
                            onDragEnd = { isDragging = false },
                            onDragCancel = { isDragging = false },
                            onDrag = { change, _ ->
                                change.consume()
                                val newNormalized = (change.position.x / trackWidthPx).coerceIn(0f, 1f)
                                val newValue = valueRange.start + newNormalized * (valueRange.endInclusive - valueRange.start)
                                onValueChange(newValue)
                            }
                        )
                    },
                contentAlignment = Alignment.CenterStart
            ) {
                // Inactive track
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(trackHeight)
                        .clip(ArcaneRadius.Full)
                        .background(trackColor)
                        .border(ArcaneBorder.Title, borderColor, ArcaneRadius.Full)
                )

                // Active track
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = normalizedValue)
                        .height(trackHeight)
                        .clip(ArcaneRadius.Full)
                        .background(activeTrackColor)
                )

                // Thumb
                Box(
                    modifier = Modifier
                        .offset {
                            val maxOffset = trackWidthPx - with(density) { thumbSize.toPx() }
                            IntOffset(
                                x = (normalizedValue * maxOffset).roundToInt(),
                                y = 0
                            )
                        }
                        .size(thumbSize)
                        .clip(CircleShape)
                        .background(thumbColor)
                        .border(ArcaneBorder.Medium, colors.surface, CircleShape)
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
git add arcane-components/
git commit -m "feat(components): add ArcaneSlider with value tooltip"
```

---

## Task 7: Update Catalog App with ControlsScreen

**Files:**
- Create: `catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/screens/ControlsScreen.kt`
- Modify: `catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/App.kt`

**Step 1: Create ControlsScreen.kt**

```kotlin
// catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/screens/ControlsScreen.kt
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
import io.github.devmugi.arcane.design.components.controls.ArcaneButtonStyle
import io.github.devmugi.arcane.design.components.controls.ArcaneCheckbox
import io.github.devmugi.arcane.design.components.controls.ArcaneRadioButton
import io.github.devmugi.arcane.design.components.controls.ArcaneSlider
import io.github.devmugi.arcane.design.components.controls.ArcaneSwitch
import io.github.devmugi.arcane.design.components.controls.ArcaneTextButton
import io.github.devmugi.arcane.design.components.controls.ArcaneTextField
import io.github.devmugi.arcane.design.foundation.primitives.ArcaneSurface
import io.github.devmugi.arcane.design.foundation.primitives.SurfaceVariant
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun ControlsScreen() {
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
            text = "Controls",
            style = typography.displayMedium,
            color = colors.text
        )

        // Buttons Section
        SectionTitle("Buttons")
        ArcaneSurface(
            variant = SurfaceVariant.Raised,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
            ) {
                ArcaneTextButton(
                    text = "Primary",
                    onClick = {},
                    style = ArcaneButtonStyle.Primary
                )
                ArcaneTextButton(
                    text = "Secondary",
                    onClick = {},
                    style = ArcaneButtonStyle.Secondary
                )
                ArcaneTextButton(
                    text = "Loading",
                    onClick = {},
                    loading = true
                )
                ArcaneTextButton(
                    text = "Disabled",
                    onClick = {},
                    enabled = false
                )
            }
        }

        // TextField Section
        SectionTitle("Text Field")
        ArcaneSurface(
            variant = SurfaceVariant.Raised,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
            ) {
                var text1 by remember { mutableStateOf("") }
                ArcaneTextField(
                    value = text1,
                    onValueChange = { text1 = it },
                    placeholder = "Placeholder"
                )

                var text2 by remember { mutableStateOf("") }
                ArcaneTextField(
                    value = text2,
                    onValueChange = { text2 = it },
                    label = "Helper Text",
                    helperText = "Password must be 8+ characters"
                )

                var text3 by remember { mutableStateOf("") }
                ArcaneTextField(
                    value = text3,
                    onValueChange = { text3 = it },
                    label = "Focused Input Highlight",
                    placeholder = "Enter text..."
                )

                ArcaneTextField(
                    value = "Invalid email format",
                    onValueChange = {},
                    label = "Error State",
                    errorText = "Invalid email format"
                )
            }
        }

        // Tactile Section (Checkbox & Radio)
        SectionTitle("Tactile")
        ArcaneSurface(
            variant = SurfaceVariant.Raised,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
            ) {
                var checked by remember { mutableStateOf(true) }
                ArcaneCheckbox(
                    checked = checked,
                    onCheckedChange = { checked = it },
                    label = "Checkbox"
                )

                var selectedRadio by remember { mutableStateOf(0) }
                ArcaneRadioButton(
                    selected = selectedRadio == 0,
                    onClick = { selectedRadio = 0 },
                    label = "Radio Button"
                )
                ArcaneRadioButton(
                    selected = selectedRadio == 1,
                    onClick = { selectedRadio = 1 },
                    label = "OFF"
                )
            }
        }

        // Switch Section
        SectionTitle("Switch")
        ArcaneSurface(
            variant = SurfaceVariant.Raised,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(ArcaneSpacing.Medium),
                horizontalArrangement = Arrangement.spacedBy(ArcaneSpacing.Large)
            ) {
                var switchOn by remember { mutableStateOf(true) }
                ArcaneSwitch(
                    checked = switchOn,
                    onCheckedChange = { switchOn = it },
                    label = "ON"
                )
                ArcaneSwitch(
                    checked = false,
                    onCheckedChange = {},
                    label = "OFF"
                )
            }
        }

        // Slider Section
        SectionTitle("Slider")
        ArcaneSurface(
            variant = SurfaceVariant.Raised,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(ArcaneSpacing.Medium)
            ) {
                var sliderValue by remember { mutableStateOf(0.5f) }
                ArcaneSlider(
                    value = sliderValue,
                    onValueChange = { sliderValue = it },
                    modifier = Modifier.fillMaxWidth()
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

**Step 2: Update App.kt to use ControlsScreen**

Replace the entire content of `catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/App.kt`:

```kotlin
// catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/App.kt
package io.github.devmugi.arcane.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.devmugi.arcane.catalog.screens.ControlsScreen
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme

@Composable
fun App() {
    ArcaneTheme {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ArcaneTheme.colors.surface)
        ) {
            ControlsScreen()
        }
    }
}
```

**Step 3: Verify build compiles**

Run: `./gradlew :catalog:composeApp:compileKotlinDesktop`

Expected: BUILD SUCCESSFUL

**Step 4: Commit**

```bash
git add catalog/
git commit -m "feat(catalog): add ControlsScreen showcasing all Phase 2 components"
```

---

## Task 8: Full Build Verification

**Step 1: Build all modules**

Run: `./gradlew build`

Expected: BUILD SUCCESSFUL

**Step 2: Run the desktop catalog app**

Run: `./gradlew :catalog:composeApp:run`

Expected: Window opens showing Controls screen with:
- Buttons: Primary (teal), Secondary (outline), Loading (spinner), Disabled (dimmed)
- Text Fields: Placeholder, Helper text, Focused, Error states
- Tactile: Checkbox (checked/unchecked), RadioButton (selected/unselected)
- Switch: ON (teal), OFF (gray)
- Slider: Draggable with tooltip showing percentage

**Step 3: Final commit**

```bash
git add -A
git commit -m "feat: complete Phase 2 - core controls implementation"
```

---

## Summary

After completing all tasks, you will have:

1. **ArcaneButton** - Primary/Secondary styles with Loading and Disabled states, glow effect
2. **ArcaneTextField** - Label, placeholder, helper text, error state, password mode
3. **ArcaneCheckbox** - Checked/unchecked with animated checkmark
4. **ArcaneRadioButton** - Selected/unselected with animated inner dot
5. **ArcaneSwitch** - ON/OFF toggle with animated thumb
6. **ArcaneSlider** - Draggable with value tooltip
7. **ControlsScreen** - Catalog page showcasing all controls

**Next Phase:** Phase 3 - Navigation Components (Tabs, Breadcrumbs, Pagination, Stepper)
