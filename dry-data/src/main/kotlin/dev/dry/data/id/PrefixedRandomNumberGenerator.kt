package dev.dry.data.id

import java.security.SecureRandom
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

abstract class PrefixedRandomNumberGenerator(
    prefixDigits: UShort = 5u,
    private val randomDigits: UShort = 10u,
    private val randomSupplier: () -> Double = SecureRandom()::nextDouble,
) : IdGenerator {
    init {
        val totalDigits = prefixDigits + randomDigits
        if (totalDigits > MAX_DIGITS) {
            throw IllegalArgumentException("total digits of $totalDigits exceeds maximum $MAX_DIGITS")
        }
    }

    abstract fun getPrefix(): Double

    override fun nextNumber(): Long {
        val prefix = getPrefix()
        val random = nextRandomNumber(randomSupplier, randomDigits)
        val value = concat(prefix, random)
        return value.toLong()
    }

    companion object {
        private const val TEN: Double = 10.0
        private val MAX_DIGITS: UInt = floor(log10(Long.MAX_VALUE.toDouble())).toUInt() + 1u

        fun nextRandomNumber(randomSupplier: () -> Double, digits: UShort): Double {
            val min = TEN.pow((digits - 1u).toDouble())
            val max = min * 10 - 1
            val random = randomSupplier()
            return (random * max) + if (random <= 0.1) min else 0.0
        }

        fun concat(prefix: Double, suffix: Double): Double {
            val suffixDigits = floor(log10(suffix)) + 1
            return (prefix * TEN.pow(suffixDigits)) + suffix
        }
    }
}
