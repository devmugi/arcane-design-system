# PR Changes Feature Design

**Date:** 2026-01-24
**Status:** Approved

## Overview

When a PR is created, automatically detect which UI components changed and display them in a dedicated "PR Changes" tab on the GitHub Pages preview site.

### Key Decisions

| Decision | Choice |
|----------|--------|
| Detection method | Git diff analysis at build time |
| Scope | Direct source file changes only (no dependency tracking) |
| Display | Filtered version of existing Catalog app |
| Data passing | JSON manifest generated during CI |
| Tab visibility | Only appears on PR preview builds |
| Empty state | Friendly message with link to full Catalog |
| File-to-screen mapping | Convention-based (directory → Catalog screen) |

### User Flow

1. Developer opens PR with component changes
2. CI builds preview and analyzes git diff
3. Preview site shows 4 tabs: Initial Prompt, Catalog, Chat, **PR Changes**
4. Reviewer clicks PR Changes → sees only modified components
5. If no components changed → sees helpful empty state message

## Architecture

### Component Detection Pipeline

```
PR Created/Updated
       ↓
GitHub Actions workflow
       ↓
Git diff: main...HEAD
       ↓
Filter: arcane-components/src/**/components/**/*.kt
       ↓
Map file paths → component names → Catalog screens
       ↓
Generate pr-changes.json
       ↓
Build PR Changes Catalog (filtered mode)
       ↓
Publish to docs/pr-changes/
```

### Directory-to-Screen Mapping

| Directory Path | Catalog Screen |
|---------------|----------------|
| `components/controls/` | Controls |
| `components/navigation/` | Navigation |
| `components/display/` | Data Display |
| `components/feedback/` | Feedback |
| `arcane-foundation/` | Design Spec |

### JSON Manifest Structure

**File:** `docs/pr-changes/pr-changes.json`

```json
{
  "prNumber": 42,
  "baseBranch": "main",
  "changedComponents": [
    {
      "name": "ArcaneButton",
      "screen": "Controls",
      "changeType": "modified",
      "filePath": "arcane-components/.../controls/ArcaneButton.kt"
    }
  ],
  "affectedScreens": ["Controls"],
  "generatedAt": "2026-01-24T12:00:00Z"
}
```

## CI Workflow Changes

### Modified `.github/workflows/pr-preview.yml`

```yaml
jobs:
  build-preview:
    steps:
      # ... existing setup steps ...

      - name: Detect changed components
        id: detect-changes
        run: |
          # Get changed files in PR
          git diff --name-only origin/main...HEAD > changed-files.txt

          # Run detection script
          ./scripts/detect-pr-changes.sh changed-files.txt > docs/pr-changes/pr-changes.json

      - name: Build all WasmJS apps
        run: ./gradlew publishAllWasmJsToDocs -PbuildWasm=true

      # ... existing publish steps ...
```

### Detection Script

**File:** `scripts/detect-pr-changes.sh`

A bash script that:
1. Reads the list of changed files
2. Filters for component source files (`*.kt` in component directories)
3. Extracts component names from file names (e.g., `ArcaneButton.kt` → `ArcaneButton`)
4. Maps directories to Catalog screens
5. Outputs JSON manifest

### Build Output Structure

```
docs/
├── index.html           # Landing page (3 tabs for main, 4 tabs for PR)
├── catalog/
│   ├── index.html       # Full Catalog
│   └── composeApp.js
├── catalog-chat/
│   ├── index.html       # Chat demo
│   └── composeApp.js
└── pr-changes/          # NEW
    ├── index.html       # Filtered Catalog
    ├── pr-changes.json  # Manifest
    └── composeApp.js
```

## Catalog App Changes

### Filtered Mode Configuration

```kotlin
// PrChangesConfig.kt
object PrChangesConfig {
    var isFilteredMode: Boolean = false
    var changedComponents: List<String> = emptyList()
    var affectedScreens: List<String> = emptyList()
}
```

On WasmJS, the app fetches `/pr-changes/pr-changes.json` via JavaScript interop. If the file exists and has content, filtered mode activates.

### UI Behavior

1. **Screen Tabs** - Only show tabs for `affectedScreens`
2. **Component Highlighting** - Visual indicator (badge) for changed components
3. **Empty State** - When no components changed:
   ```
   No component changes in this PR

   This PR doesn't modify any UI components.
   [View Full Catalog →]
   ```

### New Files

```
catalog/composeApp/src/commonMain/kotlin/.../
├── PrChangesConfig.kt        # Config holder
└── components/
    └── ChangedBadge.kt       # Visual indicator
```

## HTML & Navigation

### Template Strategy

Two HTML template variants:

- `index.html` - Standard 3-tab version (for main deployment)
- `index-pr-preview.html` - 4-tab version (copied during PR builds)

### Navigation Structure

**Main deployment (3 tabs):**
```
Arcane | Initial Prompt | Catalog | Chat
```

**PR preview (4 tabs):**
```
Arcane | Initial Prompt | Catalog | Chat | PR Changes
```

### Template Locations

```
catalog/composeApp/src/wasmJsMain/resources/
├── index.html              # Standard template
└── index-pr-preview.html   # PR preview template (4 tabs)
```

PR preview workflow copies `index-pr-preview.html` to all page directories during build.

## Edge Cases

| Scenario | Behavior |
|----------|----------|
| No component files changed | Empty state message displayed |
| New component added | Shows with "added" change type |
| Component deleted | Not shown (nothing to render) |
| Foundation tokens changed | Maps to Design Spec screen |
| Multiple screens affected | All affected screens shown as tabs |

## Testing & Verification

### Local Testing

```bash
# Simulate a PR with changed files
echo "arcane-components/src/.../controls/ArcaneButton.kt" > test-changes.txt
./scripts/detect-pr-changes.sh test-changes.txt
# Should output valid JSON with ArcaneButton in Controls screen
```

### Build Verification

```bash
./gradlew publishAllWasmJsToDocs -PbuildWasm=true
# Check docs/pr-changes/ exists with index.html and composeApp.js
```

### End-to-End Test

1. Open a real PR that modifies a component
2. Verify PR Changes tab appears in preview
3. Verify only changed components are shown
4. Verify empty state works for non-component PRs

## Rollback Safety

Main deployment workflow remains unchanged. PR Changes only affects PR preview builds. If something breaks, PR previews fail but main site stays stable.

## Files to Create/Modify

### New Files
- `scripts/detect-pr-changes.sh` - Component detection script
- `docs/pr-changes/index.html` - PR Changes page template
- `catalog/composeApp/src/wasmJsMain/resources/index-pr-preview.html` - 4-tab template
- `catalog/composeApp/src/commonMain/kotlin/.../PrChangesConfig.kt` - Config holder
- `catalog/composeApp/src/commonMain/kotlin/.../components/ChangedBadge.kt` - Badge component

### Modified Files
- `.github/workflows/pr-preview.yml` - Add detection step
- `build.gradle.kts` - Extend `publishAllWasmJsToDocs` task
- `build-logic/src/main/kotlin/arcane.multiplatform.application.gradle.kts` - PR Changes build task
- `catalog/composeApp/src/commonMain/kotlin/.../App.kt` - Support filtered mode
