import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
    application
}

group = "me.alexander"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    val scalaVersion = "2.13"
    val akkaVersion = "2.6.15"

    implementation("com.typesafe.akka", "akka-actor_$scalaVersion", akkaVersion)
    implementation("io.reactivex.rxjava3:rxkotlin:3.0.1")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

