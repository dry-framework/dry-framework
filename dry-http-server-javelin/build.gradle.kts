plugins {
    id("dry.kotlin")
    id("dry.logging")
}

dependencies {
    implementation(project(":dry-common"))
    implementation(project(":dry-application"))
    api(project(":dry-http-server"))

    implementation("io.javalin:javalin:5.4.2")
}