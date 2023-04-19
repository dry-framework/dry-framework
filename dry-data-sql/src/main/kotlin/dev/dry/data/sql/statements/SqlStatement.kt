package dev.dry.data.sql.statements

import dev.dry.data.sql.parameter.ParameterBinding

interface SqlStatement {
    val sql: String
    val parameterBindings: List<ParameterBinding<*>>

    interface QueryStatement : SqlStatement {
        class DefaultQueryStatement(
            override val sql: String,
            override val parameterBindings: List<ParameterBinding<*>>,
        ) : QueryStatement
    }

    interface UpdateStatement : SqlStatement {
        class DefaultUpdateStatement(
            override val sql: String,
            override val parameterBindings: List<ParameterBinding<*>>,
        ) : UpdateStatement
    }
}
