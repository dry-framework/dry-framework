package dev.dry.data.sql.schema

import dev.dry.data.sql.Database
import dev.dry.data.sql.expressions.TableReferenceExpression
import dev.dry.data.sql.factory.SqlFactory
import dev.dry.data.sql.schema.Column.DefaultColumn
import dev.dry.data.sql.visittor.SqlExpressionVisitor
import java.math.BigDecimal
import java.sql.Blob
import java.sql.Clob
import java.sql.NClob
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.reflect.full.primaryConstructor

inline infix fun <reified T: Table> T.alias(alias: String): T =
    T::class.primaryConstructor?.call(schema.database, alias) ?:
        throw IllegalStateException(
            "Table instances must have a primary constructor taking a database and alias arguments"
        )

interface Table : TableReferenceExpression {
    val tableName: String
    val schema: Schema
    //val qualifiedName: String get() = "${schema.qualifiedName}.$tableName"
    val columns: Collection<Column<*>>

    fun <C> addColumn(name: String, type: SqlType<C>, options: ColumnOptions = ColumnOptions.DEFAULT): Column<C>

    fun findColumn(name: String): Column<*>? = columns.find { it.name == name }

    // ========================================================================
    // ---{ TableReferenceExpression }---
    // ========================================================================

    override val table: Table get() = this

    // ========================================================================
    // ---{ DefaultTable : Table }---
    // ========================================================================

    open class BaseTable<T : BaseTable<T>>(
        override val tableName: String,
        override val schema: Schema,
        override val alias: String?,
    ) : Table {
        constructor(
            name: String,
            database: Database,
            alias: String?,
        ): this(name, database.defaultSchema, alias)

        //override val qualifiedName: String get() = alias ?: super.qualifiedName
        override val factory: SqlFactory get() = schema.database.factory

        private val columnByName: MutableMap<String, Column<*>> = HashMap()
        override val columns: Collection<Column<*>> get() = columnByName.values

        override fun <C> addColumn(
            name: String,
            sqlType: SqlType<C>,
            options: ColumnOptions,
        ): Column<C> {
            return DefaultColumn(factory, sqlType, this, name, options = options).also { columnByName[name] = it }
        }

        // ---{ SqlExpression }---
        // ========================================================================

        override fun <A> accept(visitor: SqlExpressionVisitor<A>, arg: A) {
            visitor.visitTableReference(this, arg)
        }

        // ---{ Foreign Key Columns }---
        // ========================================================================

        fun <C> foreignKey(referencedColumn: Column<C>): ColumnDelegate<T, C> {
            return ColumnDelegate(
                referencedColumn.sqlType,
                referencedColumn.length,
                referencedColumn.options,
                referencedColumn
            )
        }
        
        // ---{ Character Columns }---
        // ========================================================================

        protected fun varchar(options: ColumnOptions = ColumnOptions.DEFAULT) =
            varchar(255, options)
        protected fun varchar(length: Int, options: ColumnOptions = ColumnOptions.DEFAULT) =
            ColumnDelegate<T, String>(SqlTypes.VARCHAR, length, options)
        protected fun nvarchar(options: ColumnOptions = ColumnOptions.DEFAULT) =
            nvarchar(255, options)
        protected fun nvarchar(length: Int, options: ColumnOptions = ColumnOptions.DEFAULT) =
            ColumnDelegate<T, String>(SqlTypes.NVARCHAR, length, options)
        protected fun char(length: Int, options: ColumnOptions = ColumnOptions.DEFAULT) =
            ColumnDelegate<T, String>(SqlTypes.CHAR, length, options)
        protected fun nchar(length: Int, options: ColumnOptions = ColumnOptions.DEFAULT) =
            ColumnDelegate<T, String>(SqlTypes.NCHAR, length, options)

        // ---{ Boolean Column }---
        // ========================================================================

        protected fun boolean(options: ColumnOptions = ColumnOptions.DEFAULT) =
            ColumnDelegate<T, Boolean>(SqlTypes.BOOLEAN, options = options)

        // ---{ Numeric Columns }---
        // ========================================================================

        protected fun short(options: ColumnOptions = ColumnOptions.DEFAULT) =
            ColumnDelegate<T, Short>(SqlTypes.SHORT, options = options)
        protected fun integer(options: ColumnOptions = ColumnOptions.DEFAULT) =
            ColumnDelegate<T, Int>(SqlTypes.INTEGER, options = options)
        protected fun long(options: ColumnOptions = ColumnOptions.DEFAULT) =
            ColumnDelegate<T, Long>(SqlTypes.LONG, options = options)
        protected fun float(options: ColumnOptions = ColumnOptions.DEFAULT) =
            ColumnDelegate<T, Float>(SqlTypes.FLOAT, options = options)
        protected fun double(options: ColumnOptions = ColumnOptions.DEFAULT) =
            ColumnDelegate<T, Double>(SqlTypes.DOUBLE, options = options)
        protected fun bigDecimal(options: ColumnOptions = ColumnOptions.DEFAULT) =
            ColumnDelegate<T, BigDecimal>(SqlTypes.BIG_DECIMAL, options = options)

        // ---{ Temporal Columns }---
        // ========================================================================

        protected fun date(options: ColumnOptions = ColumnOptions.DEFAULT) =
            ColumnDelegate<T, LocalDate>(SqlTypes.DATE, options = options)
        protected fun time(options: ColumnOptions = ColumnOptions.DEFAULT) =
            ColumnDelegate<T, LocalTime>(SqlTypes.TIME, options = options)
        protected fun dateTime(options: ColumnOptions = ColumnOptions.DEFAULT) =
            ColumnDelegate<T, LocalDateTime>(SqlTypes.DATE_TIME, options = options)

        // ---{ Large Object Columns }---
        // ========================================================================

        protected fun clob(options: ColumnOptions = ColumnOptions.DEFAULT) =
            ColumnDelegate<T, Clob>(SqlTypes.CLOB, options = options)
        protected fun nclob(options: ColumnOptions = ColumnOptions.DEFAULT) =
            ColumnDelegate<T, NClob>(SqlTypes.NCLOB, options = options)
        protected fun blob(options: ColumnOptions = ColumnOptions.DEFAULT) =
            ColumnDelegate<T, Blob>(SqlTypes.BLOB, options = options)
    }
}
