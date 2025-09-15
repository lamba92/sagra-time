@file:Suppress("OPT_IN_USAGE")

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
    jvmToolchain(21)
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
        freeCompilerArgs.add("-Xexpect-actual-classes")
        freeCompilerArgs.add("-Xwhen-guards")
    }
}
