package com.github.sbnarra.mongo.gradle.task

import com.github.sbnarra.mongo.core.MongoFactory
import com.github.sbnarra.mongo.core.MongoParams
import com.github.sbnarra.mongo.gradle.MongoExtension
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

abstract class StartMongoTask: BaseMongoTask() {

    @get:Input
    abstract val runWith: Property<MongoParams>

    init {
        runWith.convention(project.extensions.getByType(MongoExtension::class.java).defaults)
    }

    @TaskAction
    fun start() = MongoFactory().create(runWith.get()).start()

    companion object {
        const val name = "startMongo"
    }
}