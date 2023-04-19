package dev.dry.http.server.error

import dev.dry.common.error.CodedError
import dev.dry.common.error.ErrorCodeCollection
import dev.dry.common.error.toError
import dev.dry.common.text.message.DynamicMessage
import dev.dry.common.text.message.MapParameterValueResolver.Companion.parameters

object HttpErrors : ErrorCodeCollection(1,"http error")  {
    val SERVER_ERROR = server.get(0, "server error").toError()
    val JWT_CREATION_FAILED = server.get(
        1,
        description = "JWT creation failed",
        errorMessage = "server error",
        exceptionMessage = "JWT creation failed",
    )

    val UNAUTHORIZED_ERROR = unauthorized.get(0, "unauthorized").toError()
    val AUTHENTICATION_TOKEN_EXPIRED = unauthorized.get(
        1,
        description = "authentication token expired",
    )
    val AUTHENTICATION_TOKEN_INVALID = unauthorized.get(
        2,
        description = "authentication token invalid",
    )

    val BAD_REQUEST = badRequest.get(0, "bad request").toError()
    val INVALID_QUERY_PARAMETER = badRequest.get(
        1,
        "invalid query parameter",
        DynamicMessage {
            "invalid query parameter '${it["name"]}' expected a valid '${it["type"]}'"
        }
    )
    val MISSING_REQUIRED_QUERY_PARAMETER = badRequest.get(
        2,
        "required query parameter must not be blank",
        DynamicMessage { "required query parameter '${it["name"]}' must not be blank" }
    )
    val MISSING_REQUIRED_PATH_PARAMETER = badRequest.get(
        3,
        "required path parameter must not be blank",
        DynamicMessage { "required path parameter '${it["name"]}' must not be blank" }
    )
    private val INVALID_REQUEST_BODY_PROPERTY_PARAMETER = badRequest.get(
        4,
        "invalid request body property",
        DynamicMessage { "invalid '${it["name"]}'" }
    )
    fun invalidRequestBodyProperty(propertyName: String): CodedError {
        return INVALID_REQUEST_BODY_PROPERTY_PARAMETER.toError(parameters("name" to propertyName))
    }

    val NOT_FOUND = notFound.get(0, "not found").toError()

    // ========================================================================
    // ---{ UNSUPPORTED_MEDIA_TYPE(415) }---
    // ========================================================================

    val UNSUPPORTED_MEDIA_TYPE = unsupportedMediaType.get(0, "unsupported media type").toError()
}
