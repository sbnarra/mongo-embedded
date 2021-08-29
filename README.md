# Mongo Embedded

---

[![CI](https://github.com/sbnarra/mongo-embedded/actions/workflows/ci.yml/badge.svg)](https://github.com/sbnarra/mongo-embedded/actions/workflows/ci.yml)

---

`core` is the code for running a mongodb instance

`test-core` provides test annotations for custom params per-test and inject the mongo object or params

`junit4` provides rule class

`junit5` provides extension class

`gradle-plugin` gradle plugin provides start/stop tasks

`spock` ...

`testng` ...

```
buildscript {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/sbnarra/mongo-embedded")
        }
    }
    dependencies {
        classpath("com.github.sbnarra.mongo:gradle-plugin:0.0.1-SNAPSHOT")
    }
}

apply {
    plugin("com.github.sbnarra.mongo")
}

repositories {
    maven {
        url = uri("https://maven.pkg.github.com/sbnarra/mongo-embedded")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
            password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
        }
    }
}

dependencies {
    implementation(platform("com.github.sbnarra.mongo.embedded:bom:0.0.0-SNAPSHOT"))
    testImplementation("com.github.sbnarra.mongo.embedded:junit4")
}
```

---

use `act` to test CI, https://github.com/nektos/act