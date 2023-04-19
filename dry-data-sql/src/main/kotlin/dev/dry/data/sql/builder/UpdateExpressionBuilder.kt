package dev.dry.data.sql.builder

import dev.dry.data.sql.builder.UpdateBuilder.AbstractUpdateBuilder
import dev.dry.data.sql.expressions.condition.ConditionExpression
import dev.dry.data.sql.factory.SqlFactory
import dev.dry.data.sql.schema.Table
import dev.dry.data.sql.statements.UpdateExpression
import dev.dry.data.sql.statements.UpdateExpression.DefaultUpdateExpression

interface UpdateExpressionBuilder : UpdateBuilder<UpdateExpression> {
    fun where(condition: ConditionExpression): UpdateExpressionBuilder

    class DefaultUpdateExpressionBuilder(
        factory: SqlFactory,
        table: Table,
    ) : AbstractUpdateBuilder<UpdateExpression>(factory, table), UpdateExpressionBuilder {
        private var where: ConditionExpression? = null

        override fun build(): UpdateExpression = DefaultUpdateExpression(table, assignments, where, factory)

        override fun where(condition: ConditionExpression): UpdateExpressionBuilder {
            where = condition
            return this
        }
    }
}
