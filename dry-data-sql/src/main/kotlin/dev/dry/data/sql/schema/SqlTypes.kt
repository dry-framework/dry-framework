package dev.dry.data.sql.schema

object SqlTypes {
    // ---{ Character Types }---
    // ========================================================================

    val VARCHAR = VarcharSqlType()
    val NVARCHAR = NVarcharSqlType()
    val CHAR = CharSqlType()
    val NCHAR = NCharSqlType()

    // ---{ Boolean Type }---
    // ========================================================================

    val BOOLEAN = BooleanSqlType()

    // ---{ Numeric Columns }---
    // ========================================================================

    val SHORT = ShortSqlType()
    val INTEGER = IntegerSqlType()
    val LONG = LongSqlType()
    val FLOAT = FloatSqlType()
    val DOUBLE = DoubleSqlType()
    val BIG_DECIMAL = BigDecimalSqlType()

    // ---{ Temporal Types }---
    // ========================================================================

    val DATE = DateSqlType()
    val TIME = TimeSqlType()
    val DATE_TIME = DateTimeSqlType()

    // ---{ Large Object Types }---
    // ========================================================================

    val CLOB = ClobSqlType()
    val NCLOB = NClobSqlType()
    val BLOB = BlobSqlType()
}
