package dev.dry.common.error

import dev.dry.common.exception.CodedException
import dev.dry.common.text.message.DynamicMessage
import dev.dry.common.text.message.ParameterValueResolver

interface CodedError {
    val code: ErrorCode
    val message: String
    val exceptionMessage: String?
    val cause: Throwable?

    fun toException(): CodedException =
        CodedException(
            error = this,
            message = exceptionMessage ?: message,
            cause = cause,
        )

    open class DefaultCodedError(
        override val code: ErrorCode,
        override val message: String,
        override val exceptionMessage: String? = null,
        override val cause: Throwable? = null,
    ) : CodedError {
        constructor(error: CodedError) : this(error.code, error.message)

        constructor(
            errorTemplate: ErrorTemplate<DynamicMessage, DynamicMessage>,
            parameterValueResolver: ParameterValueResolver
        ) : this(errorTemplate.toError(parameterValueResolver))
    }
}
