plugins {
    id("arcane.multiplatform.application")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":arcane-foundation"))
            implementation(project(":arcane-components"))
            implementation(libs.kotlinx.serialization.json)
        }
    }
}
