plugins {
    `economy-java`
    `economy-repositories`
    `economy-publish`
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:${Versions.PAPER_API}")
    api("org.jetbrains:annotations:${Versions.JETBRAINS_ANNOTATIONS}")
}

