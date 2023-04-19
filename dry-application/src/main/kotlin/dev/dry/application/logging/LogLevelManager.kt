package dev.dry.application.logging


import ch.qos.logback.classic.LoggerContext
import org.slf4j.LoggerFactory
import org.slf4j.event.Level
import org.slf4j.event.Level.DEBUG
import org.slf4j.event.Level.ERROR
import org.slf4j.event.Level.INFO
import org.slf4j.event.Level.TRACE
import org.slf4j.event.Level.WARN
import kotlin.reflect.KClass
import ch.qos.logback.classic.Level as LogbackLevel

class LoggerLevel(val name: String, val level: Level?, val effectiveLevel: Level?)

interface LogLevelManager {
    fun <T: Any> setLevel(clazz: KClass<T>, level: Level)

    fun setLevel(name: String, level: Level)
}

class LogbackClassicLogLevelManager: LogLevelManager {
    private val loggerContext = LoggerFactory.getILoggerFactory() as LoggerContext

    companion object {
        private fun toLogbackLevel(level: Level): ch.qos.logback.classic.Level? {
            val logbackLevel = when (level) {
                ERROR -> LogbackLevel.ERROR
                WARN -> LogbackLevel.WARN
                INFO -> LogbackLevel.INFO
                DEBUG -> LogbackLevel.DEBUG
                TRACE -> LogbackLevel.TRACE
            }
            return logbackLevel
        }

        private fun toLevel(level: ch.qos.logback.classic.Level): Level? {
            return when (level) {
                LogbackLevel.ERROR -> ERROR
                LogbackLevel.WARN -> WARN
                LogbackLevel.INFO -> INFO
                LogbackLevel.DEBUG -> DEBUG
                LogbackLevel.TRACE -> TRACE
                LogbackLevel.ALL -> TRACE
                //LogbackLevel.OFF -> null
                else -> null
            }
        }
    }

    fun listLoggerLevels(): List<LoggerLevel> {
        return loggerContext.loggerList.map {
            LoggerLevel(it.name, toLevel(it.level), toLevel(it.effectiveLevel))
        }
    }

    override fun <T: Any> setLevel(clazz: KClass<T>, level: Level) {
        loggerContext.getLogger(clazz.java).level = toLogbackLevel(level)
    }

    override fun setLevel(name: String, level: Level) {
        loggerContext.getLogger(name).level = toLogbackLevel(level)
    }
}
