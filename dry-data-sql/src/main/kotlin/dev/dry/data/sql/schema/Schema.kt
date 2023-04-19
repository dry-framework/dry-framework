package dev.dry.data.sql.schema

import dev.dry.data.sql.Database

interface Schema {
    val name: String
    val database: Database
    val tables: Collection<Table>

    val qualifiedName: String get() {
        return "${database.name}.$name"
    }

    fun addTable(table: Table): Table

    open class BaseSchema(override val name: String, override val database: Database) : Schema {
        override val tables: MutableList<Table> = mutableListOf()

        override fun addTable(table: Table): Table = table.also(tables::add)
    }

    class NullSchema(override val database: Database) : BaseSchema("", database) {
        override val qualifiedName: String get() = database.name
    }
}
