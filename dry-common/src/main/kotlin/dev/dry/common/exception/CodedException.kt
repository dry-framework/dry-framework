package dev.dry.common.exception

import dev.dry.common.error.CodedError

open class CodedException(
    val error: CodedError,
    message: String? = null,
    cause: Throwable? = null,
): RuntimeException(message ?: error.message, cause)
