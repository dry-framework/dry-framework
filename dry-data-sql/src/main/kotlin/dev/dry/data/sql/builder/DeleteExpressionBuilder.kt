package dev.dry.data.sql.builder

import dev.dry.data.sql.expressions.condition.ConditionExpression
import dev.dry.data.sql.factory.SqlFactory
import dev.dry.data.sql.schema.Table
import dev.dry.data.sql.statements.DeleteExpression
import dev.dry.data.sql.statements.DeleteExpression.DefaultDeleteExpression

interface DeleteExpressionBuilder {
    fun build(): DeleteExpression

    fun where(condition: ConditionExpression): DeleteExpressionBuilder

    class DefaultDeleteExpressionBuilder(val table: Table, private val factory: SqlFactory) : DeleteExpressionBuilder {
        private var where: ConditionExpression? = null

        override fun build(): DeleteExpression = DefaultDeleteExpression(factory, table, where)

        override fun where(condition: ConditionExpression): DeleteExpressionBuilder {
            where = condition
            return this
        }
    }
}
