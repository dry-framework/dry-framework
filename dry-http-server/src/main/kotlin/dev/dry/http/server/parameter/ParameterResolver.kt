package dev.dry.http.server.parameter

import dev.dry.http.server.Request

interface ParameterResolver {
    val parameterName: String
    val required: Boolean

    fun resolve(request: Request): Any?
}
