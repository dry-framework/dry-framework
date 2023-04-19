package dev.dry.data.sql.expressions

import dev.dry.data.sql.factory.SqlFactory
import dev.dry.data.sql.visittor.SqlExpressionVisitor

interface OrderByExpression : SqlExpression {
    val expression: ScalarExpression<*>
    val sortOrder: SortOrder

    enum class SortOrder { ASC, DESC }

    open class DefaultOrderByExpression(
        override val expression: ScalarExpression<*>,
        override val sortOrder: SortOrder,
        override val factory: SqlFactory
    ) : OrderByExpression {
        override fun <A> accept(visitor: SqlExpressionVisitor<A>, arg: A) {
            visitor.visitOrderBy(this, arg)
        }
    }
}
