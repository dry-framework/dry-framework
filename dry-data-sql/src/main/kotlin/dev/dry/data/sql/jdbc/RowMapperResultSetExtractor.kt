package dev.dry.data.sql.jdbc

import java.sql.ResultSet

class RowMapperResultSetExtractor<T>(
    private val rowMapper: RowMapper<T>,
    private val rowsExpected: Int = 0,
) : ResultSetExtractor<List<T>> {
    override fun extract(rs: ResultSet): List<T> {
        val results: MutableList<T> = if (rowsExpected > 0) ArrayList(rowsExpected) else ArrayList()
        var rowIndex = 0
        while (rs.next()) {
            results.add(rowMapper.mapRow(rs, rowIndex++))
        }
        return results
    }
}
