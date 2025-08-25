import gradle.kotlin.dsl.accessors._881290fa1b4709c12738a585e490e04d.docker

plugins {
    id("convention-version")
    id("io.github.lamba92.docker")
}

docker {
    registries {
        register("test") {
            imageTagPrefix = "localhost:5000"
        }
    }
}
