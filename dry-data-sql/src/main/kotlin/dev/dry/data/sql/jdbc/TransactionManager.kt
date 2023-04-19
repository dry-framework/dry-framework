package dev.dry.data.sql.jdbc

import java.io.Closeable
import java.sql.Connection

interface TransactionManager {
    val defaultIsolation: TransactionIsolation?

    val currentTransaction: Transaction?

    fun newTransaction(isolation: TransactionIsolation? = defaultIsolation): Transaction

    fun newConnection(): Connection

    fun <T> doInCurrentOrNewTransaction(
        isolation: TransactionIsolation? = defaultIsolation,
        callback: (Transaction) -> T
    ): T
}

interface Transaction : Closeable {
    val connection: Connection
    fun commit()
    fun rollback()

    override fun close()
}

enum class TransactionIsolation(val level: Int) {
    NONE(Connection.TRANSACTION_NONE),
    READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED),
    READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED),
    REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),
    SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE);

    companion object {
        fun valueOf(level: Int): TransactionIsolation {
            return values().firstOrNull() { it.level == level } ?:
                throw IllegalArgumentException("invalid transaction isolation level $level")
        }
    }
}
