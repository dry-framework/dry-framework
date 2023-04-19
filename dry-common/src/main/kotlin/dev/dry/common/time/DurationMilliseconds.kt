package dev.dry.common.time

import java.time.Duration

@JvmInline
value class DurationMilliseconds(val value: Long) {
    companion object {
        val ZERO = DurationMilliseconds(0)
    }

    fun toDuration(): Duration = Duration.ofMillis(value)

    override fun toString(): String = value.toString()
}
