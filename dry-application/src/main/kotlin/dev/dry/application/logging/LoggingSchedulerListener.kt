package dev.dry.application.logging

import dev.dry.application.logging.LoggingSchedulerListener.EventType.COMPLETED
import dev.dry.application.logging.LoggingSchedulerListener.EventType.ERROR
import dev.dry.application.logging.LoggingSchedulerListener.EventType.STARTED
import dev.dry.common.concurrent.scheduler.DefaultScheduler
import dev.dry.common.exception.Exceptions
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

class LoggingSchedulerListener: DefaultScheduler.Listener {
    private enum class EventType { STARTED, COMPLETED, ERROR }
    companion object {
        private val logger = LoggerFactory.getLogger(LoggingSchedulerListener::class.java)

        private fun <T : Runnable> logMessage(taskClass: KClass<T>, event: EventType, exception: Throwable? = null) {
            if (exception == null) {
                if (logger.isDebugEnabled) {
                    logger.debug("scheduled task '{}' {}", taskName(taskClass), event)
                }
            } else {
                if (logger.isErrorEnabled) {
                    logger.error(
                        "scheduled task '{}' {} -- {}",
                        taskName(taskClass),
                        event,
                        Exceptions.getMessageChain(exception)
                    )
                }
            }
        }

        private fun <T: Any> taskName(taskClass: KClass<T>): String = taskClass.simpleName ?: taskClass.java.simpleName
    }

    override fun <T : Runnable> onStartTaskRun(taskClass: KClass<T>) {
        logMessage(taskClass, STARTED)
    }

    override fun <T : Runnable> onCompletedTaskRun(taskClass: KClass<T>) {
        logMessage(taskClass, COMPLETED)
    }

    override fun <T : Runnable> onTaskExecutionError(taskClass: KClass<T>, cause: Throwable) {
        logMessage(taskClass, ERROR, cause)
    }
}
