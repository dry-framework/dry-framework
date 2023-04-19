package dev.dry.configuration.properties.system

import dev.dry.configuration.properties.DefaultConfigurationProperties

class SystemConfigurationProperties private constructor(
    properties: Map<String, String>,
    val systemSourceProperties: SystemSourceConfigurationProperties,
    val environmentSourceProperties: SystemEnvironmentSourceConfigurationProperties,
): DefaultConfigurationProperties(properties) {
    companion object {
        const val ACTIVE_PROFILES = "active.profiles"

        fun load(environmentVariableNamePrefix: String): SystemConfigurationProperties {
            val systemSourceProperties = SystemSourceConfigurationProperties.load()
            val environmentSourceProperties =
                SystemEnvironmentSourceConfigurationProperties.load(environmentVariableNamePrefix)
            return SystemConfigurationProperties(
                mutableMapOf<String, String>().apply {
                    putAll(systemSourceProperties.properties)
                    putAll(environmentSourceProperties.properties)
                },
                systemSourceProperties,
                environmentSourceProperties,
            )
        }

        fun parseActiveProfiles(value: String): Set<dev.dry.configuration.ActiveProfile> {
            val activeProfiles = value.split(",")
                .mapNotNull { it.trim().ifEmpty { null } }
                .mapIndexed(dev.dry.configuration.ActiveProfile::from)
            return linkedSetOf(*activeProfiles.toTypedArray())
        }
    }

    val activeProfiles: Set<dev.dry.configuration.ActiveProfile>

    init {
        activeProfiles = properties[ACTIVE_PROFILES]?.let(Companion::parseActiveProfiles) ?: emptySet()
    }
}
