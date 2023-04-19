package dev.dry.configuration.format.properties

import java.io.InputStream
import java.util.*

object InputStreamToProperties {
    operator fun invoke(inputStream: InputStream): Properties = Properties().also { it.load(inputStream) }
}
