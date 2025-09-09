plugins {
    distribution
    id(libs.plugins.ktlint)
    `convention-docker`
}

distributions {
    main {
        contents {
            from("src/main/python") {
                into("app")
            }
            from("requirements.txt")
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
                from("requirements.txt")
            }
        }
    }
}
