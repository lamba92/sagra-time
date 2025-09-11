import com.android.build.gradle.internal.tasks.factory.dependsOn

plugins {
    id("convention-jvm")
    id("distribution-elements")
    application
    `convention-docker`
}

val generatedResourcesDir = layout.buildDirectory.dir("generated/main/resources")

kotlin {
    sourceSets {
        main {
            resources.srcDir(generatedResourcesDir)
        }
    }
}

application {
    mainClass = "it.sagratime.app.web.server.MainKt"
}

docker {
    images {
        main {
            imageName = "sagra-time-webapp-server"
        }
    }
}

dependencies {
    implementation(projects.app.appCore)
    implementation(libs.bundles.ktor.server)
    implementation(libs.bundles.ktor.client)
    implementation(libs.kotlinx.html.jvm)

    runtimeOnly(libs.logback.classic)
    distributions(projects.app.appWebCompose)
    testImplementation(kotlin("test-junit5"))
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.ktor.server.test.host)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.logback.classic)
}

tasks {
    val unpackWebApp by registering(Sync::class) {
        dependsOn(configurations.distributions)
        from(configurations.distributions.map { it.files.map { zipTree(it) } }) {
            eachFile {
                relativePath = RelativePath(true, *relativePath.segments.drop(1).toTypedArray())
            }
        }
        into(generatedResourcesDir)
        exclude("**/*.LICENSE.txt", "**/*.html")
        includeEmptyDirs = false
    }
    processResources.dependsOn(unpackWebApp)
}
