import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    id("kotlin")
    kotlin("jvm")

    `java-library`
    `maven-publish`
}

group = "dev.dry"
version = "0.0.1"

publishing {
    repositories {
        maven {
            name = "DRY-Framework"
            url = uri("https://dry-framework@maven.pkg.github.com/dry-framework/dry-framework")
            credentials {
                username = System.getenv("DRY_USERNAME") // project.findProperty("gpr.user") as String? ?:
                password = System.getenv("DRY_TOKEN") // project.findProperty("gpr.key") as String? ?:
            }
        }
    }
    publications {
        register<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

// https://mvnrepository.com/artifact/org.slf4j/slf4j-api
extra["sl4jVersion"] = "1.7.33"

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
	mavenCentral()
	gradlePluginPortal()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}")

    testImplementation(platform("org.junit:junit-bom:${Versions.junit}"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
    // https://mvnrepository.com/artifact/org.assertj/assertj-core
    testImplementation("org.assertj:assertj-core:3.24.2")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.test {
    useJUnitPlatform()
}
