plugins {
    id("arcane.multiplatform.uitests")
}

kotlin {
    sourceSets {
        val desktopTest by getting {
            dependencies {
                implementation(project(":arcane-foundation"))
                implementation(project(":arcane-components"))
            }
        }
    }
}
