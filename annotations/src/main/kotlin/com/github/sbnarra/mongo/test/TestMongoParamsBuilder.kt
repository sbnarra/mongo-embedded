package com.github.sbnarra.mongo.test

import com.github.sbnarra.mongo.core.MongoParams
import java.io.File
import java.util.*

class TestMongoParamsBuilder {
    fun toMongoParams(testMongoParams: TestMongoParams,
                      getValue: (key: String, default: Any) -> String = MongoParams.GET_SYS_PROP): MongoParams {
        return MongoParams.withDefaults(
            getValue = getValue,
            defaults = MongoParams(
                id = if (testMongoParams.id != "") testMongoParams.id else UUID.randomUUID().toString(),
                version = testMongoParams.version,
                port = testMongoParams.port,
                type = testMongoParams.type,
                workingDir = File(testMongoParams.workingDirectory),
                startupTimeout = testMongoParams.startupTimeout,
            )
        )
    }
}
