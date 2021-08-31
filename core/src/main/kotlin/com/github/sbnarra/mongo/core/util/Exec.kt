package com.github.sbnarra.mongo.core.util

import java.lang.reflect.Field

class Exec private constructor(){
    companion object {
        fun run(vararg args: String): Process = run(redirect = ProcessBuilder.Redirect.INHERIT, args = args)

        fun run(redirect: ProcessBuilder.Redirect = ProcessBuilder.Redirect.INHERIT, vararg args: String): Process {
            log.info("exec($redirect): ${args.toList()}")
            return ProcessBuilder(args.toList())
                .redirectErrorStream(true)
                .redirectOutput(redirect)
                .start()
        }

        fun findPid(p: Process): Int {
            return when (p.javaClass.name) {
                "java.lang.Win32Process" -> winPid()
                "java.lang.UNIXProcess", "java.lang.ProcessImpl" -> unixPid(p)
                else -> -1
            }.toInt()
        }

        private fun unixPid(p: Process): Long {
            val f: Field = p.javaClass.getDeclaredField("pid")
            try {
                f.isAccessible = true
                return f.getLong(p)
            } finally {
                f.isAccessible = false
            }
        }

        private fun winPid(): Long = throw TODO("need to write to retrieve the pid on windows")

        private val log by LoggerDelegate()
    }
}