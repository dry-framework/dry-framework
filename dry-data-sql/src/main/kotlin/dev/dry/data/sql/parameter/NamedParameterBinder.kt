package dev.dry.data.sql.parameter

import dev.dry.data.sql.expressions.ParameterExpression
import dev.dry.data.sql.parameter.NamedParameterBinding.DefaultNamedParameterBinding
import dev.dry.data.sql.schema.SqlTypes
import java.math.BigDecimal

interface NamedParameterBinder {
    companion object {
        fun bind(
            parameters: List<ParameterExpression<*>>,
            block: NamedParameterBinder.() -> Unit,
        ): List<ParameterBinding<*>> {
            val binder = DefaultNamedParameterBinder()
            binder.block()

            val namedParameterBindingByName = binder.namedParameterBindings.associateBy { it.name }
            return if (parameters.isNotEmpty()) {
                parameters.map { p ->
                    p.getBinding { name -> namedParameterBindingByName[name]
                        ?: throw IllegalStateException("parameter binding not found with name '$name'")
                    }
                }
            } else {
                if (namedParameterBindingByName.isNotEmpty()) {
                    throw IllegalStateException("unexpected named parameter binding " + namedParameterBindingByName.keys)
                }
                emptyList()
            }
        }
    }

    val namedParameterBindings: Collection<NamedParameterBinding<*>>

    infix fun NamedParameter.bind(value: String) = bind(name, value)
    infix fun NamedParameter.bindNVarchar(value: String) = bindNVarchar(name, value)
    infix fun NamedParameter.bindChar(value: String) = bindChar(name, value)
    infix fun NamedParameter.bindNChar(value: String) = bindNChar(name, value)
    infix fun NamedParameter.bind(value: Short) = bind(name, value)
    infix fun NamedParameter.bind(value: Int) = bind(name, value)
    infix fun NamedParameter.bind(value: Long) = bind(name, value)
    infix fun NamedParameter.bind(value: Float) = bind(name, value)
    infix fun NamedParameter.bind(value: Double) = bind(name, value)
    infix fun NamedParameter.bind(value: BigDecimal) = bind(name, value)

    fun bind(name: String, value: String)
    fun bindNVarchar(name: String, value: String)
    fun bindChar(name: String, value: String)
    fun bindNChar(name: String, value: String)
    fun bind(name: String, value: Short)
    fun bind(name: String, value: Int)
    fun bind(name: String, value: Long)
    fun bind(name: String, value: Float)
    fun bind(name: String, value: Double)
    fun bind(name: String, value: BigDecimal)

    class DefaultNamedParameterBinder : NamedParameterBinder {
        override val namedParameterBindings: MutableCollection<NamedParameterBinding<*>> = mutableListOf()

        override fun bind(name: String, value: String) {
            namedParameterBindings += DefaultNamedParameterBinding(SqlTypes.VARCHAR, value, name)
        }
        override fun bindNVarchar(name: String, value: String) {
            namedParameterBindings += DefaultNamedParameterBinding(SqlTypes.NVARCHAR, value, name)
        }
        override fun bindChar(name: String, value: String) {
            namedParameterBindings += DefaultNamedParameterBinding(SqlTypes.CHAR, value, name)
        }
        override fun bindNChar(name: String, value: String) {
            namedParameterBindings += DefaultNamedParameterBinding(SqlTypes.NCHAR, value, name)
        }

        override fun bind(name: String, value: Short) {
            namedParameterBindings += DefaultNamedParameterBinding(SqlTypes.SHORT, value, name)
        }

        override fun bind(name: String, value: Int) {
            namedParameterBindings += DefaultNamedParameterBinding(SqlTypes.INTEGER, value, name)
        }

        override fun bind(name: String, value: Long) {
            namedParameterBindings += DefaultNamedParameterBinding(SqlTypes.LONG, value, name)
        }

        override fun bind(name: String, value: Float) {
            namedParameterBindings += DefaultNamedParameterBinding(SqlTypes.FLOAT, value, name)
        }

        override fun bind(name: String, value: Double) {
            namedParameterBindings += DefaultNamedParameterBinding(SqlTypes.DOUBLE, value, name)
        }

        override fun bind(name: String, value: BigDecimal) {
            namedParameterBindings += DefaultNamedParameterBinding(SqlTypes.BIG_DECIMAL, value, name)
        }
    }
}
