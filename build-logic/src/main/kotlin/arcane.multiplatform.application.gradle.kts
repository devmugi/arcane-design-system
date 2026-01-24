import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.application")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

val libs = versionCatalogs.named("libs")

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

    // iOS targets - only configure if explicitly enabled
    val buildIos = project.findProperty("buildIos")?.toString()?.toBoolean() ?: false
    if (buildIos) {
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
    }

    // WebAssembly target - only configure if explicitly enabled
    val buildWasm = project.findProperty("buildWasm")?.toString()?.toBoolean() ?: false
    if (buildWasm) {
        @OptIn(ExperimentalWasmDsl::class)
        wasmJs {
            browser {
                commonWebpackConfig {
                    outputFileName = "composeApp.js"
                }
            }
            binaries.executable()
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.findLibrary("compose-runtime").get())
            implementation(libs.findLibrary("compose-foundation").get())
            implementation(libs.findLibrary("compose-material3").get())
            implementation(libs.findLibrary("compose-ui").get())
            implementation(libs.findLibrary("compose-material-icons-core").get())
            implementation(libs.findLibrary("compose-material-icons-extended").get())
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

// Task to copy production wasmJs build to docs/ for GitHub Pages
val buildWasmForTask = project.findProperty("buildWasm")?.toString()?.toBoolean() ?: false
if (buildWasmForTask) {
    tasks.register<Copy>("publishWasmJsToDocs") {
        group = "distribution"
        description = "Copies production wasmJs build to docs folder for GitHub Pages"

        dependsOn("wasmJsBrowserProductionWebpack")

        // Determine target folder based on project path (catalog or catalog-chat)
        val targetFolder = when {
            project.path.contains("catalog-chat") -> "catalog-chat"
            else -> "catalog"
        }

        // Copy webpack output (JS, WASM files)
        from(layout.buildDirectory.dir("kotlin-webpack/wasmJs/productionExecutable"))
        // Copy index.html from resources
        from("src/wasmJsMain/resources")
        into(rootProject.layout.projectDirectory.dir("docs/$targetFolder"))
    }

    // Task to copy production wasmJs build to docs/pr-changes for PR previews
    // Only register for catalog project (not catalog-chat)
    val isCatalogProject = project.path.contains("catalog") && !project.path.contains("catalog-chat")
    if (isCatalogProject) {
        tasks.register<Copy>("publishPrChangesWasmJsToDocs") {
            group = "distribution"
            description = "Copies production wasmJs build to docs/pr-changes folder for PR previews"

            dependsOn("wasmJsBrowserProductionWebpack")

            // Copy webpack output (JS, WASM files)
            from(layout.buildDirectory.dir("kotlin-webpack/wasmJs/productionExecutable"))
            // Copy PR preview template
            from("src/wasmJsMain/resources/index-pr-preview.html") {
                rename { "index.html" }
            }
            into(rootProject.layout.projectDirectory.dir("docs/pr-changes"))
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
