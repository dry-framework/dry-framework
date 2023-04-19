package dev.dry.http.server

import dev.dry.http.Status
import java.nio.ByteBuffer

interface Response : HttpMessage {
    val status: Status

    companion object {
        operator fun invoke(status: Status, body: String): Response {
            return ByteBufferResponse(status, emptyList(), ByteBuffer.wrap(body.toByteArray()))
        }

        operator fun invoke(status: Status, headers: Headers, bodyBytes: ByteArray): Response {
            return ByteBufferResponse(status, headers, ByteBuffer.wrap(bodyBytes))
        }
    }

    class ByteBufferResponse(
        override val status: Status,
        override val headers: Headers,
        private val bodyBuffer: ByteBuffer,
    ) : Response {
        override fun bodyBuffer(): ByteBuffer = bodyBuffer
    }
}
