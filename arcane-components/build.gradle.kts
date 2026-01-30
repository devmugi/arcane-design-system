plugins {
    id("arcane.multiplatform.library")
    alias(libs.plugins.skie)
    alias(libs.plugins.kmmbridge)
}

kotlin {
    // Configure framework settings for existing iOS targets from convention plugin
    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>()
        .matching { it.konanTarget.family.isAppleFamily }
        .configureEach {
            binaries.withType<org.jetbrains.kotlin.gradle.plugin.mpp.Framework>().configureEach {
                baseName = "ArcaneComponents"
                export(project(":arcane-foundation"))
            }
        }

    sourceSets {
        commonMain.dependencies {
            api(project(":arcane-foundation"))
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

kmmbridge {
    mavenPublishArtifacts()
    spm {
        iOS { v("15") }
    }
}

skie {
    build {
        // Enable library mode for XCFramework distribution
        produceDistributableFramework()
    }
}
