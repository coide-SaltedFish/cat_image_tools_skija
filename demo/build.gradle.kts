import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

group = "org.sereinfish.catcat.image.tools.skiko"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    implementation(project(":"))
    implementation("org.jetbrains.skiko:skiko-awt-runtime-windows-x64:0.7.89.1")
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.6")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}
