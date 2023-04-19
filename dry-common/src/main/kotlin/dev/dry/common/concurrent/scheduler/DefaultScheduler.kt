package dev.dry.common.concurrent.scheduler

import java.util.*
import java.util.concurrent.*
import kotlin.reflect.KClass


class DefaultScheduler(
    threadPoolSize: Int = 1,
    private val listener: Listener = object: Listener {}
) : FixedDelayScheduler, FixedRateScheduler {
    interface Listener {
        fun <T: Runnable> onStartTaskRun(taskClass: KClass<T>) {}
        fun <T: Runnable> onCompletedTaskRun(taskClass: KClass<T>) {}
        fun <T: Runnable> onTaskExecutionError(taskClass: KClass<T>, cause: Throwable) {}
    }

    private val executor = Executors.newScheduledThreadPool(threadPoolSize)

    private class ScheduledRunnableTimerTask<T: Runnable>(
        private val runnable: T,
        private val listener: Listener,
    ) : TimerTask() {
        val taskClass: KClass<out T> = runnable::class

        override fun run() {
            listener.onStartTaskRun(taskClass)
            runnable.run()
            listener.onCompletedTaskRun(taskClass)
        }
    }

    override fun scheduleWithFixedDelay(initialDelay: Long, delay: Long, unit: TimeUnit, runnable: Runnable) {
        val timerTask = ScheduledRunnableTimerTask(runnable, listener)
        try {
            executor.scheduleAtFixedRate(timerTask, initialDelay, delay, unit)//.get()
        } catch (th: Throwable) {
            listener.onTaskExecutionError(timerTask.taskClass, th)
        }
    }

    override fun scheduleAtFixedRate(initialDelay: Long, period: Long, unit: TimeUnit, runnable: Runnable) {
        val timerTask = ScheduledRunnableTimerTask(runnable, listener)
        try {
            executor.scheduleAtFixedRate(timerTask, initialDelay, period, unit)//.get()
        } catch (th: Throwable) {
            listener.onTaskExecutionError(timerTask.taskClass, th)
        }
    }
}
