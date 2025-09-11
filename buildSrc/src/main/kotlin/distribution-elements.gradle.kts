plugins {
    distribution
}

val distributionType = Attribute.of("it.sagratime.distribution", String::class.java)

val distElements by project.configurations.creating {
    isCanBeConsumed = true
    isCanBeResolved = false
    isCanBeDeclared = false
    attributes {
        attribute(distributionType, "zip")
        attribute(ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE, ArtifactTypeDefinition.ZIP_TYPE)
    }
}

project.configurations.create("distributions") {
    isCanBeConsumed = false
    isCanBeResolved = true
    isCanBeDeclared = true
    attributes {
        attribute(distributionType, "zip")
        attribute(ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE, ArtifactTypeDefinition.ZIP_TYPE)
    }
}

artifacts {
    add(distElements.name, tasks.distZip)
}
