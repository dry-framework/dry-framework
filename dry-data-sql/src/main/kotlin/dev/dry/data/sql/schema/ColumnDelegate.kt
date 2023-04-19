package dev.dry.data.sql.schema

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ColumnDelegate<T: Table, C>(
    private val sqlType: SqlType<C>,
    private val length: Int? = null,
    private val options: ColumnOptions,
    private val referencedColumn: Column<C>? = null,
) : ReadOnlyProperty<T, Column<C>> {
    private var cached: Column<C>? = null

    private fun constructColumn(
        table: T,
        property: KProperty<*>,
    ): Column<C> = table.addColumn(property.name, sqlType, options)

    override operator fun getValue(thisRef: T, property: KProperty<*>): Column<C> {
        return cached ?: synchronized(this) {
            cached ?: constructColumn(thisRef, property).also { cached = it }
        }
    }

    fun foreignKey(referencedColumn: Column<C>): ColumnDelegate<T, C> {
        return ColumnDelegate(sqlType, length, ColumnOptions.DEFAULT, referencedColumn)
    }
}
