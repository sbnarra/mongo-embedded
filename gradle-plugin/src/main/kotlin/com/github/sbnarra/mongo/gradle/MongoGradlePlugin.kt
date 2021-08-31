package com.github.sbnarra.mongo.gradle

import com.github.sbnarra.mongo.gradle.task.StartMongoTask
import com.github.sbnarra.mongo.gradle.task.StopMongoTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class MongoGradlePlugin: Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.create(MongoExtension.name, MongoExtension::class.java, project)

        project.repositories.maven {
            it.setUrl("https://maven.pkg.github.com/sbnarra/mongo-embedded")
        }

//        project.dependencies.add("implementation",
//            project.dependencies.platform("${project.group}:bom:${project.version}"))

        project.tasks.create(StartMongoTask.name, StartMongoTask::class.java)
        project.tasks.create(StopMongoTask.name, StopMongoTask::class.java)
    }
}
