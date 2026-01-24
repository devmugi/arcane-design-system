# Phase 4: Data Display Components - Design Specification

## Overview

Phase 4 introduces six data display components for presenting content, user information, and tabular data. These components follow the established Arcane Design System visual language with teal glow effects on dark surfaces.

**Components**:
1. ArcaneCard
2. ArcaneListItem
3. ArcaneBadge
4. ArcaneAvatar
5. ArcaneTooltip
6. ArcaneTable

**Implementation Order**: Badge → Avatar → ListItem → Card → Tooltip → Table

---

## Component 1: ArcaneCard

### Purpose
Container for grouped content with optional image, title, description, and actions.

### API

```kotlin
@Composable
fun ArcaneCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
)

@Composable
fun ArcaneCardImage(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier
)

@Composable
fun ArcaneCardContent(
    title: String,
    description: String? = null,
    modifier: Modifier = Modifier
)

@Composable
fun ArcaneCardActions(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
)
```

### Visual Specs

| Property | Value |
|----------|-------|
| Surface | `RaisedSurface` |
| Corner radius | `Radius.R8` (8dp) |
| Content padding | `Spacing.M` (16dp) |
| Title typography | `Typography.headlineMedium` |
| Title color | `textPrimary` |
| Description typography | `Typography.bodyMedium` |
| Description color | `textSecondary` |
| Image | Fills width, aspect ratio preserved |

### Behavior

- Optional `onClick` makes entire card tappable with ripple
- Clickable variant adds subtle glow on hover/press
- Image slot accepts any `Painter` (placeholder, network, resource)
- Actions row aligns children to start with `Spacing.S` gap

---

## Component 2: ArcaneListItem

### Purpose
Repeatable row for lists showing icon, primary/secondary text, and optional metadata.

### API

```kotlin
@Composable
fun ArcaneListItem(
    headlineText: String,
    modifier: Modifier = Modifier,
    supportingText: String? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
)

@Composable
fun ArcaneListItemIcon(
    painter: Painter,
    contentDescription: String? = null,
    tint: Color = ArcaneTheme.colors.primary
)
```

### Visual Specs

| Property | Value |
|----------|-------|
| Min height | 56dp |
| Horizontal padding | `Spacing.M` (16dp) |
| Vertical padding | `Spacing.S` (12dp) |
| Leading icon size | 24dp |
| Leading icon container | 32dp `InsetSurface` circle |
| Headline typography | `Typography.bodyLarge` |
| Headline color | `textPrimary` |
| Supporting typography | `Typography.bodySmall` |
| Supporting color | `textSecondary` |
| Divider | Optional 1px `Border.thin` |

### Behavior

- Clickable with ripple effect when `onClick` provided
- Leading/trailing content vertically centered
- Groups naturally in `LazyColumn`

---

## Component 3: ArcaneBadge

### Purpose
Small status indicators for labeling items (New, Featured, Sale, etc.)

### API

```kotlin
@Composable
fun ArcaneBadge(
    text: String,
    modifier: Modifier = Modifier,
    style: ArcaneBadgeStyle = ArcaneBadgeStyle.Default
)

sealed class ArcaneBadgeStyle {
    object Default : ArcaneBadgeStyle()
    object Success : ArcaneBadgeStyle()
    object Warning : ArcaneBadgeStyle()
    object Error : ArcaneBadgeStyle()
    object Neutral : ArcaneBadgeStyle()
    data class Custom(
        val backgroundColor: Color,
        val contentColor: Color
    ) : ArcaneBadgeStyle()
}
```

### Visual Specs

| Property | Value |
|----------|-------|
| Height | 20dp |
| Horizontal padding | `Spacing.S` (8dp) |
| Corner radius | `Radius.R4` (4dp) |
| Typography | `Typography.labelSmall` |
| Background | Style color at 0.2 alpha |
| Border | 1px style color at 0.5 alpha |
| Text | Style color at full opacity |

### Color Mapping

| Style | Color Token |
|-------|-------------|
| Default | `primary` |
| Success | `success` |
| Warning | `warning` |
| Error | `error` |
| Neutral | `textSecondary` |

### Behavior

- Non-interactive (display only)
- Wraps text content naturally
- Can be placed inline or as overlay

---

## Component 4: ArcaneAvatar

### Purpose
Display user profile images with fallback initials and stacking support for groups.

### API

```kotlin
@Composable
fun ArcaneAvatar(
    modifier: Modifier = Modifier,
    image: Painter? = null,
    name: String? = null,
    size: ArcaneAvatarSize = ArcaneAvatarSize.Medium
)

enum class ArcaneAvatarSize(val dp: Dp) {
    Small(24.dp),
    Medium(32.dp),
    Large(48.dp)
}

@Composable
fun ArcaneAvatarGroup(
    avatars: List<ArcaneAvatarData>,
    modifier: Modifier = Modifier,
    maxVisible: Int = 3,
    size: ArcaneAvatarSize = ArcaneAvatarSize.Medium,
    overlap: Dp = 8.dp
)

data class ArcaneAvatarData(
    val image: Painter? = null,
    val name: String? = null
)
```

### Visual Specs

| Property | Value |
|----------|-------|
| Shape | Circle (50% radius) |
| Border | 2px `surfaceBase` |
| Small size | 24dp |
| Medium size | 32dp |
| Large size | 48dp |
| Fallback background | `InsetSurface` |
| Initials typography | `Typography.labelMedium` |
| Initials color | `textPrimary` |
| Stack overlap | -8dp margin |

### Behavior

- Fallback order: Image → Initials → Generic icon
- Initials: First letter of first + last name, uppercase
- Stack renders left-to-right with z-index layering
- Overflow badge shows "+N" when exceeding `maxVisible`

---

## Component 5: ArcaneTooltip

### Purpose
Contextual information popup triggered on hover or long-press.

### API

```kotlin
@Composable
fun ArcaneTooltip(
    tooltip: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit
)

@Composable
fun ArcaneTooltip(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit
)

@Composable
fun ArcaneTooltipBox(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
)
```

### Visual Specs

| Property | Value |
|----------|-------|
| Background | `surfaceRaised` |
| Corner radius | `Radius.R4` (4dp) |
| Horizontal padding | `Spacing.XS` (8dp) |
| Vertical padding | `Spacing.XXS` (4dp) |
| Typography | `Typography.bodySmall` |
| Text color | `textPrimary` |
| Shadow | `Elevation.mid` |
| Max width | 200dp |
| Arrow size | 6dp triangle |

### Positioning

- Default: Above anchor, horizontally centered
- Auto-flip: Below anchor if insufficient space above
- Arrow points toward anchor element

### Behavior

| Platform | Trigger | Dismiss |
|----------|---------|---------|
| Desktop | Hover after 500ms | Mouse exit |
| Mobile | Long-press | Tap outside |
| Both | - | Auto-hide after 3s or on scroll |

---

## Component 6: ArcaneTable

### Purpose
Compact data table with sortable columns, filtering indicators, and row selection.

### API

```kotlin
@Composable
fun <T> ArcaneTable(
    items: List<T>,
    columns: List<ArcaneTableColumn<T>>,
    modifier: Modifier = Modifier,
    onRowClick: ((T) -> Unit)? = null,
    sortState: ArcaneTableSortState? = null,
    onSortChange: ((ArcaneTableSortState) -> Unit)? = null
)

data class ArcaneTableColumn<T>(
    val header: String,
    val weight: Float = 1f,
    val sortable: Boolean = false,
    val filterable: Boolean = false,
    val content: @Composable (T) -> Unit
)

data class ArcaneTableSortState(
    val columnIndex: Int,
    val ascending: Boolean
)

@Composable
fun ArcaneTableHeader(
    columns: List<ArcaneTableColumn<*>>,
    sortState: ArcaneTableSortState?,
    onSortChange: ((ArcaneTableSortState) -> Unit)?
)

@Composable
fun ArcaneTableRow(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit
)
```

### Visual Specs

| Property | Value |
|----------|-------|
| Header background | `surfaceInset` |
| Header typography | `Typography.labelMedium` |
| Header color | `textSecondary` |
| Row height | 48dp minimum |
| Row background | Alternating `surfaceBase` / subtle variant |
| Cell horizontal padding | `Spacing.S` (12dp) |
| Row divider | 1px bottom border |
| Sort icon | Up/down arrow (↕) |
| Filter icon | Funnel icon |

### Behavior

- Click sortable header: Toggle asc → desc → none
- Filter icon: Opens dropdown (future enhancement)
- Row hover: Subtle background highlight
- Row click: Optional callback for selection/navigation

---

## Catalog Integration

### DataDisplayScreen.kt

The catalog screen will showcase all components:

1. **Cards Section**
   - Basic card with image placeholder
   - Card with title and description
   - Clickable card with actions

2. **List Items Section**
   - Simple list item
   - List item with icon
   - List item with trailing metadata

3. **Badges Section**
   - All 5 built-in styles
   - Custom color example

4. **Avatars Section**
   - Three sizes
   - Initials fallback
   - Avatar group with overflow

5. **Tooltips Section**
   - Text tooltip on icon
   - Tooltip on button

6. **Table Section**
   - Sample data table
   - Sortable columns demo
   - Filterable column indicator

---

## Implementation Checklist

- [ ] ArcaneBadge component
- [ ] ArcaneBadge catalog section
- [ ] ArcaneAvatar component
- [ ] ArcaneAvatarGroup component
- [ ] ArcaneAvatar catalog section
- [ ] ArcaneListItem component
- [ ] ArcaneListItemIcon component
- [ ] ArcaneListItem catalog section
- [ ] ArcaneCard component
- [ ] ArcaneCardImage, ArcaneCardContent, ArcaneCardActions
- [ ] ArcaneCard catalog section
- [ ] ArcaneTooltip component
- [ ] ArcaneTooltipBox component
- [ ] ArcaneTooltip catalog section
- [ ] ArcaneTable component
- [ ] ArcaneTableHeader, ArcaneTableRow
- [ ] ArcaneTable catalog section
- [ ] DataDisplayScreen integration with main navigation
- [ ] Build verification on all platforms

---

## File Structure

```
arcane-components/src/commonMain/kotlin/.../components/
└── display/
    ├── Badge.kt
    ├── Avatar.kt
    ├── ListItem.kt
    ├── Card.kt
    ├── Tooltip.kt
    └── Table.kt

catalog/composeApp/src/commonMain/kotlin/.../catalog/
└── screens/
    └── DataDisplayScreen.kt
```
