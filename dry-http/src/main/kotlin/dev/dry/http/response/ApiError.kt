package dev.dry.http.response

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import dev.dry.common.error.CodedError
import dev.dry.common.error.ErrorCode
import dev.dry.common.error.ValidationError.ValidationErrorMessage
import dev.dry.http.response.ApiError.ApiErrorSerializer
import java.io.IOException

@JsonSerialize(using = ApiErrorSerializer::class)
open class ApiError(
    val code: ErrorCode,
    val message: String,
    val validationErrorMessages: List<ValidationErrorMessage>? = null,
) {
    class ApiErrorSerializer @JvmOverloads constructor(t: Class<ApiError>? = null) : StdSerializer<ApiError>(t) {
        @Throws(IOException::class, JsonProcessingException::class)
        override fun serialize(
            value: ApiError,
            jgen: JsonGenerator,
            provider: SerializerProvider?
        ) {
            jgen.writeStartObject()
            jgen.writeStringField("code", value.code.toString())
            jgen.writeStringField("message", value.message)
            jgen.writeEndObject()
        }
    }

    constructor(
        error: CodedError,
        validationErrorMessages: List<ValidationErrorMessage>? = null
    ): this(error.code, error.message, validationErrorMessages)
}
