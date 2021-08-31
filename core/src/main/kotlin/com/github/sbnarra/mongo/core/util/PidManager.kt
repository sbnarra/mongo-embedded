package com.github.sbnarra.mongo.core.util

import java.io.File
import java.util.*

class PidManager(private val pids: File) {
    private val storage = Properties()

    init {
        if (pids.exists()) storage.load(pids.inputStream())
    }

    fun save(port: Int, pid: Int) {
        val port = port.toString()
        if (storage.contains(port)) {
            throw RuntimeException("unable to save, port $port already exists within storage: pid=${storage[port]}")
        }

        storage[port] = pid.toString()
        writeChanges()
    }

    fun kill(port: Int) {
        val pid = storage.remove(port.toString())
        if (pid != null) {
            log.info("sending kill signal to $pid")
            Exec.run("kill", pid.toString()).waitFor()
        }
        writeChanges()
    }

    private fun writeChanges() = storage.store(pids.outputStream(), "DO NOT ALTER BY HAND. Tracks mongo instances, <PORT>=<PID>")

    companion object {
        private val log by LoggerDelegate()
    }
}