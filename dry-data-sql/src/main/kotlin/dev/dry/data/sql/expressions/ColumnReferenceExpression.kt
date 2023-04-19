package dev.dry.data.sql.expressions

import dev.dry.data.sql.expressions.ScalarExpression.AbstractScalarExpression
import dev.dry.data.sql.factory.SqlFactory
import dev.dry.data.sql.schema.Column
import dev.dry.data.sql.visittor.SqlExpressionVisitor

interface ColumnReferenceExpression<T> : ScalarExpression<T> {
    val column: Column<T>
    val alias: String?
    //val qualifiedName: String get() = if (alias != null) "${column.qualifiedName} AS $alias" else column.qualifiedName

    open class DefaultColumnReferenceExpression<T>(
        factory: SqlFactory,
        override val column: Column<T>,
        override val alias: String?,
    ) : AbstractScalarExpression<T>(factory, column.sqlType), ColumnReferenceExpression<T> {
        override fun <A> accept(visitor: SqlExpressionVisitor<A>, arg: A) {
            visitor.visitColumnReference(this, arg)
        }
    }
}
