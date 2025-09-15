plugins {
    id("convention-jvm")
    application
    `convention-docker`
}

application {
    mainClass = "it.sagratime.server.MainKt"
}

docker {
    images {
        main {
            imageName = "sagra-time-api-server"
        }
    }
}

dependencies {
    implementation(projects.core)
    implementation(kotlinDocumentStore.leveldb)
    implementation(libs.bundles.ktor.server)
    implementation(libs.bundles.ktor.client)

    runtimeOnly(libs.logback.classic)

    testImplementation(kotlin("test-junit5"))
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.ktor.server.test.host)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.logback.classic)
}

tasks {
    withType<Test> {
        useJUnitPlatform()
        environment(
            "DB_PATH",
            project.layout.buildDirectory
                .dir("test-db")
                .get()
                .asFile.absolutePath,
        )
    }
    named<JavaExec>("run") {
        environment(
            "DB_PATH",
            project.layout.buildDirectory
                .dir("db")
                .get()
                .asFile.absolutePath,
        )
    }
}
