package dev.dry.data.sql.expressions.condition

import dev.dry.data.sql.expressions.ScalarExpression
import dev.dry.data.sql.expressions.condition.ConditionExpression.AbstractConditionExpression
import dev.dry.data.sql.factory.SqlFactory
import dev.dry.data.sql.visittor.SqlExpressionVisitor

interface UnaryConditionExpression : ConditionExpression {
    enum class UnaryConditionType(val symbol: String) {
        NOT("NOT")
    }

    val type: UnaryConditionType
    val operand: ScalarExpression<Boolean>

    class DefaultUnaryConditionExpression(
        override val type: UnaryConditionType,
        override val operand: ScalarExpression<Boolean>,
        factory: SqlFactory,
    ) : AbstractConditionExpression(factory), UnaryConditionExpression {
        override fun <A> accept(visitor: SqlExpressionVisitor<A>, arg: A) {
            visitor.visitUnaryCondition(this, arg)
        }
    }
}