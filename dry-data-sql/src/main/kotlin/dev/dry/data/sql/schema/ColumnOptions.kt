package dev.dry.data.sql.schema

data class ColumnOptions(
    val primaryKey: Boolean = false,
    val unique: Boolean = false,
    val insertable: Boolean = true,
    val updatable: Boolean = true,
) {
    companion object {
        val DEFAULT = ColumnOptions()

        fun options(
            primaryKey: Boolean = false,
            unique: Boolean = false,
            insertable: Boolean = true,
            updatable: Boolean = true,
        ) = ColumnOptions(
            primaryKey = primaryKey,
            unique = unique,
            insertable = insertable,
            updatable = updatable,
        )

        fun primaryKey() = options(primaryKey = true)
        fun unique() = options(unique = true)
    }
}
