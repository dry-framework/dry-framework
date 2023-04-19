package dev.dry.configuration.resource

import dev.dry.configuration.properties.SourceConfigurationProperties
import dev.dry.common.io.resource.Resource

interface ConfigurationResource : Resource {
    val sourceType: SourceConfigurationProperties.Type
}
