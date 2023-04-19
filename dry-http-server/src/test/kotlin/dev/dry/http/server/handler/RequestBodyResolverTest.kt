package dev.dry.http.server.handler

import dev.dry.common.io.jackson.JacksonObjectMapper
import dev.dry.common.io.jackson.TestData.Companion.TEST_DATA_OBJECT
import dev.dry.common.io.jackson.TestData.Companion.TEST_DATA_STRING
import dev.dry.common.io.jackson.TestData.Companion.getReflectedClass
import dev.dry.http.server.Headers
import dev.dry.http.server.Request
import dev.dry.http.server.parameter.RequestBodyResolver
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.nio.ByteBuffer
import kotlin.reflect.KClass

class RequestBodyResolverTest {
    companion object {
        private fun parameters(): List<Class<*>> = listOf(getReflectedClass())

        val unit = RequestBodyResolver(
            parameterName = "requestBody",
            bodyClass = parameters().first(),
            objectReader = JacksonObjectMapper.construct(),
        )

        val REQUEST_DUMMY = object: Request {
            override fun pathVariable(name: String): String? = notImplemented()
            override fun queryParameters(name: String): List<String> = notImplemented()
            override fun queryParameter(name: String): String? = notImplemented()
            override fun <T : Any> attribute(name: String, instance: T): Unit = notImplemented()
            override fun <T : Any> attribute(name: String, type: KClass<T>): T? = null

            override val headers: Headers get() = notImplemented()

            override fun bodyBuffer(): ByteBuffer = ByteBuffer.wrap(TEST_DATA_STRING.toByteArray())

            inline fun <reified T> notImplemented(): T {
                throw RuntimeException("dummy method not implemented")
            }
        }
    }

    @Test
    fun resolve() {
        val result = unit.resolve(REQUEST_DUMMY)
        assertEquals(TEST_DATA_OBJECT, result)
    }
}