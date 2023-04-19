package dev.dry.common.error

import dev.dry.common.error.CodedError.DefaultCodedError
import dev.dry.common.exception.CodedException
import dev.dry.common.text.message.Message
import dev.dry.common.text.message.ParameterValueResolver
import dev.dry.common.text.message.ParameterisedMessage
import dev.dry.common.text.message.StaticMessage

open class ErrorTemplate<out R: Message, out X: Message>(
    val code: ErrorCode,
    val errorMessage: R,
    val exceptionMessage: X,
)

fun ErrorTemplate<ParameterisedMessage, ParameterisedMessage>.toError(
    parameterValueResolver: ParameterValueResolver,
    cause: Throwable? = null,
): CodedError {
    return DefaultCodedError(
        code = code,
        message = errorMessage.toString(parameterValueResolver),
        exceptionMessage = exceptionMessage.toString(parameterValueResolver),
        cause = cause,
    )
}

fun ErrorTemplate<ParameterisedMessage, ParameterisedMessage>.toException(
    parameterValueResolver: ParameterValueResolver,
    cause: Throwable? = null,
): CodedException = toError(parameterValueResolver, cause).toException()

fun ErrorTemplate<StaticMessage, StaticMessage>.toError(
    cause: Throwable? = null,
): CodedError {
    return DefaultCodedError(
        code = code,
        message = errorMessage.toString(),
        exceptionMessage = exceptionMessage.toString(),
        cause = cause,
    )
}

fun ErrorTemplate<StaticMessage, StaticMessage>.toException(
    cause: Exception? = null,
): CodedException = toError(cause).toException()
