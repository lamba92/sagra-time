@file:Suppress("UnstableApiUsage")

plugins {
    `convention-version`
    id(libs.plugins.kotlin.jvm)
    id(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.kotlin.plugin.compose)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose.hot.reload)
    application
}

kotlin {
    jvmToolchain {
        vendor = JvmVendorSpec.JETBRAINS
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "it.sagratime.app.desktop.MainKt"
}

dependencies {
    implementation(libs.bundles.compose.desktop)
    implementation(libs.jvm.system.theme.detector)
    implementation(libs.kotlinx.coroutines.swing)
    implementation(libs.logback.classic)
    implementation(projects.app.appCore)
}
