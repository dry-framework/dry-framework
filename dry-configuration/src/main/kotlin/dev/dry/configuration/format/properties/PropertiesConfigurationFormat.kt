package dev.dry.configuration.format.properties

import dev.dry.configuration.format.ConfigurationFormat
import dev.dry.configuration.format.ConfigurationFormatExtension
import dev.dry.configuration.format.ConfigurationFormatName
import dev.dry.configuration.format.ConfigurationFormatReader

object PropertiesConfigurationFormat : ConfigurationFormat {
    override val name: ConfigurationFormatName = ConfigurationFormatName("properties")
    override val extension: ConfigurationFormatExtension = ConfigurationFormatExtension("properties")
    override val reader: ConfigurationFormatReader = ConfigurationFormatReader { inputStream ->
        PropertiesToMap(InputStreamToProperties(inputStream))
    }
}
