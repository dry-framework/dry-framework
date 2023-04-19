package dev.dry.configuration.properties

import dev.dry.configuration.format.properties.PropertiesConfigurationFormat.name

open class DefaultConfigurationProperties(private val properties: Map<String, String>): ConfigurationProperties {
    override fun get(key: String): String? = properties[key]
}

