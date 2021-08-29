package com.github.sbnarra.mongo.junit4

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.assertions.Assertions
import com.mongodb.client.MongoClients
import org.junit.ClassRule
import org.junit.Test
import java.util.concurrent.TimeUnit

class MongoRuleTest {

    companion object {
        @ClassRule @JvmField
        val rule = MongoTestRule()
    }

    @Test
    fun testRunningDatabase() {
        Assertions.assertTrue(rule.mongo.running)
        rule.mongo.use {
            MongoClients.create(settings()).use { client ->
                client.listDatabases().forEach { db -> println(db) }
            }
        }
        Assertions.assertFalse(rule.mongo.running)
    }

    private fun settings(): MongoClientSettings = MongoClientSettings.builder()
        .applyConnectionString(ConnectionString("mongodb://localhost:${rule.mongo.params.port()}/${javaClass.simpleName}"))
        .applyToSocketSettings { it
            .connectTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.SECONDS)
        }
        .applyToClusterSettings { it
            .serverSelectionTimeout(1, TimeUnit.SECONDS)
        }
        .build()
}