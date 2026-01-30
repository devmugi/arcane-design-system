# Arcane Design System - Product Requirements Document

## Overview & Goals

**Arcane Design System**

A Compose Multiplatform UI component library featuring a distinctive sci-fi aesthetic with glowing teal accents, layered surfaces, and futuristic controls. Targets Android, iOS, and JVM (Desktop).

**Vision:** Provide developers with production-ready, customizable components that deliver the Arcane visual style out-of-the-box while allowing color palette customization.

**Goals:**
- Consistent cross-platform appearance and behavior
- Color-customizable theming (swap palettes, keep aesthetic)
- Clean separation: `arcane-foundation` (tokens) + `arcane-components` (UI)
- Comprehensive documentation with catalog app and docs website

**Technical Constraints:**
- Compose Multiplatform 1.7.x (latest stable)
- Kotlin 2.0+
- Targets: Android API 26+, iOS 15+, JVM 21+

**Non-Goals (v1):**
- Web (Wasm) target - can add later
- Animations library - components include built-in animations, no separate module
- Dark/light mode toggle - Arcane aesthetic is inherently "dark mode"

**Success Criteria:**
- All components from reference design implemented
- Catalog app runs on all three platforms
- Documentation website deployable
- Library consumable via Gradle dependency

---

## Architecture & Module Structure

**Repository Structure:**
```
ArcaneDesignSystem/
├── arcane-foundation/          # Design tokens module
│   └── src/commonMain/
│       ├── theme/
│       │   ├── ArcaneTheme.kt         # Theme provider
│       │   ├── ArcaneColors.kt        # Color palette (customizable)
│       │   └── ArcaneTypography.kt    # Font styles
│       ├── tokens/
│       │   ├── Elevation.kt           # Surface levels 1-3
│       │   ├── Border.kt              # Title/Medium/Thick (1/2/6px)
│       │   ├── Radius.kt              # B0-R15 scale
│       │   └── Spacing.kt             # Consistent spacing scale
│       └── primitives/
│           └── Surface.kt             # Base/Raised/Inset/Pressed
│
├── arcane-components/          # UI components module
│   └── src/commonMain/
│       ├── controls/
│       ├── navigation/
│       ├── dataDisplay/
│       └── feedback/
│
├── catalog/                    # Showcase app (Android/iOS/Desktop)
│   ├── composeApp/
│   └── iosApp/
│
├── docs/                       # Documentation
│   ├── plans/                  # This PRD and design docs
│   └── website/                # Documentation site source
│
└── build-logic/                # Gradle convention plugins
```

**Dependency Graph:**
```
arcane-components → arcane-foundation
catalog → arcane-components
```

**Theming API:**
```kotlin
ArcaneTheme(
    colors = ArcaneColors.default().copy(
        primary = Color(0xFF00FFAA),  // Custom teal
        glow = Color(0xFF00FFAA).copy(alpha = 0.3f)
    )
) {
    // App content
}
```

---

## Component Inventory

### Foundation Tokens (arcane-foundation)

| Token | Values | Notes |
|-------|--------|-------|
| Elevation | Level 1 (0.0.0.2), Level 2 (0.0.0.25), Level 3 (0.0.0.8) | rgba alpha for glow |
| Border | Title 1px, Medium 2px, Thick 6px | Stroke thickness |
| Radius | B0, B4 (4px), B8 (6px), R12 (12px), R15 (18px) | Corner rounding |
| Iconography | 15px, 25px, 26px sizes | With home/search/settings/user icons |
| Surface | Base, Raised, Inset, Pressed | Core container primitives |

### Controls (12 components)

| Component | Variants/States |
|-----------|-----------------|
| Button | Primary, Secondary, Pressed, Loading, Disabled |
| TextField | Default, Placeholder, Helper text, Password, Error, Focused |
| Checkbox | Checked, Unchecked |
| RadioButton | Selected, Unselected |
| Switch | On, Off |
| Slider | With value tooltip |

### Navigation (5 components)

| Component | Features |
|-----------|----------|
| Tabs | Selected/unselected states |
| Breadcrumbs | Separator, clickable links |
| Pagination | Page numbers, prev/next, ellipsis |
| Stepper | 5-step process, active/complete/pending states |

### Data Display (6 components)

| Component | Features |
|-----------|----------|
| Card | Image, title, description, action button |
| ListItem | Icon, title, subtitle, timestamp |
| Badge | New, Featured, Sale variants |
| Avatar | Single, stacked group |
| Tooltip | Information popover |
| Table | Compact, sortable headers, filter indicators |

---

## Implementation Phases

### Phase 1: Foundation & Project Setup
- Gradle multi-module setup with convention plugins
- Kotlin Multiplatform configuration (Android/iOS/JVM)
- `arcane-foundation` module complete:
  - ArcaneTheme, ArcaneColors, ArcaneTypography
  - All tokens (Elevation, Border, Radius, Spacing)
  - Surface primitives (Base, Raised, Inset, Pressed)
- Empty catalog app scaffold running on all platforms

### Phase 2: Core Controls
- Button (all 5 states)
- TextField (all variants including error states)
- Checkbox, RadioButton
- Switch
- Slider with tooltip
- Catalog pages for each control

### Phase 3: Navigation Components
- Tabs
- Breadcrumbs
- Pagination
- Stepper (5-step process)
- Catalog pages for navigation

### Phase 4: Data Display Components
- Card
- ListItem
- Badge
- Avatar (single + group)
- Tooltip
- Table (compact, sortable)
- Catalog pages for data display

### Phase 5: Polish & Documentation
- Glow effects and animations refinement
- Accessibility (content descriptions, focus handling)
- KDoc for all public APIs
- Documentation website (Dokka + custom guides)
- README, CHANGELOG, contribution guidelines
- Prepare for distribution (Maven publishing setup)

**Total Component Count:** 23 components + foundation tokens

---

## Technical Specifications

### API Design Patterns

```kotlin
// All components follow this pattern:
@Composable
fun ArcaneButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    style: ArcaneButtonStyle = ArcaneButtonStyle.Primary,
    content: @Composable RowScope.() -> Unit
)

// Styles as sealed class for type safety
sealed class ArcaneButtonStyle {
    object Primary : ArcaneButtonStyle()
    object Secondary : ArcaneButtonStyle()
}
```

### Color System (customizable)

```kotlin
data class ArcaneColors(
    val primary: Color,           // Main teal accent
    val primaryVariant: Color,    // Lighter/darker variant
    val surface: Color,           // Base surface color
    val surfaceRaised: Color,     // Elevated surface
    val surfaceInset: Color,      // Recessed surface
    val glow: Color,              // Glow effect color (primary + alpha)
    val text: Color,              // Primary text
    val textSecondary: Color,     // Secondary text
    val error: Color,             // Error states
    val success: Color,           // Success states
)
```

### Testing Strategy
- Unit tests: State logic, token values
- Screenshot tests: Visual regression with Paparazzi (Android) / Roborazzi
- UI tests: Interaction behavior with Compose testing APIs

### Build & CI
- Gradle 8.5+ with version catalogs
- GitHub Actions for CI (build all targets, run tests)
- Detekt for static analysis
- Ktlint for code formatting

---

## Catalog App & Documentation

### Catalog App Structure
```
catalog/
├── composeApp/
│   └── src/commonMain/
│       ├── App.kt                    # Main navigation
│       ├── screens/
│       │   ├── HomeScreen.kt         # Overview + component grid
│       │   ├── FoundationScreen.kt   # Colors, tokens, surfaces
│       │   ├── ControlsScreen.kt     # All control components
│       │   ├── NavigationScreen.kt   # Nav components
│       │   └── DataDisplayScreen.kt  # Data display components
│       └── components/
│           └── ComponentShowcase.kt  # Reusable demo wrapper
```

### Catalog Features
- Live component previews with interactive states
- Color palette picker to demo customization
- Code snippets for each component
- Platform indicator (showing current platform)

### Documentation Website
- Built with Dokka + custom landing pages
- Sections: Getting Started, Foundation, Components (by category), Customization
- Each component page includes: preview image, API reference, usage examples, accessibility notes
- Deployable to GitHub Pages

### Package Naming
```
io.github.devmugi.arcane.design.foundation  # Foundation module
io.github.devmugi.arcane.design.components  # Components module
```

---

## Reference

Design reference image: `ArcaneDesignSystem.png` in repository root.
