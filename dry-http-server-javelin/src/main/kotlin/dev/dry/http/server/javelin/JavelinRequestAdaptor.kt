package dev.dry.http.server.javelin

import dev.dry.http.server.Headers
import dev.dry.http.server.Request
import io.javalin.http.Context
import java.io.InputStream
import java.nio.ByteBuffer
import kotlin.reflect.KClass

class JavelinRequestAdaptor(private val ctx: Context) : Request {
    override val headers: Headers get() = ctx.headerMap().entries.map { it.key to it.value }

    override fun headers(name: String): List<String> = listOf(ctx.header(name)).mapNotNull { it }

    override fun header(name: String): String? = ctx.header(name)

    override fun pathVariable(name: String): String? = ctx.pathParam(name)

    override fun queryParameters(name: String): List<String> = ctx.queryParams(name)

    override fun queryParameter(name: String): String? = ctx.queryParam(name)

    override fun bodyBuffer(): ByteBuffer = ByteBuffer.wrap(ctx.bodyAsBytes())

    override fun bodyStream(): InputStream = ctx.bodyInputStream()

    override fun bodyString(): String = ctx.body()

    override fun <T : Any> attribute(name: String, instance: T) = ctx.attribute(name, instance)

    override fun <T : Any> attribute(name: String, type: KClass<T>): T? {
        val map = ctx.attributeMap()
        return map[name]?.let { attribute ->
            if (!type.isInstance(attribute)) {
                throw IllegalArgumentException(
                    "request attribute with name '$name' is not an instance of '${type.qualifiedName}'"
                )
            }
            @Suppress("UNCHECKED_CAST")
            attribute as T
        }
    }

    companion object {
        const val JAVELIN_REQUEST_ADAPTOR_ATTRIBUTE_KEY = "JavelinRequestAdaptor"
    }
}
