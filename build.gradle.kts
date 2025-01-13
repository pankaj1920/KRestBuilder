import java.net.URI

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("org.jetbrains.intellij") version "1.17.4"
}

group = "com.app"
version = "1.0-SNAPSHOT"

repositories {
//    maven { url = URI.create("https://packages.jetbrains.team/maven/p/ij/intellij-dependencies") }
    mavenCentral()

}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2023.2.6")
    type.set("IC") // Target IDE Platform

    plugins.set(listOf("android", "org.jetbrains.kotlin"))
}

tasks {
    // Set the JVM compatibility versions
    runIde {
        ideDir.set(file("/Applications/Android Studio.app/Contents"))
    }

    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("232")
        untilBuild.set("242.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }

    dependencies {
        implementation("com.squareup.okhttp3:okhttp:4.11.0") // HTTP requests
        implementation("com.google.code.gson:gson:2.10.1") // JSON handling
        implementation("com.google.code.gson:gson:2.10.1") // JSON handling

        implementation("io.ktor:ktor-client-content-negotiation:3.0.3")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
        implementation("io.ktor:ktor-client-core:3.0.3")
        implementation("io.ktor:ktor-client-logging:3.0.3")
        implementation("io.ktor:ktor-client-cio:3.0.3")
        implementation("io.ktor:ktor-serialization-kotlinx-json:3.0.3")
        implementation("org.slf4j:slf4j-simple:2.0.9")
//        implementation ("com.jetbrains.intellij.platform:ui-dsl:1.0.0")


    }


}
