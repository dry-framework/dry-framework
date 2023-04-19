package dev.dry.data.sql.parameter

import java.sql.PreparedStatement

interface ParameterBinding<T> {
    val value: T?
    fun bind(ps: PreparedStatement, index: Int)
}
