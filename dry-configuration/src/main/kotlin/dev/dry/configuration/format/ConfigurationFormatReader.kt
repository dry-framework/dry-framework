package dev.dry.configuration.format

import java.io.InputStream

fun interface ConfigurationFormatReader {
    fun read(inputStream: InputStream): Map<String, String>
}
