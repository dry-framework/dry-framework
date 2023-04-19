package dev.dry.configuration.properties.system

import dev.dry.configuration.properties.SourceConfigurationProperties
import dev.dry.configuration.properties.SourceConfigurationProperties.Type
import dev.dry.configuration.properties.SourceConfigurationProperties.Type.ENVIRONMENT_VARIABLES

// <APP-NAME>_EXTERNAL_CONFIG_LOCATION
// <APP-NAME>_ACTIVE_PROFILES

class SystemEnvironmentSourceConfigurationProperties private constructor(
    override val properties: Map<String, String>
) : SourceConfigurationProperties {
    companion object {
        private const val ENVIRONMENT_VARIABLE_NAME_DELIMITER = "_"
        private const val PROPERTY_NAME_DELIMITER = "."

        private fun mapEnvironmentVariableNameToPropertyName(environmentVariableName: String): String {
            return environmentVariableName
                .replace(ENVIRONMENT_VARIABLE_NAME_DELIMITER, PROPERTY_NAME_DELIMITER)
                .lowercase()
        }

        private fun filterEnvironmentVariablesByNamePrefix(
            environmentVariableByName: Map<String, String>,
            environmentVariableNamePrefix: String
        ): Map<String, String> {
            val prefix = "${environmentVariableNamePrefix.uppercase()}_"
            return environmentVariableByName
                .filterKeys { name -> name.startsWith(prefix) }
                .mapKeys { entry -> entry.key.replace(prefix, "") }
        }

        private fun loadProperties(environmentVariableNamePrefix: String): Map<String, String> {
            return filterEnvironmentVariablesByNamePrefix(System.getenv(), environmentVariableNamePrefix)
                .mapKeys { entry -> mapEnvironmentVariableNameToPropertyName(entry.key) }
        }

        fun load(environmentVariableNamePrefix: String): SystemEnvironmentSourceConfigurationProperties {
            return SystemEnvironmentSourceConfigurationProperties(loadProperties(environmentVariableNamePrefix))
        }
    }

    override val name: String = "system://environment"
    override val type: Type = ENVIRONMENT_VARIABLES
    override val activeProfile: dev.dry.configuration.ActiveProfile? = null
}
