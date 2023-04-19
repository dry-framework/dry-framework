package dev.dry.data.sql.expressions

import dev.dry.data.sql.expressions.condition.ConditionExpression
import dev.dry.data.sql.factory.SqlFactory
import dev.dry.data.sql.visittor.SqlExpressionVisitor

interface JoinExpression : SqlExpression {
    enum class JoinType(val symbol: String) {
        INNER("INNER JOIN"),
        LEFT("LEFT JOIN"),
        RIGHT("RIGHT JOIN"),
        FULL("FULL JOIN"),
    }

    val type: JoinType
    val right: TableReferenceExpression
    val on: ConditionExpression

    class DefaultJoinExpression(
        override val factory: SqlFactory,
        override val type: JoinType,
        override val right: TableReferenceExpression,
        override val on: ConditionExpression,
    ) : JoinExpression {
        override fun <A> accept(visitor: SqlExpressionVisitor<A>, arg: A) {
            visitor.visitJoin(this, arg)
        }
    }
}
