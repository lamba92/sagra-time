plugins {
    id("convention-kmp-lib")
}

dependencies {
    commonMainApi(libs.kotlinx.coroutines.core)
    commonMainApi(libs.kotlinx.serialization.core)
    commonMainApi(libs.kotlinx.datetime)
}
