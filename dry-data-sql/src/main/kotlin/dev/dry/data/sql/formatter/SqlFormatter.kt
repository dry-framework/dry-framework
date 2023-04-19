package dev.dry.data.sql.formatter

import dev.dry.data.sql.expressions.ColumnReferenceExpression
import dev.dry.data.sql.expressions.FunctionExpression
import dev.dry.data.sql.expressions.JoinExpression
import dev.dry.data.sql.expressions.LiteralExpression
import dev.dry.data.sql.expressions.OrderByExpression
import dev.dry.data.sql.expressions.ParameterExpression
import dev.dry.data.sql.expressions.ParameterExpression.LiteralParameterExpression
import dev.dry.data.sql.expressions.ParameterExpression.NamedParameterExpression
import dev.dry.data.sql.expressions.QueryExpression
import dev.dry.data.sql.expressions.ScalarExpression
import dev.dry.data.sql.expressions.SelectListItem.ColumnSelectListItem
import dev.dry.data.sql.expressions.SelectListItem.ExpressionSelectListItem
import dev.dry.data.sql.expressions.SqlExpression
import dev.dry.data.sql.expressions.TableExpression
import dev.dry.data.sql.expressions.TableReferenceExpression
import dev.dry.data.sql.expressions.UnionExpression
import dev.dry.data.sql.expressions.condition.BetweenConditionExpression
import dev.dry.data.sql.expressions.condition.BinaryConditionExpression
import dev.dry.data.sql.expressions.condition.ConditionExpression
import dev.dry.data.sql.expressions.condition.InConditionExpression
import dev.dry.data.sql.expressions.condition.UnaryConditionExpression
import dev.dry.data.sql.factory.SqlFactory
import dev.dry.data.sql.formatter.SqlFormatterOptions.LimitClausePosition.LIMIT_CLAUSE_AFTER_SELECT
import dev.dry.data.sql.formatter.SqlFormatterOptions.LimitClausePosition.LIMIT_CLAUSE_BEFORE_END
import dev.dry.data.sql.statements.DeleteExpression
import dev.dry.data.sql.statements.InsertExpression
import dev.dry.data.sql.statements.SelectExpression
import dev.dry.data.sql.statements.UpdateExpression
import dev.dry.data.sql.visittor.SqlExpressionVisitor
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

interface SqlFormatter {
    class SqlFormatterContext(
        val factory: SqlFactory,
        val sql: Appendable = StringBuilder(),
        val parameters: MutableList<ParameterExpression<*>> = mutableListOf(),
    )

    fun format(insertExpression: InsertExpression): FormattedSql

    fun format(queryExpression: QueryExpression): FormattedSql {
        return queryExpression.format(this)
    }

    fun format(selectExpression: SelectExpression): FormattedSql

    fun format(unionExpression: UnionExpression): FormattedSql

    fun format(updateExpression: UpdateExpression): FormattedSql

    fun format(deleteExpression: DeleteExpression): FormattedSql

    open class DefaultSqlFormatter(
        private val options: SqlFormatterOptions = SqlFormatterOptions.DEFAULT,
    ) : SqlFormatter, SqlExpressionVisitor<SqlFormatterContext> {

        // ====================================================================
        // ---{ SqlExpressionFormatter }---
        // ====================================================================

        override fun format(insertExpression: InsertExpression): FormattedSql {
            val table = insertExpression.table
            val assignments = insertExpression.assignments
            val ctx = SqlFormatterContext(table.factory)
            val sql = ctx.sql
            sql.append("INSERT INTO ").append(table.tableName)
            appendTo(ctx, assignments, ", ", "(", ")") { a, ca -> a.append(ca.column.name) }
            sql.append(" VALUES ")
            appendTo(ctx, assignments, ", ", "(", ")") { _, ca -> appendTo(ctx, ca.expression) }
            return FormattedSql(sql.toString(), ctx.parameters)
        }

        override fun format(selectExpression: SelectExpression): FormattedSql {
            val ctx = SqlFormatterContext(selectExpression.factory)
            visitSelect(selectExpression, ctx)
            return FormattedSql(ctx.sql.toString(), ctx.parameters)
        }

        override fun format(unionExpression: UnionExpression): FormattedSql {
            val ctx = SqlFormatterContext(unionExpression.factory)
            visitUnion(unionExpression, ctx)
            return FormattedSql(ctx.sql.toString(), ctx.parameters)
        }

        override fun format(updateExpression: UpdateExpression): FormattedSql {
            val table = updateExpression.table
            val assignments = updateExpression.assignments
            val where = updateExpression.where
            val ctx = SqlFormatterContext(table.factory)
            val sql = ctx.sql
            sql.append("UPDATE ").append(table.tableName).append(" SET ")
            appendTo(ctx, assignments, separator = ", ") { _, assignment ->
                sql.append(assignment.column.name).append(" = ")
                assignment.expression.accept(this, ctx)
            }
            if (where != null) {
                sql.append(" WHERE ")
                appendTo(ctx, where)
            }
            return FormattedSql(sql.toString(), ctx.parameters)
        }

        override fun format(deleteExpression: DeleteExpression): FormattedSql {
            val table = deleteExpression.table
            val where = deleteExpression.where
            val ctx = SqlFormatterContext(table.factory)
            val sql = ctx.sql
            sql.append("DELETE FROM ").append(table.tableName)
            if (where != null) {
                sql.append(" WHERE ")
                appendTo(ctx, where)
            }
            return FormattedSql(sql.toString(), ctx.parameters)
        }

        // ====================================================================
        // ---{ SqlExpressionVisitor }---
        // ====================================================================

        override fun visitSelect(selectExpression: SelectExpression, arg: SqlFormatterContext) {
            val sql = arg.sql
            sql.append("SELECT ")
            if (selectExpression.distinct) sql.append("DISTINCT ")
            appendTo(arg, selectExpression.select, separator = ", ")
            if (options.limitClausePosition == LIMIT_CLAUSE_AFTER_SELECT) appendLimitClauseTo(arg, selectExpression)
            sql.append(" FROM ")
            //selectExpression.accept(this, arg)
            appendTo(arg, selectExpression.from)
            sql.append(" ")
            appendTo(arg, selectExpression.joins, " ")
            appendWhereClauseTo(arg, selectExpression.where)
            appendGroupByClauseTo(arg, selectExpression.groupBy)
            // TODO("Having")
            appendOrderByClauseTo(arg, selectExpression.orderBy)
            if (options.limitClausePosition == LIMIT_CLAUSE_BEFORE_END) appendLimitClauseTo(arg, selectExpression)
        }

        override fun <T : Any> visitLiteralParameterExpression(
            literalParameter: LiteralParameterExpression<T>,
            arg: SqlFormatterContext,
        ) {
            arg.parameters += literalParameter
            arg.sql.append("?")
        }

        override fun <T> visitNamedParameterExpression(namedParameter: NamedParameterExpression<T>, arg: SqlFormatterContext) {
            arg.parameters += namedParameter
            arg.sql.append("?")
        }

        override fun <T> visitColumnSelectListItem(columnSelectListItem: ColumnSelectListItem<T>, arg: SqlFormatterContext) {
            val column = columnSelectListItem.column
            val alias = columnSelectListItem.alias
            val sql = arg.sql
            //sql.append(column.qualifiedName)
            sql.append(column.table.tableName).append(".").append(column.name)
            if (alias != null) sql.append(" ").append(alias)
        }

        override fun <T> visitExpressionSelectListItem(
            expressionSelectListItem: ExpressionSelectListItem<T>,
            arg: SqlFormatterContext,
        ) {
            val sql = arg.sql
            appendTo(arg, expressionSelectListItem.expression, "(", ")")
            sql.append(" ")
            sql.append(expressionSelectListItem.name)
        }

        override fun <T> visitColumnReference(
            columnReferenceExpression: ColumnReferenceExpression<T>,
            arg: SqlFormatterContext,
        ) {
            val column = columnReferenceExpression.column
            val alias = columnReferenceExpression.alias
            val sql = arg.sql
            sql.append(column.table.tableName).append(".").append(column.name)
            if (alias != null) sql.append(" ").append(alias)
        }

        override fun <T> visitFunction(function: FunctionExpression<T>, arg: SqlFormatterContext) {
            arg.sql.append(function.name)
            appendTo(arg, function.arguments, separator = ", ", prefix = "(", postfix = ")")
        }

        override fun visitJoin(join: JoinExpression, arg: SqlFormatterContext) {
            val sql = arg.sql
            sql.append(join.type.symbol).append(" ")
            appendTo(arg, join.right)
            sql.append(" ON ")
            appendTo(arg, join.on)
        }

        override fun visitOrderBy(orderBy: OrderByExpression, arg: SqlFormatterContext) {
            val sql = arg.sql
            appendTo(arg, orderBy.expression)
            sql.append(" ").append(orderBy.sortOrder.name)
        }

        override fun visitTableExpression(tableExpression: TableExpression, arg: SqlFormatterContext) {
            val sql = arg.sql
            sql.append("(")
            appendTo(arg, tableExpression.query)
            sql.append(") AS ")
            sql.append(tableExpression.name)
        }

        override fun visitTableReference(tableReference: TableReferenceExpression, arg: SqlFormatterContext) {
            val table = tableReference.table
            val alias = tableReference.alias
            val sql = arg.sql
            if (table.schema.name.isNotEmpty()) sql.append("${table.schema.name}.")
            sql.append(table.tableName)
            if (alias != null) sql.append(" ").append(alias)
        }

        override fun visitUnion(union: UnionExpression, arg: SqlFormatterContext) {
            val sql = arg.sql
            appendTo(arg, union.left)
            sql.append(" UNION ")
            if (union.unionAll) sql.append("ALL ")
            appendTo(arg, union.right)
        }

        override fun <T> visitBetweenCondition(between: BetweenConditionExpression<T>, arg: SqlFormatterContext) {
            val sql = arg.sql
            appendTo(arg, between.expression)
            if (between.negate) sql.append(" NOT ")
            sql.append(" BETWEEN ")
            appendTo(arg, between.lower)
            sql.append(" AND ")
            appendTo(arg, between.upper)
        }

        override fun <T> visitBinaryCondition(binaryCondition: BinaryConditionExpression<T>, arg: SqlFormatterContext) {
            val sql = arg.sql
            appendTo(arg, binaryCondition.left)
            sql.append(" ")
            sql.append(binaryCondition.type.symbol)
            sql.append(" ")
            appendTo(arg, binaryCondition.right)
        }

        override fun <T> visitInCondition(inCondition: InConditionExpression<T>, arg: SqlFormatterContext) {
            val sql = arg.sql
            appendTo(arg, inCondition.expression)
            if (inCondition.negate) sql.append(" NOT ")
            sql.append(" IN")
            appendTo(arg, inCondition.values, separator = ", ", prefix = "(", postfix = ")")
        }

        override fun visitUnaryCondition(unaryCondition: UnaryConditionExpression, arg: SqlFormatterContext) {
            val sql = arg.sql
            sql.append(unaryCondition.type.symbol)
            sql.append(" ")
            appendTo(arg, unaryCondition.operand)
        }

        override fun visitStringLiteral(literal: LiteralExpression<String>, arg: SqlFormatterContext) {
            arg.parameters += LiteralParameterExpression(arg.factory, literal)
            arg.sql.append("?")
        }


        override fun <T : Number> visitNumberLiteral(literal: LiteralExpression<T>, arg: SqlFormatterContext) {
            arg.parameters += LiteralParameterExpression(arg.factory, literal)
            arg.sql.append("?")
        }

        override fun visitDateLiteral(literal: LiteralExpression<LocalDate>, arg: SqlFormatterContext) {
            arg.parameters += LiteralParameterExpression(arg.factory, literal)
            arg.sql.append("?")
        }

        override fun visitTimeLiteral(literal: LiteralExpression<LocalTime>, arg: SqlFormatterContext) {
            arg.parameters += LiteralParameterExpression(arg.factory, literal)
            arg.sql.append("?")
        }

        override fun visitDateTimeLiteral(literal: LiteralExpression<LocalDateTime>, arg: SqlFormatterContext) {
            arg.parameters += LiteralParameterExpression(arg.factory, literal)
            arg.sql.append("?")
        }

        // ====================================================================
        // ---{ Helpers }---
        // ====================================================================

        protected open fun appendTo(
            ctx: SqlFormatterContext,
            sqlExpression: SqlExpression,
            prefix: String = "",
            postfix: String = "",
        ) {
            val sql = ctx.sql
            sql.append(prefix)
            sqlExpression.accept(this, ctx)
            sql.append(postfix)
        }

        protected open fun appendWhereClauseTo(ctx: SqlFormatterContext, where: ConditionExpression?) {
            if (where != null) {
                ctx.sql.append(" WHERE ")
                appendTo(ctx, where)
            }
        }

        protected open fun appendGroupByClauseTo(ctx: SqlFormatterContext, groupBy: Collection<ScalarExpression<*>>) {
            if (groupBy.isNotEmpty()) {
                ctx.sql.append(" GROUP BY ")
                appendTo(ctx, groupBy, separator = ", ")
            }
        }

        protected open fun appendOrderByClauseTo(ctx: SqlFormatterContext, orderBy: Collection<OrderByExpression>) {
            if (orderBy.isNotEmpty()) {
                ctx.sql.append(" ORDER BY ")
                appendTo(ctx, orderBy, separator = ", ")
            }
        }

        protected open fun appendLimitClauseTo(ctx: SqlFormatterContext, queryExpression: QueryExpression) {
            val offset = queryExpression.offset
            if (offset != null) {
                ctx.sql.append(" OFFSET ").append(offset.toString()).append(" ROWS")
            }

            val limit = queryExpression.limit
            if (limit != null) {
                ctx.sql.append(" FETCH FIRST ").append(limit.toString()).append(" ROWS ONLY")
            }
        }

        protected open fun appendStringLiteralTo(ctx: SqlFormatterContext, value: String) {
            appendTo(ctx, value, prefix = "'", postfix = "'")
        }

        protected open fun appendTo(ctx: SqlFormatterContext, value: String, prefix: String = "'", postfix: String = "'") {
            ctx.sql.append(prefix, value, postfix)
        }

        protected open fun appendTo(
            ctx: SqlFormatterContext,
            collection: Collection<SqlExpression>,
            separator: String,
            prefix: String = "",
            postfix: String = "",
        ) {
            val sql = ctx.sql
            sql.append(prefix)
            collection.forEachIndexed { index, sqlExpression ->
                if (index > 0) {
                    sql.append(separator)
                }
                appendTo(ctx, sqlExpression)
            }
            sql.append(postfix)
        }

        protected open fun <T> appendTo(
            ctx: SqlFormatterContext,
            collection: Collection<T>,
            separator: String,
            prefix: String = "",
            postfix: String = "",
            appendTo: (Appendable, T) -> Unit
        ) {
            val sql = ctx.sql
            sql.append(prefix)
            collection.forEachIndexed { index, sqlExpression ->
                if (index > 0) {
                    sql.append(separator)
                }
                appendTo(sql, sqlExpression)
            }
            sql.append(postfix)
        }
    }
}
