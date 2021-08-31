package com.github.sbnarra.mongo.core

import com.github.sbnarra.mongo.core.util.Archive
import com.github.sbnarra.mongo.core.util.OS
import org.slf4j.LoggerFactory
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.file.Paths

class MongoBinaryInstaller(
    private val params: MongoParams,
    private val forceReinstall: Boolean = false,
    binaryDir: File = Paths.get(System.getProperty("user.home"), ".mongo", MongoParams.Type.BINARY.name.toLowerCase()).toFile()) {

    private val name = "mongodb-${OS.CURRENT.identifier}-${params.version}"

    val directory = File(binaryDir, name)
    val clientExec = File(directory, "bin/mongo")
    val serverExec = File(directory, "bin/mongod")

    private val archiveFilename = "$name.${OS.CURRENT.fileType.extension}"
    private val archiveFile = File(binaryDir, archiveFilename)
    private val archiveUrl = "https://fastdl.mongodb.org/${OS.CURRENT.type}/${archiveFilename}"

    fun install() {
        download()

        if (forceReinstall && directory.exists()) delete(directory)
        Archive().unpack(OS.CURRENT.fileType, archiveFile, params.workingDir)

        makeExecutable(clientExec)
        makeExecutable(serverExec)
    }

    private fun makeExecutable(file: File) = file.setExecutable(true)

    private fun delete(file: File) {
        if (file.isDirectory) file.list()?.forEach { delete(File(it)) }
        file.delete()
    }

    private fun download() {
        if (archiveFile.exists()) {
            if (!forceReinstall) return

            log.info("deleting archive $archiveFile")
            delete(archiveFile)
        }

        log.debug("downloading from '$archiveUrl' to '$archiveFile'")
        BufferedInputStream(URL(archiveUrl).openStream()).use { inputStream ->
            FileOutputStream(archiveFile).use { fileOutputStream ->
                val dataBuffer = ByteArray(1024)
                var bytesRead: Int
                while (inputStream.read(dataBuffer, 0, 1024).also { bytesRead = it } != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead)
                }
            }
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(BinaryMongo::class.java)
    }
}