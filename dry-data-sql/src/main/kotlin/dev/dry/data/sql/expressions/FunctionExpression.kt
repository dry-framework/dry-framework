package dev.dry.data.sql.expressions

import dev.dry.data.sql.expressions.ScalarExpression.AbstractScalarExpression
import dev.dry.data.sql.factory.SqlFactory
import dev.dry.data.sql.schema.SqlType
import dev.dry.data.sql.visittor.SqlExpressionVisitor

interface FunctionExpression<T> : ScalarExpression<T> {
    val name: String
    val arguments: Collection<ScalarExpression<*>>

    object FunctionNames {
        const val COUNT = "COUNT"
        const val SUM = "SUM"
        const val MAX = "MAX"
        const val MIN = "MIN"
        const val AVG = "AVG"
    }

    open class DefaultFunctionExpression<T>(
        factory: SqlFactory,
        sqlType: SqlType<T>,
        override val name: String,
        override val arguments: Collection<ScalarExpression<*>>,
    ): AbstractScalarExpression<T>(factory, sqlType), FunctionExpression<T> {
        override fun <A> accept(visitor: SqlExpressionVisitor<A>, arg: A) {
            visitor.visitFunction(this, arg)
        }
    }
}
