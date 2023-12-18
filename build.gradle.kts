plugins {
    id("java")
}

group = "com.eternalcode.eternaleconomy"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
    maven ("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven ("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://jitpack.io")

}


dependencies {
    compileOnly("org.spigotmc:spigot-api:1.13-R0.1-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")

}

