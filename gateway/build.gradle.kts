plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.3.5"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "seungkyu"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}

dependencyManagement{
    imports{
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.3")
    }
}