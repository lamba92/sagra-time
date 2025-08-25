import com.github.gradle.node.npm.task.NpxTask

plugins {
    `convention-version`
    id(libs.plugins.lamba.docker)
    id(libs.plugins.node)
    distribution
}

node {
    version = "22.18.0"
    download = true
}

val ngBuildProduction by tasks.registering(NpxTask::class) {
    dependsOn(tasks.npmInstall)
    group = "angular"
    command = "ng"
    args = listOf("build")
    inputs.files("package.json", "package-lock.json", "tsconfig.json", "tsconfig.app.json")
    inputs.dir("src")
    inputs.dir(fileTree("node_modules") { exclude(".cache") })
    outputs.dir("dist")
}

tasks.register<NpxTask>("nglintCheck") {
    dependsOn(tasks.npmInstall)
    group = "angular"
    command = "ng"
    args = listOf("lint")
}

tasks.register<NpxTask>("nglintFormat") {
    dependsOn(tasks.npmInstall)
    group = "angular"
    command = "ng"
    args = listOf("lint", "--fix")
}

distributions {
    main {
        contents {
            from(ngBuildProduction) {
                eachFile {
                    // remove the first path segment (the app name from ng build)
                    relativePath = RelativePath(
                        true,
                        *relativePath.segments.drop(1).toTypedArray()
                    )
                }
                includeEmptyDirs = false
            }
        }
    }
}

docker {
    images {
        main {
            imageName = "sagra-bot-spa-server"
            files {
                from(tasks.installDist)
                from("Dockerfile")
            }
        }
    }
}
