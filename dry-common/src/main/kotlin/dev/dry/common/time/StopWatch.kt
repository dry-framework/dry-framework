package dev.dry.common.time

import java.time.Duration
import java.time.temporal.ChronoUnit

class StopWatch private constructor() {
    companion object {
        @JvmStatic
        fun start(): StopWatch = StopWatch()
    }

    private val start = System.nanoTime()

    val elapsedTimeNanos: Long get() = System.nanoTime() - start

    val elapsedTimeMillis: Long get() = elapsedTime(ChronoUnit.MILLIS)

    fun elapsedTime(unit: ChronoUnit): Long {
        val duration = Duration.of(elapsedTimeNanos, ChronoUnit.NANOS)
        return when(unit) {
            ChronoUnit.MILLIS -> duration.toMillis()
            ChronoUnit.SECONDS -> duration.toSeconds()
            ChronoUnit.MINUTES -> duration.toMinutes()
            ChronoUnit.HOURS -> duration.toHours()
            else -> throw IllegalArgumentException("unsupported unit $unit")
        }
    }
}
