import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.android.library")
    id("io.github.takahirom.roborazzi")
}

val libs = versionCatalogs.named("libs")

android {
    namespace = "io.github.devmugi.arcane.design.screenshots"
    compileSdk = 35

    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            all {
                it.systemProperties["robolectric.graphicsMode"] = "NATIVE"
            }
        }
    }
}

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

    sourceSets {
        val commonMain by getting

        val androidUnitTest by getting {
            dependencies {
                implementation(libs.findLibrary("kotlin-test").get())
                implementation(libs.findLibrary("kotest-assertions-core").get())
                implementation(libs.findLibrary("compose-ui-test").get())
                implementation(libs.findLibrary("compose-ui-test-junit4").get())
                implementation(libs.findLibrary("androidx-compose-ui-test-manifest").get())
                implementation(libs.findLibrary("roborazzi").get())
                implementation(libs.findLibrary("roborazzi-compose").get())
                implementation(libs.findLibrary("roborazzi-junit-rule").get())
                implementation(libs.findLibrary("robolectric").get())
            }
        }

        val desktopTest by getting {
            dependencies {
                implementation(libs.findLibrary("kotlin-test").get())
                implementation(libs.findLibrary("kotest-assertions-core").get())
                implementation(libs.findLibrary("compose-ui-test").get())
                implementation(libs.findLibrary("compose-ui-test-junit4").get())
                // Note: Roborazzi is Android-only. Desktop screenshot testing uses
                // Compose Multiplatform's built-in screenshot testing or Skiko.
                implementation(compose.desktop.currentOs)
            }
        }
    }
}
