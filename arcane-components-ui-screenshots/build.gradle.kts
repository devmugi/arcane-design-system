plugins {
    id("arcane.multiplatform.screenshots")
}

android {
    namespace = "io.github.devmugi.arcane.design.screenshots.components"
}

kotlin {
    sourceSets {
        val androidUnitTest by getting {
            dependencies {
                implementation(project(":arcane-foundation"))
                implementation(project(":arcane-components"))
                implementation(libs.compose.material3)
            }
        }

        val desktopTest by getting {
            dependencies {
                implementation(project(":arcane-foundation"))
                implementation(project(":arcane-components"))
                implementation(libs.compose.material3)
            }
        }
    }
}
