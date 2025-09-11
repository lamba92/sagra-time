@file:Suppress("OPT_IN_USAGE")

plugins {
    `convention-version`
    id(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.plugin.compose)
    id(libs.plugins.kotlin.plugin.serialization)
}

kotlin {
    wasmJs {
        browser()
        binaries.executable()
    }
}

dependencies {
    commonMainApi(projects.app.appCore)
    commonMainApi(libs.compose.runtime)
}
