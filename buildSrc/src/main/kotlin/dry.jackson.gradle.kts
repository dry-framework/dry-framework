plugins {
    id("dry.kotlin")
    id("dry.jackson-annotations")
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-core:${Versions.jackson}")
    implementation("com.fasterxml.jackson.core:jackson-databind:${Versions.jackson}")
    // https://mvnrepository.com/artifact/com.fasterxml.jackson.module/jackson-module-kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${Versions.jackson}")
    // https://mvnrepository.com/artifact/com.fasterxml.jackson.datatype/jackson-datatype-jsr310
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${Versions.jackson}")
}
