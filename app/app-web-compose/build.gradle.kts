@file:Suppress("OPT_IN_USAGE")

plugins {
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.plugin.compose)
    id("convention-version")
    id("distribution-elements")
    id(libs.plugins.kotlin.multiplatform)
    id(libs.plugins.kotlin.plugin.serialization)
}

kotlin {
    wasmJs {
        browser()
        binaries.executable()
    }
}

distributions {
    main {
        contents {
            from(tasks.named("wasmJsBrowserDistribution"))
            includeEmptyDirs = false
        }
    }
}

dependencies {
    commonMainApi(projects.app.appCore)
    commonMainApi(libs.compose.runtime)
}
