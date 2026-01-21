# Arcane Design System

A Kotlin Compose Multiplatform design system featuring a distinctive sci-fi aesthetic with glowing purple accents, layered navy surfaces, and futuristic controls.

## Platforms

- Android (API 26+)
- iOS (15+)
- Desktop (JVM 21+)

## Components

### Controls
- **ArcaneButton** - Primary/Secondary/Outlined/Destructive styles with loading state
- **ArcaneTextField** - Text input with focus states, helper text, password support
- **ArcaneCheckbox** - Animated checkbox
- **ArcaneRadioButton** - Radio selection
- **ArcaneSwitch** - Toggle switch with animation
- **ArcaneSlider** - Value slider with tooltip

### Navigation
- **ArcaneTabs** - Tab selection with indicator animation
- **ArcaneBreadcrumbs** - Breadcrumb navigation
- **ArcanePagination** - Page navigation with ellipsis
- **ArcaneStepper** - Multi-step process indicator

### Data Display
- **ArcaneCard** - Content card with image, title, actions
- **ArcaneListItem** - List item with icon, title, subtitle
- **ArcaneBadge** - Status badges (New, Featured, Sale)
- **ArcaneAvatar** - Single and grouped avatars
- **ArcaneTooltip** - Information popover
- **ArcaneTable** - Compact table with sorting and filtering

### Feedback
- **ArcaneSpinner** - Loading spinner
- **ArcaneCircularProgress** - Determinate circular progress
- **ArcaneLinearProgress** - Determinate linear progress
- **ArcaneSkeleton** - Loading placeholder with shimmer
- **ArcaneEmptyState** - Empty content placeholder
- **ArcaneAlertBanner** - Inline alerts (Info/Success/Warning/Error)
- **ArcaneModal** - Modal dialog with backdrop
- **ArcaneConfirmationDialog** - Confirm/delete dialogs
- **ArcaneToast** - Toast notifications with queue management

## Installation

### Maven Local

1. Clone the repository
2. Publish to Maven Local:
```bash
./gradlew publishToMavenLocal
```

3. Add to your project's `settings.gradle.kts`:
```kotlin
dependencyResolutionManagement {
    repositories {
        mavenLocal()
        // ... other repositories
    }
}
```

4. Add dependencies to your module's `build.gradle.kts`:
```kotlin
dependencies {
    implementation("io.github.devmugi.design.arcane:arcane-foundation:0.1.1")
    implementation("io.github.devmugi.design.arcane:arcane-components:0.1.1")
}
```

## Usage

### Setup Theme

Wrap your app content with `ArcaneTheme`:

```kotlin
@Composable
fun App() {
    ArcaneTheme {
        // Your content
    }
}
```

### Using Components

```kotlin
@Composable
fun MyScreen() {
    Column {
        // Primary button (purple background)
        ArcaneTextButton(
            text = "Primary Action",
            onClick = { },
            style = ArcaneButtonStyle.Primary
        )

        // Outlined button (transparent with border)
        ArcaneTextButton(
            text = "Outlined",
            onClick = { },
            style = ArcaneButtonStyle.Outlined()
        )

        // Outlined with custom color
        ArcaneTextButton(
            text = "Custom Color",
            onClick = { },
            style = ArcaneButtonStyle.Outlined(tintColor = Color(0xFFD4A574))
        )

        // Destructive button (red background for delete actions)
        ArcaneTextButton(
            text = "Delete",
            onClick = { },
            style = ArcaneButtonStyle.Destructive
        )

        ArcaneTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = "Enter text..."
        )

        ArcaneSwitch(
            checked = isEnabled,
            onCheckedChange = { isEnabled = it }
        )
    }
}
```

### Custom Theme Colors

```kotlin
ArcaneTheme(
    colors = ArcaneColors.Default.withPrimary(Color(0xFF00FFCC))
) {
    // Content with custom primary color
}
```

### Toast Notifications

```kotlin
val toastState = rememberArcaneToastState()

ArcaneToastHost(state = toastState) {
    ArcaneButton(
        text = "Show Toast",
        onClick = {
            toastState.show(
                message = "Operation successful",
                style = ArcaneToastStyle.Success
            )
        }
    )
}
```

## License

Apache License 2.0
