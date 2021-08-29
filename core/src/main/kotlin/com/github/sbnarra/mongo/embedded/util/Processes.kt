package com.github.sbnarra.mongo.embedded.util

import java.lang.reflect.Field

class Processes {
    companion object {
        fun run(vararg args: String, redirect: ProcessBuilder.Redirect = ProcessBuilder.Redirect.INHERIT): Process =
            ProcessBuilder(args.toList())
                .redirectErrorStream(true)
                .redirectOutput(redirect)
                .start()

        fun getProcessID(p: Process): Long {
            return when (p.javaClass.name) {
                "java.lang.Win32Process", "java.lang.ProcessImpl" -> winPid()
                "java.lang.UNIXProcess" -> linuxPid(p)
                else -> -1
            }
        }

        private fun linuxPid(p: Process): Long {
            val f: Field = p.javaClass.getDeclaredField("pid")
            try {
                f.isAccessible = true
                return f.getLong(p)
            } finally {
                f.isAccessible = false
            }
        }

        private fun winPid(): Long = throw TODO("need to write to retrieve the pid on windows")
    }
}