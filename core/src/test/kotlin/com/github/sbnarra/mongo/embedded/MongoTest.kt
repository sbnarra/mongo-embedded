package com.github.sbnarra.mongo.embedded

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.assertions.Assertions
import com.mongodb.client.MongoClients
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.test.Test

val mongoVersion: String = System.getProperty("mongo.version", "5.0.2")

//@Ignore // TODO: fix ci pipeline to work with docker
class DockerMongoTest: MongoTest(DockerMongo(MongoParams(
    version = mongoVersion,
    workingDirectory = File("build/mongo/docker")))
)

class BinaryMongoTest: MongoTest(BinaryMongo(MongoParams(
    version = mongoVersion,
    workingDirectory = File("build/mongo/binary")))
)

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
        .applyConnectionString(ConnectionString("mongodb://localhost:${mongo.params.port()}/${javaClass.simpleName}"))
        .applyToSocketSettings { it
            .connectTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.SECONDS)
        }
        .applyToClusterSettings { it
            .serverSelectionTimeout(1, TimeUnit.SECONDS)
        }
        .build()
}