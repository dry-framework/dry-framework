plugins {
    id("dry.kotlin")
    id("dry.logging")
}

dependencies {
    implementation(project(":dry-common"))
    implementation(project(":dry-dependency"))
    implementation(project(":dry-validation"))
    implementation(project(":dry-security"))
    api(project(":dry-configuration"))
    api(project(":dry-http"))
    api(project(":dry-jwt"))

    testImplementation(project(":dry-common-jackson"))
}
