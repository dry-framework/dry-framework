package dev.dry.data.id

import dev.dry.common.time.TimeProvider
import dev.dry.common.time.UtcTimeProvider
import java.security.SecureRandom
import java.time.format.DateTimeFormatter

class DateTimePrefixedRandomNumberGenerator(
    randomDigits: UShort = DEFAULT_RANDOM_DIGITS,
    randomSupplier: () -> Double = SecureRandom()::nextDouble,
    dateTimePrefixPattern: String = DEFAULT_DATE_TIME_PREFIX_PATTERN,
    private val timeProvider: TimeProvider = UtcTimeProvider,
): PrefixedRandomNumberGenerator(DEFAULT_PREFIX_DIGITS, randomDigits, randomSupplier) {
    companion object {
        const val DEFAULT_DATE_TIME_PREFIX_PATTERN = "yyDDD"
        const val DEFAULT_PREFIX_DIGITS: UShort = 5u
        const val DEFAULT_RANDOM_DIGITS: UShort = 10u
    }
    private val prefixDateTimeFormatter = DateTimeFormatter.ofPattern(dateTimePrefixPattern)

    override fun getPrefix(): Double {
        return timeProvider.now().format(prefixDateTimeFormatter).toDouble()
    }
}
