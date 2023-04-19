package dev.dry.data.sql.builder

import dev.dry.data.sql.builder.UpdateBuilder.AbstractUpdateBuilder
import dev.dry.data.sql.factory.SqlFactory
import dev.dry.data.sql.schema.Table
import dev.dry.data.sql.statements.InsertExpression
import dev.dry.data.sql.statements.InsertExpression.DefaultInsertExpression

interface InsertExpressionBuilder: UpdateBuilder<InsertExpression> {
    class DefaultInsertExpressionBuilder(
        factory: SqlFactory,
        table: Table,
    ) : AbstractUpdateBuilder<InsertExpression>(factory, table), InsertExpressionBuilder {
        override fun build(): InsertExpression = DefaultInsertExpression(factory, table, assignments)
    }
}
