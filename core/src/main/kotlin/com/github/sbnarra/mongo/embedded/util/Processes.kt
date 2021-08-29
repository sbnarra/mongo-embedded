package com.github.sbnarra.mongo.embedded.util

class Processes {
    companion object {
        fun run(vararg args: String,
                redirect: ProcessBuilder.Redirect = ProcessBuilder.Redirect.INHERIT): Process =
            ProcessBuilder(args.toList())
                .redirectErrorStream(true)
                .redirectOutput(redirect)
                .start()
    }
}