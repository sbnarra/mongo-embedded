package com.github.sbnarra.mongo.test

import com.github.sbnarra.mongo.embedded.MongoParams

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class TestMongoParams(
    val id: String = "",
    val version: String = "",
    val type: MongoParams.Type = MongoParams.Type.BINARY,
    val workingDirectory: String = "",
    val port: Int = MongoParams.DEFAULT_PORT
) {
    companion object {
        const val RANDOM_PORT: Int = -1

    }
}
