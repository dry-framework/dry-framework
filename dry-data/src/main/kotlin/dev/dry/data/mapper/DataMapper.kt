package dev.dry.data.mapper

fun interface DataMapper<S, T> {
    fun map(value: S): T
}
