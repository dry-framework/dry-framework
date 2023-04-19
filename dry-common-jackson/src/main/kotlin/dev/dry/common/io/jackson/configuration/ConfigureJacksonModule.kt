package dev.dry.common.io.jackson.configuration

import dev.dry.common.io.ObjectReader
import dev.dry.common.io.ObjectWriter
import dev.dry.common.io.jackson.JacksonObjectMapper
import dev.dry.dependency.DependencyRegistry

object ConfigureJacksonModule {
    operator fun invoke(registry: DependencyRegistry) {
        val jacksonObjectMapper = JacksonObjectMapper.construct()
        registry.instance(ObjectReader::class, jacksonObjectMapper)
        registry.instance(ObjectWriter::class, jacksonObjectMapper)
    }
}