import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.21"
}

group = "org.sereinfish.catcat.image.tools.skiko"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val glfwVersion = "3.3.3"

dependencies {
    implementation("org.jetbrains.skiko:skiko-awt-runtime-windows-x64:0.7.93")
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.6")

    implementation("org.lwjgl:lwjgl:$glfwVersion")
    implementation("org.lwjgl:lwjgl-glfw:$glfwVersion")
    implementation("org.lwjgl:lwjgl-opengl:$glfwVersion")

    implementation(group = "org.lwjgl", name = "lwjgl", version = glfwVersion, classifier = "natives-windows")
    implementation(group = "org.lwjgl", name = "lwjgl-glfw", version = glfwVersion, classifier = "natives-windows")
    implementation(group = "org.lwjgl", name = "lwjgl-opengl", version = glfwVersion, classifier = "natives-windows")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}