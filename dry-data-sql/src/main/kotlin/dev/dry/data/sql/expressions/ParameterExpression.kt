package dev.dry.data.sql.expressions

import dev.dry.data.sql.expressions.ScalarExpression.AbstractScalarExpression
import dev.dry.data.sql.factory.SqlFactory
import dev.dry.data.sql.parameter.NamedParameterBinding
import dev.dry.data.sql.parameter.ParameterBinding
import dev.dry.data.sql.schema.SqlType
import dev.dry.data.sql.visittor.SqlExpressionVisitor
import java.sql.PreparedStatement

interface ParameterExpression<T> : ScalarExpression<T> {
    fun getBinding(getNamed: (String) -> NamedParameterBinding<*>): ParameterBinding<*>

    class LiteralParameterExpression<T: Any>(
        factory: SqlFactory,
        private val literal: LiteralExpression<T>,
    ) : AbstractScalarExpression<T>(factory, literal.sqlType),
        ParameterExpression<T>,
        ParameterBinding<T> {

        override val value: T? get() = literal.value

        override fun getBinding(getNamed: (String) -> NamedParameterBinding<*>): ParameterBinding<*> = this

        override fun bind(ps: PreparedStatement, index: Int) {
            literal.sqlType.setParameterValue(ps, index, literal.value)
        }

        override fun <A> accept(visitor: SqlExpressionVisitor<A>, arg: A) {
            visitor.visitLiteralParameterExpression(this, arg)
        }
    }

    class NamedParameterExpression<T>(
        factory: SqlFactory,
        sqlType: SqlType<T>,
        val name: String,
    ) : AbstractScalarExpression<T>(factory, sqlType), ParameterExpression<T> {
        override fun getBinding(getNamed: (String) -> NamedParameterBinding<*>): ParameterBinding<*> = getNamed(name)

        override fun <A> accept(visitor: SqlExpressionVisitor<A>, arg: A) {
            visitor.visitNamedParameterExpression(this, arg)
        }
    }
}
