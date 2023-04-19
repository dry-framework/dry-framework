package dev.dry.data.sql.visittor

import dev.dry.data.sql.expressions.ColumnReferenceExpression
import dev.dry.data.sql.expressions.FunctionExpression
import dev.dry.data.sql.expressions.JoinExpression
import dev.dry.data.sql.expressions.LiteralExpression
import dev.dry.data.sql.expressions.OrderByExpression
import dev.dry.data.sql.expressions.ParameterExpression.LiteralParameterExpression
import dev.dry.data.sql.expressions.ParameterExpression.NamedParameterExpression
import dev.dry.data.sql.expressions.SelectListItem.ColumnSelectListItem
import dev.dry.data.sql.expressions.SelectListItem.ExpressionSelectListItem
import dev.dry.data.sql.expressions.TableExpression
import dev.dry.data.sql.expressions.TableReferenceExpression
import dev.dry.data.sql.expressions.UnionExpression
import dev.dry.data.sql.expressions.condition.BetweenConditionExpression
import dev.dry.data.sql.expressions.condition.BinaryConditionExpression
import dev.dry.data.sql.expressions.condition.InConditionExpression
import dev.dry.data.sql.expressions.condition.UnaryConditionExpression
import dev.dry.data.sql.statements.SelectExpression
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

interface SqlExpressionVisitor<A> {
    fun visitSelect(selectExpression: SelectExpression, arg: A)
    fun <T: Any> visitLiteralParameterExpression(literalParameter: LiteralParameterExpression<T>, arg: A)
    fun <T> visitNamedParameterExpression(namedParameter: NamedParameterExpression<T>, arg: A)
    fun <T> visitColumnSelectListItem(columnSelectListItem: ColumnSelectListItem<T>, arg: A)
    fun <T> visitExpressionSelectListItem(expressionSelectListItem: ExpressionSelectListItem<T>, arg: A)
    fun <T> visitBetweenCondition(between: BetweenConditionExpression<T>, arg: A)
    fun <T> visitBinaryCondition(binaryCondition: BinaryConditionExpression<T>, arg: A)
    fun <T> visitInCondition(inCondition: InConditionExpression<T>, arg: A)
    fun visitUnaryCondition(unaryCondition: UnaryConditionExpression, arg: A)
    fun <T> visitColumnReference(columnReferenceExpression: ColumnReferenceExpression<T>, arg: A)
    fun <T> visitFunction(function: FunctionExpression<T>, arg: A)
    fun visitJoin(join: JoinExpression, arg: A)
    fun visitOrderBy(orderBy: OrderByExpression, arg: A)
    fun visitTableExpression(tableExpression: TableExpression, arg: A)
    fun visitTableReference(tableReference: TableReferenceExpression, arg: A)
    fun visitUnion(union: UnionExpression, arg: A)
    fun visitStringLiteral(literal: LiteralExpression<String>, arg: A)
    fun <T: Number> visitNumberLiteral(literal: LiteralExpression<T>, arg: A)
    fun visitDateLiteral(literal: LiteralExpression<LocalDate>, arg: A)
    fun visitTimeLiteral(literal: LiteralExpression<LocalTime>, arg: A)
    fun visitDateTimeLiteral(literal: LiteralExpression<LocalDateTime>, arg: A)
}
