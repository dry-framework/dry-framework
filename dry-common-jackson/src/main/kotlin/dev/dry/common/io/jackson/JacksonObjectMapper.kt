package dev.dry.common.io.jackson

import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES
import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES
import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS
import com.fasterxml.jackson.databind.DeserializationFeature.USE_BIG_INTEGER_FOR_INTS
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.kotlinModule
import dev.dry.common.io.ObjectReader
import dev.dry.common.io.ObjectWriter
import java.io.InputStream
import java.io.OutputStream

class JacksonObjectMapper(private val objectMapper: ObjectMapper) : ObjectReader, ObjectWriter {
    companion object {
        fun construct(): JacksonObjectMapper {
            val kotlinModule = kotlinModule()
            val objectMapper = ObjectMapper()
                .registerModule(kotlinModule)
                .registerModule(JavaTimeModule())
                .deactivateDefaultTyping()
                .configure(WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(FAIL_ON_NULL_FOR_PRIMITIVES, true)
                .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(FAIL_ON_IGNORED_PROPERTIES, false)
                .configure(USE_BIG_DECIMAL_FOR_FLOATS, false)
                .configure(USE_BIG_INTEGER_FOR_INTS, false)
                .also {
                    it.dateFormat = StdDateFormat()
                }

            return JacksonObjectMapper(objectMapper)
        }
    }

    override fun <T : Any> read(input: InputStream, objectClass: Class<T>): T {
        return objectMapper.readValue(input, objectClass)
    }

    override fun <T : Any> write(output: OutputStream, instance: T) {
        return objectMapper.writeValue(output, instance)
    }
}
