package dev.dry.data.format.csv

fun interface CsvLineMapper<T> {
    operator fun invoke(line: List<String>): T
}