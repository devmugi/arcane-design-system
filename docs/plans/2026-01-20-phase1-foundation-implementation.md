# Phase 1: Foundation & Project Setup - Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Set up Compose Multiplatform project with `arcane-foundation` module containing all design tokens and surface primitives.

**Architecture:** Gradle multi-module with convention plugins in `build-logic/`. Foundation module is pure Kotlin Multiplatform with Compose dependencies. Catalog app is a separate KMP app that demonstrates the foundation.

**Tech Stack:** Kotlin 2.0+, Compose Multiplatform 1.7.x, Gradle 8.5+, Android API 26+, iOS 15+, JVM 21

---

## Task 1: Initialize Gradle Wrapper and Settings

**Files:**
- Create: `gradle/wrapper/gradle-wrapper.properties`
- Create: `settings.gradle.kts`
- Create: `gradle.properties`

**Step 1: Create gradle wrapper properties**

```properties
# gradle/wrapper/gradle-wrapper.properties
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-8.10-bin.zip
networkTimeout=10000
validateDistributionUrl=true
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
```

**Step 2: Create gradle.properties**

```properties
# gradle.properties
org.gradle.jvmargs=-Xmx2048M -Dfile.encoding=UTF-8 -Dkotlin.daemon.jvm.options\="-Xmx2048M"
org.gradle.caching=true
org.gradle.configuration-cache=true
org.gradle.parallel=true

kotlin.code.style=official
kotlin.native.ignoreDisabledTargets=true

android.useAndroidX=true
android.nonTransitiveRClass=true
```

**Step 3: Create settings.gradle.kts**

```kotlin
// settings.gradle.kts
pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
    }
}

rootProject.name = "ArcaneDesignSystem"

include(":arcane-foundation")
include(":arcane-components")
include(":catalog:composeApp")
```

**Step 4: Commit**

```bash
git add gradle.properties settings.gradle.kts gradle/
git commit -m "chore: initialize gradle wrapper and settings"
```

---

## Task 2: Create Version Catalog

**Files:**
- Create: `gradle/libs.versions.toml`

**Step 1: Create version catalog**

```toml
# gradle/libs.versions.toml
[versions]
agp = "8.5.2"
kotlin = "2.1.0"
compose-multiplatform = "1.7.1"
androidx-activityCompose = "1.9.3"
androidx-lifecycle = "2.8.4"

[libraries]
androidx-activity-compose = { module = "androidx.activity:activity-compose", version.ref = "androidx-activityCompose" }
androidx-lifecycle-runtime-compose = { group = "androidx.lifecycle", name = "lifecycle-runtime-compose", version.ref = "androidx-lifecycle" }
androidx-lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "androidx-lifecycle" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
android-library = { id = "com.android.library", version.ref = "agp" }
kotlin-multiplatform = { id = "org.jetbrains.kotlin.multiplatform", version.ref = "kotlin" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
compose-multiplatform = { id = "org.jetbrains.compose", version.ref = "compose-multiplatform" }
compose-compiler = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
```

**Step 2: Commit**

```bash
git add gradle/libs.versions.toml
git commit -m "chore: add gradle version catalog"
```

---

## Task 3: Create Build Logic Convention Plugins

**Files:**
- Create: `build-logic/settings.gradle.kts`
- Create: `build-logic/build.gradle.kts`
- Create: `build-logic/src/main/kotlin/arcane.multiplatform.library.gradle.kts`
- Create: `build-logic/src/main/kotlin/arcane.multiplatform.application.gradle.kts`

**Step 1: Create build-logic settings**

```kotlin
// build-logic/settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "build-logic"
```

**Step 2: Create build-logic build file**

```kotlin
// build-logic/build.gradle.kts
plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.plugins.kotlin.multiplatform.toDep())
    compileOnly(libs.plugins.android.library.toDep())
    compileOnly(libs.plugins.android.application.toDep())
    compileOnly(libs.plugins.compose.multiplatform.toDep())
    compileOnly(libs.plugins.compose.compiler.toDep())
}

fun Provider<PluginDependency>.toDep() = map {
    "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}"
}
```

**Step 3: Create multiplatform library convention**

```kotlin
// build-logic/src/main/kotlin/arcane.multiplatform.library.gradle.kts
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    jvm("desktop") {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

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

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.ui)
        }
    }
}

android {
    namespace = "io.github.devmugi.arcane.design.${project.name.replace("-", ".")}"
    compileSdk = 35

    defaultConfig {
        minSdk = 26
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}
```

**Step 4: Create multiplatform application convention**

```kotlin
// build-logic/src/main/kotlin/arcane.multiplatform.application.gradle.kts
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.application")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    jvm("desktop") {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
        }

        androidMain.dependencies {
            implementation(libs.findLibrary("androidx-activity-compose").get())
        }

        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
    }
}

android {
    namespace = "io.github.devmugi.arcane.catalog"
    compileSdk = 35

    defaultConfig {
        applicationId = "io.github.devmugi.arcane.catalog"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

compose.desktop {
    application {
        mainClass = "io.github.devmugi.arcane.catalog.MainKt"
    }
}
```

**Step 5: Commit**

```bash
git add build-logic/
git commit -m "chore: add build-logic convention plugins"
```

---

## Task 4: Create arcane-foundation Module Structure

**Files:**
- Create: `arcane-foundation/build.gradle.kts`
- Create: `arcane-foundation/src/commonMain/kotlin/io/github/devmugi/arcane/design/foundation/theme/ArcaneColors.kt`

**Step 1: Create foundation build file**

```kotlin
// arcane-foundation/build.gradle.kts
plugins {
    id("arcane.multiplatform.library")
}
```

**Step 2: Create ArcaneColors**

```kotlin
// arcane-foundation/src/commonMain/kotlin/io/github/devmugi/arcane/design/foundation/theme/ArcaneColors.kt
package io.github.devmugi.arcane.design.foundation.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class ArcaneColors(
    val primary: Color = Color(0xFF00D4AA),
    val primaryVariant: Color = Color(0xFF00F5C4),
    val surface: Color = Color(0xFF0A1A1F),
    val surfaceRaised: Color = Color(0xFF122A32),
    val surfaceInset: Color = Color(0xFF051015),
    val surfacePressed: Color = Color(0xFF0D2228),
    val glow: Color = Color(0xFF00D4AA).copy(alpha = 0.3f),
    val glowStrong: Color = Color(0xFF00D4AA).copy(alpha = 0.6f),
    val text: Color = Color(0xFFE0F4F0),
    val textSecondary: Color = Color(0xFF8ABAB0),
    val textDisabled: Color = Color(0xFF4A7A70),
    val border: Color = Color(0xFF00D4AA).copy(alpha = 0.4f),
    val borderFocused: Color = Color(0xFF00D4AA),
    val error: Color = Color(0xFFFF6B6B),
    val success: Color = Color(0xFF00D4AA),
    val warning: Color = Color(0xFFFFB347),
) {
    companion object {
        fun default(): ArcaneColors = ArcaneColors()

        fun withPrimary(primary: Color): ArcaneColors = ArcaneColors(
            primary = primary,
            primaryVariant = primary.copy(alpha = 0.8f),
            glow = primary.copy(alpha = 0.3f),
            glowStrong = primary.copy(alpha = 0.6f),
            border = primary.copy(alpha = 0.4f),
            borderFocused = primary,
            success = primary,
        )
    }
}

val LocalArcaneColors = staticCompositionLocalOf { ArcaneColors() }
```

**Step 3: Commit**

```bash
git add arcane-foundation/
git commit -m "feat(foundation): add ArcaneColors with customization support"
```

---

## Task 5: Add Typography Tokens

**Files:**
- Create: `arcane-foundation/src/commonMain/kotlin/io/github/devmugi/arcane/design/foundation/theme/ArcaneTypography.kt`

**Step 1: Create ArcaneTypography**

```kotlin
// arcane-foundation/src/commonMain/kotlin/io/github/devmugi/arcane/design/foundation/theme/ArcaneTypography.kt
package io.github.devmugi.arcane.design.foundation.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Immutable
data class ArcaneTypography(
    val displayLarge: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.5).sp,
    ),
    val displayMedium: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
    ),
    val displaySmall: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    ),
    val headlineLarge: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
    ),
    val headlineMedium: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.sp,
    ),
    val bodyLarge: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.25.sp,
    ),
    val bodyMedium: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    ),
    val bodySmall: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
    ),
    val labelLarge: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    val labelMedium: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
    val labelSmall: TextStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.5.sp,
    ),
)

val LocalArcaneTypography = staticCompositionLocalOf { ArcaneTypography() }
```

**Step 2: Commit**

```bash
git add arcane-foundation/
git commit -m "feat(foundation): add ArcaneTypography tokens"
```

---

## Task 6: Add Dimension Tokens

**Files:**
- Create: `arcane-foundation/src/commonMain/kotlin/io/github/devmugi/arcane/design/foundation/tokens/Elevation.kt`
- Create: `arcane-foundation/src/commonMain/kotlin/io/github/devmugi/arcane/design/foundation/tokens/Border.kt`
- Create: `arcane-foundation/src/commonMain/kotlin/io/github/devmugi/arcane/design/foundation/tokens/Radius.kt`
- Create: `arcane-foundation/src/commonMain/kotlin/io/github/devmugi/arcane/design/foundation/tokens/Spacing.kt`

**Step 1: Create Elevation tokens**

```kotlin
// arcane-foundation/src/commonMain/kotlin/io/github/devmugi/arcane/design/foundation/tokens/Elevation.kt
package io.github.devmugi.arcane.design.foundation.tokens

import androidx.compose.runtime.Immutable

@Immutable
object ArcaneElevation {
    /** Level 1: Subtle elevation - rgba alpha 0.2 */
    const val Level1Alpha = 0.2f

    /** Level 2: Medium elevation - rgba alpha 0.25 */
    const val Level2Alpha = 0.25f

    /** Level 3: High elevation - rgba alpha 0.8 */
    const val Level3Alpha = 0.8f
}
```

**Step 2: Create Border tokens**

```kotlin
// arcane-foundation/src/commonMain/kotlin/io/github/devmugi/arcane/design/foundation/tokens/Border.kt
package io.github.devmugi.arcane.design.foundation.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
object ArcaneBorder {
    /** Title border - 1px thin accent line */
    val Title: Dp = 1.dp

    /** Medium border - 2px standard border */
    val Medium: Dp = 2.dp

    /** Thick border - 6px heavy emphasis */
    val Thick: Dp = 6.dp
}
```

**Step 3: Create Radius tokens**

```kotlin
// arcane-foundation/src/commonMain/kotlin/io/github/devmugi/arcane/design/foundation/tokens/Radius.kt
package io.github.devmugi.arcane.design.foundation.tokens

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.dp

@Immutable
object ArcaneRadius {
    /** No rounding */
    val None = RoundedCornerShape(0.dp)

    /** B4 - 4px subtle rounding */
    val Small = RoundedCornerShape(4.dp)

    /** B8 - 6px (labeled as B8 but 6px per design) */
    val Medium = RoundedCornerShape(6.dp)

    /** R12 - 12px standard rounding */
    val Large = RoundedCornerShape(12.dp)

    /** R15 - 18px (labeled R15 but 18px per design) */
    val ExtraLarge = RoundedCornerShape(18.dp)

    /** Full pill shape */
    val Full = RoundedCornerShape(50)
}
```

**Step 4: Create Spacing tokens**

```kotlin
// arcane-foundation/src/commonMain/kotlin/io/github/devmugi/arcane/design/foundation/tokens/Spacing.kt
package io.github.devmugi.arcane.design.foundation.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
object ArcaneSpacing {
    /** 4dp - Minimal spacing */
    val XXSmall: Dp = 4.dp

    /** 8dp - Tight spacing */
    val XSmall: Dp = 8.dp

    /** 12dp - Compact spacing */
    val Small: Dp = 12.dp

    /** 16dp - Standard spacing */
    val Medium: Dp = 16.dp

    /** 24dp - Comfortable spacing */
    val Large: Dp = 24.dp

    /** 32dp - Generous spacing */
    val XLarge: Dp = 32.dp

    /** 48dp - Section spacing */
    val XXLarge: Dp = 48.dp
}
```

**Step 5: Commit**

```bash
git add arcane-foundation/
git commit -m "feat(foundation): add dimension tokens (elevation, border, radius, spacing)"
```

---

## Task 7: Add Iconography Tokens

**Files:**
- Create: `arcane-foundation/src/commonMain/kotlin/io/github/devmugi/arcane/design/foundation/tokens/Iconography.kt`

**Step 1: Create Iconography tokens**

```kotlin
// arcane-foundation/src/commonMain/kotlin/io/github/devmugi/arcane/design/foundation/tokens/Iconography.kt
package io.github.devmugi.arcane.design.foundation.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
object ArcaneIconography {
    /** 15dp - Small icons for dense UI */
    val Small: Dp = 15.dp

    /** 25dp - Standard icon size */
    val Medium: Dp = 25.dp

    /** 26dp - Large icons for emphasis */
    val Large: Dp = 26.dp

    /** Default stroke width for outlined icons */
    val StrokeWidth: Dp = 1.5.dp
}
```

**Step 2: Commit**

```bash
git add arcane-foundation/
git commit -m "feat(foundation): add iconography size tokens"
```

---

## Task 8: Add Surface Primitives

**Files:**
- Create: `arcane-foundation/src/commonMain/kotlin/io/github/devmugi/arcane/design/foundation/primitives/Surface.kt`

**Step 1: Create Surface primitives**

```kotlin
// arcane-foundation/src/commonMain/kotlin/io/github/devmugi/arcane/design/foundation/primitives/Surface.kt
package io.github.devmugi.arcane.design.foundation.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneBorder
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneElevation
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneRadius

enum class SurfaceVariant {
    Base,
    Raised,
    Inset,
    Pressed
}

@Composable
fun ArcaneSurface(
    modifier: Modifier = Modifier,
    variant: SurfaceVariant = SurfaceVariant.Base,
    shape: Shape = ArcaneRadius.Medium,
    showBorder: Boolean = true,
    showGlow: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    val colors = ArcaneTheme.colors

    val backgroundColor = when (variant) {
        SurfaceVariant.Base -> colors.surface
        SurfaceVariant.Raised -> colors.surfaceRaised
        SurfaceVariant.Inset -> colors.surfaceInset
        SurfaceVariant.Pressed -> colors.surfacePressed
    }

    val glowAlpha = when (variant) {
        SurfaceVariant.Base -> ArcaneElevation.Level1Alpha
        SurfaceVariant.Raised -> ArcaneElevation.Level2Alpha
        SurfaceVariant.Inset -> 0f
        SurfaceVariant.Pressed -> ArcaneElevation.Level1Alpha
    }

    Box(
        modifier = modifier
            .then(
                if (showGlow && glowAlpha > 0f) {
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
            .clip(shape)
            .background(backgroundColor, shape)
            .then(
                if (showBorder) {
                    Modifier.border(ArcaneBorder.Title, colors.border, shape)
                } else Modifier
            ),
        content = content
    )
}
```

**Step 2: Commit**

```bash
git add arcane-foundation/
git commit -m "feat(foundation): add ArcaneSurface primitive with variants"
```

---

## Task 9: Add ArcaneTheme Composable

**Files:**
- Create: `arcane-foundation/src/commonMain/kotlin/io/github/devmugi/arcane/design/foundation/theme/ArcaneTheme.kt`

**Step 1: Create ArcaneTheme**

```kotlin
// arcane-foundation/src/commonMain/kotlin/io/github/devmugi/arcane/design/foundation/theme/ArcaneTheme.kt
package io.github.devmugi.arcane.design.foundation.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun ArcaneTheme(
    colors: ArcaneColors = ArcaneColors.default(),
    typography: ArcaneTypography = ArcaneTypography(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalArcaneColors provides colors,
        LocalArcaneTypography provides typography,
        content = content
    )
}

object ArcaneTheme {
    val colors: ArcaneColors
        @Composable
        @ReadOnlyComposable
        get() = LocalArcaneColors.current

    val typography: ArcaneTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalArcaneTypography.current
}
```

**Step 2: Commit**

```bash
git add arcane-foundation/
git commit -m "feat(foundation): add ArcaneTheme provider"
```

---

## Task 10: Create Empty arcane-components Module

**Files:**
- Create: `arcane-components/build.gradle.kts`
- Create: `arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/.gitkeep`

**Step 1: Create components build file**

```kotlin
// arcane-components/build.gradle.kts
plugins {
    id("arcane.multiplatform.library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":arcane-foundation"))
        }
    }
}
```

**Step 2: Create placeholder**

```
# arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/.gitkeep
```

**Step 3: Commit**

```bash
git add arcane-components/
git commit -m "chore: scaffold arcane-components module"
```

---

## Task 11: Create Catalog App Structure

**Files:**
- Create: `catalog/composeApp/build.gradle.kts`
- Create: `catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/App.kt`
- Create: `catalog/composeApp/src/androidMain/kotlin/io/github/devmugi/arcane/catalog/MainActivity.kt`
- Create: `catalog/composeApp/src/desktopMain/kotlin/io/github/devmugi/arcane/catalog/Main.kt`
- Create: `catalog/composeApp/src/androidMain/AndroidManifest.xml`

**Step 1: Create catalog build file**

```kotlin
// catalog/composeApp/build.gradle.kts
plugins {
    id("arcane.multiplatform.application")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":arcane-foundation"))
            implementation(project(":arcane-components"))
        }
    }
}
```

**Step 2: Create common App composable**

```kotlin
// catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/App.kt
package io.github.devmugi.arcane.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.devmugi.arcane.design.foundation.primitives.ArcaneSurface
import io.github.devmugi.arcane.design.foundation.primitives.SurfaceVariant
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme
import io.github.devmugi.arcane.design.foundation.tokens.ArcaneSpacing

@Composable
fun App() {
    ArcaneTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(ArcaneSpacing.Medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Medium)
        ) {
            Text(
                text = "Arcane Design System",
                style = ArcaneTheme.typography.displayMedium,
                color = ArcaneTheme.colors.text
            )

            Text(
                text = "Catalog App",
                style = ArcaneTheme.typography.bodyLarge,
                color = ArcaneTheme.colors.textSecondary
            )

            ArcaneSurface(
                variant = SurfaceVariant.Base,
                showGlow = true,
                modifier = Modifier.padding(ArcaneSpacing.Large)
            ) {
                Text(
                    text = "Base Surface",
                    style = ArcaneTheme.typography.bodyMedium,
                    color = ArcaneTheme.colors.text,
                    modifier = Modifier.padding(ArcaneSpacing.Medium)
                )
            }

            ArcaneSurface(
                variant = SurfaceVariant.Raised,
                showGlow = true,
                modifier = Modifier.padding(ArcaneSpacing.Large)
            ) {
                Text(
                    text = "Raised Surface",
                    style = ArcaneTheme.typography.bodyMedium,
                    color = ArcaneTheme.colors.text,
                    modifier = Modifier.padding(ArcaneSpacing.Medium)
                )
            }

            ArcaneSurface(
                variant = SurfaceVariant.Inset,
                modifier = Modifier.padding(ArcaneSpacing.Large)
            ) {
                Text(
                    text = "Inset Surface",
                    style = ArcaneTheme.typography.bodyMedium,
                    color = ArcaneTheme.colors.text,
                    modifier = Modifier.padding(ArcaneSpacing.Medium)
                )
            }

            ArcaneSurface(
                variant = SurfaceVariant.Pressed,
                modifier = Modifier.padding(ArcaneSpacing.Large)
            ) {
                Text(
                    text = "Pressed Surface",
                    style = ArcaneTheme.typography.bodyMedium,
                    color = ArcaneTheme.colors.text,
                    modifier = Modifier.padding(ArcaneSpacing.Medium)
                )
            }
        }
    }
}
```

**Step 3: Create Android MainActivity**

```kotlin
// catalog/composeApp/src/androidMain/kotlin/io/github/devmugi/arcane/catalog/MainActivity.kt
package io.github.devmugi.arcane.catalog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import io.github.devmugi.arcane.design.foundation.theme.ArcaneColors

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ArcaneColors.default().surface)
            ) {
                App()
            }
        }
    }
}
```

**Step 4: Create Desktop main**

```kotlin
// catalog/composeApp/src/desktopMain/kotlin/io/github/devmugi/arcane/catalog/Main.kt
package io.github.devmugi.arcane.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.devmugi.arcane.design.foundation.theme.ArcaneColors

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Arcane Design System Catalog"
    ) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ArcaneColors.default().surface)
        ) {
            App()
        }
    }
}
```

**Step 5: Create AndroidManifest.xml**

```xml
<!-- catalog/composeApp/src/androidMain/AndroidManifest.xml -->
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="Arcane Catalog"
        android:supportsRtl="true"
        android:theme="@style/Theme.AppCompat.NoActionBar">
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:configChanges="orientation|screenSize|screenLayout|keyboardHidden">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
</manifest>
```

**Step 6: Commit**

```bash
git add catalog/
git commit -m "feat(catalog): add catalog app scaffold for Android and Desktop"
```

---

## Task 12: Add Android Resources for Catalog

**Files:**
- Create: `catalog/composeApp/src/androidMain/res/values/styles.xml`
- Create: `catalog/composeApp/src/androidMain/res/mipmap-hdpi/ic_launcher.png` (placeholder)

**Step 1: Create styles.xml**

```xml
<!-- catalog/composeApp/src/androidMain/res/values/styles.xml -->
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <style name="Theme.AppCompat.NoActionBar" parent="android:Theme.Material.NoActionBar">
        <item name="android:windowBackground">#FF0A1A1F</item>
        <item name="android:statusBarColor">#FF0A1A1F</item>
        <item name="android:navigationBarColor">#FF0A1A1F</item>
    </style>
</resources>
```

**Step 2: Commit (skip launcher icon for now - use default)**

```bash
git add catalog/
git commit -m "chore(catalog): add Android theme resources"
```

---

## Task 13: Create Root build.gradle.kts

**Files:**
- Create: `build.gradle.kts`

**Step 1: Create root build file**

```kotlin
// build.gradle.kts
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.compose.compiler) apply false
}
```

**Step 2: Commit**

```bash
git add build.gradle.kts
git commit -m "chore: add root build.gradle.kts"
```

---

## Task 14: Verify Build

**Step 1: Run Gradle sync and build**

Run: `./gradlew build --dry-run`

Expected: Build configuration succeeds without errors

**Step 2: If dry-run succeeds, run actual build**

Run: `./gradlew :arcane-foundation:build`

Expected: BUILD SUCCESSFUL

**Step 3: Build catalog desktop app**

Run: `./gradlew :catalog:composeApp:desktopJar`

Expected: BUILD SUCCESSFUL

**Step 4: Final commit for Phase 1**

```bash
git add -A
git commit -m "feat: complete Phase 1 - foundation and project setup"
```

---

## Task 15: Run Catalog Desktop App (Verification)

**Step 1: Run desktop app**

Run: `./gradlew :catalog:composeApp:run`

Expected: Window opens showing "Arcane Design System" title with four surface variants displayed

**Step 2: Verify visual appearance**

- Dark background (surface color)
- Teal text and borders
- Four surface boxes with different backgrounds
- Glow effect visible on Base and Raised surfaces

---

## Summary

After completing all tasks, you will have:

1. ✅ Gradle multi-module setup with convention plugins
2. ✅ Version catalog with all dependencies
3. ✅ `arcane-foundation` module with:
   - ArcaneColors (customizable)
   - ArcaneTypography
   - Tokens: Elevation, Border, Radius, Spacing, Iconography
   - ArcaneSurface primitive
   - ArcaneTheme provider
4. ✅ Empty `arcane-components` module scaffold
5. ✅ Catalog app running on Desktop (Android ready)

**Next Phase:** Phase 2 - Core Controls (Button, TextField, Checkbox, RadioButton, Switch, Slider)
