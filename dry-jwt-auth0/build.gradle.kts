plugins {
    id("dry.kotlin")
    id("dry.logging")
}

dependencies {
    implementation(project(":dry-common"))
    implementation(project(":dry-configuration"))
    implementation(project(":dry-dependency"))
    implementation(project(":dry-jwt"))

    // Auth0 JWT
    implementation("com.auth0:java-jwt:3.18.3")
}
