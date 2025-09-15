plugins {
    id("convention-kmp-lib")
}

dependencies {
    commonMainApi(libs.kotlinx.coroutines.core)
    commonMainApi(libs.kotlinx.datetime)
    commonMainApi(libs.kotlinx.serialization.properties)
    commonMainApi(libs.ktor.http)
    commonTestApi(libs.kotlin.test)
    commonTestApi(libs.kotlinx.coroutines.test)
    wasmJsMainApi(libs.kotlinx.browser)
}
