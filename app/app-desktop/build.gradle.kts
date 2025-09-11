@file:Suppress("UnstableApiUsage")

plugins {
    id("convention-jvm")
    alias(libs.plugins.kotlin.plugin.compose)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose.hot.reload)
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
