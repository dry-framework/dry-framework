package dev.dry.data.sql.expressions

import dev.dry.data.sql.formatter.FormattedSql
import dev.dry.data.sql.formatter.SqlFormatter
import dev.dry.data.sql.parameter.NamedParameterBinder
import dev.dry.data.sql.statements.SqlStatement.QueryStatement
import dev.dry.data.sql.statements.SqlStatement.QueryStatement.DefaultQueryStatement

interface QueryExpression : SqlExpression {
    val orderBy: Collection<OrderByExpression>
    val offset: Int?
    val limit: Int?

    fun format(formatter: SqlFormatter): FormattedSql

    fun prepareStatement(formatter: SqlFormatter, block: NamedParameterBinder.() -> Unit): QueryStatement {
        val (sql, parameters) = formatter.format(this)
        val parameterBindings = NamedParameterBinder.bind(parameters, block)
        return DefaultQueryStatement(sql, parameterBindings)
    }

    infix fun union(right: QueryExpression): UnionExpression {
        return factory.union(this, right, false)
    }

    infix fun unionAll(right: QueryExpression): UnionExpression {
        return factory.union(this, right, true)
    }
}
