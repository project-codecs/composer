    plugins {
    id("dev.kikugie.stonecutter")
    id("fabric-loom") version "1.10-SNAPSHOT" apply false
    id("me.modmuss50.mod-publish-plugin") version "1.0.+" apply false
}

stonecutter active "1.21.4"

stonecutter parameters {
    swaps["mod_version"] = "\"${property("mod.version")}\";"
    swaps["minecraft"] = "\"${node.metadata.version}\";"
    constants["release"] = true
    dependencies["fapi"] = node.project.property("deps.fabric_api") as String

    replacements {

    }
}
