val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm")
    id("io.ktor.plugin")
    kotlin("plugin.serialization")
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}
ktor {
    fatJar {
        archiveFileName.set("fat.jar")
    }
}
group = "org.example"
version = "unspecified"

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

dependencies {
    implementation(project(":core-auth"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-server-auth:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-server-status-pages:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("io.ktor:ktor-server-config-yaml:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.flywaydb:flyway-core:8.5.13")
    implementation("org.postgresql:postgresql:42.7.3")
    implementation("org.ktorm:ktorm-core:4.0.0")
    implementation("org.ktorm:ktorm-support-postgresql:4.0.0")
    implementation("commons-validator:commons-validator:1.9.0")
    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("com.neovisionaries:nv-i18n:1.28")
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")

    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.3.1")
}


tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(11)
}
