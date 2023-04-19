package dev.dry.configuration.properties

import dev.dry.configuration.properties.SourceConfigurationProperties.Type
import dev.dry.configuration.ActiveProfile

class DefaultSourceConfigurationProperties(
    override val properties: Map<String, String>,
    override val name: String,
    override val type: Type,
    override val activeProfile: dev.dry.configuration.ActiveProfile?
) : SourceConfigurationProperties
