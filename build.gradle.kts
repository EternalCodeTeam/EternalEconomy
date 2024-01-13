plugins {
    `java-library`

    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("xyz.jpenilla.run-paper") version "2.2.0"
}

group = "com.eternalcode"
version = "1.0.0"
description = "A simple plugin adding economy system to your server."


repositories {
    gradlePluginPortal()
    mavenCentral()

    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://storehouse.okaeri.eu/repository/maven-public/") }
    maven { url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/") }
}

dependencies {
    // okaeri configs
    val okaeriConfigsVersion = "5.0.0-beta.5"
    implementation("eu.okaeri:okaeri-configs-yaml-snakeyaml:${okaeriConfigsVersion}")
    implementation("eu.okaeri:okaeri-configs-serdes-commons:${okaeriConfigsVersion}")

    // kyori adventure & minimessage
    implementation("net.kyori:adventure-platform-bukkit:4.3.1")
    implementation("net.kyori:adventure-text-minimessage:4.14.0")

    // hikari
    implementation("com.zaxxer:HikariCP:5.1.0")

    // spigot api
    compileOnly("org.spigotmc:spigot-api:1.20.2-R0.1-SNAPSHOT")
    testImplementation("org.spigotmc:spigot-api:1.20.2-R0.1-SNAPSHOT")

    // PlaceholerAPI
    compileOnly("me.clip:placeholderapi:2.11.4")

    // VaultAPI
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.3")

    // unit test
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

bukkit {
    main = "com.eternalcode.eternaleconomy.EternalEconomy"
    apiVersion = "1.13"
    prefix = "EternalEconomy"
    author = "EternalCodeTeam"
    name = "EternalEconomy"
    description = project.description
    version = project.version as String
}

tasks {
    compileJava {
        options.compilerArgs = listOf("-Xlint:deprecation")
        options.encoding = "UTF-8"
    }

    test {
        useJUnitPlatform()
    }

    runServer {
        minecraftVersion("1.20.1")

        downloadPlugins {
            hangar("PlaceholderAPI", "2.11.5")
            github("MilkBowl", "Vault", "1.7.3", "Vault.jar")
        }
    }

    shadowJar {
        archiveFileName.set("EternalEconomy v${project.version} (MC 1.8 - 1.20.4).jar")

        exclude(
            "org/intellij/lang/annotations/**",
            "org/jetbrains/annotations/**",
            "META-INF/**",
        )

        mergeServiceFiles()

        val prefix = "com.eternalcode.eternaleconomy.libs"
        listOf(
            "eu.okaeri",
            "org.yaml",
        ).forEach { relocate(it, prefix) }
    }
}


