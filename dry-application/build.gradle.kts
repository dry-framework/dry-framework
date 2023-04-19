plugins {
    id("dry.kotlin")
    id("dry.logging")
}

dependencies {
    implementation(project(":dry-common"))
    api(project(":dry-configuration"))
    api(project(":dry-dependency"))
}
