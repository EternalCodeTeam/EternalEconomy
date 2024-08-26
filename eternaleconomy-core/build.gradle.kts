plugins {
    `economy-java`
    `economy-repositories`
    `economy-unit`
    `economy-checkstyle`

    id("net.minecrell.plugin-yml.bukkit")
    id("com.gradleup.shadow")
    id("xyz.jpenilla.run-paper")
}

dependencies {
    // api module
    api(project(":eternaleconomy-api"))

    // spigot-api
    compileOnly("org.spigotmc:spigot-api:${Versions.SPIGOT_API}")
    testImplementation("org.spigotmc:spigot-api:${Versions.SPIGOT_API}")

    // eternalcode commons
    implementation("com.eternalcode:eternalcode-commons-adventure:${Versions.ETERNALCODE_COMMONS}")
    implementation("com.eternalcode:eternalcode-commons-bukkit:${Versions.ETERNALCODE_COMMONS}")
    implementation("com.eternalcode:eternalcode-commons-shared:${Versions.ETERNALCODE_COMMONS}")

    implementation("org.mariadb.jdbc:mariadb-java-client:${Versions.MARIA_DB}")
    implementation("org.postgresql:postgresql:${Versions.POSTGRESQL}")
    implementation("com.h2database:h2:${Versions.H2}")
    implementation("com.j256.ormlite:ormlite-core:${Versions.ORMLITE}")
    implementation("com.j256.ormlite:ormlite-jdbc:${Versions.ORMLITE}")
    implementation("com.zaxxer:HikariCP:${Versions.HIKARI_CP}")

    // expressible
    implementation("org.panda-lang:expressible:${Versions.EXPRESSIBLE}")
    implementation("org.panda-lang:panda-utilities:${Versions.PANDA_UTILITIES}")

    implementation("dev.rollczi:litecommands-bukkit:${Versions.LITE_COMMANDS}")
    implementation("dev.rollczi:litecommands-adventure:${Versions.LITE_COMMANDS}")

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
}

bukkit {
    main = "com.eternalcode.economy.EconomyBukkitPlugin"
    apiVersion = "1.13"
    prefix = "EternalEconomy"
    author = "EternalCodeTeam"
    name = "EternalEconomy"
    website = "www.eternalcode.pl"
    version = "${project.version}"
}

tasks.runServer {
    minecraftVersion("1.21.1")
}

tasks.shadowJar {
    archiveFileName.set("EternalEconomy v${project.version}.jar")

    exclude(
        "org/intellij/lang/annotations/**",
        "org/jetbrains/annotations/**",
        "META-INF/**",
    )

    mergeServiceFiles()

    val prefix = "com.eternalcode.economy.libs"
    listOf(
        "dev.rollczi",
        "eu.okaeri",
        "panda",
        "org.yaml",
        "net.kyori",
        "com.eternalcode.commons",
    ).forEach { relocate(it, prefix) }
}
