plugins {
    alias(libs.plugins.ktlint)
}

ktlint {
    filter {
        exclude("**/generated/**")
    }
}
