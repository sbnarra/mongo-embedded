package com.github.sbnarra.mongo.test

import com.github.sbnarra.mongo.core.MongoParams

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class TestMongoParams(
    val id: String = "",
    val version: String = MongoParams.DEFAULT_VERSION,
    val type: MongoParams.Type = MongoParams.Type.BINARY,
    val workingDirectory: String = "build/mongo-test",
    val port: Int = 27017,
    val startupTimeout: Int = 10
)
