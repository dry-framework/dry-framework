package dev.dry.http.server.parameter

import dev.dry.common.error.toError
import dev.dry.common.text.message.MapParameterValueResolver.Companion.parameters
import dev.dry.http.server.Request
import dev.dry.http.server.error.HttpErrors
import dev.dry.http.server.handler.ParameterValueMapper

class QueryParameterResolver(
    override val parameterName: String,
    override val required: Boolean,
    private val parameterValueMapper: ParameterValueMapper,
) : ParameterResolver {
    override fun resolve(request: Request): Any? {
        val value = request.queryParameter(parameterName)
        if (value != null) return parameterValueMapper(value)

        if (required) {
            throw HttpErrors.MISSING_REQUIRED_QUERY_PARAMETER
                .toError(parameters("name" to parameterName))
                .toException()
        }

        return null
    }
}
