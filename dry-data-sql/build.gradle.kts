plugins {
    id("dry.kotlin")
    id("dry.mokito")
}

dependencies {
    // https://mvnrepository.com/artifact/org.hsqldb/hsqldb
    testImplementation("org.hsqldb:hsqldb:${Versions.hsqldb}")
    // https://mvnrepository.com/artifact/com.h2database/h2
    testImplementation("com.h2database:h2:2.1.214")
}