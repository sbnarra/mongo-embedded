# Mongo Embedded

[![CI](https://github.com/sbnarra/mongo-embedded/actions/workflows/ci.yml/badge.svg)](https://github.com/sbnarra/mongo-embedded/actions/workflows/ci.yml)

---
## Modules

* `core` - services for starting/stopping mongodb instances
* `test-core` - generic annotations for configuring tests
* `junit4` - test rule to start/stop mongodb test instances
  * https://junit.org/junit4/javadoc/4.13/org/junit/Rule.html
* `junit5` - test extension to start/stop mongodb test instances
  * https://junit.org/junit5/docs/current/user-guide/#extensions
* `gradle-plugin` - gradle plugin providing start/stop tasks for wrapping `run` and `bootRun` tasks
  * https://docs.gradle.org/current/userguide/publishing_gradle_plugins.html
* `spock` - ... TODO ...
* `testng` - ... TODO ...

## How to use

Applying gradle plugin...
```kotlin
buildscript {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/sbnarra/mongo-embedded")
        }
    }
    dependencies {
        classpath("com.github.sbnarra.mongo:gradle-plugin:0.0.1")
    }
}

apply {
    plugin("com.github.sbnarra.mongo")
}
```

Adding test helpers...
```kotlin
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/sbnarra/mongo-embedded")
    }
}

dependencies {
    implementation(platform("com.github.sbnarra.mongo.embedded:bom:0.0.1"))
    testImplementation("com.github.sbnarra.mongo.embedded:junit4")
  testImplementation("com.github.sbnarra.mongo.embedded:junit5")
}
```

---

use `act` to test CI, https://github.com/nektos/act