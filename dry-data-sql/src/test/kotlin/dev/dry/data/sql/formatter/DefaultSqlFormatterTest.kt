package dev.dry.data.sql.formatter

import dev.dry.data.sql.Database
import dev.dry.data.sql.Database.DefaultDatabase
import dev.dry.data.sql.expressions.JoinExpression.JoinType.LEFT
import dev.dry.data.sql.expressions.eq
import dev.dry.data.sql.factory.SqlFactory.DefaultSqlFactory
import dev.dry.data.sql.formatter.SqlFormatter.DefaultSqlFormatter
import dev.dry.data.sql.jdbc.JdbcTransactionManager
import dev.dry.data.sql.jdbc.TransactionIsolation.READ_COMMITTED
import dev.dry.data.sql.parameter.NamedParameter
import dev.dry.data.sql.parameter.NamedParameterBinding
import dev.dry.data.sql.schema.ColumnOptions
import dev.dry.data.sql.schema.Table.BaseTable
import dev.dry.data.sql.schema.alias
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.sql.Connection
import java.sql.DriverManager

class DefaultSqlFormatterTest {
    class OrderTable(database: Database, alias: String? = null) : BaseTable<OrderTable>("ord", database, alias) {
        //val name by varchar(56) // .nullable() -- TODO("add support for nullable")
        val orderNumber by varchar(30, ColumnOptions.primaryKey())
        //val invoiceNumber by varchar(30)
        val customerNumber by varchar(30)
    }

    class OrderLineTable(database: Database, alias: String? = null) : BaseTable<OrderLineTable>("order_line", database, alias) {
        //val orderNumber foreignKey(ORDER_TABLE.orderNumber)
        //val v = OrderTable::orderNumber
        //val orderNumber foreignKey(OrderTable::orderNumber)
        val orderNumber by varchar(30)//, foreignKey(OrderTable::orderNumber))
        val quantity by integer()
    }

    companion object {
        private val CREATE_ORDER_TABLE = """
            CREATE TABLE ord (
               orderNumber VARCHAR (30) NOT NULL,
               customerNumber VARCHAR (30) NOT NULL, 
               PRIMARY KEY (orderNumber)
            ); 
        """.trimIndent()
        private val CREATE_ORDER_LINE_TABLE = """
            CREATE TABLE order_line (
               orderNumber VARCHAR (30) NOT NULL,
               quantity INT NOT NULL
            ); 
        """.trimIndent()
        const val DB_URL = "jdbc:h2:mem:"
        // "jdbc:hsqldb:mem:db1"
        var connection: Connection? = null
        val getConnection = {
            connection ?: DriverManager.getConnection(DB_URL).also { c ->
                c.createStatement().executeUpdate(CREATE_ORDER_TABLE)
                c.createStatement().executeUpdate(CREATE_ORDER_LINE_TABLE)
                connection = c
            }
        }
        val database: Database = DefaultDatabase(
            name = "db",
            factory = DefaultSqlFactory,
            transactionManager = JdbcTransactionManager(getConnection),
            sqlFormatter = DefaultSqlFormatter(),
        )
        val ORDER = OrderTable(database)
        val ORDER_LINE = OrderLineTable(database)

        @AfterEach
        fun afterEach() {
            connection = null
        }
    }

    fun insertOrder(orderNumber: String, customerNumber: String, quantity: Int) {
        val insertOrder = database
            .insertInto(ORDER) {o ->
                set(o.orderNumber, orderNumber)
                set(o.customerNumber, customerNumber)
            }
            .prepareStatement(database.sqlFormatter)
        println("inserted count: " + database.executeUpdate(insertOrder))

        val insertOrderLine = database
            .insertInto(ORDER_LINE) { ol ->
                set(ol.orderNumber, orderNumber)
                set(ol.quantity, quantity)
            }
            .prepareStatement(database.sqlFormatter)
        println("inserted order lines: " + database.executeUpdate(insertOrderLine))
    }

    fun updateOrderQuantity(orderNumber: String, quantity: Int) {
        val updateOrderLine = database
            .update(ORDER_LINE) { ol ->
                set(ol.quantity, quantity)

                where(ol.orderNumber eq orderNumber)
            }
            .prepareStatement(database.sqlFormatter)
        println("inserted count: " + database.executeUpdate(updateOrderLine))
    }

    @Test
    fun defineSchema() {
        database.transaction(READ_COMMITTED) {
            val orderNumber = "123"
            val customerNumberParameter = database.parameter("customerNumber")

            insertOrder(orderNumber, "456", 3)

            updateOrderQuantity(orderNumber, 4)

            queryOrder(customerNumberParameter)

            val deleteOrderLine = database.deleteFrom(ORDER_LINE) { ol ->
                where(ol.orderNumber eq orderNumber)
            }
            .prepareStatement(database.sqlFormatter)
            println("deleted count: " + database.executeUpdate(deleteOrderLine))

            queryOrder(customerNumberParameter)
        }
    }

    private fun queryOrder(customerNumberParameter: NamedParameter) {
        val selectStatement = database
            .query()
            .from(ORDER alias "ord") { order ->
                //join(LEFT, ORDER_LINE alias "ol")
                val orderLine = join(LEFT, ORDER_LINE, "order_line") { ol -> order.orderNumber eq ol.orderNumber }
                select(order.orderNumber, order.customerNumber alias "cnum", orderLine.quantity)
                where((order.orderNumber eq "123") and (order.customerNumber eq customerNumberParameter))
                //orderBy(order.customerNumber)
            }
            .prepareStatement(database.sqlFormatter) {
                customerNumberParameter bind "456"
            }

        println(selectStatement.sql)
        println(selectStatement.parameterBindings.mapIndexed { index, p ->
            when (p) {
                is NamedParameterBinding<*> -> "$index:${p.name}:${p.value}"
                else -> "$index:${p.value}"
            }
        })

        database.executeQuery(selectStatement) { rs, _ ->
            val orderNumberValue = rs.getString(1)
            val customerNumber = rs.getString(2)
            val quantity = rs.getInt(3)
            println("Order Number: $orderNumberValue, Customer Number: $customerNumber, Quantity: $quantity")
            //val customerNumber = rs.getString(ORDER.customerNumber.name)
            //val quantity = rs.getInt(ORDER_LINE.quantity.name)
        }
    }
}
