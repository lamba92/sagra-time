@file:Suppress("UnstableApiUsage", "OPT_IN_USAGE")

import com.android.build.api.dsl.androidLibrary
import gradle.kotlin.dsl.accessors._dece36eebfa45006e335f49d7d490da6.androidMain
import gradle.kotlin.dsl.accessors._dece36eebfa45006e335f49d7d490da6.commonMain
import gradle.kotlin.dsl.accessors._dece36eebfa45006e335f49d7d490da6.publishing
import gradle.kotlin.dsl.accessors._dece36eebfa45006e335f49d7d490da6.sourceSets
import org.jetbrains.kotlin.gradle.internal.platform.wasm.WasmPlatforms.wasmJs

plugins {
    `maven-publish`
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.plugin.serialization)
    id("convention-ktlint")
    id("convention-version")
}

publishing {
    repositories {
        maven(layout.buildDirectory.dir("mavenTestRepository")) {
            name = "test"
        }
    }
}

kotlin {
    jvmToolchain(11)
    androidLibrary {
        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
        namespace = group.toString()
        compileSdk = 36
        minSdk = 24
    }
    wasmJs {
        browser()
        binaries.executable()
    }
    jvm()
    iosArm64()
    iosSimulatorArm64()
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-sensitive-resolution")
        freeCompilerArgs.add("-Xwhen-guards")
    }
}
