package dev.dry.data.sql.expressions

import dev.dry.data.sql.factory.SqlFactory
import dev.dry.data.sql.schema.Table
import dev.dry.data.sql.visittor.SqlExpressionVisitor

interface TableReferenceExpression : SqlExpression {
    val table: Table
    val alias: String?

    class DefaultTableReferenceExpression(
        override val table: Table,
        override val alias: String?,
        override val factory: SqlFactory,
    ) : TableReferenceExpression {
        override fun <A> accept(visitor: SqlExpressionVisitor<A>, arg: A) {
            visitor.visitTableReference(this, arg)
        }
    }
}
