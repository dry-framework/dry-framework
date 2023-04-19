package dev.dry.common.io.resource

import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

interface Resource {
    val name: String

    fun openStream(): InputStream?

    @Throws(IOException::class, OutOfMemoryError::class)
    fun readAllBytes(): ByteArray? = openStream()?.readAllBytes()

    @Throws(IOException::class, OutOfMemoryError::class)
    fun readText(charset: Charset = Charsets.UTF_8): String? = readAllBytes()?.toString(charset)
}
