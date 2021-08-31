package com.github.sbnarra.mongo.gradle.task

import com.github.sbnarra.mongo.core.MongoFactory
import org.gradle.api.tasks.TaskAction

abstract class StartMongoTask: BaseMongoTask() {

    @TaskAction
    fun start() = MongoFactory().create(params.get()).start()

    companion object {
        const val name = "startMongo"
    }
}