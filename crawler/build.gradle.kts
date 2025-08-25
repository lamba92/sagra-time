plugins {
    `convention-version`
    distribution
    id(libs.plugins.ktlint)
    id(libs.plugins.lamba.docker)
}

distributions {
    main {
        contents {
            from("src/main/python") {
                into("app")

            }
        }
    }
}

docker {
    images {
        main {
            imageName = "sagra-bot-crawler"
            files {
                from(tasks.installDist)
                from("Dockerfile")
            }
        }
    }
}
