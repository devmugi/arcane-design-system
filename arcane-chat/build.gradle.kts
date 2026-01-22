plugins {
    id("arcane.multiplatform.library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":arcane-foundation"))
            implementation(project(":arcane-components"))
        }
    }
}
