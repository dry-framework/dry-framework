package dev.dry.data.sql

import dev.dry.data.sql.builder.DeleteExpressionBuilder
import dev.dry.data.sql.builder.InsertExpressionBuilder
import dev.dry.data.sql.builder.QueryBuilder
import dev.dry.data.sql.builder.UpdateExpressionBuilder
import dev.dry.data.sql.factory.SqlFactory
import dev.dry.data.sql.formatter.SqlFormatter
import dev.dry.data.sql.jdbc.ResultSetExtractor
import dev.dry.data.sql.jdbc.RowMapper
import dev.dry.data.sql.jdbc.RowMapperResultSetExtractor
import dev.dry.data.sql.jdbc.Transaction
import dev.dry.data.sql.jdbc.TransactionIsolation
import dev.dry.data.sql.jdbc.TransactionManager
import dev.dry.data.sql.parameter.NamedParameter
import dev.dry.data.sql.schema.Schema
import dev.dry.data.sql.schema.Table
import dev.dry.data.sql.statements.DeleteExpression
import dev.dry.data.sql.statements.InsertExpression
import dev.dry.data.sql.statements.SqlStatement.QueryStatement
import dev.dry.data.sql.statements.SqlStatement.UpdateStatement
import dev.dry.data.sql.statements.UpdateExpression
import java.sql.Connection
import java.sql.SQLException
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

interface Database {
    val name: String
    val factory: SqlFactory
    val schemas: Collection<Schema>
    val defaultSchema: Schema
    val sqlFormatter: SqlFormatter

    fun addSchema(name: String): Schema

    fun findSchema(name: String): Schema? = schemas.find { it.name == name }

    fun schema(name: String): Schema = findSchema(name) ?: addSchema(name)

    fun <T> useConnection(func: (Connection) -> T): T

    fun <T> transaction(isolation: TransactionIsolation? = null, block: (Transaction) -> T): T

    fun parameter(name: String): NamedParameter = NamedParameter(name)

    fun query(): QueryBuilder

    fun <T: Table> insertInto(table: T, block: InsertExpressionBuilder.(table: T) -> Unit): InsertExpression

    fun <T: Table> update(table: T, block: UpdateExpressionBuilder.(T) -> Unit): UpdateExpression

    fun <T: Table> deleteFrom(table: T, block: DeleteExpressionBuilder.(T) -> Unit): DeleteExpression

    fun <T> executeQuery(queryStatement: QueryStatement, rowMapper: RowMapper<T>): List<T> =
        executeQuery(queryStatement, RowMapperResultSetExtractor(rowMapper))

    fun <T> executeQuery(queryStatement: QueryStatement, resultSetExtractor: ResultSetExtractor<T>): T

    fun executeUpdate(updateStatement: UpdateStatement): Int

    class DefaultDatabase(
        override val name: String,
        override val factory: SqlFactory,
        private val transactionManager: TransactionManager,
        override val sqlFormatter: SqlFormatter,
        //override val getConnection: () -> Connection,
    ) : Database {
        override val schemas: MutableList<Schema> = mutableListOf()
        override val defaultSchema: Schema = factory.schema(this).also(schemas::add)

        override fun addSchema(name: String): Schema = factory.schema(name, this).also(schemas::add)

        override fun <T> transaction(isolation: TransactionIsolation?, block: (Transaction) -> T): T {
            val current = transactionManager.currentTransaction
            val isOuter = current == null
            val transaction = current ?: transactionManager.newTransaction(isolation)
            var throwable: Throwable? = null

            try {
                return block(transaction)
            } catch (ex: SQLException) {
                //throwable = exceptionTranslator?.invoke(e) ?: e
                throwable = ex
                throw throwable
            } catch (ex: Throwable) {
                throwable = ex
                throw throwable
            } finally {
                if (isOuter) {
                    @Suppress("ConvertTryFinallyToUseCall")
                    try {
                        if (throwable == null) transaction.commit() else transaction.rollback()
                    } finally {
                        transaction.close()
                    }
                }
            }
        }

        override fun <T> useConnection(func: (Connection) -> T): T {
            try {
                val transaction = transactionManager.currentTransaction
                val connection = transaction?.connection ?: transactionManager.newConnection()
                try {
                    return func(connection)
                } finally {
                    if (transaction == null) connection.close()
                }
            } catch (ex: SQLException) {
                throw RuntimeException(ex)
                //throw exceptionTranslator?.invoke(e) ?: e
            }
        }

        override fun <T> executeQuery(
            queryStatement: QueryStatement,
            resultSetExtractor: ResultSetExtractor<T>,
        ): T {
            return useConnection { connection ->
                connection.prepareStatement(queryStatement.sql).use { ps ->
                    queryStatement.parameterBindings.forEachIndexed { index, pb -> pb.bind(ps, index + 1) }
                    ps.executeQuery().use { rs ->
                        resultSetExtractor.extract(rs)
                    }
                }
            }
        }

        override fun executeUpdate(updateStatement: UpdateStatement): Int {
            return useConnection { connection ->
                connection.prepareStatement(updateStatement.sql).use { ps ->
                    updateStatement.parameterBindings.forEachIndexed { index, pb -> pb.bind(ps, index + 1) }
                    ps.executeUpdate()
                }
            }
        }

        override fun <T: Table> insertInto(table: T, block: InsertExpressionBuilder.(table: T) -> Unit): InsertExpression {
            val builder = factory.insertExpressionBuilder(table)
            builder.block(table)
            return builder.build()
        }

        override fun <T: Table> update(table: T, block: UpdateExpressionBuilder.(T) -> Unit): UpdateExpression {
            val builder = factory.updateExpressionBuilder(table)
            builder.block(table)
            return builder.build()
        }

        override fun <T: Table> deleteFrom(table: T, block: DeleteExpressionBuilder.(T) -> Unit): DeleteExpression {
            val builder = factory.deleteExpressionBuilder(table)
            builder.block(table)
            return builder.build()
        }

        override fun query(): QueryBuilder = factory.queryBuilder()
    }
}
