package dev.dry.configuration.properties

open class DefaultConfigurationProperties(private val properties: Map<String, String>): ConfigurationProperties {
    override fun get(name: String): String? = properties[name]
}

