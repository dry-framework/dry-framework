package dev.dry.data.sql.builder

import dev.dry.data.sql.builder.SelectExpressionBuilder.DefaultSelectExpressionBuilder
import dev.dry.data.sql.factory.SqlFactory
import dev.dry.data.sql.schema.Table
import dev.dry.data.sql.statements.SelectExpression

interface QueryBuilder {
    fun <T: Table> from(table: T, build: SelectExpressionBuilder.(table: T) -> Unit): SelectExpression

    class DefaultQueryBuilder(private val factory: SqlFactory) : QueryBuilder {
        override fun <T : Table> from(table: T, build: SelectExpressionBuilder.(table: T) -> Unit): SelectExpression {
            val builder = DefaultSelectExpressionBuilder(table, factory)
            builder.build(table)
            return builder.build()
        }
    }
}
