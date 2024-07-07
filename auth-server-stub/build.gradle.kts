val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm")
    kotlin("plugin.serialization")
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

group = "org.example.auth"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":core-auth"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-server-auth:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("io.ktor:ktor-server-config-yaml:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(11)
}
