package dev.dry.data.sql.expressions.condition

import dev.dry.data.sql.expressions.ScalarExpression
import dev.dry.data.sql.expressions.condition.ConditionExpression.AbstractConditionExpression
import dev.dry.data.sql.factory.SqlFactory
import dev.dry.data.sql.visittor.SqlExpressionVisitor

interface BetweenConditionExpression<T> : ConditionExpression {
    val expression: ScalarExpression<T>
    val lower: ScalarExpression<T>
    val upper: ScalarExpression<T>
    val negate: Boolean

    class DefaultBetweenConditionExpression<T>(
        override val expression: ScalarExpression<T>,
        override val lower: ScalarExpression<T>,
        override val upper: ScalarExpression<T>,
        override val negate: Boolean,
        factory: SqlFactory,
    ) : AbstractConditionExpression(factory), BetweenConditionExpression<T> {
        override fun <A> accept(visitor: SqlExpressionVisitor<A>, arg: A) {
            visitor.visitBetweenCondition(this, arg)
        }
    }
}
