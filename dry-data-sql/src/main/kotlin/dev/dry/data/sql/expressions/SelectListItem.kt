package dev.dry.data.sql.expressions

import dev.dry.data.sql.factory.SqlFactory
import dev.dry.data.sql.schema.Column
import dev.dry.data.sql.visittor.SqlExpressionVisitor

interface SelectListItem<T> : SqlExpression {

    interface ColumnSelectListItem<T> : SelectListItem<T> {
        val column: Column<T>
        val alias: String?
    }

    interface ExpressionSelectListItem<T> : SelectListItem<T> {
        val expression: ScalarExpression<T>
        val name: String
    }

    class DefaultColumnSelectListItem<T>(
        override val factory: SqlFactory,
        override val column: Column<T>,
        override val alias: String?,
    ) : ColumnSelectListItem<T> {
        override fun <A> accept(visitor: SqlExpressionVisitor<A>, arg: A) {
            visitor.visitColumnSelectListItem(this, arg)
        }
    }

    class DefaultExpressionSelectListItem<T>(
        override val factory: SqlFactory,
        override val expression: ScalarExpression<T>,
        override val name: String,
    ) : ExpressionSelectListItem<T> {
        override fun <A> accept(visitor: SqlExpressionVisitor<A>, arg: A) {
            visitor.visitExpressionSelectListItem(this, arg)
        }
    }
}
