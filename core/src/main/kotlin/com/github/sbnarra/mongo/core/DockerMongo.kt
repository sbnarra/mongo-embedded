package com.github.sbnarra.mongo.core

import com.github.sbnarra.mongo.core.util.Exec
import com.github.sbnarra.mongo.core.util.LoggerDelegate
import java.io.File

class DockerMongo(override var params: MongoParams,
                  running: Boolean = false) : Mongo(running) {

    override var isReadyCmd: Array<String> = arrayOf("docker", "exec", params.id, "mongo", "--eval", "db.version()")

    override fun startup() {
        Exec.run(ProcessBuilder.Redirect.appendTo(params.logFile),
            "docker", "run", "--rm", "--name", params.id, "-v", "${params.dataDir.absolutePath}:/data/db",
            "-p", "${params.port}:27017", "mongo:${params.version}")
    }

    override fun stop() {
        Exec.run("docker", "container", "stop", params.id).waitFor()
    }
}