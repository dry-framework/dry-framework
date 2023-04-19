package dev.dry.data.format.csv

import java.io.InputStream

object CsvReader {
    fun <T> read(
        inputStream: InputStream,
        skipFirstRow: Boolean,
        mapper: CsvLineMapper<T>,
    ): List<T> {
        val reader = inputStream.bufferedReader(Charsets.UTF_8)
        return reader.lineSequence()
            .drop(if (skipFirstRow) 1 else 0)
            .filter { it.isNotBlank() }
            .map { splitLine(it).map(CsvReader::removeSurroundingQuotes) }
            .map(mapper::invoke)
            .toList()
    }

    fun <T> read(
        inputStream: InputStream,
        skipFirstRow: Boolean,
        columns: Int,
        constructor: (List<String>) -> T,
    ): List<T> {
        return read(inputStream, skipFirstRow) { values ->
            if (values.size == columns && values.all(String::isNotBlank)) {
                constructor(values)
            } else null
        }.filterNotNull().toList()
    }

    private fun splitLine(line: String): List<String> = line.split(',')
    private fun removeSurroundingQuotes(string: String): String = string.trim().removeSurrounding("\"")
}
