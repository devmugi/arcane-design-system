# PR Changes Feature Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Automatically detect changed UI components in PRs and display them in a filtered "PR Changes" tab on GitHub Pages previews.

**Architecture:** Git diff analysis generates a JSON manifest at build time. The Catalog app reads this manifest and filters to show only affected screens/components. The PR Changes tab only appears on PR preview builds via separate HTML templates.

**Tech Stack:** Bash (detection script), Kotlin/Compose Multiplatform (Catalog app), GitHub Actions (CI), HTML/CSS (templates)

---

## Task 1: Create Detection Script

**Files:**
- Create: `scripts/detect-pr-changes.sh`

**Step 1: Create the script file**

```bash
#!/bin/bash
# detect-pr-changes.sh
# Analyzes git diff and outputs pr-changes.json manifest

set -e

INPUT_FILE="${1:-}"
PR_NUMBER="${2:-0}"
BASE_BRANCH="${3:-main}"

# If no input file, read from stdin
if [ -z "$INPUT_FILE" ]; then
    CHANGED_FILES=$(cat)
elif [ -f "$INPUT_FILE" ]; then
    CHANGED_FILES=$(cat "$INPUT_FILE")
else
    CHANGED_FILES=""
fi

# Initialize arrays
declare -a COMPONENTS=()
declare -a SCREENS=()

# Map directory to screen name
get_screen() {
    local path="$1"
    case "$path" in
        *"/components/controls/"*) echo "Controls" ;;
        *"/components/navigation/"*) echo "Navigation" ;;
        *"/components/display/"*) echo "Data Display" ;;
        *"/components/feedback/"*) echo "Feedback" ;;
        *"arcane-foundation/"*) echo "Design Spec" ;;
        *) echo "" ;;
    esac
}

# Get change type (added, modified, deleted)
get_change_type() {
    local file="$1"
    if git ls-files --error-unmatch "$file" &>/dev/null 2>&1; then
        echo "modified"
    else
        echo "added"
    fi
}

# Process each changed file
while IFS= read -r file; do
    # Skip empty lines
    [ -z "$file" ] && continue

    # Only process .kt files in component directories
    if [[ "$file" == *.kt ]] && [[ "$file" == *"/components/"* || "$file" == *"arcane-foundation/"* ]]; then
        # Extract component name from filename
        filename=$(basename "$file" .kt)
        screen=$(get_screen "$file")

        if [ -n "$screen" ]; then
            # Add to components array (JSON format)
            change_type=$(get_change_type "$file")
            COMPONENTS+=("{\"name\":\"$filename\",\"screen\":\"$screen\",\"changeType\":\"$change_type\",\"filePath\":\"$file\"}")

            # Track unique screens
            if [[ ! " ${SCREENS[*]} " =~ " ${screen} " ]]; then
                SCREENS+=("$screen")
            fi
        fi
    fi
done <<< "$CHANGED_FILES"

# Build JSON output
TIMESTAMP=$(date -u +"%Y-%m-%dT%H:%M:%SZ")

# Convert arrays to JSON
COMPONENTS_JSON=$(printf '%s\n' "${COMPONENTS[@]}" | paste -sd ',' -)
SCREENS_JSON=$(printf '"%s"\n' "${SCREENS[@]}" | paste -sd ',' -)

# Handle empty arrays
[ -z "$COMPONENTS_JSON" ] && COMPONENTS_JSON=""
[ -z "$SCREENS_JSON" ] && SCREENS_JSON=""

cat << EOF
{
  "prNumber": $PR_NUMBER,
  "baseBranch": "$BASE_BRANCH",
  "changedComponents": [$COMPONENTS_JSON],
  "affectedScreens": [$SCREENS_JSON],
  "generatedAt": "$TIMESTAMP"
}
EOF
```

**Step 2: Make script executable**

Run: `chmod +x scripts/detect-pr-changes.sh`

**Step 3: Test the script locally**

```bash
# Create test input
echo "arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/Button.kt
arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/feedback/Spinner.kt" > /tmp/test-changes.txt

# Run script
./scripts/detect-pr-changes.sh /tmp/test-changes.txt 42 main
```

Expected output: Valid JSON with Button (Controls) and Spinner (Feedback)

**Step 4: Commit**

```bash
git add scripts/detect-pr-changes.sh
git commit -m "feat(ci): add PR changes detection script

Analyzes git diff to identify changed components and maps them
to Catalog screens. Outputs JSON manifest for filtered view."
```

---

## Task 2: Create PR Preview HTML Template

**Files:**
- Create: `catalog/composeApp/src/wasmJsMain/resources/index-pr-preview.html`

**Step 1: Create 4-tab template**

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Arcane Design System Catalog</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        html, body {
            width: 100%;
            height: 100%;
            overflow: hidden;
            background-color: #0d1117;
        }

        /* Topbar */
        .topbar {
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            height: 48px;
            background-color: #161b22;
            border-bottom: 1px solid #30363d;
            display: flex;
            align-items: center;
            padding: 0 16px;
            z-index: 1000;
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Helvetica, Arial, sans-serif;
        }

        .logo {
            font-size: 18px;
            font-weight: 700;
            color: #a855f7;
            margin-right: 32px;
            text-decoration: none;
        }

        .nav-tabs {
            display: flex;
            gap: 4px;
        }

        .nav-tab {
            padding: 8px 16px;
            border-radius: 6px;
            text-decoration: none;
            color: #8b949e;
            font-size: 14px;
            transition: all 0.15s ease;
        }

        .nav-tab:hover {
            background-color: #21262d;
            color: #e6edf3;
        }

        .nav-tab.active {
            background-color: #a855f7;
            color: #ffffff;
        }

        /* App container */
        .app-container {
            position: absolute;
            top: 48px;
            left: 0;
            right: 0;
            bottom: 0;
        }

        #ComposeTarget {
            width: 100%;
            height: 100%;
        }

        @media (max-width: 600px) {
            .nav-tabs {
                gap: 2px;
            }
            .nav-tab {
                padding: 6px 10px;
                font-size: 13px;
            }
            .logo {
                margin-right: 16px;
            }
        }
    </style>
</head>
<body>
    <nav class="topbar">
        <a href="../" class="logo">Arcane</a>
        <div class="nav-tabs">
            <a href="../" class="nav-tab">Initial Prompt</a>
            <a href="../catalog/" class="nav-tab">Catalog</a>
            <a href="../catalog-chat/" class="nav-tab">Chat</a>
            <a href="./" class="nav-tab active">PR Changes</a>
        </div>
    </nav>
    <div class="app-container">
        <div id="ComposeTarget"></div>
        <script src="composeApp.js"></script>
    </div>
</body>
</html>
```

**Step 2: Verify template**

Run: `diff catalog/composeApp/src/wasmJsMain/resources/index.html catalog/composeApp/src/wasmJsMain/resources/index-pr-preview.html`

Expected: Only nav-tabs section differs (4 tabs vs 3, different active state)

**Step 3: Commit**

```bash
git add catalog/composeApp/src/wasmJsMain/resources/index-pr-preview.html
git commit -m "feat(catalog): add PR preview HTML template with 4 tabs

Adds PR Changes tab to navigation for PR preview builds only."
```

---

## Task 3: Create PR Changes Data Model

**Files:**
- Create: `catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/prchanges/PrChangesData.kt`

**Step 1: Create data classes**

```kotlin
package io.github.devmugi.arcane.catalog.prchanges

import kotlinx.serialization.Serializable

@Serializable
data class PrChangesManifest(
    val prNumber: Int = 0,
    val baseBranch: String = "main",
    val changedComponents: List<ChangedComponent> = emptyList(),
    val affectedScreens: List<String> = emptyList(),
    val generatedAt: String = ""
)

@Serializable
data class ChangedComponent(
    val name: String,
    val screen: String,
    val changeType: String, // "added", "modified", "deleted"
    val filePath: String
)
```

**Step 2: Verify it compiles**

Run: `./gradlew :catalog:composeApp:compileKotlinDesktop`

Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/prchanges/PrChangesData.kt
git commit -m "feat(catalog): add PR changes data model

Serializable data classes for pr-changes.json manifest."
```

---

## Task 4: Add kotlinx-serialization Dependency

**Files:**
- Modify: `catalog/composeApp/build.gradle.kts`
- Modify: `gradle/libs.versions.toml` (if needed)

**Step 1: Check if serialization is already in version catalog**

Run: `grep -i serial gradle/libs.versions.toml`

**Step 2: Add dependency to catalog build.gradle.kts**

In `catalog/composeApp/build.gradle.kts`, add to `commonMain.dependencies`:

```kotlin
implementation(libs.kotlinx.serialization.json)
```

And add the plugin:

```kotlin
plugins {
    // ... existing plugins
    alias(libs.plugins.kotlin.serialization)
}
```

**Step 3: Verify it compiles**

Run: `./gradlew :catalog:composeApp:compileKotlinDesktop`

Expected: BUILD SUCCESSFUL

**Step 4: Commit**

```bash
git add catalog/composeApp/build.gradle.kts gradle/libs.versions.toml
git commit -m "feat(catalog): add kotlinx-serialization dependency

Required for parsing pr-changes.json manifest."
```

---

## Task 5: Create PR Changes Config Holder

**Files:**
- Create: `catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/prchanges/PrChangesConfig.kt`

**Step 1: Create config object**

```kotlin
package io.github.devmugi.arcane.catalog.prchanges

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object PrChangesConfig {
    var manifest: PrChangesManifest by mutableStateOf(PrChangesManifest())
        private set

    val isFilteredMode: Boolean
        get() = manifest.affectedScreens.isNotEmpty()

    val changedComponentNames: Set<String>
        get() = manifest.changedComponents.map { it.name }.toSet()

    val affectedScreens: List<String>
        get() = manifest.affectedScreens

    fun loadManifest(manifest: PrChangesManifest) {
        this.manifest = manifest
    }

    fun isComponentChanged(componentName: String): Boolean {
        return componentName in changedComponentNames
    }

    fun isScreenAffected(screenName: String): Boolean {
        return screenName in affectedScreens
    }
}
```

**Step 2: Verify it compiles**

Run: `./gradlew :catalog:composeApp:compileKotlinDesktop`

Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/prchanges/PrChangesConfig.kt
git commit -m "feat(catalog): add PR changes config holder

Runtime state for filtered mode based on loaded manifest."
```

---

## Task 6: Create Changed Badge Component

**Files:**
- Create: `catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/components/ChangedBadge.kt`

**Step 1: Create badge component**

```kotlin
package io.github.devmugi.arcane.catalog.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme

@Composable
fun ChangedBadge(
    changeType: String,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor, label) = when (changeType) {
        "added" -> Triple(
            Color(0xFF238636),
            Color.White,
            "NEW"
        )
        "modified" -> Triple(
            ArcaneTheme.colors.primary,
            Color.White,
            "CHANGED"
        )
        else -> Triple(
            ArcaneTheme.colors.surfaceContainer,
            ArcaneTheme.colors.textSecondary,
            changeType.uppercase()
        )
    }

    Text(
        text = label,
        style = ArcaneTheme.typography.labelSmall,
        color = textColor,
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
            .padding(horizontal = 6.dp, vertical = 2.dp)
    )
}
```

**Step 2: Verify it compiles**

Run: `./gradlew :catalog:composeApp:compileKotlinDesktop`

Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/components/ChangedBadge.kt
git commit -m "feat(catalog): add ChangedBadge component

Visual indicator for new/modified components in PR Changes view."
```

---

## Task 7: Create Empty State Component

**Files:**
- Create: `catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/components/PrChangesEmptyState.kt`

**Step 1: Create empty state component**

```kotlin
package io.github.devmugi.arcane.catalog.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.devmugi.arcane.design.components.controls.ArcaneButton
import io.github.devmugi.arcane.design.components.controls.ArcaneButtonStyle
import io.github.devmugi.arcane.design.foundation.theme.ArcaneTheme

@Composable
fun PrChangesEmptyState(
    onViewFullCatalog: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.CheckCircle,
            contentDescription = null,
            tint = ArcaneTheme.colors.textSecondary,
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "No component changes in this PR",
            style = ArcaneTheme.typography.headlineSmall,
            color = ArcaneTheme.colors.textPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "This PR doesn't modify any UI components.",
            style = ArcaneTheme.typography.bodyMedium,
            color = ArcaneTheme.colors.textSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        ArcaneButton(
            text = "View Full Catalog",
            onClick = onViewFullCatalog,
            style = ArcaneButtonStyle.Secondary
        )
    }
}
```

**Step 2: Verify it compiles**

Run: `./gradlew :catalog:composeApp:compileKotlinDesktop`

Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/components/PrChangesEmptyState.kt
git commit -m "feat(catalog): add PR changes empty state component

Shown when PR has no component changes, with link to full Catalog."
```

---

## Task 8: Create WasmJS Manifest Loader

**Files:**
- Create: `catalog/composeApp/src/wasmJsMain/kotlin/io/github/devmugi/arcane/catalog/prchanges/ManifestLoader.kt`

**Step 1: Create platform-specific loader for WasmJS**

```kotlin
package io.github.devmugi.arcane.catalog.prchanges

import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.serialization.json.Json
import org.w3c.fetch.Response
import kotlin.js.Promise

private val json = Json { ignoreUnknownKeys = true }

suspend fun loadPrChangesManifest(): PrChangesManifest? {
    return try {
        val response = window.fetch("pr-changes.json").await<Response>()
        if (response.ok) {
            val text = response.text().await<String>()
            json.decodeFromString<PrChangesManifest>(text)
        } else {
            null
        }
    } catch (e: Exception) {
        console.log("PR changes manifest not found or invalid: ${e.message}")
        null
    }
}

private external val console: Console

private external interface Console {
    fun log(message: String)
}

private suspend fun <T> Promise<T>.await(): T = kotlinx.coroutines.await()
```

**Step 2: Verify it compiles**

Run: `./gradlew :catalog:composeApp:compileKotlinWasmJs -PbuildWasm=true`

Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add catalog/composeApp/src/wasmJsMain/kotlin/io/github/devmugi/arcane/catalog/prchanges/ManifestLoader.kt
git commit -m "feat(catalog): add WasmJS manifest loader

Fetches and parses pr-changes.json on app startup."
```

---

## Task 9: Create Desktop/Common Manifest Loader Stub

**Files:**
- Create: `catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/prchanges/ManifestLoader.kt`

**Step 1: Create expect declaration**

```kotlin
package io.github.devmugi.arcane.catalog.prchanges

expect suspend fun loadPrChangesManifest(): PrChangesManifest?
```

**Step 2: Create desktop actual implementation**

Create: `catalog/composeApp/src/desktopMain/kotlin/io/github/devmugi/arcane/catalog/prchanges/ManifestLoader.kt`

```kotlin
package io.github.devmugi.arcane.catalog.prchanges

actual suspend fun loadPrChangesManifest(): PrChangesManifest? {
    // Desktop doesn't use PR changes filtering
    return null
}
```

**Step 3: Update WasmJS loader to be actual**

Update the wasmJsMain version to include `actual`:

```kotlin
actual suspend fun loadPrChangesManifest(): PrChangesManifest? {
    // ... existing implementation
}
```

**Step 4: Verify it compiles**

Run: `./gradlew :catalog:composeApp:compileKotlinDesktop`

Expected: BUILD SUCCESSFUL

**Step 5: Commit**

```bash
git add catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/prchanges/ManifestLoader.kt \
        catalog/composeApp/src/desktopMain/kotlin/io/github/devmugi/arcane/catalog/prchanges/ManifestLoader.kt \
        catalog/composeApp/src/wasmJsMain/kotlin/io/github/devmugi/arcane/catalog/prchanges/ManifestLoader.kt
git commit -m "feat(catalog): add expect/actual manifest loader

Desktop returns null (no filtering), WasmJS fetches pr-changes.json."
```

---

## Task 10: Modify App.kt to Support Filtered Mode

**Files:**
- Modify: `catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/App.kt`

**Step 1: Add imports and filtered mode logic**

Add at top of file:

```kotlin
import androidx.compose.runtime.LaunchedEffect
import io.github.devmugi.arcane.catalog.components.PrChangesEmptyState
import io.github.devmugi.arcane.catalog.prchanges.PrChangesConfig
import io.github.devmugi.arcane.catalog.prchanges.loadPrChangesManifest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
```

**Step 2: Modify CatalogTopBar to filter tabs**

Replace the `ArcaneTabs` in `CatalogTopBar` with filtered version:

```kotlin
@Composable
private fun CatalogTopBar(
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit,
    currentTheme: ThemeVariant,
    onThemeChange: (ThemeVariant) -> Unit,
    availableScreens: List<Screen> = Screen.all(),
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(ArcaneTheme.colors.surfaceContainer)
            .padding(horizontal = ArcaneSpacing.Medium, vertical = ArcaneSpacing.Small),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left side: Navigation tabs (filtered if in PR changes mode)
        ArcaneTabs(
            tabs = availableScreens.map { ArcaneTab(it.displayName) },
            selectedIndex = availableScreens.indexOf(currentScreen).coerceAtLeast(0),
            onTabSelected = { index ->
                availableScreens.getOrNull(index)?.let { onScreenSelected(it) }
            },
            style = ArcaneTabStyle.Filled,
            scrollable = true
        )

        // Right side: Theme selector
        ThemeSelector(
            currentTheme = currentTheme,
            onThemeChange = onThemeChange
        )
    }
}
```

**Step 3: Add Screen extension properties**

Add after the `Screen` sealed class:

```kotlin
val Screen.displayName: String
    get() = when (this) {
        Screen.DesignSpec -> "Overview"
        Screen.Controls -> "Controls"
        Screen.Navigation -> "Navigation"
        Screen.DataDisplay -> "Data Display"
        Screen.Feedback -> "Feedback"
    }

val Screen.catalogName: String
    get() = when (this) {
        Screen.DesignSpec -> "Design Spec"
        Screen.Controls -> "Controls"
        Screen.Navigation -> "Navigation"
        Screen.DataDisplay -> "Data Display"
        Screen.Feedback -> "Feedback"
    }

fun Screen.Companion.all(): List<Screen> = listOf(
    Screen.DesignSpec,
    Screen.Controls,
    Screen.Navigation,
    Screen.DataDisplay,
    Screen.Feedback
)

fun Screen.Companion.fromCatalogName(name: String): Screen? = when (name) {
    "Design Spec" -> Screen.DesignSpec
    "Controls" -> Screen.Controls
    "Navigation" -> Screen.Navigation
    "Data Display" -> Screen.DataDisplay
    "Feedback" -> Screen.Feedback
    else -> null
}
```

**Step 4: Modify App composable**

```kotlin
@Composable
fun App(isFilteredMode: Boolean = false) {
    var currentTheme by remember { mutableStateOf(ThemeVariant.ARCANE) }
    val colors = when (currentTheme) {
        ThemeVariant.ARCANE -> ArcaneColors.default()
        ThemeVariant.PERPLEXITY -> ArcaneColors.perplexity()
        ThemeVariant.CLAUDE -> ArcaneColors.claude()
        ThemeVariant.MTG -> ArcaneColors.mtg()
    }

    // Load PR changes manifest on startup
    LaunchedEffect(Unit) {
        if (isFilteredMode) {
            val manifest = withContext(Dispatchers.Default) {
                loadPrChangesManifest()
            }
            manifest?.let { PrChangesConfig.loadManifest(it) }
        }
    }

    // Determine available screens based on mode
    val availableScreens = if (isFilteredMode && PrChangesConfig.isFilteredMode) {
        PrChangesConfig.affectedScreens.mapNotNull { Screen.fromCatalogName(it) }
    } else {
        Screen.all()
    }

    ArcaneTheme(colors = colors) {
        var currentScreen by remember(availableScreens) {
            mutableStateOf(availableScreens.firstOrNull() ?: Screen.DesignSpec)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ArcaneTheme.colors.surface)
        ) {
            // Show empty state if filtered mode but no affected screens
            if (isFilteredMode && !PrChangesConfig.isFilteredMode) {
                PrChangesEmptyState(
                    onViewFullCatalog = {
                        // Navigate to full catalog via JS
                        navigateToFullCatalog()
                    }
                )
            } else {
                // Top navigation bar (persistent)
                CatalogTopBar(
                    currentScreen = currentScreen,
                    onScreenSelected = { currentScreen = it },
                    currentTheme = currentTheme,
                    onThemeChange = { currentTheme = it },
                    availableScreens = availableScreens
                )

                // Screen content area
                Box(modifier = Modifier.weight(1f)) {
                    when (currentScreen) {
                        Screen.DesignSpec -> DesignSpecScreen()
                        Screen.Controls -> ControlsScreen()
                        Screen.Navigation -> NavigationScreen()
                        Screen.DataDisplay -> DataDisplayScreen()
                        Screen.Feedback -> FeedbackScreen()
                    }
                }
            }
        }
    }
}

expect fun navigateToFullCatalog()
```

**Step 5: Verify it compiles**

Run: `./gradlew :catalog:composeApp:compileKotlinDesktop`

Expected: BUILD SUCCESSFUL (may need to add companion object to Screen)

**Step 6: Commit**

```bash
git add catalog/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/App.kt
git commit -m "feat(catalog): add filtered mode support to App

Filters tabs based on PR changes manifest. Shows empty state
when no components changed."
```

---

## Task 11: Add Platform Navigation Functions

**Files:**
- Create: `catalog/composeApp/src/desktopMain/kotlin/io/github/devmugi/arcane/catalog/Navigation.kt`
- Create: `catalog/composeApp/src/wasmJsMain/kotlin/io/github/devmugi/arcane/catalog/Navigation.kt`

**Step 1: Create desktop stub**

```kotlin
package io.github.devmugi.arcane.catalog

actual fun navigateToFullCatalog() {
    // No-op on desktop
}
```

**Step 2: Create WasmJS implementation**

```kotlin
package io.github.devmugi.arcane.catalog

import kotlinx.browser.window

actual fun navigateToFullCatalog() {
    window.location.href = "../catalog/"
}
```

**Step 3: Verify it compiles**

Run: `./gradlew :catalog:composeApp:compileKotlinDesktop && ./gradlew :catalog:composeApp:compileKotlinWasmJs -PbuildWasm=true`

Expected: BUILD SUCCESSFUL

**Step 4: Commit**

```bash
git add catalog/composeApp/src/desktopMain/kotlin/io/github/devmugi/arcane/catalog/Navigation.kt \
        catalog/composeApp/src/wasmJsMain/kotlin/io/github/devmugi/arcane/catalog/Navigation.kt
git commit -m "feat(catalog): add platform navigation functions

WasmJS navigates to full catalog, desktop is no-op."
```

---

## Task 12: Create PR Changes Main Entry Point

**Files:**
- Create: `catalog/composeApp/src/wasmJsMain/kotlin/io/github/devmugi/arcane/catalog/PrChangesMain.kt`

**Step 1: Create entry point**

```kotlin
package io.github.devmugi.arcane.catalog

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow

@OptIn(ExperimentalComposeUiApi::class)
fun prChangesMain() {
    CanvasBasedWindow(canvasElementId = "ComposeTarget") {
        App(isFilteredMode = true)
    }
}
```

**Step 2: Verify it compiles**

Run: `./gradlew :catalog:composeApp:compileKotlinWasmJs -PbuildWasm=true`

Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add catalog/composeApp/src/wasmJsMain/kotlin/io/github/devmugi/arcane/catalog/PrChangesMain.kt
git commit -m "feat(catalog): add PR changes entry point

Separate main function that enables filtered mode."
```

---

## Task 13: Update Build Logic for PR Changes

**Files:**
- Modify: `build-logic/src/main/kotlin/arcane.multiplatform.application.gradle.kts`

**Step 1: Add PR changes build task**

Add after the existing `publishWasmJsToDocs` task:

```kotlin
if (buildWasmForTask) {
    tasks.register<Copy>("publishPrChangesWasmJsToDocs") {
        group = "distribution"
        description = "Copies production wasmJs build to docs/pr-changes folder for PR previews"

        dependsOn("wasmJsBrowserProductionWebpack")

        // Only run for catalog project (not catalog-chat)
        onlyIf { project.path.contains("catalog") && !project.path.contains("catalog-chat") }

        // Copy webpack output (JS, WASM files)
        from(layout.buildDirectory.dir("kotlin-webpack/wasmJs/productionExecutable"))
        // Copy PR preview template
        from("src/wasmJsMain/resources/index-pr-preview.html") {
            rename { "index.html" }
        }
        into(rootProject.layout.projectDirectory.dir("docs/pr-changes"))
    }
}
```

**Step 2: Verify it compiles**

Run: `./gradlew tasks --group=distribution`

Expected: `publishPrChangesWasmJsToDocs` task appears

**Step 3: Commit**

```bash
git add build-logic/src/main/kotlin/arcane.multiplatform.application.gradle.kts
git commit -m "feat(build): add PR changes WasmJS publish task

Publishes filtered Catalog to docs/pr-changes/ with 4-tab template."
```

---

## Task 14: Update Root Build to Include PR Changes Task

**Files:**
- Modify: `build.gradle.kts`

**Step 1: Add PR changes task to root**

```kotlin
// Convenience task to publish all wasmJs apps including PR changes
tasks.register("publishAllWasmJsToDocsWithPrChanges") {
    group = "distribution"
    description = "Publishes all wasmJs catalog apps including PR changes to docs folder"
    dependsOn(
        ":catalog:composeApp:publishWasmJsToDocs",
        ":catalog:composeApp:publishPrChangesWasmJsToDocs",
        ":catalog-chat:composeApp:publishWasmJsToDocs"
    )
}
```

**Step 2: Verify task exists**

Run: `./gradlew tasks --group=distribution`

Expected: Both `publishAllWasmJsToDocs` and `publishAllWasmJsToDocsWithPrChanges` appear

**Step 3: Commit**

```bash
git add build.gradle.kts
git commit -m "feat(build): add combined PR changes publish task

New task includes PR changes build alongside regular catalog builds."
```

---

## Task 15: Update CI Workflow

**Files:**
- Modify: `.github/workflows/pr-preview.yml`

**Step 1: Update workflow with detection and PR changes build**

```yaml
name: PR Preview

on:
  pull_request:
    types: [opened, reopened, synchronize, closed]

concurrency: preview-${{ github.ref }}

jobs:
  build-and-preview:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
        with:
          fetch-depth: 0  # Need full history for git diff

      - name: Setup Java
        if: github.event.action != 'closed'
        uses: actions/setup-java@v4
        with:
          distribution: 'temurin'
          java-version: '21'

      - name: Setup Gradle
        if: github.event.action != 'closed'
        uses: gradle/actions/setup-gradle@v4

      - name: Detect changed components
        if: github.event.action != 'closed'
        run: |
          mkdir -p docs/pr-changes
          git diff --name-only origin/${{ github.base_ref }}...HEAD > /tmp/changed-files.txt
          ./scripts/detect-pr-changes.sh /tmp/changed-files.txt ${{ github.event.pull_request.number }} ${{ github.base_ref }} > docs/pr-changes/pr-changes.json
          cat docs/pr-changes/pr-changes.json

      - name: Build WasmJS Docs
        if: github.event.action != 'closed'
        run: ./gradlew publishAllWasmJsToDocsWithPrChanges -PbuildWasm=true

      - name: Copy PR preview templates
        if: github.event.action != 'closed'
        run: |
          # Replace standard templates with PR preview templates (4 tabs)
          cp catalog/composeApp/src/wasmJsMain/resources/index-pr-preview.html docs/catalog/index.html
          cp catalog-chat/composeApp/src/wasmJsMain/resources/index-pr-preview.html docs/catalog-chat/index.html 2>/dev/null || true
          # Update landing page nav (if exists)
          if [ -f docs/index.html ]; then
            sed -i 's|</div>\s*</nav>|<a href="./pr-changes/" class="nav-tab">PR Changes</a></div></nav>|' docs/index.html
          fi

      - name: Deploy PR Preview
        uses: rossjrw/pr-preview-action@v1
        with:
          source-dir: ./docs/
          preview-branch: gh-pages
          umbrella-dir: pr-preview
```

**Step 2: Test workflow syntax**

Run: `cat .github/workflows/pr-preview.yml | python3 -c "import sys, yaml; yaml.safe_load(sys.stdin); print('Valid YAML')"`

Expected: "Valid YAML"

**Step 3: Commit**

```bash
git add .github/workflows/pr-preview.yml
git commit -m "feat(ci): integrate PR changes detection into preview workflow

Detects changed components, generates manifest, builds filtered
Catalog, and updates all pages with 4-tab navigation."
```

---

## Task 16: Create PR Preview Template for catalog-chat

**Files:**
- Create: `catalog-chat/composeApp/src/wasmJsMain/resources/index-pr-preview.html`

**Step 1: Check existing catalog-chat template**

Run: `cat catalog-chat/composeApp/src/wasmJsMain/resources/index.html`

**Step 2: Create 4-tab version**

Copy the existing template and add PR Changes tab to nav-tabs (similar to Task 2).

**Step 3: Commit**

```bash
git add catalog-chat/composeApp/src/wasmJsMain/resources/index-pr-preview.html
git commit -m "feat(catalog-chat): add PR preview HTML template

Adds PR Changes tab to Chat demo navigation for PR previews."
```

---

## Task 17: End-to-End Local Test

**Step 1: Simulate changed files**

```bash
echo "arcane-components/src/commonMain/kotlin/io/github/devmugi/arcane/design/components/controls/Button.kt" > /tmp/test-changes.txt
```

**Step 2: Generate manifest**

```bash
mkdir -p docs/pr-changes
./scripts/detect-pr-changes.sh /tmp/test-changes.txt 1 main > docs/pr-changes/pr-changes.json
cat docs/pr-changes/pr-changes.json
```

Expected: JSON with Button in Controls screen

**Step 3: Build all**

```bash
./gradlew publishAllWasmJsToDocsWithPrChanges -PbuildWasm=true
```

Expected: BUILD SUCCESSFUL, docs/pr-changes/ contains index.html and composeApp.js

**Step 4: Verify structure**

```bash
ls -la docs/pr-changes/
```

Expected: index.html, pr-changes.json, composeApp.js, *.wasm

**Step 5: Commit any fixes if needed**

---

## Summary

| Task | Description | Files |
|------|-------------|-------|
| 1 | Detection script | `scripts/detect-pr-changes.sh` |
| 2 | PR preview HTML (catalog) | `catalog/.../index-pr-preview.html` |
| 3 | Data model | `prchanges/PrChangesData.kt` |
| 4 | Serialization dependency | `build.gradle.kts` |
| 5 | Config holder | `prchanges/PrChangesConfig.kt` |
| 6 | Changed badge | `components/ChangedBadge.kt` |
| 7 | Empty state | `components/PrChangesEmptyState.kt` |
| 8 | WasmJS manifest loader | `wasmJsMain/.../ManifestLoader.kt` |
| 9 | Desktop manifest stub | `desktopMain/.../ManifestLoader.kt` |
| 10 | App.kt filtered mode | `App.kt` |
| 11 | Platform navigation | `Navigation.kt` |
| 12 | PR Changes entry point | `PrChangesMain.kt` |
| 13 | Build logic task | `arcane.multiplatform.application.gradle.kts` |
| 14 | Root build task | `build.gradle.kts` |
| 15 | CI workflow | `pr-preview.yml` |
| 16 | catalog-chat template | `catalog-chat/.../index-pr-preview.html` |
| 17 | E2E test | Local verification |
