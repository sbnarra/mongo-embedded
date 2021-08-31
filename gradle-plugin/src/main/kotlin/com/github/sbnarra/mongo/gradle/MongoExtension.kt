package com.github.sbnarra.mongo.gradle

import com.github.sbnarra.mongo.core.MongoParams
import com.github.sbnarra.mongo.gradle.task.StartMongoTask
import com.github.sbnarra.mongo.gradle.task.StopMongoTask
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.provider.Property
import org.gradle.api.tasks.TaskProvider

abstract class MongoExtension(project: Project) {

    abstract val defaults: Property<MongoParams>

    init {
        defaults.convention(MongoParams.withDefaults(
            id = project.name,
            getValue = fun(key: String, default: Any): String = (project.properties[key] ?: default).toString()
        ))
    }

    fun runWithMongo(task: TaskProvider<Task>) = task.configure { runWithMongo(it) }

    fun runWithMongo(task: Task) {
        task.dependsOn(task.project.tasks.getByName(StartMongoTask.name))
        task.finalizedBy(task.project.tasks.getByName(StopMongoTask.name))
    }

    companion object {
        const val name = "mongo"
    }
}