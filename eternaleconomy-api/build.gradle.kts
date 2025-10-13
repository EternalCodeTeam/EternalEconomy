plugins {
    `economy-java`
    `economy-repositories`
    //    `economy-checkstyle`
    `economy-publish`
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:${Versions.PAPER_API}")
    api("org.jetbrains:annotations:${Versions.JETBRAINS_ANNOTATIONS}")
}

