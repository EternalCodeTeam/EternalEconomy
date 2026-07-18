import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    `economy-java`
    `economy-repositories`
    `economy-publish-plugin`

    id("net.minecrell.plugin-yml.paper")
    id("com.gradleup.shadow")
    id("xyz.jpenilla.run-paper")
    id("me.champeau.jmh")
}

ext {
    set("modrinthProjectId", "EternalEconomy")
    set("hangarProjectId", "EternalEconomy")
}

repositories {
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    // api module
    implementation(project(":eternaleconomy-api"))

    // paper-api
    compileOnly("io.papermc.paper:paper-api:${Versions.PAPER_API}")

    // eternalcode commons
    paperLibrary("com.eternalcode:eternalcode-commons-adventure:${Versions.ETERNALCODE_COMMONS}")
    paperLibrary("com.eternalcode:eternalcode-commons-bukkit:${Versions.ETERNALCODE_COMMONS}")
    paperLibrary("com.eternalcode:eternalcode-commons-shared:${Versions.ETERNALCODE_COMMONS}")
    paperLibrary("com.eternalcode:eternalcode-commons-folia:${Versions.ETERNALCODE_COMMONS}")

    paperLibrary("org.mariadb.jdbc:mariadb-java-client:${Versions.MARIA_DB}")
    paperLibrary("org.postgresql:postgresql:${Versions.POSTGRESQL}")
    paperLibrary("com.h2database:h2:${Versions.H2}")
    paperLibrary("com.j256.ormlite:ormlite-core:${Versions.ORMLITE}")
    paperLibrary("com.j256.ormlite:ormlite-jdbc:${Versions.ORMLITE}")
    paperLibrary("com.zaxxer:HikariCP:${Versions.HIKARI_CP}")

    paperLibrary("dev.rollczi:litecommands-bukkit:${Versions.LITE_COMMANDS}")
    paperLibrary("dev.rollczi:litecommands-adventure:${Versions.LITE_COMMANDS}")
    paperLibrary("dev.rollczi:litecommands-jakarta:${Versions.LITE_COMMANDS}")

    // multification
    paperLibrary("com.eternalcode:multification-bukkit:${Versions.MULTIFICATION}")
    paperLibrary("com.eternalcode:multification-okaeri:${Versions.MULTIFICATION}")

    // vault
    compileOnly("com.github.MilkBowl:VaultAPI:${Versions.VAULT_API}")

    // okaeri configs
    paperLibrary("eu.okaeri:okaeri-configs-yaml-snakeyaml:${Versions.OKAERI_CONFIGS}")
    paperLibrary("eu.okaeri:okaeri-configs-serdes-commons:${Versions.OKAERI_CONFIGS}")
    paperLibrary("eu.okaeri:okaeri-configs-serdes-bukkit:${Versions.OKAERI_CONFIGS}")

    paperLibrary("com.github.cryptomorin:XSeries:13.8.0")
    paperLibrary("com.github.ben-manes.caffeine:caffeine:3.2.4")

    compileOnly("me.clip:placeholderapi:${Versions.PLACEHOLDER_API}")

    implementation("org.bstats:bstats-bukkit:${Versions.BSTATS_BUKKIT}")

    // TriumphGUI for GUI
    implementation("dev.triumphteam:triumph-gui-paper:${Versions.TRIUMPH_GUI}")
    paperLibrary("dev.rollczi:liteskullapi:${Versions.LITE_SKULL_API}")

    testImplementation(platform("org.junit:junit-bom:6.0.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("com.github.ben-manes.caffeine:caffeine:3.2.4")
    testImplementation("com.google.guava:guava:33.6.0-jre")

    jmh("org.openjdk.jmh:jmh-core:1.37")
    jmh("org.openjdk.jmh:jmh-generator-annprocess:1.37")
    jmh("org.openjdk.jmh:jmh-generator-bytecode:1.37")
    jmh("io.papermc.paper:paper-api:${Versions.PAPER_API}")

    jmh("com.eternalcode:eternalcode-commons-adventure:${Versions.ETERNALCODE_COMMONS}")
    jmh("com.eternalcode:eternalcode-commons-bukkit:${Versions.ETERNALCODE_COMMONS}")
    jmh("com.eternalcode:eternalcode-commons-shared:${Versions.ETERNALCODE_COMMONS}")
    jmh("com.github.MilkBowl:VaultAPI:${Versions.VAULT_API}")

    jmh("eu.okaeri:okaeri-configs-yaml-snakeyaml:${Versions.OKAERI_CONFIGS}")
    jmh("eu.okaeri:okaeri-configs-serdes-commons:${Versions.OKAERI_CONFIGS}")
    jmh("eu.okaeri:okaeri-configs-serdes-bukkit:${Versions.OKAERI_CONFIGS}")
}

paper {
    main = "com.eternalcode.economy.EconomyBukkitPlugin"
    loader = "com.eternalcode.economy.EconomyBukkitLoader"
    apiVersion = "1.19"
    prefix = "EternalEconomy"
    author = "EternalCodeTeam"
    name = "EternalEconomy"
    website = "https://www.eternalcode.pl"
    load = BukkitPluginDescription.PluginLoadOrder.STARTUP
    version = project.version.toString()

    serverDependencies {
        register("Vault") {
            required = true
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
        }

        register("PlaceholderAPI") {
            required = false
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
        }
    }

    foliaSupported = true
    generateLibrariesJson = true
}

tasks.runServer {
    minecraftVersion("1.21.11")
    downloadPlugins {
        url("https://github.com/MilkBowl/Vault/releases/download/1.7.3/Vault.jar")
    }
}

tasks.shadowJar {
    archiveFileName.set("EternalEconomy-v${project.version}.jar")

    exclude(
        "org/intellij/lang/annotations/**",
        "org/jetbrains/annotations/**"
    )

    val relocationPrefix = "com.eternalcode.economy.libs"

    mapOf(
        "org.bstats" to "$relocationPrefix.bstats",
        "dev.triumphteam.gui" to "$relocationPrefix.triumphgui"
    ).forEach { (source, target) ->
        relocate(source, target)
    }
}
