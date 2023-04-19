package dev.dry.common.concurrent.scheduler

import java.util.concurrent.*

interface FixedRateScheduler {
    fun scheduleAtFixedRate(
        initialDelay: Long,
        period: Long, unit:
        TimeUnit = TimeUnit.MILLISECONDS,
        runnable: Runnable
    )
}
