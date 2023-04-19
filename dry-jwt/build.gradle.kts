plugins {
    id("dry.kotlin")
    id("dry.jackson")
}

dependencies {
    implementation(project(":dry-common"))
    api(project(":dry-security"))
}
