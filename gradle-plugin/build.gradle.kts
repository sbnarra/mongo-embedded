plugins {
    `java-gradle-plugin`
}

dependencies {
    implementation(project(":core"))
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

gradlePlugin {
    val mongo by plugins.creating {
        id = "com.github.sbnarra.mongo"
        implementationClass = "com.github.sbnarra.mongo.gradle.MongoGradlePlugin"
        displayName = "Gradle Mongo Plugin"
        description = "Plugin provides mongo tasks"
    }
}

afterEvaluate {
    // causes duplicate upload which results in a conflict error
    tasks.findByName("publishPluginMavenPublicationToGitHubPackagesRepository")?.enabled = false
}

val functionalTestSourceSet = sourceSets.create("functionalTest") {}

gradlePlugin.testSourceSets(functionalTestSourceSet)
configurations["functionalTestImplementation"]
    .extendsFrom(configurations["testImplementation"])
    .exclude(group = "org.slf4j", module = "slf4j-simple") // gradle api provides this

val functionalTest by tasks.registering(Test::class) {
    group = "verification"
    testClassesDirs = functionalTestSourceSet.output.classesDirs
    classpath = functionalTestSourceSet.runtimeClasspath
}

tasks.check {
    dependsOn(functionalTest)
}

tasks.withType(Test::class.java) {
    useJUnitPlatform()
}