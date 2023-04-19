plugins {
    id("dry.kotlin")
}

dependencies {
    // https://mvnrepository.com/artifact/org.slf4j/slf4j-api
    // implementation("org.slf4j:slf4j-api:${sl4jVersion}")
    // https://mvnrepository.com/artifact/ch.qos.logback/logback-classic
    implementation("ch.qos.logback:logback-classic:${Versions.logback}")
}
