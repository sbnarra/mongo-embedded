package com.github.sbnarra.mongo.embedded

import com.github.sbnarra.mongo.embedded.util.Archive
import com.github.sbnarra.mongo.embedded.util.PidManager
import com.github.sbnarra.mongo.embedded.util.Processes
import org.apache.commons.lang3.SystemUtils
import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Files

class BinaryMongo(override var params: MongoParams,
                  override var running: Boolean = false): Mongo() {

    private val name = "mongodb-${OS.CURRENT.identifier}-${params.version}"
    private val archiveFilename = "$name.${OS.CURRENT.fileType.extension}"
    private val archiveFile = File(params.workingDirectory, archiveFilename)
    private val archiveUrl = "https://fastdl.mongodb.org/${OS.CURRENT.type}/${archiveFilename}"

    private val installationDirectory = File(params.workingDirectory, name)
    private val clientExec = File(installationDirectory, "bin/mongo")
    private val serverExec = File(installationDirectory, "bin/mongod")

    override var isReadyCmd = arrayOf(clientExec.absolutePath, "--port", "${params.port()}", "--eval", "db.version()")

    private val runningDirectory = File(File(params.workingDirectory, "instance"), params.id)

    private val pidManager = PidManager(File(installationDirectory, "pids.properties"))

    init {
        if (!params.workingDirectory.exists()) params.workingDirectory.mkdirs()
        download()
        Archive().unpack(OS.CURRENT.fileType, archiveFile, params.workingDirectory)

        setExecutePermission(clientExec)
        setExecutePermission(serverExec)
    }

    override fun start() {
        val logFile = File(runningDirectory, "log.out")
        val dataDirectory = File(runningDirectory, "data")
        dataDirectory.mkdirs()

        println("starting mongo...")
        val process = Processes.run(
            redirect = ProcessBuilder.Redirect.appendTo(logFile),
            args = arrayOf(serverExec.absolutePath, "--port", "${params.port()}", "--dbpath", dataDirectory.absolutePath)
        )
        checkReadiness()

        pidManager.save(params, process.pid())

        println("client: ${clientExec.absolutePath}")
        println("log file: $logFile")
        running = true
    }

    private fun download() {
        if (archiveFile.exists()) {
            // println("skipping download, file exists: $archiveFile")
            // TODO: add force redownload option
            return
        }
        println("downloading from '$archiveUrl' to '$archiveFile'")

        val request = HttpRequest.newBuilder().GET().uri(URI(archiveUrl)).build()
        val responseBodyHandler = HttpResponse.BodyHandlers.ofFile(archiveFile.toPath())
        val response = HttpClient.newHttpClient().send(request, responseBodyHandler)
        if (response.statusCode() != 200) {
            throw RuntimeException("error downloading from ${archiveUrl}: ${response.statusCode()}")
        }
    }

    override fun close() {
        val pid = pidManager.get(params.port())
        if (pid != null) {
            println("killing mongo process $pid")
            Processes.run("kill", pid.toString()).waitFor()
        }
        pidManager.delete(params)
        running = false
    }

    private fun setExecutePermission(file: File) {
        file.setReadable(true)
        file.setWritable(true)
        file.setExecutable(true)
    }

    enum class OS(val type: String, val identifier: String, val fileType: Archive.FileType) {
        MAC("osx", "macos-x86_64", Archive.FileType.TAR),
        WINDOWS("windows", "windows-x86_64", Archive.FileType.ZIP),
        UBUNTU("linux", "linux-x86_64-ubuntu2004", Archive.FileType.TAR),
        CUSTOM(
            System.getProperty(OS.TYPE_KEY, "linux"),
            System.getProperty(OS.IDENTIFIER_KEY, "linux-x86_64-ubuntu1804"),
            Archive.FileType.valueOf(System.getProperty(OS.FILETYPE_KEY, Archive.FileType.TAR.name)));

        companion object {
            fun useCustom(): Boolean = System.getProperty(TYPE_KEY) != null ||
                    System.getProperty(IDENTIFIER_KEY) != null ||
                    System.getProperty(FILETYPE_KEY) != null

            val CURRENT: OS = if (useCustom()) CUSTOM
            else if (SystemUtils.IS_OS_MAC) MAC
            else if (SystemUtils.IS_OS_WINDOWS) WINDOWS
            else if (SystemUtils.IS_OS_LINUX) UBUNTU
            else throw RuntimeException("unknown OS: ${SystemUtils.OS_NAME}")

            const val TYPE_KEY = "mongo.os.type"
            const val IDENTIFIER_KEY = "mongo.os.identifier"
            const val FILETYPE_KEY = "mongo.os.fileType"
        }
    }
}