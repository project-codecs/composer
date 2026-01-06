plugins {
    id("com.modrinth.minotaur") version "2+"
    id("me.modmuss50.mod-publish-plugin")
    id("net.fabricmc.fabric-loom-remap")

    id("maven-publish")
    id("java")
}

@Suppress("UnstableApiUsage")
val buildNum: String =
    providers.environmentVariable("GITHUB_RUN_NUMBER")
        .filter { it.isNotBlank() }
        .map { "build.$it" }
        .orElse("local")
        .get()

version = "${property("mod.version")}+mc${sc.current.version}"
base.archivesName = property("mod.archives_base_name").toString()

val requiredJava = when {
    sc.current.parsed >= "1.20.6" -> JavaVersion.VERSION_21
    sc.current.parsed >= "1.18" -> JavaVersion.VERSION_17
    sc.current.parsed >= "1.17" -> JavaVersion.VERSION_16
    else -> JavaVersion.VERSION_1_8
}

fun env(key: String): String {
    return providers.environmentVariable(key).orElse("").get()
}

fun present(key: String): Boolean {
    return providers.environmentVariable(key).isPresent
}

repositories {
    mavenCentral()
    mavenLocal()
    fun strictMaven(url: String, alias: String, vararg groups: String) = exclusiveContent {
        forRepository { maven(url) { name = alias } }
        filter { groups.forEach(::includeGroup) }
    }
    strictMaven("https://dl.cloudsmith.io/public/lilbrocodes/constructive/maven/", "Constructive Maven")
    strictMaven("https://www.cursemaven.com", "CurseForge", "curse.maven")
    strictMaven("https://api.modrinth.com/maven", "Modrinth", "maven.modrinth")
    strictMaven("https://maven.ladysnake.org/releases", "Ladysnake")
    strictMaven("https://maven.fzzyhmstrs.me/", "FZZY Maven")
    strictMaven("https://maven.terraformersmc.com/", "Terraformers")
    strictMaven("https://maven.nucleoid.xyz/", "Nucleoid")
}

dependencies {
    fun cca(): Any? {
        return property("deps.cca_source")
    }

    minecraft("com.mojang:minecraft:${sc.current.version}")
    mappings("net.fabricmc:yarn:${property("deps.yarn")}:v2")
    modImplementation("net.fabricmc:fabric-loader:${property("deps.fabric_loader")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("deps.fabric_api")}")

    modImplementation("me.fzzyhmstrs:fzzy_config:${property("deps.fzzy")}")

    modImplementation("${cca()}.cardinal-components-api:cardinal-components-base:${property("deps.cca")}")
    modImplementation("${cca()}.cardinal-components-api:cardinal-components-entity:${property("deps.cca")}")

    include("${cca()}.cardinal-components-api:cardinal-components-base:${property("deps.cca")}")
    include("${cca()}.cardinal-components-api:cardinal-components-entity:${property("deps.cca")}")
    modRuntimeOnly("${cca()}.cardinal-components-api:cardinal-components-api:${property("deps.cca")}")
//    modLocalRuntime("com.terraformersmc:modmenu:${property("r.deps.mod_menu")}")

    implementation("org.lilbrocodes:constructive-core:${property("constructive_version")}")
    include("org.lilbrocodes:constructive-core:${property("constructive_version")}")
    annotationProcessor("org.lilbrocodes:constructive-processor:${property("constructive_version")}")

    testImplementation(platform("org.junit:junit-bom:${property("junit_version")}"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:${property("mockito_version")}")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

loom {
    fabricModJsonPath = rootProject.file("src/main/resources/fabric.mod.json")
    accessWidenerPath = project.file("src/main/resources/${property("mod.mc_title")}.accesswidener")

    decompilerOptions.named("vineflower") {
        options.put("mark-corresponding-synthetics", "1")
    }

    runConfigs.all {
        ideConfigGenerated(true)
        runDir = "../../run"
    }
}

fabricApi {
    configureDataGeneration {
        client = true
    }
}

java {
    withSourcesJar()
    targetCompatibility = requiredJava
    sourceCompatibility = requiredJava
}

val canPublish = present("CLOUDSMITH_USERNAME") && present("CLOUDSMITH_API_KEY")
tasks {
    processResources {
        inputs.property("id", project.property("mod.id"))
        inputs.property("name", project.property("mod.name"))
        inputs.property("version", project.property("mod.version"))
        inputs.property("minecraft", project.property("mod.mc_dep"))
        inputs.property("loader", project.property("deps.fabric_loader"))
        inputs.property("cca", project.property("deps.cca"))
        inputs.property("fapi", project.property("deps.fabric_api"))
        inputs.property("aw", project.property("mod.mc_title"))

        val props = mapOf(
            "id" to project.property("mod.id"),
            "name" to project.property("mod.name"),
            "version" to project.property("mod.version"),
            "minecraft" to project.property("mod.mc_dep"),
            "loader" to project.property("deps.fabric_loader"),
            "cca" to project.property("deps.cca"),
            "fapi" to project.property("deps.fabric_api"),
            "aw" to project.property("mod.mc_title")
        )

        filesMatching("fabric.mod.json") { expand(props) }

        val mixinJava = "JAVA_${requiredJava.majorVersion}"
        filesMatching("*.mixins.json") { expand("java" to mixinJava) }
    }

    register<Delete>("cleanArtifacts") {
        delete(rootProject.file("artifacts"))
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        from(remapJar.map { it.archiveFile }, remapSourcesJar.map { it.archiveFile })
        into(rootProject.layout.buildDirectory.file("libs/${project.property("mod.version")}"))
        dependsOn("build")
    }

    register<Copy>("collectArtifacts") {
        group = "build"
        dependsOn("buildAndCollect")

        from(remapJar.map { it.archiveFile })
        into(rootProject.file("artifacts"))
    }

    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    withType<PublishToMavenRepository>().configureEach {
        onlyIf {
            if (!canPublish) {
                logger.lifecycle(
                    """
                Running a cloudsmith dry-run because no username or API key provided for ${project.path}. 
                Would publish:
                  groupId: ${project.group}
                  artifactId: ${base.archivesName.get()}
                  version: ${project.version}
                  repository: cloudsmith
                With project properties:
                  id: ${project.property("mod.id")},
                  name: ${project.property("mod.name")},
                  version: ${project.property("mod.version")},
                  group: ${project.property("mod.group")},
                  minecraft: ${project.property("mod.mc_dep")},
                  loader: ${project.property("deps.fabric_loader")},
                  cca: ${project.property("deps.cca")},
                  fapi: ${project.property("deps.fabric_api")},
                  fzzy: ${project.findProperty("deps.fzzy") ?: "none"}
                """.trimIndent()
                )
            }
            canPublish
        }
    }

    withType<AbstractArchiveTask>().configureEach {
        if (name == "remapJar" || name == "sourcesJar") {
            archiveFileName.set(
                "${base.archivesName.get()}-" +
                "${project.property("mod.version")}+" +
                "$buildNum-mc${sc.current.version}" +
                if (name == "remapSourcesJar") "-sources.jar" else ".jar"
            )
        }
    }
}

publishMods {
    file = tasks.remapJar.map { it.archiveFile.get() }
    displayName = "${property("mod.version")} for ${property("mod.mc_title")}"
    version = "${property("mod.version")}-$buildNum+mc${sc.current.version}"
    changelog = rootProject.file("CHANGELOG.md").readText()
    type = STABLE
    modLoaders.add("fabric")

    dryRun = !present("MODRINTH_TOKEN")

    modrinth {
        projectId = property("publish.modrinth") as String
        accessToken = env("MODRINTH_TOKEN")
        minecraftVersions.addAll(property("mod.mc_targets").toString().split(' '))
        requires {
            slug = "fabric-api"
            version = project.property("deps.fabric_api").toString()
        }

        requires {
            slug = "cardinal-components-api"
            version = project.property("deps.cca").toString()
        }

        requires {
            slug = "fzzy-config"
            version = project.property("deps.fzzy").toString()
        }

        optional {
            slug = "modmenu"
            version = project.property("r.deps.mod_menu").toString()
        }
    }
}

modrinth {
    syncBodyFrom = rootProject.file("README.md").readText()
    token = env("MODRINTH_TOKEN")
    projectId = project.property("publish.modrinth").toString()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            groupId = property("mod.group") as String
            artifactId = property("mod.archives_base_name").toString()
            version = project.version.toString()
        }
    }

    repositories {
        maven("https://maven.cloudsmith.io/project-codex/composer/") {
            name = "cloudsmith"

            credentials {
                username = env("CLOUDSMITH_USERNAME")
                password = env("CLOUDSMITH_API_KEY")
            }
        }
    }
}
