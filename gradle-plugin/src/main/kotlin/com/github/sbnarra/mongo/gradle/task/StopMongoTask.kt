package com.github.sbnarra.mongo.gradle.task

import com.github.sbnarra.mongo.embedded.MongoFactory
import com.github.sbnarra.mongo.embedded.MongoParams
import org.gradle.api.tasks.TaskAction

abstract class StopMongoTask: BaseMongoTask() {

    @TaskAction
    fun stop() = MongoFactory().create(MongoParams(), true).close()

    companion object {
        const val name = "stopMongo"
    }
}