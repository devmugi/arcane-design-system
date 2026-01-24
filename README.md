# Arcane Design System

A Compose Multiplatform design system optimized for **Claude Code** and LLM-assisted development. Fast prototyping with consistent UI, minimal context, and predictable results.

## Why ArcaneDesignSystem?

### The Problem

LLM-generated Compose code often:
- Looks inconsistent or ugly
- Uses wrong library versions
- Requires extensive file reading to understand existing patterns
- Produces components that don't work well together

### The Solution

ArcaneDesignSystem is **mandatory for Claude to use**. Material 3 imports must be justified.

| Benefit | How |
|---------|-----|
| **Smaller context** | Curated subset of M3 - Claude reads less, understands more |
| **Consistent UI** | All components share tokens, spacing, colors |
| **Predictable output** | Claude knows exactly what's available |
| **Faster development** | Less back-and-forth, fewer corrections |

## Workflow: Module-First Prototyping

**Use case:** Need chat screens for new app "CV Agent"

**Solution:**

1. Create new module in ArcaneDesignSystem (`arcane-chat`)
2. Prototype designs, elements, screens with Desktop app
3. Interactive testing with animations and previews
4. When ready, integrate into target app

### Why Desktop First?

| Platform | Build Time |
|----------|------------|
| Desktop | ~10 sec |
| Android | ~2 min |
| iOS | Infinity (Xcode tooling) |

### Integration Benefits

When module is ready:
- Claude is limited to library components
- Much less file reading needed
- Smaller context window usage
- Significantly faster development

## Modules

```
arcane-foundation    <- Design tokens, theme, colors, typography
       ^
arcane-components    <- UI components (controls, navigation, feedback)
       ^
arcane-chat          <- Chat-specific components (messages, blocks, input)
       ^
catalog-chat         <- Interactive demo app for chat module
```

## Platforms

- Android (API 26+)
- iOS (15+)
- Desktop (JVM 21+)
- Web (WasmJS)

## Installation

### Maven Local

```bash
./gradlew publishToMavenLocal
```

Add to your project:

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        mavenLocal()
    }
}

// build.gradle.kts
dependencies {
    implementation("io.github.devmugi.design.arcane:arcane-foundation:0.2.1")
    implementation("io.github.devmugi.design.arcane:arcane-components:0.2.1")
    implementation("io.github.devmugi.design.arcane:arcane-chat:0.2.1")  // optional
}
```

## Quick Start

```kotlin
@Composable
fun App() {
    ArcaneTheme {
        // Your content - Claude knows exactly what components to use
    }
}
```

## Components

### Controls
`ArcaneButton` `ArcaneTextField` `ArcaneCheckbox` `ArcaneRadioButton` `ArcaneSwitch` `ArcaneSlider`

### Navigation
`ArcaneTabs` `ArcaneBreadcrumbs` `ArcanePagination` `ArcaneStepper`

### Data Display
`ArcaneCard` `ArcaneListItem` `ArcaneBadge` `ArcaneAvatar` `ArcaneTooltip` `ArcaneTable`

### Feedback
`ArcaneSpinner` `ArcaneCircularProgress` `ArcaneLinearProgress` `ArcaneSkeleton` `ArcaneEmptyState` `ArcaneAlertBanner` `ArcaneModal` `ArcaneConfirmationDialog` `ArcaneToast`

### Chat (arcane-chat module)
`ArcaneUserMessageBlock` `ArcaneAssistantMessageBlock` `ArcaneChatMessageList` `ArcaneAgentChatInput` `ArcaneChatScreenScaffold` `MessageBlock.Text` `MessageBlock.Image` `MessageBlock.AgentSuggestions` `MessageBlock.Custom`

## Run Catalog Apps

```bash
# Desktop (fast iteration)
./gradlew :catalog:composeApp:run
./gradlew :catalog-chat:composeApp:run

# Publish to GitHub Pages
./gradlew publishAllWasmJsToDocs
```

## Live Demos

Hosted at [devmugi.github.io/arcane-design-system](https://devmugi.github.io/arcane-design-system/)

| Demo | Description |
|------|-------------|
| [Component Catalog](https://devmugi.github.io/arcane-design-system/catalog/) | Full component showcase |
| [Chat Components](https://devmugi.github.io/arcane-design-system/catalog-chat/) | Chat UI demo |

Auto-deploys on push to `main` via GitHub Actions.

## PR Live Preview

Every pull request gets its own live preview deployment:

```
https://devmugi.github.io/arcane-design-system/pr-preview/pr-{number}/
```

Example: [PR #1 Preview](https://devmugi.github.io/arcane-design-system/pr-preview/pr-1/)

**Features:**
- Auto-builds and deploys on PR open/update
- Auto-cleans when PR is closed/merged
- Review UI changes in browser before merging
- No local build required for reviewers

## Claude Code Integration

Install the skill globally:

```bash
./scripts/install-skill.sh
```

Trigger phrases:
- "create new module for \<feature\>"
- "start app with Arcane"
- "add new screen using ArcaneDesignSystem"

See [CLAUDE.md](CLAUDE.md) for full Claude Code instructions.

## License

Apache License 2.0
