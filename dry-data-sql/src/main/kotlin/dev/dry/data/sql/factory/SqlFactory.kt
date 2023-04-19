package dev.dry.data.sql.factory

import dev.dry.data.sql.Database
import dev.dry.data.sql.builder.DeleteExpressionBuilder
import dev.dry.data.sql.builder.DeleteExpressionBuilder.DefaultDeleteExpressionBuilder
import dev.dry.data.sql.builder.InsertExpressionBuilder
import dev.dry.data.sql.builder.InsertExpressionBuilder.DefaultInsertExpressionBuilder
import dev.dry.data.sql.builder.QueryBuilder
import dev.dry.data.sql.builder.QueryBuilder.DefaultQueryBuilder
import dev.dry.data.sql.builder.UpdateExpressionBuilder
import dev.dry.data.sql.builder.UpdateExpressionBuilder.DefaultUpdateExpressionBuilder
import dev.dry.data.sql.expressions.ColumnAssignment
import dev.dry.data.sql.expressions.ColumnAssignment.DefaultColumnAssignment
import dev.dry.data.sql.expressions.JoinExpression
import dev.dry.data.sql.expressions.JoinExpression.DefaultJoinExpression
import dev.dry.data.sql.expressions.JoinExpression.JoinType
import dev.dry.data.sql.expressions.LiteralExpression
import dev.dry.data.sql.expressions.LiteralExpression.BigDecimalLiteralExpression
import dev.dry.data.sql.expressions.LiteralExpression.DoubleLiteralExpression
import dev.dry.data.sql.expressions.LiteralExpression.FloatLiteralExpression
import dev.dry.data.sql.expressions.LiteralExpression.IntLiteralExpression
import dev.dry.data.sql.expressions.LiteralExpression.LocalDateLiteralExpression
import dev.dry.data.sql.expressions.LiteralExpression.LocalDateTimeLiteralExpression
import dev.dry.data.sql.expressions.LiteralExpression.LocalTimeLiteralExpression
import dev.dry.data.sql.expressions.LiteralExpression.LongLiteralExpression
import dev.dry.data.sql.expressions.LiteralExpression.ShortLiteralExpression
import dev.dry.data.sql.expressions.LiteralExpression.StringLiteralExpression
import dev.dry.data.sql.expressions.OrderByExpression
import dev.dry.data.sql.expressions.OrderByExpression.DefaultOrderByExpression
import dev.dry.data.sql.expressions.OrderByExpression.SortOrder
import dev.dry.data.sql.expressions.ParameterExpression.LiteralParameterExpression
import dev.dry.data.sql.expressions.ParameterExpression.NamedParameterExpression
import dev.dry.data.sql.expressions.QueryExpression
import dev.dry.data.sql.expressions.ScalarExpression
import dev.dry.data.sql.expressions.SelectListItem
import dev.dry.data.sql.expressions.SelectListItem.ColumnSelectListItem
import dev.dry.data.sql.expressions.SelectListItem.DefaultColumnSelectListItem
import dev.dry.data.sql.expressions.SelectListItem.DefaultExpressionSelectListItem
import dev.dry.data.sql.expressions.SelectListItem.ExpressionSelectListItem
import dev.dry.data.sql.expressions.TableExpression
import dev.dry.data.sql.expressions.TableExpression.DefaultTableExpression
import dev.dry.data.sql.expressions.TableReferenceExpression
import dev.dry.data.sql.expressions.TableReferenceExpression.DefaultTableReferenceExpression
import dev.dry.data.sql.expressions.UnionExpression
import dev.dry.data.sql.expressions.UnionExpression.DefaultUnionExpression
import dev.dry.data.sql.expressions.condition.BetweenConditionExpression
import dev.dry.data.sql.expressions.condition.BetweenConditionExpression.DefaultBetweenConditionExpression
import dev.dry.data.sql.expressions.condition.BinaryConditionExpression
import dev.dry.data.sql.expressions.condition.BinaryConditionExpression.BinaryConditionType
import dev.dry.data.sql.expressions.condition.BinaryConditionExpression.DefaultBinaryConditionExpression
import dev.dry.data.sql.expressions.condition.ConditionExpression
import dev.dry.data.sql.expressions.condition.InConditionExpression
import dev.dry.data.sql.expressions.condition.InConditionExpression.DefaultInConditionExpression
import dev.dry.data.sql.expressions.condition.UnaryConditionExpression
import dev.dry.data.sql.expressions.condition.UnaryConditionExpression.DefaultUnaryConditionExpression
import dev.dry.data.sql.expressions.condition.UnaryConditionExpression.UnaryConditionType
import dev.dry.data.sql.schema.Column
import dev.dry.data.sql.schema.Column.DefaultColumn
import dev.dry.data.sql.schema.ColumnOptions
import dev.dry.data.sql.schema.Schema
import dev.dry.data.sql.schema.Schema.BaseSchema
import dev.dry.data.sql.schema.Schema.NullSchema
import dev.dry.data.sql.schema.SqlType
import dev.dry.data.sql.schema.Table
import dev.dry.data.sql.statements.DeleteExpression
import dev.dry.data.sql.statements.DeleteExpression.DefaultDeleteExpression
import dev.dry.data.sql.statements.InsertExpression
import dev.dry.data.sql.statements.InsertExpression.DefaultInsertExpression
import dev.dry.data.sql.statements.SelectExpression
import dev.dry.data.sql.statements.SelectExpression.DefaultSelectExpression
import dev.dry.data.sql.statements.UpdateExpression
import dev.dry.data.sql.statements.UpdateExpression.DefaultUpdateExpression
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

interface SqlFactory {
    object DefaultSqlFactory : SqlFactory

    fun insertExpressionBuilder(table: Table): InsertExpressionBuilder =
        DefaultInsertExpressionBuilder(this, table)

    fun insertExpression(
        table: Table,
        assignments: Collection<ColumnAssignment<*>>,
    ): InsertExpression = DefaultInsertExpression(this, table, assignments)

    fun updateExpressionBuilder(table: Table): UpdateExpressionBuilder =
        DefaultUpdateExpressionBuilder(this, table)

    fun updateExpression(
        table: Table,
        assignments: Collection<ColumnAssignment<*>>,
        where: ConditionExpression?,
    ): UpdateExpression = DefaultUpdateExpression(table, assignments, where, this)

    fun deleteExpressionBuilder(table: Table): DeleteExpressionBuilder = DefaultDeleteExpressionBuilder(table,this)

    fun deleteStatement(
        table: Table,
        where: ConditionExpression?,
    ): DeleteExpression = DefaultDeleteExpression(this, table, where)

    fun queryBuilder(): QueryBuilder = DefaultQueryBuilder(this)

    fun selectExpression(
        offset: Int?,
        limit: Int?,
        distinct: Boolean,
        select: Collection<SelectListItem<*>>,
        from: TableReferenceExpression,
        joins: Collection<JoinExpression>,
        where: ConditionExpression?,
        groupBy: Collection<ScalarExpression<*>>,
        orderBy: Collection<OrderByExpression>,
    ): SelectExpression = DefaultSelectExpression(
        factory = this,
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

    fun <T: Any> literalParameter(literal: LiteralExpression<T>): LiteralParameterExpression<T> =
        LiteralParameterExpression(this, literal)
    fun <T> namedParameter(sqlType: SqlType<T>, name: String): NamedParameterExpression<T> =
        NamedParameterExpression(this, sqlType, name)

    fun schema(name: String, database: Database): Schema = BaseSchema(name, database)
    fun schema(database: Database): Schema = NullSchema(database)

    fun <T> column(
        table: Table,
        name: String,
        sqlType: SqlType<T>,
        options: ColumnOptions = ColumnOptions.DEFAULT,
    ): Column<T> = DefaultColumn(this, sqlType, table, name, options = options)

    fun <T> columnAssignment(
        column: Column<T>,
        expression: ScalarExpression<T>,
    ): ColumnAssignment<T> = DefaultColumnAssignment(column, expression)

    fun <T> between(
        expression: ScalarExpression<T>,
        lower: ScalarExpression<T>,
        upper: ScalarExpression<T>,
        negate: Boolean,
    ): BetweenConditionExpression<T> = DefaultBetweenConditionExpression(expression, lower, upper, negate, this)

    fun <T> binaryCondition(
        left: ScalarExpression<T>,
        type: BinaryConditionType,
        right: ScalarExpression<T>,
    ): BinaryConditionExpression<T> = DefaultBinaryConditionExpression(left, type, right, this)

    fun <T> inCondition(
        expression: ScalarExpression<T>,
        values: Collection<ScalarExpression<T>>,
        negate: Boolean,
    ): InConditionExpression<T> = DefaultInConditionExpression(expression, values, negate, this)

    fun unaryCondition(
        type: UnaryConditionType,
        operand: ScalarExpression<Boolean>,
    ): UnaryConditionExpression = DefaultUnaryConditionExpression(type, operand, this)

    fun <T> columnSelectListItem(column: Column<T>, alias: String? = null): ColumnSelectListItem<T> =
        DefaultColumnSelectListItem(this, column, alias)

    fun <T> expressionSelectListItem(expression: ScalarExpression<T>, alias: String): ExpressionSelectListItem<T> =
        DefaultExpressionSelectListItem(this, expression, alias)
/*
    fun <T: Any> function(name: String, arguments: Collection<ScalarExpression<*>>): FunctionExpression<T> =
        DefaultFunctionExpression(this, arguments.first().sqlType, name, arguments)
*/
    fun join(type: JoinType, right: TableReferenceExpression, on: ConditionExpression): JoinExpression =
        DefaultJoinExpression(this, type, right, on)

    fun literal(value: String) = StringLiteralExpression(this, value)
    fun literal(value: Short) = ShortLiteralExpression(this, value)
    fun literal(value: Int) = IntLiteralExpression(this, value)
    fun literal(value: Long) = LongLiteralExpression(this, value)
    fun literal(value: Float) = FloatLiteralExpression(this, value)
    fun literal(value: Double) = DoubleLiteralExpression(this, value)
    fun literal(value: BigDecimal) = BigDecimalLiteralExpression(this, value)
    fun literal(value: LocalDate) = LocalDateLiteralExpression(this, value)
    fun literal(value: LocalTime) = LocalTimeLiteralExpression(this, value)
    fun literal(value: LocalDateTime) = LocalDateTimeLiteralExpression(this, value)

    fun orderBy(expression: ScalarExpression<*>, sortOrder: SortOrder): OrderByExpression =
        DefaultOrderByExpression(expression, sortOrder, this)

    fun tableExpression(name: String, query: QueryExpression): TableExpression =
        DefaultTableExpression(name, query, this)

    fun tableReference(table: Table, alias: String? = null): TableReferenceExpression =
        DefaultTableReferenceExpression(table, alias, this)

    fun union(
        left: QueryExpression,
        right: QueryExpression,
        unionAll: Boolean,
        orderBy: Collection<OrderByExpression> = emptyList(),
        offset: Int? = null,
        limit: Int? = null,
    ): UnionExpression = DefaultUnionExpression(left, right, unionAll, orderBy, offset, limit, this)
}
