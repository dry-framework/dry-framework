package dev.dry.configuration.resource

import dev.dry.configuration.properties.SourceConfigurationProperties.Type
import dev.dry.configuration.properties.SourceConfigurationProperties.Type.CLASSPATH
import dev.dry.configuration.properties.SourceConfigurationProperties.Type.FILE_SYSTEM
import dev.dry.common.io.resource.ClassPathResource
import dev.dry.common.io.resource.FileResource
import dev.dry.common.io.resource.Resource
import java.io.InputStream

class DefaultConfigurationResource(
    private val resource: Resource,
    override val sourceType: Type,
): ConfigurationResource {
    companion object {
        fun classpath(resourceName: String): DefaultConfigurationResource = DefaultConfigurationResource(
            resource = ClassPathResource(resourceName),
            sourceType = CLASSPATH
        )

        fun file(filePath: String): DefaultConfigurationResource = DefaultConfigurationResource(
            resource = FileResource(filePath),
            sourceType = FILE_SYSTEM
        )
    }

    override val name: String get() = resource.name
    override fun openStream(): InputStream? = resource.openStream()
}
