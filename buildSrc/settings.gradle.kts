@file:Suppress("UnstableApiUsage")

plugins {
    id("dev.panuszewski.typesafe-conventions") version "0.7.4"
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
    rulesMode = RulesMode.PREFER_SETTINGS
}

rootProject.name = "buildSrc"
