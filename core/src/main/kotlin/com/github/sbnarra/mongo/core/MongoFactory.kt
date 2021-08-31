package com.github.sbnarra.mongo.core

class MongoFactory {
    fun create(params: MongoParams, isRunning: Boolean = false): Mongo = when (params.type) {
        MongoParams.Type.BINARY -> BinaryMongo(params, MongoBinaryInstaller(params), isRunning)
        MongoParams.Type.DOCKER -> DockerMongo(params, isRunning)
    }
}