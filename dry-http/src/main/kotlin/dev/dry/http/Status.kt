package dev.dry.http

import dev.dry.common.error.ErrorCategory

enum class Status {
    OK,
    BAD_REQUEST,
    UNAUTHORIZED,
    FORBIDDEN,
    NOT_FOUND,
    CONFLICT,
    UNSUPPORTED_MEDIA_TYPE,
    SERVER,
    ;

    companion object {
        fun from(errorCategory: ErrorCategory): Status = when(errorCategory) {
            ErrorCategory.BAD_REQUEST -> BAD_REQUEST
            ErrorCategory.UNAUTHORIZED -> UNAUTHORIZED
            ErrorCategory.FORBIDDEN -> FORBIDDEN
            ErrorCategory.NOT_FOUND -> NOT_FOUND
            ErrorCategory.CONFLICT -> CONFLICT
            ErrorCategory.UNSUPPORTED_MEDIA_TYPE -> UNSUPPORTED_MEDIA_TYPE
            ErrorCategory.SERVER -> SERVER
        }
    }
}