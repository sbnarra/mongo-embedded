package com.github.sbnarra.mongo.test

import com.github.sbnarra.mongo.embedded.MongoParams
import java.io.File

class TestMongoParamsBuilder {
    fun toMongoParams(testMongoParams: TestMongoParams): MongoParams {
        val defaults = MongoParams()
        return MongoParams(
            version = if (testMongoParams.version != "") testMongoParams.version else defaults.version,
            port = if (testMongoParams.port != MongoParams.DEFAULT_PORT) testMongoParams.port else defaults.port(),
            type = if (testMongoParams.type != MongoParams.Type.BINARY) testMongoParams.type else defaults.type,
            id = if (testMongoParams.id != "") testMongoParams.id else defaults.id,
            workingDirectory = if (testMongoParams.workingDirectory != "") File(testMongoParams.workingDirectory) else defaults.workingDirectory,
        )
    }
}
