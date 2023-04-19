package dev.dry.data.sql.expressions.condition

import dev.dry.data.sql.expressions.ScalarExpression
import dev.dry.data.sql.expressions.ScalarExpression.AbstractScalarExpression
import dev.dry.data.sql.expressions.condition.BinaryConditionExpression.BinaryConditionType
import dev.dry.data.sql.factory.SqlFactory
import dev.dry.data.sql.schema.SqlTypes

interface ConditionExpression : ScalarExpression<Boolean> {
    infix fun and(right: ConditionExpression): ConditionExpression
    infix fun or(right: ConditionExpression): ConditionExpression

    abstract class AbstractConditionExpression(
        override val factory: SqlFactory
    ) : AbstractScalarExpression<Boolean>(factory, SqlTypes.BOOLEAN), ConditionExpression {
        override fun and(right: ConditionExpression): ConditionExpression {
            return factory.binaryCondition(this, BinaryConditionType.AND, right)
        }

        override fun or(right: ConditionExpression): ConditionExpression {
            return factory.binaryCondition(this, BinaryConditionType.OR, right)
        }
    }
}
