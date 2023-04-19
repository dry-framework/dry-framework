package dev.dry.configuration.properties

import dev.dry.configuration.properties.SourceConfigurationProperties.PriorityComparator
import dev.dry.configuration.properties.system.SystemConfigurationProperties
import dev.dry.configuration.resource.ConfigurationResourceLoader

class ConfigurationPropertiesBuilder(
    private val systemConfigurationProperties: SystemConfigurationProperties,
    private val resourceLoaders: List<ConfigurationResourceLoader>,
) {
    private val resourceNames: MutableSet<String> = mutableSetOf()

    fun resourceName(resourceName: String): ConfigurationPropertiesBuilder {
        resourceNames += resourceName
        return this
    }

    fun build(): ConfigurationProperties {
        val activeProfiles = systemConfigurationProperties.activeProfiles
        val sources: MutableList<SourceConfigurationProperties> = mutableListOf(
            systemConfigurationProperties.systemSourceProperties,
            systemConfigurationProperties.environmentSourceProperties
        )

        resourceNames.forEach { resourceName ->
            resourceLoaders.map { resourceLoader -> resourceLoader.load(resourceName, activeProfiles) }
                .flatten()
                .forEach(sources::add)
        }

        sources.sortWith(PriorityComparator)

        val properties = mutableMapOf<String, String>()
        sources.forEach { properties.putAll(it.properties) }

        return DefaultConfigurationProperties(properties)
    }
}