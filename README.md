# What is Composer?

**Composer** is a general-purpose Fabric library mod developed under **Project Codex**.
It provides shared systems, utilities, and architectural building blocks used across multiple mods, with a focus on clean APIs, long-term stability, and multi-version support.
    
Composer does **not** add gameplay content on its own (except for a plushie). It exists to support other mods at runtime and during development.

---
# *History Lesson!*
Feel free to skip this part, it's just here for the people who are curious. <br>

Composer began its life as a small utility mod providing some tools for targeting that I (LilBroCodes) made a while ago for myself.
After using that version of the mod though, it became apparent that it's code quality and API schema was - let's just say - horrible.
I had no idea how to actually do proper APIs, per-usage configs or anything, so the whole thing was barely usable. That's where **Composer Reloades** came in.

I set out to rewrite composer from the ground up (which granted, didn't take much time) when I started working on some of my larger scale mods,
and ended up not just rewriting the targeting but adding along a lot more useful things. Over time, **Composer Reloaded** grew to be an invaluable
utility almost - if not - all of my mods used, and was growing in scope constantly.

That is when I decided that **Composer Reloaded** was way too long of a name, and I was way too unprofessional to be the sole owner, so since then Composer
has been under the **Project Codex** organization. I've since learned better API schemas, maintaining versions and all that, so **Composer** should be a library
on a professional enough level for anyone to be able to use.

---

# Installation

You can download Composer from [Modrinth](https://modrinth.com/mod/composer-reloaded).  
If none of your mods require it, you don’t need to install it - it doesn’t add any gameplay content on its own, only
functionality for other mods.

Although, if you really want the LilBro plushie then feel free to install Composer just for that :P

> [!WARNING]
> If you do use Composer in your own mod, or want to include it anyway, you must also install a compatible version of **Cardinal
Components** and **Fzzy Config**. For more info, look at the dependencies of the specific version on modrinth.

---

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
