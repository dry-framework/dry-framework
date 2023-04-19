package dev.dry.configuration.format.properties

import java.util.*

object PropertiesToMap {
    operator fun invoke(properties: Properties): Map<String, String> = mapOf(
        *properties.entries.map { it.key.toString() to it.value.toString() }.toTypedArray()
    )
}
