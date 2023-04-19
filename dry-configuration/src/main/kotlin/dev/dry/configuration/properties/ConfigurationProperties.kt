package dev.dry.configuration.properties

interface ConfigurationProperties {
    fun get(key: String): String?
}