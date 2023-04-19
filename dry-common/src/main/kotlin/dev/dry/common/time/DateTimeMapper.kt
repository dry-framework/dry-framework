package dev.dry.common.time

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

object DateTimeMapper {
    @JvmStatic
    fun toDate(value: LocalDateTime): Date = try {
        Date.from(value.toInstant(ZoneOffset.UTC))
    } catch (ex: IllegalArgumentException) {
        throw IllegalArgumentException("date time instant too large -- '${value}'", ex)
    }

    @JvmStatic
    fun toLocalDateTime(value: Date): LocalDateTime = try {
        Instant.ofEpochMilli(value.time)
            .atZone(ZoneOffset.UTC)
            .toLocalDateTime()
    } catch (ex: IllegalArgumentException) {
        throw IllegalArgumentException("date time instant too large -- '${value}'", ex)
    }
}
