package com.github.sbnarra.mongo.gradle.task

import com.github.sbnarra.mongo.gradle.MongoExtension
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input

abstract class BaseMongoTask: DefaultTask() {
    init {
        group = "Mongo"
    }

    @get:Input
    val params = project.extensions.getByType(MongoExtension::class.java).defaults.get()
}