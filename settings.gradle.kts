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
    id("dev.kikugie.stonecutter") version "0.9+"
}

stonecutter {
    create(rootProject) {
        versions("1.20.1", "1.20.4", "1.20.6", "1.21", "1.21.3", "1.21.4", "1.21.5", "1.21.6", "1.21.10", "1.21.11")
//        version("26.1.2") // Disabled for now as it does not build in dev env and I don't trust it
        vcsVersion = "1.21.4"
    }
}

rootProject.name = "Composer"