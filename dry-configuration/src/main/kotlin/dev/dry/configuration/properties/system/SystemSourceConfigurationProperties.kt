package dev.dry.configuration.properties.system

import dev.dry.configuration.format.properties.PropertiesToMap
import dev.dry.configuration.properties.SourceConfigurationProperties
import dev.dry.configuration.properties.SourceConfigurationProperties.Type
import dev.dry.configuration.properties.SourceConfigurationProperties.Type.SYSTEM_PROPERTIES

class SystemSourceConfigurationProperties(override val properties: Map<String, String>) :
    SourceConfigurationProperties {
    companion object {
        fun load(): SystemSourceConfigurationProperties = SystemSourceConfigurationProperties(
            PropertiesToMap(System.getProperties())
        )
    }

    override val name: String = "system://properties"
    override val type: Type = SYSTEM_PROPERTIES
    override val activeProfile: dev.dry.configuration.ActiveProfile? = null
}
