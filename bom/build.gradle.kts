dependencies {
    constraints {
        api("junit:junit:4.13.2")

        api(project(":core"))

        api(project(":junit4"))
        api(project(":junit5"))
//        api(project(":spock"))
//        api(project(":testng"))

        api(project(":gradle-plugin"))
    }
}
