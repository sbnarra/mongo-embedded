package com.github.sbnarra.mongo.core

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.assertions.Assertions
import com.mongodb.client.MongoClients
import java.util.concurrent.TimeUnit
import kotlin.test.Test

val params = MongoParams.withDefaults(port = 0)

class DockerMongoTest: MongoTest(DockerMongo(params.copy(type = MongoParams.Type.DOCKER)))

class BinaryMongoTest: MongoTest(MongoFactory().create(params.copy(type = MongoParams.Type.BINARY)))

abstract class MongoTest(val mongo: Mongo) {

    @Test
    fun testRunningDatabase() {
        mongo.start()
        Assertions.assertTrue(mongo.running)
        mongo.use {
            MongoClients.create(settings()).use { client ->
                client.listDatabases().forEach { db -> println(db) }
            }
        }
        Assertions.assertFalse(mongo.running)
    }

    private fun settings(): MongoClientSettings = MongoClientSettings.builder()
        .applyConnectionString(ConnectionString("mongodb://localhost:${mongo.params.port}/${javaClass.simpleName}"))
        .applyToSocketSettings { it
            .connectTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.SECONDS)
        }
        .applyToClusterSettings { it
            .serverSelectionTimeout(1, TimeUnit.SECONDS)
        }
        .build()
}