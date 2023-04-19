package dev.dry.http.server

import java.io.InputStream
import java.nio.ByteBuffer

typealias Header = Pair<String, String>
typealias Headers = List<Header>

interface HttpMessage {
    val headers: Headers

    fun headers(name: String): List<String> = headers.filter { it.first.equals(name, true) }.map { it.second }

    fun header(name: String): String? = headers.firstOrNull { it.first.equals(name, true) }?.second

    fun bodyBuffer(): ByteBuffer

    fun bodyStream(): InputStream {
        val buf = bodyBuffer()
        val length = buf.limit() - buf.position()
        return buf.array().inputStream(buf.position(), length)
    }

    fun bodyString(): String {
        val buf = bodyBuffer()
        val length = buf.limit() - buf.position()
        return String(buf.array(), buf.position(), length)
    }
}
