// build.gradle.kts
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.compose.compiler) apply false
}

// Convenience task to publish all wasmJs apps to docs folder
tasks.register("publishAllWasmJsToDocs") {
    group = "distribution"
    description = "Publishes all wasmJs catalog apps to docs folder for GitHub Pages"
    dependsOn(
        ":catalog:composeApp:publishWasmJsToDocs",
        ":catalog-chat:composeApp:publishWasmJsToDocs"
    )
}

// Convenience task to publish all wasmJs apps including PR changes to docs folder
tasks.register("publishAllWasmJsToDocsWithPrChanges") {
    group = "distribution"
    description = "Publishes all wasmJs catalog apps including PR changes to docs folder"
    dependsOn(
        ":catalog:composeApp:publishWasmJsToDocs",
        ":catalog:composeApp:publishPrChangesWasmJsToDocs",
        ":catalog-chat:composeApp:publishWasmJsToDocs"
    )
}
