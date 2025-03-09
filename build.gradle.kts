plugins {
    kotlin("jvm") version "2.1.10"
    kotlin("plugin.spring") version "1.9.0"
    id("org.springframework.boot") version "3.1.4"
    application
}

group = "supervisor"
version = "3.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // Spring
    implementation("org.springframework.boot:spring-boot-starter-web:3.4.3")
    implementation("org.springframework.boot:spring-boot-starter-actuator:3.4.3")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.4.3")
    implementation("org.springframework.boot:spring-boot-starter-graphql:3.4.3")
    implementation("org.springframework.boot:spring-boot-starter-security:3.4.3")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client:3.4.3")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server:3.4.3")
    implementation("org.springframework.security:spring-security-oauth2-jose:6.4.3")
    implementation("org.springframework.security:spring-security-crypto:6.4.3")

    // Database
    implementation("org.postgresql:postgresql:42.7.5")
    implementation("org.hibernate.validator:hibernate-validator:9.0.0.CR1")

    // Other
    implementation("com.google.guava:guava:33.4.0-jre")
    implementation("jakarta.mail:jakarta.mail-api:2.1.3")
}

application {
    mainClass.set("supervisor.MainKt")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(23)
}