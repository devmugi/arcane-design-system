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
