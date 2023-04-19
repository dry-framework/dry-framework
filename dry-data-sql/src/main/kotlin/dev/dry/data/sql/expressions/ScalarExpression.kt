package dev.dry.data.sql.expressions

import dev.dry.data.sql.expressions.condition.BinaryConditionExpression.BinaryConditionType.EQUAL
import dev.dry.data.sql.expressions.condition.BinaryConditionExpression.BinaryConditionType.GREATER_THAN
import dev.dry.data.sql.expressions.condition.BinaryConditionExpression.BinaryConditionType.GREATER_THAN_OR_EQUAL
import dev.dry.data.sql.expressions.condition.BinaryConditionExpression.BinaryConditionType.LESS_THAN
import dev.dry.data.sql.expressions.condition.BinaryConditionExpression.BinaryConditionType.LESS_THAN_OR_EQUAL
import dev.dry.data.sql.expressions.condition.BinaryConditionExpression.BinaryConditionType.NOT_EQUAL
import dev.dry.data.sql.expressions.condition.ConditionExpression
import dev.dry.data.sql.factory.SqlFactory
import dev.dry.data.sql.parameter.NamedParameter
import dev.dry.data.sql.schema.SqlType
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

interface ScalarExpression<T> : SqlExpression {
    val sqlType: SqlType<T>

    infix fun eq(right: ScalarExpression<T>): ConditionExpression
    infix fun eq(right: NamedParameter): ConditionExpression {
        return eq(factory.namedParameter(sqlType, right.name))
    }

    infix fun neq(right: ScalarExpression<T>): ConditionExpression
    infix fun neq(right: NamedParameter): ConditionExpression {
        return neq(factory.namedParameter(sqlType, right.name))
    }

    infix fun gt(right: ScalarExpression<T>): ConditionExpression
    infix fun gt(right: NamedParameter): ConditionExpression {
        return gt(factory.namedParameter(sqlType, right.name))
    }

    infix fun gte(right: ScalarExpression<T>): ConditionExpression
    infix fun gte(right: NamedParameter): ConditionExpression {
        return gte(factory.namedParameter(sqlType, right.name))
    }

    infix fun lt(right: ScalarExpression<T>): ConditionExpression
    infix fun lt(right: NamedParameter): ConditionExpression {
        return lt(factory.namedParameter(sqlType, right.name))
    }

    infix fun lte(right: ScalarExpression<T>): ConditionExpression
    infix fun lte(right: NamedParameter): ConditionExpression {
        return lte(factory.namedParameter(sqlType, right.name))
    }

    abstract class AbstractScalarExpression<T>(
        override val factory: SqlFactory,
        override val sqlType: SqlType<T>,
    ) : ScalarExpression<T> {
        override infix fun eq(right: ScalarExpression<T>): ConditionExpression {
            return factory.binaryCondition(this, EQUAL, right)
        }

        override infix fun neq(right: ScalarExpression<T>): ConditionExpression {
            return factory.binaryCondition(this, NOT_EQUAL, right)
        }

        override infix fun gt(right: ScalarExpression<T>): ConditionExpression {
            return factory.binaryCondition(this, GREATER_THAN, right)
        }

        override infix fun gte(right: ScalarExpression<T>): ConditionExpression {
            return factory.binaryCondition(this, GREATER_THAN_OR_EQUAL, right)
        }

        override infix fun lt(right: ScalarExpression<T>): ConditionExpression {
            return factory.binaryCondition(this, LESS_THAN, right)
        }

        override infix fun lte(right: ScalarExpression<T>): ConditionExpression {
            return factory.binaryCondition(this, LESS_THAN_OR_EQUAL, right)
        }
    }
}

infix fun ScalarExpression<String>.eq(right: String): ConditionExpression = eq(factory.literal(right))
infix fun ScalarExpression<String>.neq(right: String): ConditionExpression = neq(factory.literal(right))

infix fun ScalarExpression<Short>.eq(right: Short): ConditionExpression = eq(factory.literal(right))
infix fun ScalarExpression<Short>.neq(right: Short): ConditionExpression = neq(factory.literal(right))
infix fun ScalarExpression<Short>.gt(right: Short): ConditionExpression = gt(factory.literal(right))
infix fun ScalarExpression<Short>.gte(right: Short): ConditionExpression = gte(factory.literal(right))
infix fun ScalarExpression<Short>.lt(right: Short): ConditionExpression = lt(factory.literal(right))
infix fun ScalarExpression<Short>.lte(right: Short): ConditionExpression = lte(factory.literal(right))

infix fun ScalarExpression<Int>.eq(right: Int): ConditionExpression = eq(factory.literal(right))
infix fun ScalarExpression<Int>.neq(right: Int): ConditionExpression = neq(factory.literal(right))
infix fun ScalarExpression<Int>.gt(right: Int): ConditionExpression = gt(factory.literal(right))
infix fun ScalarExpression<Int>.gte(right: Int): ConditionExpression = gte(factory.literal(right))
infix fun ScalarExpression<Int>.lt(right: Int): ConditionExpression = lt(factory.literal(right))
infix fun ScalarExpression<Int>.lte(right: Int): ConditionExpression = lte(factory.literal(right))

infix fun ScalarExpression<Long>.eq(right: Long): ConditionExpression = eq(factory.literal(right))
infix fun ScalarExpression<Long>.neq(right: Long): ConditionExpression = neq(factory.literal(right))
infix fun ScalarExpression<Long>.gt(right: Long): ConditionExpression = gt(factory.literal(right))
infix fun ScalarExpression<Long>.gte(right: Long): ConditionExpression = gte(factory.literal(right))
infix fun ScalarExpression<Long>.lt(right: Long): ConditionExpression = lt(factory.literal(right))
infix fun ScalarExpression<Long>.lte(right: Long): ConditionExpression = lte(factory.literal(right))

infix fun ScalarExpression<Float>.eq(right: Float): ConditionExpression = eq(factory.literal(right))
infix fun ScalarExpression<Float>.neq(right: Float): ConditionExpression = neq(factory.literal(right))
infix fun ScalarExpression<Float>.gt(right: Float): ConditionExpression = gt(factory.literal(right))
infix fun ScalarExpression<Float>.gte(right: Float): ConditionExpression = gte(factory.literal(right))
infix fun ScalarExpression<Float>.lt(right: Float): ConditionExpression = lt(factory.literal(right))
infix fun ScalarExpression<Float>.lte(right: Float): ConditionExpression = lte(factory.literal(right))

infix fun ScalarExpression<Double>.eq(right: Double): ConditionExpression = eq(factory.literal(right))
infix fun ScalarExpression<Double>.neq(right: Double): ConditionExpression = neq(factory.literal(right))
infix fun ScalarExpression<Double>.gt(right: Double): ConditionExpression = gt(factory.literal(right))
infix fun ScalarExpression<Double>.gte(right: Double): ConditionExpression = gte(factory.literal(right))
infix fun ScalarExpression<Double>.lt(right: Double): ConditionExpression = lt(factory.literal(right))
infix fun ScalarExpression<Double>.lte(right: Double): ConditionExpression = lte(factory.literal(right))

infix fun ScalarExpression<BigDecimal>.eq(right: BigDecimal): ConditionExpression = eq(factory.literal(right))
infix fun ScalarExpression<BigDecimal>.neq(right: BigDecimal): ConditionExpression = neq(factory.literal(right))
infix fun ScalarExpression<BigDecimal>.gt(right: BigDecimal): ConditionExpression = gt(factory.literal(right))
infix fun ScalarExpression<BigDecimal>.gte(right: BigDecimal): ConditionExpression = gte(factory.literal(right))
infix fun ScalarExpression<BigDecimal>.lt(right: BigDecimal): ConditionExpression = lt(factory.literal(right))
infix fun ScalarExpression<BigDecimal>.lte(right: BigDecimal): ConditionExpression = lte(factory.literal(right))

infix fun ScalarExpression<LocalDate>.eq(right: LocalDate): ConditionExpression = eq(factory.literal(right))
infix fun ScalarExpression<LocalDate>.neq(right: LocalDate): ConditionExpression = neq(factory.literal(right))
infix fun ScalarExpression<LocalDate>.gt(right: LocalDate): ConditionExpression = gt(factory.literal(right))
infix fun ScalarExpression<LocalDate>.gte(right: LocalDate): ConditionExpression = gte(factory.literal(right))
infix fun ScalarExpression<LocalDate>.lt(right: LocalDate): ConditionExpression = lt(factory.literal(right))
infix fun ScalarExpression<LocalDate>.lte(right: LocalDate): ConditionExpression = lte(factory.literal(right))

infix fun ScalarExpression<LocalTime>.eq(right: LocalTime): ConditionExpression = eq(factory.literal(right))
infix fun ScalarExpression<LocalTime>.neq(right: LocalTime): ConditionExpression = neq(factory.literal(right))
infix fun ScalarExpression<LocalTime>.gt(right: LocalTime): ConditionExpression = gt(factory.literal(right))
infix fun ScalarExpression<LocalTime>.gte(right: LocalTime): ConditionExpression = gte(factory.literal(right))
infix fun ScalarExpression<LocalTime>.lt(right: LocalTime): ConditionExpression = lt(factory.literal(right))
infix fun ScalarExpression<LocalTime>.lte(right: LocalTime): ConditionExpression = lte(factory.literal(right))

infix fun ScalarExpression<LocalDateTime>.eq(right: LocalDateTime): ConditionExpression = eq(factory.literal(right))
infix fun ScalarExpression<LocalDateTime>.neq(right: LocalDateTime): ConditionExpression = neq(factory.literal(right))
infix fun ScalarExpression<LocalDateTime>.gt(right: LocalDateTime): ConditionExpression = gt(factory.literal(right))
infix fun ScalarExpression<LocalDateTime>.gte(right: LocalDateTime): ConditionExpression = gte(factory.literal(right))
infix fun ScalarExpression<LocalDateTime>.lt(right: LocalDateTime): ConditionExpression = lt(factory.literal(right))
infix fun ScalarExpression<LocalDateTime>.lte(right: LocalDateTime): ConditionExpression = lte(factory.literal(right))
