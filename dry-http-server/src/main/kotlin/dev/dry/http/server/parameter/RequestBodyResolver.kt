package dev.dry.http.server.parameter

import dev.dry.common.io.ObjectReader
import dev.dry.http.server.Request

class RequestBodyResolver(
    override val parameterName: String,
    private val bodyClass: Class<*>,
    private val objectReader: ObjectReader,
) : ParameterResolver {
    override val required: Boolean = true

    override fun resolve(request: Request): Any? {
        val bodyBuffer = request.bodyBuffer()
        val position = bodyBuffer.position()
        val length = bodyBuffer.limit() - position
        val stream = bodyBuffer.array().inputStream(position, length)
        return objectReader.read(stream, bodyClass)
    }
}
