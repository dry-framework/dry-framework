package dev.dry.data.sql.schema

import dev.dry.data.sql.expressions.ColumnReferenceExpression
import dev.dry.data.sql.expressions.ScalarExpression
import dev.dry.data.sql.expressions.ScalarExpression.AbstractScalarExpression
import dev.dry.data.sql.expressions.SelectListItem.ColumnSelectListItem
import dev.dry.data.sql.factory.SqlFactory
import dev.dry.data.sql.visittor.SqlExpressionVisitor

interface Column<T> : ScalarExpression<T>, ColumnReferenceExpression<T>, ColumnSelectListItem<T> {
    val table: Table
    val name: String
    val length: Int?
    //override val qualifiedName: String get() = "${table.qualifiedName}.$name"
    val options: ColumnOptions

    infix fun alias(alias: String): ColumnSelectListItem<T>

    // ---{ ColumnSelectListItem<T> }---
    // ========================================================================

    override val column: Column<T> get() = this
    override val alias: String? get() = null

    open class DefaultColumn<T>(
        factory: SqlFactory,
        sqlType: SqlType<T>,
        override val table: Table,
        override val name: String,
        override val length: Int? = null,
        override val options: ColumnOptions,
    ) : AbstractScalarExpression<T>(factory, sqlType),  Column<T> {
        override infix fun alias(alias: String): ColumnSelectListItem<T> =
            factory.columnSelectListItem(this, alias)

        override fun <A> accept(visitor: SqlExpressionVisitor<A>, arg: A) {
            visitor.visitColumnReference(this, arg)
        }
    }

    open class DefaultForeignKeyColumn<T, R : T>(
        factory: SqlFactory,
        sqlType: SqlType<T>,
        val referencedColumn: Column<R>,
        table: Table,
        name: String,
        length: Int? = null,
        options: ColumnOptions,
    ) : DefaultColumn<T>(factory, sqlType, table, name, length, options)
}
