package dev.dry.common.io

import java.io.ByteArrayInputStream
import java.io.InputStream
import kotlin.reflect.KClass

interface ObjectReader {
    fun <T: Any> read(input: InputStream, objectClass: Class<T>): T

    fun <T: Any> read(inputStream: InputStream, objectClass: KClass<T>): T {
        return read(inputStream, objectClass.java)
    }
}

inline fun <reified T: Any> ObjectReader.read(input: InputStream): T = read(input, T::class.java)

inline fun <reified T: Any> ObjectReader.read(input: String): T = read(input, T::class.java)
fun <T: Any> ObjectReader.read(input: String, objectClass: KClass<T>): T = read(input, objectClass.java)
fun <T: Any> ObjectReader.read(input: String, objectClass: Class<T>): T {
    return read(ByteArrayInputStream(input.toByteArray()), objectClass)
}
