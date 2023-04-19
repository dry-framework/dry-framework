package dev.dry.data.sql.statements

import dev.dry.data.sql.expressions.ColumnAssignment
import dev.dry.data.sql.expressions.SqlExpression
import dev.dry.data.sql.expressions.condition.ConditionExpression
import dev.dry.data.sql.factory.SqlFactory
import dev.dry.data.sql.formatter.SqlFormatter
import dev.dry.data.sql.parameter.NamedParameterBinder
import dev.dry.data.sql.schema.Table
import dev.dry.data.sql.statements.SqlStatement.UpdateStatement
import dev.dry.data.sql.statements.SqlStatement.UpdateStatement.DefaultUpdateStatement
import dev.dry.data.sql.visittor.SqlExpressionVisitor

interface UpdateExpression : SqlExpression {
    val table: Table
    val assignments: Collection<ColumnAssignment<*>>
    val where: ConditionExpression?

    fun prepareStatement(formatter: SqlFormatter, block: NamedParameterBinder.() -> Unit = {}): UpdateStatement {
        val (sql, parameters) = formatter.format(this)
        val parameterBindings = NamedParameterBinder.bind(parameters, block)
        return DefaultUpdateStatement(sql, parameterBindings)
    }

    class DefaultUpdateExpression(
        override val table: Table,
        override val assignments: Collection<ColumnAssignment<*>>,
        override val where: ConditionExpression?,
        override val factory: SqlFactory,
    ) : UpdateExpression {
        override fun <A> accept(visitor: SqlExpressionVisitor<A>, arg: A) {

        }
    }
}
