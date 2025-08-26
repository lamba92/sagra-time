plugins {
    id("convention-kmp-lib")
}

kotlin {
    androidLibrary {
        namespace += ".app.core"
    }
}

dependencies {
    commonMainApi(projects.core)
    commonMainApi(libs.coil.compose)
    commonMainApi(libs.coil.svg)
    commonMainApi(libs.compose.foundation)
    commonMainApi(libs.compose.lifecycle.viewmodel)
    commonMainApi(libs.compose.material3)
    commonMainApi(libs.compose.navigation)
    commonMainApi(libs.compose.resources)
    commonMainApi(libs.koin.compose.viewmodel)
    commonMainApi(libs.material.kolor)
}
