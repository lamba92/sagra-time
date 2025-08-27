plugins {
    id("convention-kmp-lib")
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.plugin.compose)
}

kotlin {
    androidLibrary {
        namespace += ".app.core"
    }
}

dependencies {
    commonMainApi(libs.coil.compose)
    commonMainApi(libs.coil.svg)
    commonMainApi(libs.compose.foundation)
    commonMainApi(libs.compose.lifecycle.viewmodel)
    commonMainApi(libs.compose.shimmer)
    commonMainApi(libs.compose.material.icons.extended)
    commonMainApi(libs.compose.material3)
    commonMainApi(libs.compose.navigation)
    commonMainApi(libs.compose.resources)
    commonMainApi(libs.koin.compose.viewmodel)
    commonMainApi(libs.material.kolor)
    commonMainApi(projects.core)
}
