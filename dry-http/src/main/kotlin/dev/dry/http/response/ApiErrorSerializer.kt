package dev.dry.http.response

/*
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
*/