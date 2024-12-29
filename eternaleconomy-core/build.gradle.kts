import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    `economy-java`
    `economy-repositories`
//    `economy-checkstyle`

    id("net.minecrell.plugin-yml.bukkit")
    id("com.gradleup.shadow")
    id("xyz.jpenilla.run-paper")
    id("me.champeau.jmh")
}

dependencies {
    // api module
    implementation(project(":eternaleconomy-api"))

    // spigot-api
    compileOnly("org.spigotmc:spigot-api:${Versions.SPIGOT_API}")

    // eternalcode commons
    implementation("com.eternalcode:eternalcode-commons-adventure:${Versions.ETERNALCODE_COMMONS}")
    implementation("com.eternalcode:eternalcode-commons-bukkit:${Versions.ETERNALCODE_COMMONS}")
    implementation("com.eternalcode:eternalcode-commons-shared:${Versions.ETERNALCODE_COMMONS}")
    implementation("com.eternalcode:eternalcode-commons-folia:${Versions.ETERNALCODE_COMMONS}")

    bukkitLibrary("org.mariadb.jdbc:mariadb-java-client:${Versions.MARIA_DB}")
    bukkitLibrary("org.postgresql:postgresql:${Versions.POSTGRESQL}")
    bukkitLibrary("com.h2database:h2:${Versions.H2}")
    bukkitLibrary("com.j256.ormlite:ormlite-core:${Versions.ORMLITE}")
    bukkitLibrary("com.j256.ormlite:ormlite-jdbc:${Versions.ORMLITE}")
    bukkitLibrary("com.zaxxer:HikariCP:${Versions.HIKARI_CP}")

    implementation("dev.rollczi:litecommands-bukkit:${Versions.LITE_COMMANDS}")
    implementation("dev.rollczi:litecommands-adventure:${Versions.LITE_COMMANDS}")
    implementation("dev.rollczi:litecommands-jakarta:${Versions.LITE_COMMANDS}")

    // multification
    implementation("com.eternalcode:multification-bukkit:${Versions.MULTIFICATION}")
    implementation("com.eternalcode:multification-okaeri:${Versions.MULTIFICATION}")

    // kyori
    implementation("net.kyori:adventure-platform-bukkit:${Versions.ADVENTURE_PLATFORM_BUKKIT}")
    implementation("net.kyori:adventure-text-minimessage:${Versions.ADVENTURE_API}")

    // vault
    compileOnly("com.github.MilkBowl:VaultAPI:${Versions.VAULT_API}")

    // okaeri configs
    implementation("eu.okaeri:okaeri-configs-yaml-snakeyaml:${Versions.OKAERI_CONFIGS}")
    implementation("eu.okaeri:okaeri-configs-serdes-commons:${Versions.OKAERI_CONFIGS}")

    compileOnly("me.clip:placeholderapi:${Versions.PLACEHOLDER_API}")

    testImplementation(platform("org.junit:junit-bom:5.11.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    jmh("org.openjdk.jmh:jmh-core:1.37")
    jmh("org.openjdk.jmh:jmh-generator-annprocess:1.37")
    jmh("org.openjdk.jmh:jmh-generator-bytecode:1.37")
}

tasks.test {
    useJUnitPlatform()
}

bukkit {
    main = "com.eternalcode.economy.EconomyBukkitPlugin"
    apiVersion = "1.13"
    prefix = "EternalEconomy"
    author = "EternalCodeTeam"
    name = "EternalEconomy"
    website = "www.eternalcode.pl"
    // Enabling this option previously caused issues where the plugin was loaded before Vault,
    // preventing the Vault Economy Provider from registering and causing dependent plugins to malfunction.
    // Setting the load order to startup ensures the economy plugin is one of the first to load, avoiding these issues.
    load = BukkitPluginDescription.PluginLoadOrder.STARTUP
    version = "${project.version}"

    depend = listOf("Vault")
    softDepend = listOf("PlaceholderAPI")

    foliaSupported = true
}

tasks.runServer {
    minecraftVersion("1.21.1")
}

tasks.shadowJar {
    archiveFileName.set("EternalEconomy v${project.version}.jar")

    exclude(
        "org/intellij/lang/annotations/**",
        "org/jetbrains/annotations/**"
    )

    val prefix = "com.eternalcode.economy.libs"
    listOf(
        "dev.rollczi",
        "eu.okaeri",
        "panda",
        "org.yaml",
        "net.kyori",
        "com.eternalcode.commons",
        "net.jodah",
    ).forEach { relocate(it, prefix) }
}
