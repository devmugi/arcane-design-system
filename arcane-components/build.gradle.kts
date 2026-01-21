plugins {
    id("arcane.multiplatform.library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":arcane-foundation"))
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}
