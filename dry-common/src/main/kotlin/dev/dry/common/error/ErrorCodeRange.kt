package dev.dry.common.error

import dev.dry.common.text.message.Message
import dev.dry.common.text.message.ParameterisedMessage
import dev.dry.common.text.message.StaticMessage
import dev.dry.common.text.message.StaticParameterisedMessage

class ErrorCodeRange(val category: ErrorCategory, private val group: ErrorGroup, private val codeRange: IntRange) {
    val first: Int get() = codeRange.first

    val last: Int get() = codeRange.last

    private val errorCodeByIndex = mutableMapOf<Int, ErrorCode>()

    val errorCodes: Collection<ErrorCode> get() = errorCodeByIndex.values

    private fun errorCodeFromIndex(index: Int, description: String): ErrorCode {
        val code = first + index
        if (codeRange.contains(code)) {
            return ErrorCode.from(category, group, code, description)
        }
        throw IllegalArgumentException("invalid error code index $index for range [$codeRange] of $category-$group")
    }

    fun <R : Message, X : Message> get(
        index: Int,
        description: String,
        errorMessage: R,
        exceptionMessage: X,
    ): ErrorTemplate<R, X> {
        errorCodeByIndex.computeIfPresent(index) { _, existingErrorCode ->
            throw IllegalStateException(
                "failed to register error code index '$index' with description '$description' " +
                        "for category '$category' within group '${group.description}' (${group.groupNumber}) - " +
                        "already registered with description '${existingErrorCode.description}'"
            )
        }
        val errorCode = errorCodeFromIndex(index, description)
        errorCodeByIndex[index] = errorCode
        return ErrorTemplate(errorCode, errorMessage, exceptionMessage)
    }

    fun <R : ParameterisedMessage> get(
        index: Int,
        description: String,
        errorMessage: R,
    ): ErrorTemplate<R, R> = get(index, description, errorMessage, errorMessage)

    fun <X : ParameterisedMessage> get(
        index: Int,
        description: String,
        errorMessage: String,
        exceptionMessage: X,
    ): ErrorTemplate<ParameterisedMessage, X> = get(index, description, StaticParameterisedMessage(errorMessage), exceptionMessage)

    fun <R : ParameterisedMessage> get(
        index: Int,
        description: String,
        errorMessage: R,
        exceptionMessage: String,
    ): ErrorTemplate<R, ParameterisedMessage> = get(index, description, errorMessage, StaticParameterisedMessage(exceptionMessage))

    fun get(
        index: Int,
        description: String,
        errorMessage: String,
        exceptionMessage: String,
    ): ErrorTemplate<StaticMessage, StaticMessage> = get(
        index,
        description,
        StaticMessage(errorMessage),
        StaticMessage(exceptionMessage),
    )

    fun get(
        index: Int,
        description: String,
        errorMessage: String
    ): ErrorTemplate<StaticMessage, StaticMessage> = get(index, description, errorMessage, errorMessage)

    fun get(
        index: Int,
        description: String,
    ): ErrorTemplate<StaticMessage, StaticMessage> = get(index, description, description, description)
}
