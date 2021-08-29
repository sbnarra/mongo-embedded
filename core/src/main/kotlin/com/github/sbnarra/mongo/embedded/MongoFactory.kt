package com.github.sbnarra.mongo.embedded

class MongoFactory {
    fun create(params: MongoParams, isRunning: Boolean = false): Mongo = when (params.type) {
        MongoParams.Type.BINARY -> BinaryMongo(params, isRunning)
        MongoParams.Type.DOCKER -> DockerMongo(params, isRunning)
    }
}