# iOS Support Design

## Overview

Add iOS framework publishing for Arcane Design System, enabling native Swift developers to consume the library via Swift Package Manager.

## Decision Summary

| Aspect | Decision |
|--------|----------|
| Distribution | KMMBridge + SPM with SKIE |
| Published module | `arcane-components` (exports foundation) |
| Framework name | `ArcaneComponents` |
| Hosting | Maven Local (primary), GitHub Releases (future) |
| Min iOS version | iOS 15 |
| SKIE | Yes, for Flow/suspend → async/await |

## Architecture

### What Gets Published

```
ArcaneComponents.xcframework
    └── exports arcane-foundation (transitively included)
```

### Build Targets

- `iosArm64` - Physical devices (iPhone, iPad)
- `iosSimulatorArm64` - Apple Silicon simulators
- `iosX64` - Intel simulators

### Consumer Experience

```swift
// Package.swift dependency
.package(url: "https://github.com/user/ArcaneDesignSystem", from: "0.4.0")

// Swift usage
import ArcaneComponents

// Kotlin suspend functions become Swift async
let result = await viewModel.loadData()

// Kotlin Flows become AsyncSequence
for await state in viewModel.stateFlow {
    updateUI(state)
}
```

## Gradle Configuration

### libs.versions.toml additions

```toml
[versions]
kmmbridge = "1.0.1"
skie = "0.10.1"

[plugins]
kmmbridge = { id = "co.touchlab.kmmbridge", version.ref = "kmmbridge" }
skie = { id = "co.touchlab.skie", version.ref = "skie" }
```

### arcane.multiplatform.library.gradle.kts

Remove the `buildIos` flag - iOS targets always enabled:

```kotlin
kotlin {
    // iOS targets - always enabled
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = project.name
            isStatic = true
        }
    }
}
```

### arcane-components/build.gradle.kts

```kotlin
plugins {
    id("arcane.multiplatform.library")
    alias(libs.plugins.skie)
    alias(libs.plugins.kmmbridge)
}

kotlin {
    val xcf = XCFramework("ArcaneComponents")

    listOf(iosArm64(), iosSimulatorArm64(), iosX64()).forEach {
        it.binaries.framework {
            baseName = "ArcaneComponents"
            isStatic = true
            xcf.add(this)
            export(project(":arcane-foundation"))
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":arcane-foundation")) // api so it's exported
        }
    }
}

kmmbridge {
    mavenPublishArtifacts()
    spm {
        iOS { v("15") }
    }
}
```

## Source Set Structure

```
arcane-foundation/src/
├── commonMain/
├── androidMain/
├── desktopMain/
├── wasmJsMain/
└── iosMain/             # iOS-specific implementations

arcane-components/src/
├── commonMain/
├── androidMain/
├── desktopMain/
├── wasmJsMain/
└── iosMain/             # iOS-specific implementations
```

## Build Commands

### Local Development

```bash
# Build iOS frameworks (debug)
./gradlew :arcane-components:assembleArcaneComponentsXCFramework

# Build + publish to Maven Local (includes iOS)
./gradlew publishToMavenLocal

# Run iOS simulator tests
./gradlew :arcane-components:iosSimulatorArm64Test
```

### Output Locations

```
arcane-components/build/
├── XCFrameworks/
│   ├── debug/ArcaneComponents.xcframework
│   └── release/ArcaneComponents.xcframework
└── bin/
    ├── iosArm64/
    ├── iosSimulatorArm64/
    └── iosX64/
```

### Local Swift Consumer Testing

```swift
// In Package.swift, point to local path:
.binaryTarget(
    name: "ArcaneComponents",
    path: "../ArcaneDesignSystem/arcane-components/build/XCFrameworks/debug/ArcaneComponents.xcframework"
)
```

### Future GitHub Release Workflow

```bash
# Tag triggers KMMBridge to build and publish
git tag 0.4.0
git push --tags
```

## Implementation Steps

### Phase 1: Enable iOS compilation
1. Update `libs.versions.toml` with SKIE and KMMBridge plugins
2. Modify `arcane.multiplatform.library.gradle.kts` to always enable iOS targets
3. Add SKIE plugin to `arcane-foundation` and `arcane-components`

### Phase 2: Configure XCFramework export
4. Add KMMBridge plugin to `arcane-components`
5. Configure XCFramework with foundation export
6. Add `Package.swift` to repo root

### Phase 3: Handle platform-specific code
7. Scan for `expect` declarations missing iOS `actual`
8. Create `iosMain` source sets with implementations
9. Fix any iOS-specific compilation errors

### Phase 4: Verify
10. Build XCFramework: `./gradlew :arcane-components:assembleArcaneComponentsXCFramework`
11. Run iOS tests: `./gradlew :arcane-components:iosSimulatorArm64Test`
12. Test import in a sample Swift project (optional)

## Requirements

- macOS with Xcode 14.3+
- Kotlin 2.3 (current, SKIE compatible)
- iOS 15+ deployment target

## References

- [Kotlin iOS Integration Overview](https://kotlinlang.org/docs/multiplatform/multiplatform-ios-integration-overview.html)
- [KMMBridge Documentation](https://kmmbridge.touchlab.co/docs/)
- [SKIE Documentation](https://skie.touchlab.co/)
- [SPM Export Setup](https://kotlinlang.org/docs/multiplatform/multiplatform-spm-export.html)
