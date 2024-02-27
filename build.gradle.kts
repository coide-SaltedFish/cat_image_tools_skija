import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"

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

    implementation("ch.qos.logback:logback-classic:1.4.14")

    implementation("org.lwjgl:lwjgl:$glfwVersion")
    implementation("org.lwjgl:lwjgl-glfw:$glfwVersion")
    implementation("org.lwjgl:lwjgl-opengl:$glfwVersion")

    implementation(group = "org.lwjgl", name = "lwjgl", version = glfwVersion, classifier = "natives-windows")
    implementation(group = "org.lwjgl", name = "lwjgl-glfw", version = glfwVersion, classifier = "natives-windows")
    implementation(group = "org.lwjgl", name = "lwjgl-opengl", version = glfwVersion, classifier = "natives-windows")

    implementation("org.lwjgl:lwjgl:3.3.3")
    implementation("org.lwjgl:lwjgl-glfw:3.3.3")
    implementation("org.lwjgl:lwjgl-opengl:3.3.3")
    implementation("org.lwjgl:lwjgl-opengles:3.3.3")

    implementation(group = "org.lwjgl", name = "lwjgl", version = "3.3.3", classifier = "natives-windows")
    implementation(group = "org.lwjgl", name = "lwjgl-glfw", version = "3.3.3", classifier = "natives-windows")
    implementation(group = "org.lwjgl", name = "lwjgl-opengl", version = "3.3.3", classifier = "natives-windows")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}