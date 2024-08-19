plugins {
    `economy-java`
    `economy-repositories`
    `economy-checkstyle`
    `economy-publish`
}

dependencies {
    api("org.spigotmc:spigot-api:${Versions.SPIGOT_API}")
    api("org.jetbrains:annotations:${Versions.JETBRAINS_ANNOTATIONS}")
}

