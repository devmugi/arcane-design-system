plugins {
    id("arcane.multiplatform.screenshots")
}

android {
    namespace = "io.github.devmugi.arcane.chat.screenshots"
}

kotlin {
    sourceSets {
        val androidUnitTest by getting {
            dependencies {
                implementation(project(":arcane-foundation"))
                implementation(project(":arcane-components"))
                implementation(project(":arcane-chat"))
                implementation(compose.material3)
            }
        }

        val desktopTest by getting {
            dependencies {
                implementation(project(":arcane-foundation"))
                implementation(project(":arcane-components"))
                implementation(project(":arcane-chat"))
                implementation(compose.material3)
            }
        }
    }
}
