package com.github.sbnarra.mongo.gradle

import org.gradle.testkit.runner.GradleRunner
import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

class MongoGradlePluginPluginFunctionalTest {
    @Test
    fun `can run task`() {
        val projectDir = File("build/functionalTest")
        projectDir.mkdirs()
        projectDir.resolve("settings.gradle").writeText("")
        projectDir.resolve("build.gradle").writeText("""
            plugins {
                id('com.github.sbnarra.mongo')
            }
        """.trimIndent())

        val runner = GradleRunner.create()
        runner.forwardOutput()
        runner.withPluginClasspath()
        runner.withArguments("startMongo", "stopMongo", "--stacktrace")
        runner.withProjectDir(projectDir)
        val result = runner.build()

        assertTrue(result.output.contains("starting mongo..."))
    }
}
