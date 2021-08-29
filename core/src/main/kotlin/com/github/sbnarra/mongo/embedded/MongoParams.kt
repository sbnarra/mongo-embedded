package com.github.sbnarra.mongo.embedded

import java.io.File
import java.net.ServerSocket
import java.util.*

data class MongoParams(
    val version: String = version(),
    val workingDirectory: File = workingDirectory(),
    val id: String = UUID.randomUUID().toString(),
    private val port: Int = MongoParams.port(),
    val type: Type = Type.valueOf(type().toUpperCase())
) {

    private val _port = if (port != -1) port else ServerSocket(0).use { it.localPort }
    fun port(): Int = _port

    enum class Type {
        BINARY, DOCKER
    }

    companion object {
        fun workingDirectory(): File = File(System.getProperty("user.home"), ".mongo/${type().toLowerCase()}")
        const val DEFAULT_PORT: Int = 27017
        fun port(): Int = System.getProperty("mongo.port", "$DEFAULT_PORT").toInt()
        fun version(): String = System.getProperty("mongo.version", "5.0.2")
        fun type(): String = System.getProperty("mongo.type", "BINARY")
    }
}
