package dev.dry.data.sql.statements

import dev.dry.data.sql.expressions.JoinExpression
import dev.dry.data.sql.expressions.OrderByExpression
import dev.dry.data.sql.expressions.QueryExpression
import dev.dry.data.sql.expressions.ScalarExpression
import dev.dry.data.sql.expressions.SelectListItem
import dev.dry.data.sql.expressions.TableReferenceExpression
import dev.dry.data.sql.expressions.condition.ConditionExpression
import dev.dry.data.sql.factory.SqlFactory
import dev.dry.data.sql.formatter.FormattedSql
import dev.dry.data.sql.formatter.SqlFormatter
import dev.dry.data.sql.visittor.SqlExpressionVisitor

interface SelectExpression : QueryExpression {
    val distinct: Boolean
    val select: Collection<SelectListItem<*>>
    val from: TableReferenceExpression
    val joins: Collection<JoinExpression>
    val where: ConditionExpression?
    val groupBy: Collection<ScalarExpression<*>>

    override fun format(formatter: SqlFormatter): FormattedSql {
        return formatter.format(this)
    }

    override fun <A> accept(visitor: SqlExpressionVisitor<A>, arg: A) {
        visitor.visitSelect(this, arg)
    }

    class DefaultSelectExpression(
        override val factory: SqlFactory,
        override val distinct: Boolean,
        override val select: Collection<SelectListItem<*>>,
        override val from: TableReferenceExpression,
        override val joins: Collection<JoinExpression>,
        override val where: ConditionExpression?,
        override val groupBy: Collection<ScalarExpression<*>>,
        override val orderBy: Collection<OrderByExpression>,
        override val offset: Int?,
        override val limit: Int?,
    ) : SelectExpression
}
