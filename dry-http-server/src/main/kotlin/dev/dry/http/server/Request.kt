package dev.dry.http.server

import kotlin.reflect.KClass

interface Request : HttpMessage {
    fun pathVariable(name: String): String?
    fun queryParameters(name: String): List<String>
    fun queryParameter(name: String): String?
    fun <T: Any> attribute(name: String, instance: T)
    fun <T: Any> attribute(name: String, type: KClass<T>): T?
}

inline fun <reified T: Any> Request.attribute(name: String): T? = attribute(name, T::class)
