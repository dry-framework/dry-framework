package dev.dry.data.sql.formatter

import dev.dry.data.sql.expressions.LiteralExpression
import dev.dry.data.sql.formatter.SqlFormatter.DefaultSqlFormatter
import dev.dry.data.sql.formatter.SqlFormatter.SqlFormatterContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class OracleSqlFormatter : DefaultSqlFormatter(OracleSqlFormatterOptions) {
    object OracleSqlFormatterOptions : SqlFormatterOptions()

    override fun visitDateLiteral(literal: LiteralExpression<LocalDate>, arg: SqlFormatterContext) {
        arg.sql.append("DATE")
        super.visitDateLiteral(literal, arg)
    }

    override fun visitTimeLiteral(literal: LiteralExpression<LocalTime>, arg: SqlFormatterContext) {
        arg.sql.append("TO_DATE(")
        super.visitTimeLiteral(literal, arg)
        arg.sql.append(", 'HH24:MI:SS')")
    }

    override fun visitDateTimeLiteral(literal: LiteralExpression<LocalDateTime>, arg: SqlFormatterContext) {
        arg.sql.append("TIMESTAMP")
        super.visitDateTimeLiteral(literal, arg)
    }
}
