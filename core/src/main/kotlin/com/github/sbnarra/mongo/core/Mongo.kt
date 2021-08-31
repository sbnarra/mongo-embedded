package com.github.sbnarra.mongo.core

import com.github.sbnarra.mongo.core.util.Exec
import com.github.sbnarra.mongo.core.util.LoggerDelegate
import java.io.Closeable
import java.time.Instant
import java.util.concurrent.TimeUnit

abstract class Mongo(running: Boolean = false): Closeable {
    protected abstract val isReadyCmd: Array<String>
    abstract val params: MongoParams

    var running: Boolean = running
        private set

    protected abstract fun startup()
    protected abstract fun stop()

    fun start() =
        if (running) throw RuntimeException("unable to start, mongo already running")
        else running = runCatching {
            params.workingDir.mkdirs()
            params.dataDir.mkdirs()

            startup()
            checkReadiness()

            log.info("mongo is running on port ${params.port}")
        }.onFailure {
            log.error("failed to start mongo: ${it.message}", it)
        }.isSuccess

    override fun close() {
        if (running) runCatching(this::stop)
        else log.warn("unable to stop, mongo not running")
        running = false
    }

    private fun checkReadiness(expiry: Instant = Instant.now().plusSeconds(params.startupTimeout.toLong())) {
        while (!isReady()) {
            TimeUnit.SECONDS.sleep(2)
            if (Instant.now().isAfter(expiry)) throw RuntimeException("failed to startup mongo: $params")
            log.info("waiting for mongo readiness")
        }
    }

    private fun isReady(): Boolean = Exec.run(*isReadyCmd).waitFor() == 0

    companion object {
        private val log by LoggerDelegate()
    }
}