package com.github.sbnarra.mongo.embedded.util

import org.apache.commons.compress.archivers.ArchiveEntry
import org.apache.commons.compress.archivers.ArchiveInputStream
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream
import java.io.BufferedInputStream
import java.io.File
import java.nio.file.Files

class Archive {
    fun unpack(fileType: FileType, source: File, target: File) {
        Files.newInputStream(source.toPath()).use { inputStream ->
            BufferedInputStream(inputStream).use { bufferedInputStream ->
                when (fileType) {
                    FileType.TAR -> unpackTar(bufferedInputStream, target)
                    FileType.ZIP -> unpackZip(bufferedInputStream, target)
                }
            }
        }
    }

    private fun unpackTar(bufferedInputStream: BufferedInputStream, target: File) =
        GzipCompressorInputStream(bufferedInputStream).use { gzipSource ->
            TarArchiveInputStream(gzipSource).use { unpackArchive(it, target) }
        }

    private fun unpackZip(bufferedInputStream: BufferedInputStream, target: File) =
        ZipArchiveInputStream(bufferedInputStream).use { unpackArchive(it, target) }

    private fun unpackArchive(tarSource: ArchiveInputStream, target: File) {
        var entry: ArchiveEntry?
        while (tarSource.nextEntry.also { entry = it } != null) {
            val file = File(target, entry!!.name)
            if (file.exists()) continue
            file.parentFile.mkdirs()
            Files.copy(tarSource, file.toPath())
        }
    }

    enum class FileType(val extension: String) {
        TAR("tgz"), ZIP("zip")
    }}