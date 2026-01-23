plugins {
    id("arcane.multiplatform.application")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":arcane-foundation"))
            implementation(project(":arcane-components"))
            implementation(project(":arcane-chat"))
            implementation(libs.kotlinx.datetime)
        }
    }
}

android {
    namespace = "io.github.devmugi.arcane.catalog.chat"

    defaultConfig {
        applicationId = "io.github.devmugi.arcane.catalog.chat"
    }

    lint {
        abortOnError = false
    }
}

compose.desktop {
    application {
        mainClass = "io.github.devmugi.arcane.catalog.chat.MainKt"
    }
}
