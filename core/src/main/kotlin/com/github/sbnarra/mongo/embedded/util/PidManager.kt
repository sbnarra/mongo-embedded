package com.github.sbnarra.mongo.embedded.util

import com.github.sbnarra.mongo.embedded.MongoParams
import java.io.File
import java.util.*

class PidManager(private val pids: File) {
    private val storage = Properties()
    init {
        if (pids.exists()) {
            storage.load(pids.inputStream())
        }
    }

    fun get(port: Int): Long? = storage.getProperty(port.toString())?.toLong()

    fun save(params: MongoParams, pid: Long) {
        val port = params.port().toString()
        if (storage.contains(port)) {
            throw RuntimeException("unable to save, port $port already exists within storage: pid=${storage[port]}")
        }

        storage[port] = pid.toString()
        save()
    }

    fun delete(params: MongoParams) {
        storage.remove(params.port().toString())
        save()
    }

    private fun save() = storage.store(pids.outputStream(), "DO NOT ALTER BY HAND. Tracks mongo instances, <PORT>=<PID>")
}