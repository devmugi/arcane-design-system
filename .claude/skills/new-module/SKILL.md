---
name: new-module
description: Use when creating new modules, screens, or apps using ArcaneDesignSystem. Triggers on "new module", "new screen", "add feature", "start app with Arcane", or when user wants consistent UI components.
---

# ArcaneDesignSystem New Module Skill

Create modules and apps using ArcaneDesignSystem for consistent, beautiful UI with minimal context overhead.

## Context Detection

First, detect which mode to use:

```
Check if current repo contains "arcane-foundation" directory:
  YES → Internal mode (convention plugins)
  NO  → External mode (Maven dependencies)
```

## Internal Mode (Inside ArcaneDesignSystem repo)

### 1. Ask Questions

Ask one at a time:
- Module name? (e.g., `arcane-cv`, `arcane-auth`)
- Module type: library or application?
- Needs chat components? (depends on arcane-chat)

### 2. Create Module Structure

```
{module-name}/
├── build.gradle.kts
└── src/
    └── commonMain/
        └── kotlin/io/github/devmugi/arcane/{name}/
            ├── components/
            └── screens/
```

### 3. Configure Build

Use convention plugin in `build.gradle.kts`:

```kotlin
plugins {
    id("arcane.multiplatform.library")  // or arcane.multiplatform.application
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":arcane-foundation"))
            implementation(project(":arcane-components"))
            // If chat needed:
            implementation(project(":arcane-chat"))
        }
    }
}
```

### 4. Add to settings.gradle.kts

```kotlin
include(":module-name")
```

## External Mode (New app using Arcane as dependency)

### 1. Add Maven Dependencies

In `libs.versions.toml`:
```toml
[versions]
arcane = "0.2.1"

[libraries]
arcane-foundation = { module = "io.github.devmugi.design.arcane:arcane-foundation", version.ref = "arcane" }
arcane-components = { module = "io.github.devmugi.design.arcane:arcane-components", version.ref = "arcane" }
arcane-chat = { module = "io.github.devmugi.design.arcane:arcane-chat", version.ref = "arcane" }
```

In `build.gradle.kts`:
```kotlin
commonMain.dependencies {
    implementation(libs.arcane.foundation)
    implementation(libs.arcane.components)
    // If chat needed:
    implementation(libs.arcane.chat)
}
```

### 2. Setup Theme

Wrap your app in ArcaneTheme:

```kotlin
@Composable
fun App() {
    ArcaneTheme {
        // Your content - access colors via ArcaneTheme.colors
    }
}
```

## Component Mapping

**CRITICAL: Use Arcane components, NOT raw Material 3.**

See `component-map.md` for full reference. Key mappings:

| Instead of (M3) | Use (Arcane) |
|-----------------|--------------|
| `Button` | `ArcaneButton` |
| `TextField` | `ArcaneTextField` |
| `Card` | `ArcaneCard` |
| `Surface` | `ArcaneSurface` |
| `CircularProgressIndicator` | `ArcaneSpinner` or `ArcaneCircularProgress` |
| `LinearProgressIndicator` | `ArcaneLinearProgress` |
| `Switch` | `ArcaneSwitch` |
| `Checkbox` | `ArcaneCheckbox` |
| `AlertDialog` | `ArcaneConfirmationDialog` or `ArcaneModal` |

## Screen Template

```kotlin
@Composable
fun MyScreen() {
    val colors = ArcaneTheme.colors

    ArcaneSurface(variant = SurfaceVariant.ContainerLow) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(ArcaneSpacing.md)
        ) {
            // Your content using Arcane components
        }
    }
}
```

## Chat Screens (if using arcane-chat)

```kotlin
@Composable
fun ChatScreen(
    messages: List<ChatMessage>,
    onSendMessage: (String) -> Unit
) {
    ArcaneChatScreenScaffold(
        bottomBar = {
            ArcaneAgentChatInput(
                value = inputText,
                onValueChange = { inputText = it },
                onSend = { onSendMessage(inputText) }
            )
        }
    ) {
        ArcaneChatMessageList(messages = messages) { message ->
            when (message) {
                is ChatMessage.User -> ArcaneUserMessageBlock(message)
                is ChatMessage.Assistant -> ArcaneAssistantMessageBlock(message)
            }
        }
    }
}
```

## Desktop-First Development

For fast iteration (10s builds vs 2min Android):

```bash
./gradlew :your-module:run  # Desktop
```

Only test on Android/iOS when desktop UI is finalized.

## Verification Checklist

After creating module:
- [ ] Build succeeds: `./gradlew :module-name:build`
- [ ] No raw M3 imports (search for `androidx.compose.material3`)
- [ ] Theme wrapped: `ArcaneTheme { }`
- [ ] Desktop runs: `./gradlew :module-name:run`
