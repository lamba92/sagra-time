import dev.panuszewski.gradle.pluginMarker

plugins {
    `kotlin-dsl`
    kotlin("plugin.serialization") version embeddedKotlinVersion
}

dependencies {
    implementation(pluginMarker(libs.plugins.kotlin.multiplatform))
    implementation(pluginMarker(libs.plugins.android.multiplatform.library))
    implementation(pluginMarker(libs.plugins.kotlin.plugin.serialization))
    implementation(pluginMarker(libs.plugins.kotlin.plugin.compose))
    implementation(pluginMarker(libs.plugins.jetbrains.compose))
    implementation(pluginMarker(libs.plugins.ktlint))
    implementation(pluginMarker(libs.plugins.lamba.docker))
    implementation(pluginMarker(libs.plugins.node))
    implementation(libs.kotlinx.serialization.json)
}