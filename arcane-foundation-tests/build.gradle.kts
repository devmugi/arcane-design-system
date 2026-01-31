plugins {
    id("arcane.multiplatform.tests")
}

kotlin {
    sourceSets {
        commonTest.dependencies {
            implementation(project(":arcane-foundation"))
            implementation(libs.compose.ui)
        }
    }
}
