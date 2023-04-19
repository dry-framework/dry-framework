package dev.dry.data.sql.expressions

import dev.dry.data.sql.factory.SqlFactory
import dev.dry.data.sql.visittor.SqlExpressionVisitor

interface TableExpression : SqlExpression {
    val name: String
    val query: QueryExpression

    open class DefaultTableExpression(
        override val name: String,
        override val query: QueryExpression,
        override val factory: SqlFactory
    ) : TableExpression {
        override fun <A> accept(visitor: SqlExpressionVisitor<A>, arg: A) {
            visitor.visitTableExpression(this, arg)
        }
    }
}
