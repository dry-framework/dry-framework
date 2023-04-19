plugins {
    id("dry.kotlin")
    id("dry.logging")
}

dependencies {
    implementation(project(":dry-common"))
    implementation(project(":dry-data"))

    // Jakarta Validation
    // https://mvnrepository.com/artifact/jakarta.validation/jakarta.validation-api
    api("jakarta.validation:jakarta.validation-api:3.0.2")
    // https://mvnrepository.com/artifact/org.glassfish/jakarta.el
    implementation("org.glassfish:jakarta.el:4.0.2")
    // https://mvnrepository.com/artifact/org.hibernate.validator/hibernate-validator
    implementation("org.hibernate.validator:hibernate-validator:8.0.0.Final")

    // Google Libphonenumber
    // https://mvnrepository.com/artifact/com.googlecode.libphonenumber/libphonenumber
    implementation("com.googlecode.libphonenumber:libphonenumber:8.13.9")
}