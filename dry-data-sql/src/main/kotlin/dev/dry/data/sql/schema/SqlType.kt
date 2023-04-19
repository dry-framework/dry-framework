package dev.dry.data.sql.schema

import java.math.BigDecimal
import java.sql.Blob
import java.sql.Clob
import java.sql.NClob
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.Temporal

interface SqlType<T> {
    fun setParameterValue(ps: PreparedStatement, index: Int, value: T?)
    fun getColumnValue(rs: ResultSet, index: Int): T?
}

abstract class AbstractSqlType<T>(protected val sqlType: Int) : SqlType<T> {
    protected abstract fun setValue(ps: PreparedStatement, index: Int, value: T)
    protected abstract fun getValue(rs: ResultSet, index: Int): T?

    override fun setParameterValue(ps: PreparedStatement, index: Int, value: T?) {
        if (value == null) {
            ps.setNull(index, sqlType)
        } else {
            setValue(ps, index, value)
        }
    }

    override fun getColumnValue(rs: ResultSet, index: Int): T? {
        val value = getValue(rs, index)
        return if (rs.wasNull()) null else value
    }
}

open class StringSqlType(sqlType: Int) : AbstractSqlType<String>(sqlType) {
    override fun setValue(ps: PreparedStatement, index: Int, value: String) {
        if (sqlType == Types.NVARCHAR || sqlType == Types.NCHAR) {
            ps.setNString(index, value)
        } else {
            ps.setString(index, value)
        }
    }
    override fun getValue(rs: ResultSet, index: Int): String? {
        return if (sqlType == Types.NVARCHAR || sqlType == Types.NCHAR) {
            rs.getNString(index)
        } else {
            rs.getString(index)
        }
    }
}

class VarcharSqlType : StringSqlType(Types.VARCHAR)

class NVarcharSqlType : StringSqlType(Types.NVARCHAR)

class CharSqlType : StringSqlType(Types.CHAR)

class NCharSqlType : StringSqlType(Types.NCHAR)

class BooleanSqlType : AbstractSqlType<Boolean>(Types.BOOLEAN) {
    override fun setValue(ps: PreparedStatement, index: Int, value: Boolean) = ps.setBoolean(index, value)
    override fun getValue(rs: ResultSet, index: Int): Boolean? = rs.getBoolean(index)
}

abstract class NumericSqlType<T: Number>(jdbcType: Int) : AbstractSqlType<T>(jdbcType)

class ShortSqlType : NumericSqlType<Short>(Types.SMALLINT) {
    override fun setValue(ps: PreparedStatement, index: Int, value: Short) = ps.setShort(index, value)
    override fun getValue(rs: ResultSet, index: Int): Short = rs.getShort(index)
}

class IntegerSqlType : NumericSqlType<Int>(Types.INTEGER) {
    override fun setValue(ps: PreparedStatement, index: Int, value: Int) = ps.setInt(index, value)
    override fun getValue(rs: ResultSet, index: Int): Int = rs.getInt(index)
}

class LongSqlType : NumericSqlType<Long>(Types.BIGINT) {
    override fun setValue(ps: PreparedStatement, index: Int, value: Long) = ps.setLong(index, value)
    override fun getValue(rs: ResultSet, index: Int): Long = rs.getLong(index)
}

class FloatSqlType : NumericSqlType<Float>(Types.REAL) {
    override fun setValue(ps: PreparedStatement, index: Int, value: Float) = ps.setFloat(index, value)
    override fun getValue(rs: ResultSet, index: Int): Float = rs.getFloat(index)
}

class DoubleSqlType : NumericSqlType<Double>(Types.DOUBLE) {
    override fun setValue(ps: PreparedStatement, index: Int, value: Double) = ps.setDouble(index, value)
    override fun getValue(rs: ResultSet, index: Int): Double = rs.getDouble(index)
}

class BigDecimalSqlType : NumericSqlType<BigDecimal>(Types.DECIMAL) {
    override fun setValue(ps: PreparedStatement, index: Int, value: BigDecimal) = ps.setBigDecimal(index, value)
    override fun getValue(rs: ResultSet, index: Int): BigDecimal? = rs.getBigDecimal(index)
}

abstract class TemporalSqlType<T: Temporal>(jdbcType: Int) : AbstractSqlType<T>(jdbcType)

class DateSqlType : TemporalSqlType<LocalDate>(Types.DATE) {
    override fun setValue(ps: PreparedStatement, index: Int, value: LocalDate) = ps.setObject(index, value)
    override fun getValue(rs: ResultSet, index: Int): LocalDate? = rs.getObject(index) as LocalDate
}


class TimeSqlType : TemporalSqlType<LocalTime>(Types.TIME) {
    override fun setValue(ps: PreparedStatement, index: Int, value: LocalTime) = ps.setObject(index, value)
    override fun getValue(rs: ResultSet, index: Int): LocalTime? = rs.getObject(index) as LocalTime
}

class DateTimeSqlType : TemporalSqlType<LocalDateTime>(Types.TIMESTAMP) {
    override fun setValue(ps: PreparedStatement, index: Int, value: LocalDateTime) = ps.setObject(index, value)
    override fun getValue(rs: ResultSet, index: Int): LocalDateTime? = rs.getObject(index) as LocalDateTime
}

class ClobSqlType : AbstractSqlType<Clob>(Types.CLOB) {
    override fun setValue(ps: PreparedStatement, index: Int, value: Clob) = ps.setClob(index, value)
    override fun getValue(rs: ResultSet, index: Int): Clob? = rs.getClob(index)
}

class NClobSqlType : AbstractSqlType<NClob>(Types.NCLOB) {
    override fun setValue(ps: PreparedStatement, index: Int, value: NClob) = ps.setNClob(index, value)
    override fun getValue(rs: ResultSet, index: Int): NClob? = rs.getNClob(index)
}

class BlobSqlType : AbstractSqlType<Blob>(Types.BLOB) {
    override fun setValue(ps: PreparedStatement, index: Int, value: Blob) = ps.setBlob(index, value)
    override fun getValue(rs: ResultSet, index: Int): Blob? = rs.getBlob(index)
}
