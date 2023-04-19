package dev.dry.common.time

import java.time.Clock
import java.time.LocalDateTime

interface TimeProvider {
    val clock: Clock

    fun now(): LocalDateTime = LocalDateTime.now(clock)

    companion object {
        operator fun invoke(clock: Clock): TimeProvider {
            return object : TimeProvider {
                override val clock: Clock = clock
            }
        }
    }
}
