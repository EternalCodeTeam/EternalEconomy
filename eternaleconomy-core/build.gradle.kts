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

    // okaeri configs
    implementation("eu.okaeri:okaeri-configs-yaml-snakeyaml:${Versions.OKAERI_CONFIGS}")
    implementation("eu.okaeri:okaeri-configs-serdes-commons:${Versions.OKAERI_CONFIGS}")


}

bukkit {
    main = "com.eternalcode.economy.BukkitEconomyPlugin"
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
