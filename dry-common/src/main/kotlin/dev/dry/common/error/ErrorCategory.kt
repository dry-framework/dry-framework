package dev.dry.common.error

enum class ErrorCategory(val categoryNumber: Int, val description: String) {
    BAD_REQUEST(400,"bad request"),
    UNAUTHORIZED(401, "unauthorized"),
    FORBIDDEN(403, "forbidden"),
    NOT_FOUND(404, "not found"),
    CONFLICT(409, "conflict"),
    UNSUPPORTED_MEDIA_TYPE(415, "unsupported media type"),
    SERVER(500, "server error"),
    ;
}
