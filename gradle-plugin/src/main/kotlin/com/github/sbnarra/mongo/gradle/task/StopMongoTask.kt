package com.github.sbnarra.mongo.gradle.task

import com.github.sbnarra.mongo.core.MongoFactory
import org.gradle.api.tasks.TaskAction

abstract class StopMongoTask: BaseMongoTask() {

    @TaskAction
    fun stop() {
        MongoFactory().create(params, true).close()
    }

    companion object {
        const val name = "stopMongo"
    }
}