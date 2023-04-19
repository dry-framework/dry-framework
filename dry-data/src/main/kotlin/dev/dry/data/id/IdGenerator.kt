package dev.dry.data.id

fun interface IdGenerator {
    fun nextNumber(): Long
}