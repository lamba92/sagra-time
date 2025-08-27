plugins {
    alias(libs.plugins.lamba.docker)
    id("convention-version")
}

docker {
    registries {
        register("test") {
            imageTagPrefix = "localhost:5000"
        }
    }
}
