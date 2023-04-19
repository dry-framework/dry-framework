package dev.dry.jwt.error

import dev.dry.common.error.ErrorCodeCollection
import dev.dry.common.error.toError

object JwtErrors : ErrorCodeCollection(1,"JWT error")  {
    val SERVER_ERROR = server.get(0, "server error").toError()
    val JWT_CREATION_FAILED = server.get(
        1,
        description = "JWT creation failed",
        errorMessage = "server error",
        exceptionMessage = "JWT creation failed",
    )

    val UNAUTHORIZED_ERROR = unauthorized.get(0, "unauthorized").toError()
    val JWT_TOKEN_EXPIRED = unauthorized.get(
        1,
        description = "authentication token expired",
    )
    val JWT_TOKEN_INVALID = unauthorized.get(
        2,
        description = "authentication token invalid",
    )
}
