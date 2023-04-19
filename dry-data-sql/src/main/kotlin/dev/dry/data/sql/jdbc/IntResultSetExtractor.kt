package dev.dry.data.sql.jdbc

import java.sql.ResultSet


class IntResultSetExtractor : ResultSetExtractor<Int> {
    override fun extract(rs: ResultSet): Int {
        return if (rs.next()) {
            rs.getInt(1)
        } else {
            //val (sql, _) = database.formatExpression(countExpr, beautifySql = true)
            val sql = "" // TODO("Fix this")
            throw IllegalStateException("No result return for sql: $sql")
        }
    }
}
