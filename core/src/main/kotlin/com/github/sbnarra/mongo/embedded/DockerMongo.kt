package com.github.sbnarra.mongo.embedded

import com.github.sbnarra.mongo.embedded.util.Processes
import java.io.File

class DockerMongo(override var params: MongoParams,
                  override var running: Boolean = false) : Mongo() {

    override var isReadyCmd: Array<String> = arrayOf("docker", "exec", params.id, "mongo", "--eval", "db.version()")

    private val runningDirectory: File = File(File(params.workingDirectory, "instance"), params.id)

    override fun start() {
        val logFile = File(runningDirectory, "log.out")
        val dataDirectory = File(runningDirectory, "data")
        dataDirectory.mkdirs()

        println("starting mongo...")
        val startParams = arrayOf("docker", "run", "--rm", "--name", params.id,
            "-v", "${dataDirectory.absolutePath}:/data/db", "-p", "${params.port()}:27017", "mongo:${params.version}")

        Processes.run(redirect = ProcessBuilder.Redirect.appendTo(logFile), args = startParams)

        checkReadiness()
        running = true
    }

    override fun close() {
        println("shutting down mongo: ${params.id}")
        Processes.run("docker", "container", "stop", params.id).waitFor()
        running = false
    }
}