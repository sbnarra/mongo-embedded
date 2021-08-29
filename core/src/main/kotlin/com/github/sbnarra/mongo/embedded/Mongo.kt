package com.github.sbnarra.mongo.embedded

import com.github.sbnarra.mongo.embedded.util.Processes
import java.io.Closeable
import java.time.Instant
import java.util.concurrent.TimeUnit

abstract class Mongo: Closeable {
    abstract var params: MongoParams
    abstract var running: Boolean
    protected abstract var isReadyCmd: Array<String>

    abstract fun start()

    protected fun checkReadiness(expiry: Instant = Instant.now().plusSeconds(180)) {
        while (!isReady()) {
            TimeUnit.SECONDS.sleep(1)
            if (Instant.now().isAfter(expiry)) throw RuntimeException("failed to startup mongo: $params")
            println("checking mongo is ready")
        }
    }

    protected fun isReady(): Boolean = Processes.run(args = isReadyCmd).waitFor() == 0
}