plugins {
    id("dry.kotlin")
    id("dry.jackson")
    id("dry.jackson-annotations")
}

dependencies {
    implementation(project(":dry-common"))
}
