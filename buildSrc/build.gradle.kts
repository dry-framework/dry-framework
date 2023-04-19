plugins {
    `kotlin-dsl`
}

val kotlinVersion: String by project

repositories {
    mavenCentral()
    maven {
        url = uri("https://plugins.gradle.org/m2/")
    }
    gradlePluginPortal()
}

dependencies {
    // ---{ Kotlin }---
    // ================

    // jvm
    // - https://plugins.gradle.org/plugin/org.jetbrains.kotlin.jvm
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlinVersion}")
}
