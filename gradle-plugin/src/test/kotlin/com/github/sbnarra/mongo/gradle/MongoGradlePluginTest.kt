package com.github.sbnarra.mongo.gradle

import com.github.sbnarra.mongo.gradle.task.StartMongoTask
import com.github.sbnarra.mongo.gradle.task.StopMongoTask
import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Test
import kotlin.test.assertNotNull

class MongoGradlePluginTest {
    @Test
    fun `plugin registers task`() {
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("com.github.sbnarra.mongo")

        assertNotNull(project.tasks.findByName(StartMongoTask.name))
        assertNotNull(project.tasks.findByName(StopMongoTask.name))
    }
}
