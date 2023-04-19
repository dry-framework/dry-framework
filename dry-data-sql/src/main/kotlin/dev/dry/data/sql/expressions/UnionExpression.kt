package dev.dry.data.sql.expressions

import dev.dry.data.sql.factory.SqlFactory
import dev.dry.data.sql.formatter.FormattedSql
import dev.dry.data.sql.formatter.SqlFormatter
import dev.dry.data.sql.visittor.SqlExpressionVisitor

interface UnionExpression : QueryExpression {
    val left: QueryExpression
    val right: QueryExpression
    val unionAll: Boolean

    override fun format(formatter: SqlFormatter): FormattedSql {
        return formatter.format(this)
    }

    override fun <A> accept(visitor: SqlExpressionVisitor<A>, arg: A) {
        visitor.visitUnion(this, arg)
    }

    class DefaultUnionExpression(
        override val left: QueryExpression,
        override val right: QueryExpression,
        override val unionAll: Boolean,
        override val orderBy: Collection<OrderByExpression>,
        override val offset: Int?,
        override val limit: Int?,
        override val factory: SqlFactory
    ) : UnionExpression
}
