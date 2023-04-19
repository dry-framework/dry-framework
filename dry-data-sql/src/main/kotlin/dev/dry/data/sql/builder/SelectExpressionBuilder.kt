package dev.dry.data.sql.builder

import dev.dry.data.sql.expressions.JoinExpression
import dev.dry.data.sql.expressions.JoinExpression.JoinType
import dev.dry.data.sql.expressions.OrderByExpression
import dev.dry.data.sql.expressions.OrderByExpression.SortOrder
import dev.dry.data.sql.expressions.OrderByExpression.SortOrder.ASC
import dev.dry.data.sql.expressions.ScalarExpression
import dev.dry.data.sql.expressions.SelectListItem
import dev.dry.data.sql.expressions.TableReferenceExpression
import dev.dry.data.sql.expressions.condition.ConditionExpression
import dev.dry.data.sql.factory.SqlFactory
import dev.dry.data.sql.parameter.NamedParameter
import dev.dry.data.sql.schema.Table
import dev.dry.data.sql.statements.SelectExpression

interface SelectExpressionBuilder {
    fun build(): SelectExpression

    fun distinct(): SelectExpressionBuilder
    fun select(vararg selectListItem: SelectListItem<*>): SelectExpressionBuilder
    fun <T: Table> join(joinType: JoinType, right: T, alias: String, on: (T) -> ConditionExpression): T
    fun join(joinType: JoinType, right: TableReferenceExpression, on: ConditionExpression): SelectExpressionBuilder
    fun where(condition: ConditionExpression): SelectExpressionBuilder
    fun orderBy(scalarExpression: ScalarExpression<*>, sortOrder: SortOrder = ASC): SelectExpressionBuilder
    fun groupBy(scalarExpression: ScalarExpression<*>): SelectExpressionBuilder
    fun offset(offset: Int): SelectExpressionBuilder
    fun limit(limit: Int): SelectExpressionBuilder

    class DefaultSelectExpressionBuilder(
        private val from: TableReferenceExpression,
        private val factory: SqlFactory,
    ) : SelectExpressionBuilder {
        private var distinct: Boolean = false
        private var select = emptyList<SelectListItem<*>>()
        private val joins = mutableListOf<JoinExpression>()
        private var where: ConditionExpression? = null
        private val orderBy = mutableListOf<OrderByExpression>()
        private val groupBy = mutableListOf<ScalarExpression<*>>()
        private var offset: Int? = null
        private var limit: Int? = null

        override fun build(): SelectExpression = factory.selectExpression(
            offset = offset,
            limit = limit,
            distinct = distinct,
            select = select,
            from = from,
            joins = joins,
            where = where,
            orderBy = orderBy,
            groupBy = groupBy,
        )

        override fun distinct(): SelectExpressionBuilder {
            this.distinct = true
            return this
        }

        override fun limit(limit: Int): SelectExpressionBuilder {
            this.limit = limit
            return this
        }

        override fun offset(offset: Int): SelectExpressionBuilder {
            this.offset = offset
            return this
        }

        override fun select(vararg selectListItem: SelectListItem<*>): SelectExpressionBuilder {
            select = listOf(*selectListItem)
            return this
        }

        override fun <T : Table> join(joinType: JoinType, right: T, alias: String, on: (T) -> ConditionExpression): T {
            val condition = on(right)
            joins.add(factory.join(joinType, right, condition))
            return right
        }
        /*
        override fun join(joinType: JoinType, right: Table, on: ConditionExpression): SelectQueryBuilder {
            join(joinType, factory.tableReference(right), on)
            return this
        }*/

        override fun join(
            joinType: JoinType,
            right: TableReferenceExpression,
            on: ConditionExpression
        ): SelectExpressionBuilder {
            joins.add(factory.join(joinType, right, on))
            return this
        }

        override fun where(condition: ConditionExpression): SelectExpressionBuilder {
            where = condition
            return this
        }

        override fun orderBy(scalarExpression: ScalarExpression<*>, sortOrder: SortOrder): SelectExpressionBuilder {
            orderBy.add(factory.orderBy(scalarExpression, sortOrder))
            return this
        }

        override fun groupBy(scalarExpression: ScalarExpression<*>): SelectExpressionBuilder {
            groupBy.add(scalarExpression)
            return this
        }
    }
}

fun SelectExpressionBuilder.parameter(name: String): NamedParameter {
    return NamedParameter(name)
}
