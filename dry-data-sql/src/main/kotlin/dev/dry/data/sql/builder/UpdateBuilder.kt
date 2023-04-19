package dev.dry.data.sql.builder

import dev.dry.data.sql.expressions.ColumnAssignment
import dev.dry.data.sql.expressions.ScalarExpression
import dev.dry.data.sql.factory.SqlFactory
import dev.dry.data.sql.schema.Column
import dev.dry.data.sql.schema.Table

interface UpdateBuilder<E> {
    fun build(): E

    fun <B : UpdateBuilder<E>, T> B.set(column: Column<T>, expression: ScalarExpression<T>): B
    fun <B : UpdateBuilder<E>> B.set(column: Column<String>, value: String): B
    fun <B : UpdateBuilder<E>> B.set(column: Column<Int>, value: Int): B

    abstract class AbstractUpdateBuilder<E>(
        protected val factory: SqlFactory,
        protected val table: Table,
    ) : UpdateBuilder<E> {
        protected val assignments: MutableList<ColumnAssignment<*>> = mutableListOf()

        override fun <B : UpdateBuilder<E>, T> B.set(column: Column<T>, expression: ScalarExpression<T>): B {
            assignments.add(factory.columnAssignment(column, expression))
            return this
        }

        override fun <B : UpdateBuilder<E>> B.set(column: Column<String>, value: String): B {
            assignments.add(factory.columnAssignment(column, factory.literal(value)))
            return this
        }

        override fun <B : UpdateBuilder<E>> B.set(column: Column<Int>, value: Int): B {
            assignments.add(factory.columnAssignment(column, factory.literal(value)))
            return this
        }
    }
}
