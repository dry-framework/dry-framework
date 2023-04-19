package dev.dry.data.sql.expressions

import dev.dry.data.sql.expressions.ScalarExpression.AbstractScalarExpression
import dev.dry.data.sql.factory.SqlFactory
import dev.dry.data.sql.schema.SqlType
import dev.dry.data.sql.schema.SqlTypes
import dev.dry.data.sql.visittor.SqlExpressionVisitor
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

interface LiteralExpression<T: Any?> : ScalarExpression<T> {
    val value: T?

    class StringLiteralExpression(
        factory: SqlFactory,
        override val value: String,
    ) : AbstractScalarExpression<String>(factory, SqlTypes.VARCHAR), LiteralExpression<String> {
        override fun <A> accept(visitor: SqlExpressionVisitor<A>, arg: A) {
            visitor.visitStringLiteral(this, arg)
        }
    }

    abstract class NumberLiteralExpression<T: Number>(
        factory: SqlFactory,
        sqlType: SqlType<T>,
        override val value: T?,
    ) : AbstractScalarExpression<T>(factory, sqlType), LiteralExpression<T> {
        override fun <A> accept(visitor: SqlExpressionVisitor<A>, arg: A) {
            visitor.visitNumberLiteral(this, arg)
        }
    }

    class ShortLiteralExpression(factory: SqlFactory, value: Short?) :
        NumberLiteralExpression<Short>(factory, SqlTypes.SHORT, value)

    class IntLiteralExpression(factory: SqlFactory, value: Int?) :
        NumberLiteralExpression<Int>(factory, SqlTypes.INTEGER, value)

    class LongLiteralExpression(factory: SqlFactory, value: Long?) :
        NumberLiteralExpression<Long>(factory, SqlTypes.LONG, value)

    class FloatLiteralExpression(factory: SqlFactory, value: Float?) :
        NumberLiteralExpression<Float>(factory, SqlTypes.FLOAT, value)

    class DoubleLiteralExpression(factory: SqlFactory, value: Double?) :
        NumberLiteralExpression<Double>(factory, SqlTypes.DOUBLE, value)

    class BigDecimalLiteralExpression(factory: SqlFactory, value: BigDecimal?) :
        NumberLiteralExpression<BigDecimal>(factory, SqlTypes.BIG_DECIMAL, value)

    class LocalDateLiteralExpression(
        factory: SqlFactory,
        override val value: LocalDate?,
    ) : AbstractScalarExpression<LocalDate>(factory, SqlTypes.DATE), LiteralExpression<LocalDate> {
        override fun <A> accept(visitor: SqlExpressionVisitor<A>, arg: A) {
            visitor.visitDateLiteral(this, arg)
        }
    }

    class LocalTimeLiteralExpression(
        factory: SqlFactory,
        override val value: LocalTime?,
    ) : AbstractScalarExpression<LocalTime>(factory, SqlTypes.TIME), LiteralExpression<LocalTime> {
        override fun <A> accept(visitor: SqlExpressionVisitor<A>, arg: A) {
            visitor.visitTimeLiteral(this, arg)
        }
    }

    class LocalDateTimeLiteralExpression(
        factory: SqlFactory,
        override val value: LocalDateTime?,
    ) : AbstractScalarExpression<LocalDateTime>(factory, SqlTypes.DATE_TIME), LiteralExpression<LocalDateTime> {
        override fun <A> accept(visitor: SqlExpressionVisitor<A>, arg: A) {
            visitor.visitDateTimeLiteral(this, arg)
        }
    }
}
