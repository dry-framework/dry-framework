package dev.dry.data.sql.parameter

import dev.dry.data.sql.schema.SqlType
import java.sql.PreparedStatement

interface NamedParameterBinding<T> : ParameterBinding<T> {
    val name: String

    class DefaultNamedParameterBinding<T>(
        private val sqlType: SqlType<T>,
        override val value: T?,
        override val name: String,
    ) : NamedParameterBinding<T> {
        override fun bind(ps: PreparedStatement, index: Int) {
            sqlType.setParameterValue(ps, index, value)
        }
    }
}
