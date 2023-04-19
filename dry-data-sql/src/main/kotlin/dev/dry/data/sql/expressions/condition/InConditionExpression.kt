package dev.dry.data.sql.expressions.condition

import dev.dry.data.sql.expressions.ScalarExpression
import dev.dry.data.sql.expressions.condition.ConditionExpression.AbstractConditionExpression
import dev.dry.data.sql.factory.SqlFactory
import dev.dry.data.sql.visittor.SqlExpressionVisitor

interface InConditionExpression<T> : ConditionExpression {
    val expression: ScalarExpression<T>
    val values: Collection<ScalarExpression<T>>
    val negate: Boolean

    class DefaultInConditionExpression<T>(
        override val expression: ScalarExpression<T>,
        override val values: Collection<ScalarExpression<T>>,
        override val negate: Boolean,
        factory: SqlFactory,
    ): AbstractConditionExpression(factory), InConditionExpression<T> {
        override fun <A> accept(visitor: SqlExpressionVisitor<A>, arg: A) {
            visitor.visitInCondition(this, arg)
        }
    }
}
