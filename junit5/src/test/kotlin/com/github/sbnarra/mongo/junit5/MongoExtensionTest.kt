package com.github.sbnarra.mongo.junit5

import com.github.sbnarra.mongo.core.Mongo
import com.github.sbnarra.mongo.core.MongoParams
import com.github.sbnarra.mongo.test.InjectMongo
import com.github.sbnarra.mongo.test.InjectMongoParams
import com.github.sbnarra.mongo.test.TestMongoParams
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.assertions.Assertions
import com.mongodb.client.MongoClients
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.concurrent.TimeUnit

@ExtendWith(MongoExtension::class)
@TestMongoParams(port = -1)
class MongoExtensionTest {

    @InjectMongo
    lateinit var mongo: Mongo

    @InjectMongoParams
    lateinit var params: MongoParams

    @Test
    fun testRunningDatabase() {
        Assertions.assertTrue(mongo.running)
        mongo.use {
            MongoClients.create(settings()).use { client ->
                client.listDatabases().forEach { db -> println(db) }
            }
        }
        Assertions.assertFalse(mongo.running)
    }

    private fun settings(): MongoClientSettings = MongoClientSettings.builder()
        .applyConnectionString(ConnectionString("mongodb://localhost:${params.port}/${javaClass.simpleName}"))
        .applyToSocketSettings { it
            .connectTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.SECONDS)
        }
        .applyToClusterSettings { it
            .serverSelectionTimeout(1, TimeUnit.SECONDS)
        }
        .build()
}