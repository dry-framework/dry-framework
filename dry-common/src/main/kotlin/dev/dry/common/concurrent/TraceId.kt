package dev.dry.common.concurrent

import java.util.*

@JvmInline
value class TraceId(val value: String) {
    override fun toString(): String = value

    companion object {
        fun newInstance(): TraceId = TraceId(UUID.randomUUID().toString())
    }
}
