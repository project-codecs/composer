# What is Composer?

**Composer** is a general-purpose Fabric library mod developed under **Project Codex**.
It provides shared systems, utilities, and architectural building blocks used across multiple mods, with a focus on clean APIs, long-term stability, and multi-version support.
    
Composer does **not** add gameplay content on its own (except for a plushie). It exists to support other mods at runtime and during development.

For more details, take a look at the [wiki](https://moddedmc.wiki/composer-reloaded/latest)! <br>
Information like style and contributing guidelines are also there!

---

<details>
<summary>Old development usage docs</summary>
# Development Usage

If you want to use Composer as a library in your mod, add the Composer Maven repository and Cardinal Components
repository to your build file, then add Composer as a dependency.

Replace `(latest_version)` with the latest Composer version compatible with your Minecraft version.

---
<details>
<summary>Gradle (Groovy DSL) - build.gradle</summary>

```properties
# gradle.properties
composer_version = (latest_version)
```

```groovy
// build.gradle

repositories {
    // Before 3.0
    maven {
        name = "Composer Maven"
        url = "https://dl.cloudsmith.io/public/lilbrocodes/composer-reloaded/maven/"
    }
    
    // After 3.0
    maven {
        name = "Composer Maven"
        url = "https://dl.cloudsmith.io/public/project-codex/composer/maven/"
    }
    
    // After 3.0.4
    maven {
        name "Constructive"
        url "https://dl.cloudsmith.io/public/lilbrocodes/constructive/maven/"
    }
    
    maven {
        name = "Cardinal Components"
        url = "https://maven.ladysnake.org/releases"
    }
    maven {
        name "Fuzzy Hamsters"
        url "https://maven.fzzyhmstrs.me/"
    }
}

dependencies {
    // <2.0
    modImplementation "org.lilbrocodes:composer-reloaded:$composer_version"

    // >=2.0 <3.0
    modImplementation "org.lilbrocodes:composer-reloaded:$composer_version+mc$minecraft_version"

    // >=3.0
    modImplementation "com.codex:composer:$composer_version+mc$minecraft_version"
}
```

</details>

<details>
<summary>Gradle (Kotlin DSL) - build.gradle.kts</summary>

```properties
# gradle.properties
composer_version = (latest_version)
```

```groovy
// build.gradle.kts

repositories {
    // Before 3.0
    maven("https://dl.cloudsmith.io/public/lilbrocodes/composer-reloaded/maven/") {
        name = "Composer Maven"
    }
    
    // After 3.0
    maven("https://dl.cloudsmith.io/public/project-codex/composer/maven/") {
        name = "Composer Maven"
    }

    // After 3.0.4
    maven("https://dl.cloudsmith.io/public/lilbrocodes/constructive/maven/") {
        name = "Constructive"
    }
    
    maven("https://maven.ladysnake.org/releases") {
        name = "Cardinal Components"
    }
    maven("https://maven.fzzyhmstrs.me/") {
        name = "Fuzzy Hamsters"
    }
}

dependencies {
    // <2.0
    modImplementation("org.lilbrocodes:composer-reloaded:$composer_version")
    
    // >=2.0 <3.0
    modImplementation("org.lilbrocodes:composer-reloaded:$composer_version+mc$minecraft_version")
    
    // >=3.0
    modImplementation("com.codex:composer:$composer_version+mc$minecraft_version")
}
```

</details>

<details>
<summary>Maven - pom.xml</summary>

```xml
<repositories>
    <!-- Before 3.0 -->
    <repository>
        <id>composer-maven</id>
        <url>https://dl.cloudsmith.io/public/lilbrocodes/composer-reloaded/maven/</url>
    </repository>
    <!-- After 3.0 -->
    <repository>
        <id>composer-maven</id>
        <url>https://dl.cloudsmith.io/public/project-codex/composer/maven/</url>
    </repository>
    <!-- After 3.0.4 -->
    <repository>
        <id>constructive-maven</id>
        <url>https://dl.cloudsmith.io/public/lilbrocodes/constructive/maven/</url>
    </repository>
    <repository>
        <id>cardinal-components</id>
        <url>https://maven.ladysnake.org/releases</url>
    </repository>
    <repository>
        <id>fuzzy-hamsters</id>
        <url>https://maven.fzzyhmstrs.me/</url>
    </repository>
</repositories>

<dependencies>
    <!-- <2.0 -->
    <dependency>
        <groupId>org.lilbrocodes</groupId>
        <artifactId>composer-reloaded</artifactId>
        <version>${composer.version}</version>
        <scope>compile</scope>
    </dependency>
    
    <!-- >=2.0 <3.0 -->
    <dependency>
        <groupId>org.lilbrocodes</groupId>
        <artifactId>composer-reloaded</artifactId>
        <version>${composer.version}+mc${minecraft.version}</version>
        <scope>compile</scope>
    </dependency>

    <!-- >=3.0 -->
    <dependency>
        <groupId>com.codex</groupId>
        <artifactId>composer</artifactId>
        <version>${composer.version}+mc${minecraft.version}</version>
        <scope>compile</scope>
    </dependency>
</dependencies>

```

Add the following property to your `<properties>` section:

```xml
<properties>
    <composer.version>(latest_version)</composer.version>
</properties>
```

</details>
</details>
