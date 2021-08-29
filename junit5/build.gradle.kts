dependencies {
    api("org.junit.jupiter:junit-jupiter")
    implementation(project(":core"))
    implementation(project(":annotations"))
    testImplementation(kotlin("test-junit5"))
}

tasks.test {
    useJUnitPlatform()
}
