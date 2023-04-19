package dev.dry.configuration.format

interface ConfigurationFormat {
    val name: ConfigurationFormatName
    val extension: ConfigurationFormatExtension
    val reader: ConfigurationFormatReader
}
