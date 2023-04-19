package dev.dry.common.concurrent.scheduler

import java.util.concurrent.*

interface FixedDelayScheduler {
    fun scheduleWithFixedDelay(
        initialDelay: Long,
        delay: Long,
        unit: TimeUnit = TimeUnit.MILLISECONDS,
        runnable: Runnable
    )
}
