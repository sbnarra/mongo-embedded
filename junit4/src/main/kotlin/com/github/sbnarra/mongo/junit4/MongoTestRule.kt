package com.github.sbnarra.mongo.junit4

import com.github.sbnarra.mongo.core.MongoFactory
import com.github.sbnarra.mongo.core.MongoParams
import org.junit.rules.ExternalResource

class MongoTestRule: ExternalResource() {
    val mongo = MongoFactory().create(MongoParams.withDefaults(port = 0))
    override fun before() = mongo.start()
    override fun after() = mongo.close()
}
