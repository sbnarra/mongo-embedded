package com.github.sbnarra.mongo.junit5

import com.github.sbnarra.mongo.core.Mongo
import com.github.sbnarra.mongo.core.MongoFactory
import com.github.sbnarra.mongo.core.MongoParams
import com.github.sbnarra.mongo.test.InjectMongo
import com.github.sbnarra.mongo.test.InjectMongoParams
import com.github.sbnarra.mongo.test.TestMongoParams
import com.github.sbnarra.mongo.test.TestMongoParamsBuilder
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

class MongoExtension: BeforeAllCallback, BeforeEachCallback, AfterAllCallback {
    lateinit var mongo: Mongo

    override fun beforeAll(context: ExtensionContext?) {
        val testMongoParams = context!!.requiredTestClass.getDeclaredAnnotation(TestMongoParams::class.java)
        val params: MongoParams =
            if (testMongoParams == null) MongoParams.withDefaults()
            else TestMongoParamsBuilder().toMongoParams(testMongoParams)

        mongo = MongoFactory().create(params)
        mongo.start()
    }

    override fun afterAll(context: ExtensionContext?) = mongo.close()

    override fun beforeEach(context: ExtensionContext?) {
        injectFields(context!!, InjectMongo::class.java, mongo)
        injectFields(context, InjectMongoParams::class.java, mongo.params)
    }

    private fun injectFields(context: ExtensionContext, annotation: Class<out Annotation>, field: Any) {
        context.requiredTestClass.fields
            .filter { it.type.isInstance(field) }
            .filter { it.getDeclaredAnnotation(annotation) != null }
            .forEach { it.set(context.requiredTestInstance, field) }
    }
}