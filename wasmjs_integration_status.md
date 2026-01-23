# WasmJS Integration Status Report

**Project:** ArcaneDesignSystem
**Date:** 2026-01-24
**Kotlin Version:** 2.3.0
**Compose Multiplatform:** 1.10.0

---

## Executive Summary

WasmJS support is **actively enabled and configured** in the ArcaneDesignSystem project. The infrastructure is in place for both library modules and application modules to target WebAssembly via Kotlin/Wasm.

---

## Configuration Status

### Global Settings (`gradle.properties`)

| Property | Value | Description |
|----------|-------|-------------|
| `buildWasm` | `true` | WasmJS targets enabled by default |
| `kotlin.wasm.stability.nowarn` | `true` | Suppresses WASM stability warnings |

**Override:** Disable with `./gradlew build -PbuildWasm=false`

### Convention Plugins

#### Library Plugin (`arcane.multiplatform.library.gradle.kts`)

```kotlin
if (buildWasm) {
    wasmJs {
        browser()
    }
}
```

- Conditionally enables `wasmJs { browser() }` target
- Supports Maven Local publication for web consumers
- Applied to: `arcane-foundation`, `arcane-components`, `arcane-chat`

#### Application Plugin (`arcane.multiplatform.application.gradle.kts`)

```kotlin
if (buildWasm) {
    wasmJs {
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
            }
        }
        binaries.executable()
    }
}
```

- Webpack configuration for browser bundling
- Output filename: `composeApp.js`
- Creates executable binary
- Applied to: `catalog/composeApp`, `catalog-chat/composeApp`

---

## Module Support Matrix

| Module | WasmJS Target | Source Set | Entry Point |
|--------|---------------|------------|-------------|
| `arcane-foundation` | Library | `commonMain` only | N/A |
| `arcane-components` | Library | `commonMain` only | N/A |
| `arcane-chat` | Library | `commonMain` only | N/A |
| `catalog/composeApp` | Application | `wasmJsMain` | `Main.kt` |
| `catalog-chat/composeApp` | Application | `wasmJsMain` | `Main.kt` |

---

## Entry Points

### Catalog App
**Path:** `catalog/composeApp/src/wasmJsMain/kotlin/io/github/devmugi/arcane/catalog/Main.kt`

### Catalog Chat App
**Path:** `catalog-chat/composeApp/src/wasmJsMain/kotlin/io/github/devmugi/arcane/catalog/chat/Main.kt`

---

## Build Commands

### Development

```bash
# Run catalog in browser (development mode)
./gradlew :catalog:composeApp:wasmJsBrowserDevelopmentRun

# Run catalog-chat in browser (development mode)
./gradlew :catalog-chat:composeApp:wasmJsBrowserDevelopmentRun
```

### Production

```bash
# Build production bundle for catalog
./gradlew :catalog:composeApp:wasmJsBrowserProductionWebpack

# Build production bundle for catalog-chat
./gradlew :catalog-chat:composeApp:wasmJsBrowserProductionWebpack
```

### Build Without WasmJS

```bash
# Skip WasmJS targets entirely
./gradlew build -PbuildWasm=false
```

---

## Build Artifacts

### Output Locations

| App | Development | Production |
|-----|-------------|------------|
| catalog | `catalog/composeApp/build/dist/wasmJs/developmentExecutable/` | `catalog/composeApp/build/dist/wasmJs/productionExecutable/` |
| catalog-chat | `catalog-chat/composeApp/build/dist/wasmJs/developmentExecutable/` | `catalog-chat/composeApp/build/dist/wasmJs/productionExecutable/` |

### Generated Files

- `composeApp.js` - Main JavaScript entry point
- `composeApp.wasm` - WebAssembly binary
- `index.html` - HTML host page
- `*.js` - Supporting JavaScript modules

---

## Dependencies

### Kotlin JS Store

**Path:** `kotlin-js-store/`

Contains cached Kotlin/JS and Kotlin/Wasm dependencies for faster incremental builds.

### Version Catalog (`gradle/libs.versions.toml`)

All Compose Multiplatform dependencies support WasmJS target:

- `org.jetbrains.compose.runtime:runtime`
- `org.jetbrains.compose.ui:ui`
- `org.jetbrains.compose.foundation:foundation`
- `org.jetbrains.compose.material3:material3`
- `org.jetbrains.compose.components:components-resources`

---

## Platform Targets Overview

| Target | Status | Conditional | Default |
|--------|--------|-------------|---------|
| Android | Active | No | Always enabled |
| Desktop (JVM) | Active | No | Always enabled |
| iOS | Optional | Yes (`-PbuildIos=true`) | Disabled |
| WasmJS | Active | Yes (`-PbuildWasm=false` to disable) | Enabled |

---

## Known Considerations

### Browser Compatibility

Kotlin/Wasm requires browsers with WebAssembly GC support:
- Chrome 119+
- Firefox 120+
- Safari 18.2+ (partial)
- Edge 119+

### Performance Notes

- Initial load may be slower than traditional JS due to WASM compilation
- Runtime performance is generally better for compute-intensive operations
- Bundle size is typically larger than equivalent JavaScript

### Compose-Specific

- Canvas-based rendering (Skiko) - not native DOM
- All UI rendered to a single `<canvas>` element
- Text input requires special handling for IME support

---

## Troubleshooting Checklist

### Build Failures

1. Verify `buildWasm=true` in `gradle.properties`
2. Check Kotlin version compatibility (2.3.0 required)
3. Ensure Compose Multiplatform version supports WasmJS (1.10.0+)
4. Run `./gradlew clean` and rebuild

### Runtime Issues

1. Check browser console for JavaScript errors
2. Verify browser supports WebAssembly GC
3. Check network tab for failed resource loads
4. Ensure all assets are properly bundled

### Blank Screen

1. Verify `index.html` loads `composeApp.js`
2. Check for JavaScript exceptions in console
3. Verify canvas element exists in DOM
4. Check for CORS issues if loading from file://

---

## Recent Activity

No WASM-specific commits in recent history. WasmJS support appears stable as infrastructure rather than active development focus.

**Latest relevant commits:**
- `fix(chat): remove onPreviewKeyEvent to fix binary incompatibility crash` - May indicate platform-specific input handling considerations

---

## Recommendations

1. **Testing:** Regularly test WasmJS builds as part of CI pipeline
2. **Browser Matrix:** Document minimum browser versions for end users
3. **Bundle Analysis:** Monitor production bundle size
4. **Fallback Strategy:** Consider providing non-WASM alternative for unsupported browsers

---

## References

- [Kotlin/Wasm Documentation](https://kotlinlang.org/docs/wasm-overview.html)
- [Compose Multiplatform Web](https://www.jetbrains.com/lp/compose-multiplatform/)
- [WebAssembly GC Proposal](https://github.com/WebAssembly/gc)
