# iOS Support Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Enable iOS framework publishing for arcane-components via KMMBridge + SPM with SKIE.

**Architecture:** Add SKIE and KMMBridge Gradle plugins, enable iOS targets in convention plugins, configure XCFramework export in arcane-components that bundles arcane-foundation.

**Tech Stack:** Kotlin 2.3, KMMBridge 1.0.1, SKIE 0.10.1, Compose Multiplatform 1.10.0

**Working directory:** `/Users/den/IdeaProjects/ArcaneDesignSystem/.worktrees/ios-support`

---

## Task 1: Add Plugin Dependencies to Version Catalog

**Files:**
- Modify: `gradle/libs.versions.toml`

**Step 1: Add SKIE and KMMBridge versions**

Add to `[versions]` section after line 11:

```toml
skie = "0.10.1"
kmmbridge = "1.0.1"
```

**Step 2: Add plugin declarations**

Add to `[plugins]` section at the end:

```toml
skie = { id = "co.touchlab.skie", version.ref = "skie" }
kmmbridge = { id = "co.touchlab.kmmbridge", version.ref = "kmmbridge" }
```

**Step 3: Verify TOML syntax**

Run: `./gradlew help --no-daemon 2>&1 | head -20`

Expected: No TOML parsing errors

**Step 4: Commit**

```bash
git add gradle/libs.versions.toml
git commit -m "chore: add SKIE and KMMBridge plugin dependencies"
```

---

## Task 2: Enable iOS Targets in Library Convention Plugin

**Files:**
- Modify: `build-logic/src/main/kotlin/arcane.multiplatform.library.gradle.kts`

**Step 1: Remove buildIos flag and always enable iOS**

Replace lines 30-43 (the conditional iOS block):

```kotlin
    // iOS targets - only configure if explicitly enabled
    val buildIos = project.findProperty("buildIos")?.toString()?.toBoolean() ?: false
    if (buildIos) {
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

With:

```kotlin
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
```

**Step 2: Verify iOS targets compile**

Run: `./gradlew :arcane-foundation:compileKotlinIosArm64 --no-daemon 2>&1 | tail -10`

Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add build-logic/src/main/kotlin/arcane.multiplatform.library.gradle.kts
git commit -m "feat: enable iOS targets in library convention plugin"
```

---

## Task 3: Add SKIE Plugin to arcane-foundation

**Files:**
- Modify: `arcane-foundation/build.gradle.kts`

**Step 1: Read current file**

Check current contents of `arcane-foundation/build.gradle.kts`

**Step 2: Add SKIE plugin**

The file should look like:

```kotlin
plugins {
    id("arcane.multiplatform.library")
    alias(libs.plugins.skie)
}
```

**Step 3: Verify compilation with SKIE**

Run: `./gradlew :arcane-foundation:compileKotlinIosArm64 --no-daemon 2>&1 | tail -10`

Expected: BUILD SUCCESSFUL

**Step 4: Commit**

```bash
git add arcane-foundation/build.gradle.kts
git commit -m "feat(foundation): add SKIE plugin for Swift interop"
```

---

## Task 4: Configure XCFramework Export in arcane-components

**Files:**
- Modify: `arcane-components/build.gradle.kts`

**Step 1: Add plugins and XCFramework configuration**

Replace entire file with:

```kotlin
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    id("arcane.multiplatform.library")
    alias(libs.plugins.skie)
    alias(libs.plugins.kmmbridge)
}

kotlin {
    // Configure XCFramework for iOS distribution
    val xcf = XCFramework("ArcaneComponents")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ArcaneComponents"
            isStatic = true
            xcf.add(this)
            export(project(":arcane-foundation"))
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":arcane-foundation"))
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
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

**Step 2: Verify XCFramework task exists**

Run: `./gradlew :arcane-components:tasks --group=build --no-daemon 2>&1 | grep -i xcframework`

Expected: `assembleArcaneComponentsXCFramework` task listed

**Step 3: Commit**

```bash
git add arcane-components/build.gradle.kts
git commit -m "feat(components): configure XCFramework export with KMMBridge"
```

---

## Task 5: Build XCFramework

**Files:**
- None (build verification)

**Step 1: Build debug XCFramework**

Run: `./gradlew :arcane-components:assembleArcaneComponentsDebugXCFramework --no-daemon 2>&1 | tail -20`

Expected: BUILD SUCCESSFUL

**Step 2: Verify XCFramework structure**

Run: `ls -la arcane-components/build/XCFrameworks/debug/`

Expected: `ArcaneComponents.xcframework` directory exists

**Step 3: Verify framework contains both architectures**

Run: `ls arcane-components/build/XCFrameworks/debug/ArcaneComponents.xcframework/`

Expected: `ios-arm64`, `ios-arm64_x86_64-simulator` directories

**Step 4: Commit (no changes, just verification)**

No commit needed - this was verification only.

---

## Task 6: Run iOS Tests

**Files:**
- None (test verification)

**Step 1: Run iOS simulator tests**

Run: `./gradlew :arcane-components:iosSimulatorArm64Test --no-daemon 2>&1 | tail -20`

Expected: BUILD SUCCESSFUL (tests pass or no tests to run)

**Step 2: Run foundation iOS tests**

Run: `./gradlew :arcane-foundation:iosSimulatorArm64Test --no-daemon 2>&1 | tail -20`

Expected: BUILD SUCCESSFUL

---

## Task 7: Generate Package.swift

**Files:**
- Create: `Package.swift` (generated by KMMBridge)

**Step 1: Run KMMBridge SPM task**

Run: `./gradlew :arcane-components:spmDevBuild --no-daemon 2>&1 | tail -20`

Expected: BUILD SUCCESSFUL, Package.swift created

**Step 2: Verify Package.swift exists**

Run: `cat Package.swift 2>/dev/null || echo "Package.swift not found"`

If not found, create manually:

```swift
// swift-tools-version:5.9
import PackageDescription

let package = Package(
    name: "ArcaneComponents",
    platforms: [
        .iOS(.v15)
    ],
    products: [
        .library(name: "ArcaneComponents", targets: ["ArcaneComponents"])
    ],
    targets: [
        .binaryTarget(
            name: "ArcaneComponents",
            path: "arcane-components/build/XCFrameworks/debug/ArcaneComponents.xcframework"
        )
    ]
)
```

**Step 3: Commit**

```bash
git add Package.swift
git commit -m "feat: add Package.swift for SPM distribution"
```

---

## Task 8: Publish to Maven Local

**Files:**
- None (publish verification)

**Step 1: Publish all modules to Maven Local**

Run: `./gradlew publishToMavenLocal --no-daemon 2>&1 | tail -30`

Expected: BUILD SUCCESSFUL

**Step 2: Verify iOS artifacts in Maven Local**

Run: `ls ~/.m2/repository/io/github/devmugi/design/arcane/arcane-components/0.3.2/ | grep -i ios`

Expected: iOS-related artifacts listed (klib, etc.)

**Step 3: Commit version bump (if needed)**

If version needs bump for release:
```bash
# Update version in build-logic/src/main/kotlin/arcane.multiplatform.library.gradle.kts
git add build-logic/src/main/kotlin/arcane.multiplatform.library.gradle.kts
git commit -m "chore: bump version to 0.4.0 for iOS support"
```

---

## Task 9: Final Verification

**Files:**
- None (verification only)

**Step 1: Run all library tests**

Run: `./gradlew :arcane-foundation:allTests :arcane-components:allTests --no-daemon 2>&1 | tail -30`

Expected: BUILD SUCCESSFUL

**Step 2: Verify desktop still works**

Run: `./gradlew :arcane-components:desktopTest --no-daemon 2>&1 | tail -10`

Expected: BUILD SUCCESSFUL

**Step 3: Build release XCFramework**

Run: `./gradlew :arcane-components:assembleArcaneComponentsReleaseXCFramework --no-daemon 2>&1 | tail -10`

Expected: BUILD SUCCESSFUL

**Step 4: Final commit if any cleanup needed**

```bash
git status
# If clean, no commit needed
```

---

## Summary

After completing all tasks:

| Artifact | Location |
|----------|----------|
| Debug XCFramework | `arcane-components/build/XCFrameworks/debug/ArcaneComponents.xcframework` |
| Release XCFramework | `arcane-components/build/XCFrameworks/release/ArcaneComponents.xcframework` |
| Maven Local | `~/.m2/repository/io/github/devmugi/design/arcane/` |
| SPM Package | `Package.swift` in repo root |

**Build commands:**
```bash
# Build debug XCFramework
./gradlew :arcane-components:assembleArcaneComponentsDebugXCFramework

# Build release XCFramework
./gradlew :arcane-components:assembleArcaneComponentsReleaseXCFramework

# Publish to Maven Local
./gradlew publishToMavenLocal
```
