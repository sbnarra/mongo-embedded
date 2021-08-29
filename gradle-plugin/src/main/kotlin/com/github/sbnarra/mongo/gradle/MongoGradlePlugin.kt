package com.github.sbnarra.mongo.gradle

import com.github.sbnarra.mongo.gradle.task.StartMongoTask
import com.github.sbnarra.mongo.gradle.task.StopMongoTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class MongoGradlePlugin: Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.create(MongoGradleExtension.name, MongoGradleExtension::class.java)

        project.repositories.maven {
            it.setUrl("https://maven.pkg.github.com/sbnarra/mongo-embedded")
        }
//
//        project.dependencies.add("implementation",
//            project.dependencies.platform("${project.group}:bom:${project.version}"))

        project.tasks.register(StartMongoTask.name, StartMongoTask::class.java)
        project.tasks.register(StopMongoTask.name, StopMongoTask::class.java)
    }
}
