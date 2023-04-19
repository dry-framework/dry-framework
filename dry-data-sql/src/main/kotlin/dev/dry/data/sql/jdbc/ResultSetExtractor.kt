package dev.dry.data.sql.jdbc

import java.sql.ResultSet

@FunctionalInterface
fun interface ResultSetExtractor<T> {
    fun extract(rs: ResultSet): T
}
