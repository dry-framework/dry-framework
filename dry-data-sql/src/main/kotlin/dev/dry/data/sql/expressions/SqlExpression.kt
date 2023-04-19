package dev.dry.data.sql.expressions

import dev.dry.data.sql.factory.SqlFactory
import dev.dry.data.sql.visittor.SqlExpressionVisitor

interface SqlExpression {
    val factory: SqlFactory
    fun <A> accept(visitor: SqlExpressionVisitor<A>, arg: A)
}
