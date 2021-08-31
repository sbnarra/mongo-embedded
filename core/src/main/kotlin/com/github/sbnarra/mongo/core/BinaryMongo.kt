package com.github.sbnarra.mongo.core

import com.github.sbnarra.mongo.core.util.Exec
import com.github.sbnarra.mongo.core.util.PidManager
import java.io.File

class BinaryMongo(override val params: MongoParams,
                  private val installer: MongoBinaryInstaller,
                  running: Boolean = false): Mongo(running) {

    private val pidManager = PidManager(File(installer.directory, "pids.properties"))

    override var isReadyCmd = arrayOf(installer.clientExec.absolutePath, "--port", "${params.port}", "--eval", "db.version()")

    override fun startup() {
        installer.install()

        val process = Exec.run(ProcessBuilder.Redirect.appendTo(params.logFile),
            installer.serverExec.absolutePath, "--port", "${params.port}", "--dbpath", params.dataDir.absolutePath)
        val pid = Exec.findPid(process)
        pidManager.save(params.port, pid)
    }

    override fun stop() = pidManager.kill(params.port)
}