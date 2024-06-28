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

    maven("https://repo.eternalcode.pl/releases/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://jitpack.io")
    maven("https://storehouse.okaeri.eu/repository/maven-public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.panda-lang.org/releases")
}

dependencies {
    // okaeri configs
    val okaeriConfigsVersion = "5.0.2"
    implementation("eu.okaeri:okaeri-configs-yaml-snakeyaml:${okaeriConfigsVersion}")
    implementation("eu.okaeri:okaeri-configs-serdes-commons:${okaeriConfigsVersion}")

    val eternalcodeCommonsVersion = "1.1.1"
    implementation("com.eternalcode:eternalcode-commons-adventure:${eternalcodeCommonsVersion}")
    implementation("com.eternalcode:eternalcode-commons-bukkit:${eternalcodeCommonsVersion}")

    // kyori adventure & minimessage
    implementation("net.kyori:adventure-platform-bukkit:4.3.1")
    implementation("net.kyori:adventure-text-minimessage:4.17.0")

    // hikari
    implementation("com.zaxxer:HikariCP:5.1.0")

    // spigot api
    compileOnly("org.spigotmc:spigot-api:1.20.2-R0.1-SNAPSHOT")
    testImplementation("org.spigotmc:spigot-api:1.20.2-R0.1-SNAPSHOT")

    // PlaceholerAPI
    compileOnly("me.clip:placeholderapi:2.11.4")

    // VaultAPI
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1")

    // unit test
    testImplementation(platform("org.junit:junit-bom:5.10.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:5.11.0")

    // litecommands
    val litecommandsVersion = "3.4.2"
    implementation("dev.rollczi:litecommands-bukkit:${litecommandsVersion}")

    // Gui lib
    implementation("dev.triumphteam:triumph-gui:3.1.10")

    // Multification
    implementation("com.eternalcode:multification-core:1.0.2")
    implementation("com.eternalcode:multification-cdn:1.0.2")

    // EternalCode Commons
    implementation("com.eternalcode:eternalcode-commons-bukkit:1.1.3")
    implementation("com.eternalcode:eternalcode-commons-adventure:1.1.3")
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

tasks.compileJava {
    options.compilerArgs = listOf("-Xlint:deprecation", "-parameters")
    options.encoding = "UTF-8"
}

tasks.test {
    useJUnitPlatform()
}

tasks.runServer {
    minecraftVersion("1.20.4")

    downloadPlugins {
        hangar("PlaceholderAPI", "2.11.5")
        github("MilkBowl", "Vault", "1.7.3", "Vault.jar")
    }
}

tasks.shadowJar {
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


