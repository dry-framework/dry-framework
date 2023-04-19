plugins {
    id("dry.kotlin")
    id("dry.jackson")
}

dependencies {
    implementation(project(":dry-common"))
    implementation(project(":dry-dependency"))
}
