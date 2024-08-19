plugins {
    `java-library`
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:${Versions.JUNIT_BOM}"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:${Versions.MOCKITO_CORE}")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

sourceSets.test {
    java.setSrcDirs(listOf("test"))
    resources.setSrcDirs(emptyList<String>())
}