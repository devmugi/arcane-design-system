# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Arcane Design System is a Kotlin Compose Multiplatform UI component library targeting Android (API 26+), iOS (15+), and Desktop (JVM 21+). It features a sci-fi aesthetic with purple accents and layered navy surfaces.

**Maven coordinates:** `io.github.devmugi.design.arcane:arcane-foundation` and `io.github.devmugi.design.arcane:arcane-components`

## Build Commands

```bash
# Build all modules
./gradlew build

# Run tests
./gradlew :arcane-components:allTests

# Publish to Maven Local
./gradlew publishToMavenLocal

# Run catalog app
./gradlew :catalog:composeApp:run              # Desktop
./gradlew :catalog:composeApp:installDebug     # Android (device/emulator)
```

## Module Architecture

```
arcane-foundation    <- Design tokens, theme, colors, typography
       ↑
arcane-components    <- All UI components (controls, navigation, feedback, etc.)
       ↑
catalog/composeApp   <- Showcase app demonstrating all components
```

**build-logic/** contains convention plugins:
- `arcane.multiplatform.library` - For library modules (foundation, components)
- `arcane.multiplatform.application` - For the catalog app

## Version Configuration

All versions are centralized in `gradle/libs.versions.toml`. The convention plugins automatically apply:
- Group: `io.github.devmugi.design.arcane`
- Version: defined in `build-logic/src/main/kotlin/arcane.multiplatform.library.gradle.kts`

**Critical:** Always use explicit TOML dependencies. Never use `compose.*` plugin shortcuts like `compose.runtime` - use `libs.compose.runtime` instead.

## Theme System

Theme is injected via CompositionLocals:

```kotlin
ArcaneTheme {
    // Access via ArcaneTheme.colors and ArcaneTheme.typography
}
```

Key files:
- `arcane-foundation/.../theme/ArcaneTheme.kt`
- `arcane-foundation/.../theme/ArcaneColors.kt`
- `arcane-foundation/.../tokens/` - Spacing, Border, Radius, Elevation

## Surface System (Material 3)

Arcane Design System follows Material 3's tone-based surface container system for consistent elevation hierarchy.

### Surface Levels

Five surface container levels from darkest to lightest:

- `surfaceContainerLowest` - Inset/depressed surfaces (darkest)
- `surfaceContainerLow` - Base level surfaces
- `surfaceContainer` - Standard cards and containers (default)
- `surfaceContainerHigh` - Elevated modals and overlays
- `surfaceContainerHighest` - Maximum emphasis dialogs (lightest)

### Surface Variants

Use the `ArcaneSurface` composable with `SurfaceVariant` enum:

```kotlin
ArcaneSurface(variant = SurfaceVariant.Container) {
    // Your content here
}
```

Available variants:
- `ContainerLowest` - For recessed/inset areas
- `ContainerLow` - For base surfaces
- `Container` - For standard cards (default)
- `ContainerHigh` - For modals
- `ContainerHighest` - For dialogs

### Elevation

Surfaces use neutral shadows (not colored glows) following M3 guidelines:
- `ContainerLowest` / `ContainerLow`: 0dp (no shadow)
- `Container`: 2dp shadow
- `ContainerHigh`: 4dp shadow
- `ContainerHighest`: 8dp shadow

### State Layers

Interactive states use transparent overlays instead of changing surface colors:

```kotlin
val stateOverlay = when {
    isPressed -> colors.primary.copy(alpha = colors.stateLayerPressed) // 12%
    isHovered -> colors.primary.copy(alpha = colors.stateLayerHover)   // 8%
    else -> Color.Transparent
}
```

State layer alphas (M3 standard):
- Hover: 8%
- Pressed: 12%
- Focus: 12%
- Dragged: 16%

### Migration from v0.1.x

**Breaking changes in v0.2.0:**

Old surface colors → New M3 names:
| Old | New |
|-----|-----|
| `surface` | `surfaceContainerLow` |
| `surfaceRaised` | `surfaceContainer` |
| `surfaceInset` | `surfaceContainerLowest` |
| `surfacePressed` | _Removed_ (use state overlays) |

Old surface variants → New M3 variants:
| Old | New |
|-----|-----|
| `SurfaceVariant.Base` | `SurfaceVariant.ContainerLow` |
| `SurfaceVariant.Raised` | `SurfaceVariant.Container` |
| `SurfaceVariant.Inset` | `SurfaceVariant.ContainerLowest` |
| `SurfaceVariant.Pressed` | _Removed_ (use state overlays) |

**Migration example:**

```kotlin
// OLD (v0.1.x)
val backgroundColor = if (isPressed) colors.surfacePressed else colors.surface
ArcaneSurface(variant = SurfaceVariant.Raised) { /* ... */ }

// NEW (v0.2.0+)
val backgroundColor = colors.surfaceContainerLow
val stateOverlay = if (isPressed) colors.primary.copy(alpha = colors.stateLayerPressed) else Color.Transparent
ArcaneSurface(variant = SurfaceVariant.Container) { /* ... */ }
```

**Note:** Deprecated properties remain available in v0.2.0 for gradual migration, but will be removed in v0.3.0.

## Component Patterns

Components use sealed classes for style variants:

```kotlin
sealed class ArcaneButtonStyle {
    data object Primary : ArcaneButtonStyle()
    data object Secondary : ArcaneButtonStyle()
    data class Outlined(val tintColor: Color? = null) : ArcaneButtonStyle()
}
```

Components are organized by category in `arcane-components/.../components/`:
- `controls/` - Button, TextField, Switch, Slider, etc.
- `navigation/` - Tabs, Breadcrumbs, Pagination, Stepper
- `display/` - Card, ListItem, Badge, Avatar, Table
- `feedback/` - Spinner, Progress, Toast, Modal, AlertBanner

## Testing

Tests use `kotlin("test")` and are located in `arcane-components/src/commonTest/`. Run with:
```bash
./gradlew :arcane-components:allTests
```
