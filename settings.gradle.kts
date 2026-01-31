pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
    }
}

rootProject.name = "ArcaneDesignSystem"

include(":arcane-foundation")
include(":arcane-components")
include(":arcane-chat")
include(":arcane-foundation-tests")
include(":arcane-components-tests")
include(":arcane-components-ui-tests")
include(":arcane-chat-tests")
include(":arcane-chat-ui-tests")
include(":arcane-components-ui-screenshots")
include(":arcane-chat-ui-screenshots")
include(":catalog:composeApp")
include(":catalog-chat:composeApp")
