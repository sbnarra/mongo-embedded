package com.github.sbnarra.mongo.junit4

import com.github.sbnarra.mongo.embedded.MongoFactory
import com.github.sbnarra.mongo.embedded.MongoParams
import org.junit.rules.ExternalResource

class MongoTestRule: ExternalResource() {
    val mongo = MongoFactory().create(MongoParams())
    override fun before() = mongo.start()
    override fun after() = mongo.close()
}
