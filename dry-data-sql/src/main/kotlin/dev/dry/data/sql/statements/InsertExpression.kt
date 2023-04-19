package dev.dry.data.sql.statements

import dev.dry.data.sql.expressions.ColumnAssignment
import dev.dry.data.sql.expressions.SqlExpression
import dev.dry.data.sql.factory.SqlFactory
import dev.dry.data.sql.formatter.SqlFormatter
import dev.dry.data.sql.parameter.NamedParameterBinder
import dev.dry.data.sql.schema.Table
import dev.dry.data.sql.statements.SqlStatement.UpdateStatement
import dev.dry.data.sql.statements.SqlStatement.UpdateStatement.DefaultUpdateStatement
import dev.dry.data.sql.visittor.SqlExpressionVisitor

interface InsertExpression : SqlExpression {
    val table: Table
    val assignments: Collection<ColumnAssignment<*>>

    fun prepareStatement(formatter: SqlFormatter, block: NamedParameterBinder.() -> Unit = {}): UpdateStatement {
        val (sql, parameters) = formatter.format(this)
        val parameterBindings = NamedParameterBinder.bind(parameters, block)
        return DefaultUpdateStatement(sql, parameterBindings)
    }

    class DefaultInsertExpression(
        override val factory: SqlFactory,
        override val table: Table,
        override val assignments: Collection<ColumnAssignment<*>>,
    ) : InsertExpression {
        override fun <A> accept(visitor: SqlExpressionVisitor<A>, arg: A) {

        }
    }
}
