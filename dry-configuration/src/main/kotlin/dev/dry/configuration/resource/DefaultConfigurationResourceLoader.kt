package dev.dry.configuration.resource

import dev.dry.configuration.ActiveProfile
import dev.dry.configuration.format.ConfigurationFormat
import dev.dry.configuration.format.ConfigurationFormatExtension
import dev.dry.configuration.properties.DefaultSourceConfigurationProperties
import dev.dry.configuration.properties.SourceConfigurationProperties

open class DefaultConfigurationResourceLoader(
    private val format: ConfigurationFormat,
    private val constructResource: (String) -> ConfigurationResource,
) : ConfigurationResourceLoader {
    companion object {
        /**
         * activeProfiles: priority ordered, from lowest to highest, set of active profiles
         **/
        fun constructResource(
            baseName: String,
            activeProfile: dev.dry.configuration.ActiveProfile?,
            formatExtension: ConfigurationFormatExtension,
            constructResource: (String) -> ConfigurationResource
        ): ConfigurationResource = constructResource(constructResourceName(baseName, activeProfile, formatExtension))

        private fun constructResourceName(
            baseName: String,
            activeProfile: dev.dry.configuration.ActiveProfile?,
            formatExtension: ConfigurationFormatExtension,
        ): String {
            return activeProfile?.let { "$baseName-${activeProfile.name}.${formatExtension.value}" }
                ?: "$baseName.${formatExtension.value}"
        }
    }

    override fun load(
        baseResourceName: String,
        activeProfiles: Set<dev.dry.configuration.ActiveProfile>
    ): List<SourceConfigurationProperties> {
        return (listOf(null) + activeProfiles)
            .map { it to constructResource(baseResourceName, it, format.extension, constructResource) }
            .mapNotNull { (profile, resource) -> loadSourceConfigurationProperties(resource, profile) }
    }

    open fun loadSourceConfigurationProperties(
        resource: ConfigurationResource,
        activeProfile: dev.dry.configuration.ActiveProfile?,
    ): SourceConfigurationProperties? {
        val inputStream = resource.openStream() ?: return null
        return DefaultSourceConfigurationProperties(
            properties = format.reader.read(inputStream),
            name = resource.name,
            type = resource.sourceType,
            activeProfile = activeProfile,
        )
    }
}
