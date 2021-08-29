plugins {
    id("org.jetbrains.kotlin.jvm") version "1.5.21"
    id("nebula.release") version "15.3.1"
}

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    group = "com.github.sbnarra.mongo"

    repositories {
        mavenCentral()
    }

    dependencies {
        api(platform(kotlin("bom")))
        api(platform("org.junit:junit-bom:5.7.2"))
        api(kotlin("stdlib-jdk8"))
        testImplementation(kotlin("test"))
        testImplementation("org.mongodb:mongodb-driver-sync:4.3.0")
    }

    apply(plugin = "maven-publish")
    configure<PublishingExtension> {
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/sbnarra/mongo-embedded")
                credentials {
                    username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_USERNAME")
                    password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
                }
            }
        }
        publications {
            register<MavenPublication>(name) {
                from(components["java"])
            }
        }
    }

    project.parent!!.tasks.release {
        dependsOn(tasks.findByName("publish"))
    }
}