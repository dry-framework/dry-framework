package dev.dry.data.sql.jdbc

import java.sql.Connection

class JdbcTransactionManager(private val getConnection: () -> Connection) : TransactionManager {
    private val threadLocal = ThreadLocal<Transaction>()

    override val defaultIsolation: TransactionIsolation? = null

    override val currentTransaction: Transaction? get() = threadLocal.get()

    override fun newTransaction(isolation: TransactionIsolation?): Transaction {
        if (currentTransaction != null) {
            val currentThread = Thread.currentThread()
            throw IllegalStateException("transaction already bound to the current thread '${currentThread.name}'")
        }

        return JdbcTransaction(isolation).apply { threadLocal.set(this) }
    }

    override fun newConnection(): Connection {
        return getConnection()
    }

    override fun <T> doInCurrentOrNewTransaction(isolation: TransactionIsolation?, callback: (Transaction) -> T): T {
        TODO("Not yet implemented")
    }

    private inner class JdbcTransaction(private val desiredIsolation: TransactionIsolation?) : Transaction {
        private var originIsolation = -1
        private var originAutoCommit = true

        private val connectionLazy = lazy(LazyThreadSafetyMode.NONE) {
            newConnection().apply {
                try {
                    if (desiredIsolation != null) {
                        originIsolation = transactionIsolation
                        if (originIsolation != desiredIsolation.level) {
                            transactionIsolation = desiredIsolation.level
                        }
                    }

                    originAutoCommit = autoCommit
                    if (originAutoCommit) {
                        autoCommit = false
                    }
                } catch (e: Throwable) {
                    closeSilently()
                    throw e
                }
            }
        }

        override val connection: Connection by connectionLazy

        override fun commit() {
            if (connectionLazy.isInitialized()) {
                connection.commit()
            }
        }

        override fun rollback() {
            if (connectionLazy.isInitialized()) {
                connection.rollback()
            }
        }

        override fun close() {
            try {
                if (connectionLazy.isInitialized() && !connection.isClosed) {
                    connection.closeSilently()
                }
            } finally {
                threadLocal.remove()
            }
        }

        @Suppress("SwallowedException")
        private fun Connection.closeSilently() {
            try {
                if (desiredIsolation != null && originIsolation != desiredIsolation.level) {
                    transactionIsolation = originIsolation
                }
                if (originAutoCommit) {
                    autoCommit = true
                }
            } catch (_: Throwable) {
            } finally {
                try {
                    close()
                } catch (_: Throwable) {
                }
            }
        }
    }
}
