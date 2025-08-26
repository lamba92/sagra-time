@file:Suppress("UnstableApiUsage")

rootProject.name = "sagra-time"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("com.gradle.develocity") version "4.1.1"
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
    }
    rulesMode = RulesMode.PREFER_SETTINGS
    versionCatalogs {
        create("kotlinDocumentStore") {
            from("com.github.lamba92:kotlin-document-store-version-catalog:1.0.4")
        }
    }
}

develocity {
    buildScan {
        termsOfUseUrl = "https://gradle.com/terms-of-service"
        termsOfUseAgree = "yes"
        publishing {
            onlyIf { System.getenv("CI") == "true" }
        }
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(
    ":crawler",
    ":api-server",
    ":core",
    ":app",
    ":app:app-core",
    ":app:app-desktop",
    ":app:app-android",
//    ":app:app-web-angular",
    ":app:app-web-compose",
)
