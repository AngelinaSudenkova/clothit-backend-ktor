
val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposedVersion: String by project
val postgresVersion: String by project
val h2_version: String by project

plugins {
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.serialization") version "1.9.23"
    id("io.ktor.plugin") version "2.3.9"
}

group = "com.clothit"
version = "0.0.1"

application {
    mainClass.set("com.clothit.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")

}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-websockets-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("io.ktor:ktor-server-swagger-jvm")
    implementation("io.ktor:ktor-server-swagger:$ktor_version")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.0")

    implementation("com.google.dagger:dagger:2.46.1")

    implementation("io.ktor:ktor-server-swagger:$ktor_version")
    implementation("io.ktor:ktor-server-cors:$ktor_version")
    implementation("io.ktor:ktor-server-openapi:$ktor_version")


    implementation("io.ktor:ktor-server-sessions-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-crypt:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
    implementation("org.postgresql:postgresql:$postgresVersion")
    implementation("dev.forst:ktor-openapi-generator:0.6.1")
    implementation("io.ktor:ktor-client-core:2.0.3")
    implementation("io.ktor:ktor-client-cio:2.0.3")
    implementation("com.h2database:h2:$h2_version")
    implementation("io.ktor:ktor-client-content-negotiation:2.0.3")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("io.ktor:ktor-server-test-host-jvm:2.3.9")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
    testImplementation("io.ktor:ktor-server-test-host:$ktor_version")

}
