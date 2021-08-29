package com.github.sbnarra.mongo.gradle.task

import com.github.sbnarra.mongo.embedded.MongoFactory
import com.github.sbnarra.mongo.embedded.MongoParams
import org.gradle.api.tasks.TaskAction

abstract class StartMongoTask: BaseMongoTask() {

    @TaskAction
    fun start() = MongoFactory().create(MongoParams()).start()

    companion object {
        const val name = "startMongo"
    }
}