package dev.dry.data.sql.statements

import dev.dry.data.sql.expressions.SqlExpression
import dev.dry.data.sql.expressions.condition.ConditionExpression
import dev.dry.data.sql.factory.SqlFactory
import dev.dry.data.sql.formatter.SqlFormatter
import dev.dry.data.sql.parameter.NamedParameterBinder
import dev.dry.data.sql.schema.Table
import dev.dry.data.sql.statements.SqlStatement.UpdateStatement
import dev.dry.data.sql.statements.SqlStatement.UpdateStatement.DefaultUpdateStatement
import dev.dry.data.sql.visittor.SqlExpressionVisitor

interface DeleteExpression : SqlExpression {
    val table: Table
    val where: ConditionExpression?

    fun prepareStatement(formatter: SqlFormatter, block: NamedParameterBinder.() -> Unit = {}): UpdateStatement {
        val (sql, parameters) = formatter.format(this)
        val parameterBindings = NamedParameterBinder.bind(parameters, block)
        return DefaultUpdateStatement(sql, parameterBindings)
    }

    class DefaultDeleteExpression(
        override val factory: SqlFactory,
        override val table: Table,
        override val where: ConditionExpression?,
    ) : DeleteExpression {
        override fun <A> accept(visitor: SqlExpressionVisitor<A>, arg: A) {

        }
    }
}
