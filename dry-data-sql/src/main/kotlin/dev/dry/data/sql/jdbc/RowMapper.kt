package dev.dry.data.sql.jdbc

import java.sql.ResultSet

fun interface RowMapper<T> {
    fun mapRow(rs: ResultSet, rowIndex: Int): T
}
