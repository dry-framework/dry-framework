package dev.dry.http

@JvmInline
value class RequestId(val value: String) {
    override fun toString(): String = value
}
