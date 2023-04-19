package dev.dry.data.sql.formatter

import dev.dry.data.sql.expressions.ParameterExpression

data class FormattedSql(val sql: String, val parameters: List<ParameterExpression<*>>)