package dev.dry.configuration.resource

import dev.dry.configuration.ActiveProfile
import dev.dry.configuration.properties.SourceConfigurationProperties

fun interface ConfigurationResourceLoader {
    fun load(baseResourceName: String, activeProfiles: Set<ActiveProfile>): List<SourceConfigurationProperties>
}
