package com.github.sbnarra.mongo.core.util

enum class OS(val type: String, val identifier: String, val fileType: Archive.FileType) {

    MAC("osx", "macos-x86_64", Archive.FileType.TAR),
    WINDOWS("windows", "windows-x86_64", Archive.FileType.ZIP),
    UBUNTU("linux", "linux-x86_64-ubuntu2004", Archive.FileType.TAR),
    CUSTOM(
        System.getProperty(OS.TYPE, "linux"),
        System.getProperty(OS.IDENTIFIER, "linux-x86_64-ubuntu1804"),
        Archive.FileType.valueOf(System.getProperty(OS.FILETYPE, Archive.FileType.TAR.name)));

    companion object {
        private const val TYPE = "mongo.os.type"
        private const val IDENTIFIER = "mongo.os.identifier"
        private const val FILETYPE = "mongo.os.fileType"

        private val useCustomOS = System.getProperty(TYPE) != null ||
                System.getProperty(IDENTIFIER) != null ||
                System.getProperty(FILETYPE) != null

        private val OS_NAME = System.getProperty("os.name").toLowerCase()

        val CURRENT: OS =
            if (useCustomOS) CUSTOM
            else if (OS_NAME.indexOf("mac") >= 0) MAC
            else if (OS_NAME.indexOf("win") >= 0) WINDOWS
            else if (OS_NAME.indexOf("nix") >= 0 || OS_NAME.indexOf("nux") >= 0 || OS_NAME.indexOf("aix") > 0) UBUNTU
            else throw RuntimeException("unknown OS: $OS_NAME")
    }
}
