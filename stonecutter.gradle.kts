plugins {
    id("dev.kikugie.stonecutter")
    id("net.fabricmc.fabric-loom-remap") version "1.16-SNAPSHOT" apply false
    id("me.modmuss50.mod-publish-plugin") version "1.0.+" apply false
    id("org.moddedmc.wiki.toolkit") version "0.4+" apply false
}

stonecutter active "1.21.4"

stonecutter parameters {
    constants["release"] = providers.gradleProperty("release").map(String::toBoolean).orElse(false).get()
    constants["dev"] = providers.gradleProperty("dev").map(String::toBoolean).orElse(false).get()
    constants["legacy"] = current.parsed < "26"
    dependencies["fapi"] = node.project.property("deps.fabric_api") as String

    replacements {
        string(current.parsed >= "1.20.6") {
            replace("dev.onyxstudios", "org.ladysnake")
        }
    }
}