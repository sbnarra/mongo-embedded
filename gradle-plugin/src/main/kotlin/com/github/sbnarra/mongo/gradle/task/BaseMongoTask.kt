package com.github.sbnarra.mongo.gradle.task

import org.gradle.api.DefaultTask

abstract class BaseMongoTask: DefaultTask() {
    init {
        group = "Mongo"
    }
}