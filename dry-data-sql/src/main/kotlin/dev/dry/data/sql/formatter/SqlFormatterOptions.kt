package dev.dry.data.sql.formatter

import dev.dry.data.sql.formatter.SqlFormatterOptions.LimitClausePosition.LIMIT_CLAUSE_BEFORE_END

open class SqlFormatterOptions(
    val lineSeparator: String = DEFAULT_LINE_SEPARATOR,
    val newlineAfterClause: Boolean = false,
    val limitClausePosition: LimitClausePosition = DEFAULT_LIMIT_CLAUSE_POSITION,
) {
    enum class LimitClausePosition { LIMIT_CLAUSE_AFTER_SELECT, LIMIT_CLAUSE_BEFORE_END }

    companion object {
        val DEFAULT_LIMIT_CLAUSE_POSITION: LimitClausePosition = LIMIT_CLAUSE_BEFORE_END
        val DEFAULT_LINE_SEPARATOR: String = System.lineSeparator()
        val DEFAULT = SqlFormatterOptions()
    }
}
