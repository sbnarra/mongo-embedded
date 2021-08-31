package com.github.sbnarra.mongo.core

import java.io.File
import java.net.ServerSocket
import java.util.*

data class MongoParams(val version: String,
                       val id: String,
                       val port: Int,
                       val type: Type,
                       val workingDir: File,
                       val startupTimeout: Int = 180) {

    val logFile = File(workingDir, "$id.log")
    val dataDir = File(workingDir, "data-$id")

    enum class Type {
        BINARY, DOCKER
    }

    companion object {
        const val DEFAULT_VERSION = "5.0.2"

        const val VERSION = "mongo.version"
        const val PORT = "mongo.port"
        const val TYPE = "mongo.type"
        const val WORKING_DIR = "mongo.workingDir"
        const val STARTUP_TIMEOUT = "mongo.startupTimeout"

        val GET_SYS_PROP = fun(key: String, default: Any) = System.getProperty(key, default.toString())

        fun withDefaults(getValue: (key: String, default: Any) -> String = GET_SYS_PROP,
                         id: String = UUID.randomUUID().toString(),
                         defaults: MongoParams = MongoParams(
                             id = id,
                             version = DEFAULT_VERSION,
                             port = 27017,
                             type = Type.BINARY,
                             workingDir = File("build")
                         ),
                         version: String = getValue(VERSION, defaults.version),
                         port: Int = getValue(PORT, defaults.port).toInt(),
                         type: Type = Type.valueOf(getValue(TYPE, defaults.type).toUpperCase()),
                         workingDirectory: File = File(getValue(WORKING_DIR, defaults.workingDir)),
                         startupTimeout: Int = getValue(STARTUP_TIMEOUT, defaults.startupTimeout).toInt()
        ): MongoParams = MongoParams(
            id = id,
            version = version,
            port = if (port > 0) port else ServerSocket(0).use { it.localPort },
            type = type,
            workingDir = workingDirectory,
            startupTimeout = startupTimeout
        )
    }
}
