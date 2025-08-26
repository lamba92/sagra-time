import gradle.kotlin.dsl.accessors._881290fa1b4709c12738a585e490e04d.docker

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
