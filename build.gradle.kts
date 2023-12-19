plugins {
    id("java")
    id("net.minecrell.plugin-yml.bukkit") version "0.5.3"
}

group = "com.eternalcode"
version = "1.0.1"


repositories {
    mavenCentral()
    mavenLocal()
    //Spigot API
    maven ("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven ("https://oss.sonatype.org/content/repositories/snapshots")

    //Vault API
    maven("https://jitpack.io")

}


dependencies {
    //Spigot API
    compileOnly("org.spigotmc:spigot-api:1.13-R0.1-SNAPSHOT")

    //Vault API
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")

}
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
bukkit {
    main = "com.eternalcode.eternaleconomy.EternalEconomy"
    apiVersion = "1.13"
    prefix = "EternalEconomy"
    author = "EternalCode"
    name = "EternalEconomy"
    description = "A simple plugin adding economy system"
    version = "${project.version}"
}


