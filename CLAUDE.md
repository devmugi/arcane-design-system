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
