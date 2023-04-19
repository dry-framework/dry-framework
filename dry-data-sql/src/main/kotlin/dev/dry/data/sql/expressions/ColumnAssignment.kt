package dev.dry.data.sql.expressions

import dev.dry.data.sql.schema.Column

interface ColumnAssignment<T> {
    val column: Column<T>
    val expression: ScalarExpression<T>

    class DefaultColumnAssignment<T>(
        override val column: Column<T>,
        override val expression: ScalarExpression<T>,
    ): ColumnAssignment<T>
}
