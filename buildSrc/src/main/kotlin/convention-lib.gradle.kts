@file:Suppress("UnstableApiUsage")

import com.android.build.api.dsl.androidLibrary

plugins {
    `maven-publish`
    id("com.android.kotlin.multiplatform.library")
    id("convention-ktlint")
    kotlin("multiplatform")
    kotlin("plugin.serialization")
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
        namespace = "io.gituhb.lamba92.corpore"
        compileSdk = 36
        minSdk = 24
    }
    jvm()
    iosArm64()
    iosSimulatorArm64()
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-sensitive-resolution")
    }
}

