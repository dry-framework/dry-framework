package dev.dry.configuration.properties

import dev.dry.configuration.ActiveProfile

interface SourceConfigurationProperties {
    enum class Type {
        CLASSPATH,
        FILE_SYSTEM,
        SYSTEM_PROPERTIES,
        ENVIRONMENT_VARIABLES,
        CONFIGURATION_SERVER,
    }

    object PriorityComparator : Comparator<SourceConfigurationProperties> {
        override fun compare(lhs: SourceConfigurationProperties, rhs: SourceConfigurationProperties): Int {
            val typeComparisonResult = lhs.type.ordinal.compareTo(rhs.type.ordinal)
            if (typeComparisonResult != 0) return typeComparisonResult
            return lhs.activeProfile?.priority?.compareTo(rhs.activeProfile?.priority ?: -1)
                ?: rhs.activeProfile?.priority
                ?: -1
        }
    }

    val properties: Map<String, String>
    val name: String
    val type: Type
    val activeProfile: ActiveProfile?
}
