pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie Snapshots" }
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.9.4"
}

stonecutter {
    create(rootProject) {
        versions("1.20.1", "1.20.4", "1.20.6", "1.21", "1.21.3", "1.21.4", "1.21.5", "1.21.6", "1.21.10", "1.21.11")
        versions("26.1.2", "26.2") // Risky versions, as they don't run in dev env and I don't trust them. Will be published as beta
        vcsVersion = "1.21.4"
    }
}

rootProject.name = "Composer"