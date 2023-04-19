package dev.dry.data.sql.expressions.condition

import dev.dry.data.sql.expressions.ScalarExpression
import dev.dry.data.sql.expressions.condition.ConditionExpression.AbstractConditionExpression
import dev.dry.data.sql.factory.SqlFactory
import dev.dry.data.sql.visittor.SqlExpressionVisitor

interface BinaryConditionExpression<T> : ConditionExpression {
    enum class BinaryConditionType(val symbol: String) {
        AND("AND"),
        OR("OR"),
        EQUAL("="),
        NOT_EQUAL("<>"),
        GREATER_THAN(">"),
        GREATER_THAN_OR_EQUAL(">="),
        LESS_THAN("<"),
        LESS_THAN_OR_EQUAL("<="),
        ;
    }

    val left: ScalarExpression<T>
    val type: BinaryConditionType
    val right: ScalarExpression<T>

    class DefaultBinaryConditionExpression<T>(
        override val left: ScalarExpression<T>,
        override val type: BinaryConditionType,
        override val right: ScalarExpression<T>,
        factory: SqlFactory,
    ): AbstractConditionExpression(factory), BinaryConditionExpression<T> {
        override fun <A> accept(visitor: SqlExpressionVisitor<A>, arg: A) {
            visitor.visitBinaryCondition(this, arg)
        }
    }
}
