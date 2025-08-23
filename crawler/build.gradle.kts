plugins {
    `convention-version`
    id(libs.plugins.lamba.docker)
    distribution
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
