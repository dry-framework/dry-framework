package dev.dry.objectstore

@JvmInline
value class ObjectName(val value: String) {
    override fun toString(): String = value
}
